package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.InvalidDirectoryException;
import ar.edu.itba.paw.persistence.DirectoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DirectoryServiceImpl implements DirectoryService{
    private final DirectoryDao directoryDao;
    private final SecurityService securityService;
    @Autowired
    public DirectoryServiceImpl(DirectoryDao directoryDao, SecurityService securityService) {
        this.directoryDao = directoryDao;
        this.securityService = securityService;
    }

    @Transactional
    @Override
    public UUID create(String name, UUID parentId, boolean visible, String iconColor) {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        return directoryDao.create(name, parentId, userId, visible, iconColor);
    }

    @Transactional
    @Override
    public Optional<Directory> getDirectoryById(UUID directoryId) {
        UUID currentUserId = securityService.getCurrentUser().map(User::getUserId).orElse(null);
        return Optional.ofNullable(directoryDao.getDirectoryById(directoryId, currentUserId));
    }

    @Transactional
    @Override
    public DirectoryPath getDirectoryPath(UUID directoryId) {
        return directoryDao.getDirectoryPath(directoryId);
    }

    @Transactional
    @Override
    public void update(Directory directory) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = directoryDao.update(directory, currentUserId);
        if (!success) throw new InvalidDirectoryException();
    }

    @Transactional
    @Override
    public void delete(UUID directoryId) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = directoryDao.delete(directoryId, currentUserId);
        if (!success) throw new InvalidDirectoryException();
    }

    @Transactional
    @Override
    public void deleteMany(UUID[] directoryIds) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = directoryDao.deleteMany(directoryIds, currentUserId);
        if (!success) throw new InvalidDirectoryException();
    }
    
    @Transactional
    @Override
    public List<Directory> getRootDirectoriesByCurrentUserCareer() {
        return directoryDao.getRootDirectoriesByCareer(securityService.getCurrentUserOrThrow().getCareer().getCareerId());
    }
}
