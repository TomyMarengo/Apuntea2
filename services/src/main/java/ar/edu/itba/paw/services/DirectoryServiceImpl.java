package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.DirectoryDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ar.edu.itba.paw.models.SearchArguments.SortBy.*;

@Service
public class DirectoryServiceImpl implements DirectoryService{
    private final DirectoryDao directoryDao;
    private final SecurityService securityService;
    @Autowired
    public DirectoryServiceImpl(DirectoryDao directoryDao, SecurityService securityService) {
        this.directoryDao = directoryDao;
        this.securityService = securityService;
    }

    @Override
    public Directory create(String name, UUID parentId) {
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        return directoryDao.create(name, parentId, userId);
    }

    @Override
    public List<Directory> search(UUID institutionId, UUID careerId, UUID subjectId, String word, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        if (sortBy.equals(SCORE.toString()))
            return new ArrayList<>();
        SearchArguments sa = new SearchArguments(institutionId, careerId, subjectId, word, sortBy, ascending, page, pageSize);
        return directoryDao.search(sa);
    }

    @Override
    public Optional<Directory> getDirectoryById(UUID directoryId) {
        return Optional.ofNullable(directoryDao.getDirectoryById(directoryId));
    }

    @Override
    public List<Directory> getChildren(UUID directoryId) {
        return directoryDao.getChildren(directoryId);
    }

    @Override
    public DirectoryPath getDirectoryPath(UUID directoryId) {
        return directoryDao.getDirectoryPath(directoryId);
    }

    @Override
    public void delete(UUID directoryId) {
        directoryDao.delete(directoryId);
    }
}
