package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.SearchArguments;
import ar.edu.itba.paw.persistence.DirectoryDao;
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

    @Autowired
    public DirectoryServiceImpl(DirectoryDao directoryDao) { this.directoryDao = directoryDao; }

    @Override
    public Optional<Directory> create(String name, String parentId, String userId) {
        try {
            return Optional.ofNullable(directoryDao.create(name, UUID.fromString(parentId), UUID.fromString(userId)));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Directory> search(UUID institutionId, UUID careerId, UUID subjectId, String word, String sortBy, boolean ascending, Integer page, Integer pageSize) {
        if (sortBy.equals(SCORE.toString()))
            return new ArrayList<>();
        SearchArguments sa = new SearchArguments(institutionId, careerId, subjectId, word, sortBy, ascending, page, pageSize);
        return directoryDao.search(sa);
    }

    @Override
    public Optional<Directory> getDirectoryById(String id) {
        try {
            return Optional.ofNullable(directoryDao.getDirectoryById(UUID.fromString(id)));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Directory> getChildren(String id) {
        try {
            return directoryDao.getChildren(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }
}
