package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;
import ar.edu.itba.paw.models.SearchArguments;
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
    private final UserDao userDao;

    @Autowired
    public DirectoryServiceImpl(DirectoryDao directoryDao, UserDao userDao) {
        this.directoryDao = directoryDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<Directory> create(String name, UUID parentId, UUID userId) {
        return Optional.ofNullable(directoryDao.create(name, parentId, userId));
    }

    @Override
    public Optional<Directory> create(String name, UUID parentId, String email) {
        UUID userId = userDao.createIfNotExists(email).getUserId();
        return Optional.ofNullable(directoryDao.create(name, parentId, userId));
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
