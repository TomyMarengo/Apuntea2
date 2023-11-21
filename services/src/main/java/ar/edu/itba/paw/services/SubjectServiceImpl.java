package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.institutional.dtos.SubjectDto;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectException;
import ar.edu.itba.paw.models.exceptions.institutional.InvalidSubjectCareerException;
import ar.edu.itba.paw.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;
    private final DirectoryDao directoryDao;
    private final UserDao userDao;
    private final CareerDao careerDao;
    private final SearchDao searchDao;
    private final SecurityService securityService;


    @Autowired
    public SubjectServiceImpl(SubjectDao subjectDao, DirectoryDao directoryDao, UserDao userDao, CareerDao careerDao, SearchDao searchDao, SecurityService securityService) {
        this.subjectDao = subjectDao;
        this.directoryDao = directoryDao;
        this.userDao = userDao;
        this.careerDao = careerDao;
        this.searchDao = searchDao;
        this.securityService = securityService;
    }


    @Override
    @Transactional
    public List<Subject> getSubjectsByCareer(UUID careerId) {
        return subjectDao.getSubjectsByCareerId(careerId);
    }

    @Override
    @Transactional
    public Map<Integer, List<Subject>> getSubjectsByCareerGroupByYear() {
        User currentUser = this.securityService.getCurrentUserOrThrow();
        List<Subject> subjects = subjectDao.getSubjectsByCareerId(currentUser.getCareer().getCareerId());
        directoryDao.setDirectoryFavorites(subjects.stream().map(Subject::getRootDirectoryId).collect(Collectors.toList()), currentUser.getUserId());
        return subjects.stream().collect(Collectors.groupingBy(Subject::getYear));
    }

    @Override
    @Transactional
    public List<SubjectDto> getSubjectsByCareerComplemented(UUID careerId) {
        return subjectDao.getSubjectsByCareerIdComplemented(careerId).stream().map(SubjectDto::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Map<Integer, List<Subject>> getSubjectsByUserIdGroupByYear(UUID userId) {
        UUID currentUserId = this.securityService.getCurrentUser().map(User::getUserId).orElse(null);
        User user = this.userDao.findById(userId).orElseThrow(UserNotFoundException::new);
//        directoryDao.getFavoriteRootDirectories(user.getUserId()).forEach(rd -> rd.setFavorite(true));
        List<Subject> subjects = subjectDao.getSubjectsByUser(user);
        if (!subjects.isEmpty()) directoryDao.setRootDirsFileQuantity(
                subjects.stream().map(Subject::getRootDirectoryId).collect(Collectors.toList()),
                user.getUserId(),
                currentUserId
        );
        return subjects.stream().collect(Collectors.groupingBy(Subject::getYear));
    }

    @Override
    @Transactional
    public UUID createSubject(String name, UUID careerId, int year) {
        Directory rootDirectory = directoryDao.createRootDirectory(name);
        Subject subject = subjectDao.create(name, rootDirectory.getId());
        subjectDao.linkSubjectToCareer(subject, careerId, year);
        return subject.getSubjectId();
    }

    @Override
    @Transactional
    public void linkSubjectToCareer(UUID subjectId, UUID careerId, int year) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(InvalidSubjectException::new);
        subjectDao.linkSubjectToCareer(subject, careerId, year);
    }

    @Override
    @Transactional
    public void updateSubject(UUID subjectId, String name) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(InvalidSubjectException::new);
        // TODO: Check if the amount of queries is ok
        subject.setName(name);
    }

    @Override
    @Transactional
    public void updateSubjectCareer(UUID subjectId, String subjectName, UUID careerId, int year) {
        updateSubject(subjectId, subjectName);
        boolean success = subjectDao.updateSubjectCareer(subjectId, careerId, year);
        if (!success) {
            throw new InvalidSubjectCareerException();
        }
    }

    @Override
    @Transactional
    public void unlinkSubjectFromCareer(UUID subjectId, UUID careerId) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(InvalidSubjectException::new);
        boolean success = subjectDao.unlinkSubjectFromCareer(subjectId, careerId);
        if (!success) {
            throw new InvalidSubjectCareerException();
        } else if (careerDao.countCareersBySubjectId(subjectId) == 0 && searchDao.countChildren(subject.getRootDirectoryId()) == 0) {
            success = subjectDao.delete(subjectId);
            if (!success)
                throw new InvalidSubjectException();
            success = directoryDao.delete(Collections.singletonList(subject.getRootDirectoryId()));
            if (!success)
                throw new InvalidDirectoryException();
        }
    }

    @Transactional
    @Override
    public boolean isSubjectDetachable(UUID subjectId) {
        Subject subject = subjectDao.getSubjectById(subjectId).orElseThrow(InvalidSubjectException::new);
        return careerDao.countCareersBySubjectId(subjectId) > 1 || searchDao.countChildren(subject.getRootDirectoryId()) == 0;
    }

}
