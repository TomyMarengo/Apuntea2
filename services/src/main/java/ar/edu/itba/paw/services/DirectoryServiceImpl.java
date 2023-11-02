package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.persistence.DirectoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DirectoryServiceImpl implements DirectoryService{
    private final DirectoryDao directoryDao;
    private final SecurityService securityService;
    private final EmailService emailService;

    @Autowired
    public DirectoryServiceImpl(DirectoryDao directoryDao, SecurityService securityService, EmailService emailService) {
        this.directoryDao = directoryDao;
        this.securityService = securityService;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public UUID create(String name, UUID parentId, boolean visible, String iconColor) {
        User user = securityService.getCurrentUserOrThrow();
        return directoryDao.create(name, parentId, user, visible, iconColor).getId();
    }

    @Transactional
    @Override
    public Optional<Directory> getDirectoryById(UUID directoryId) {
        UUID currentUserId = securityService.getCurrentUser().map(User::getUserId).orElse(null);
        return directoryDao.getDirectoryById(directoryId, currentUserId);
    }

    @Transactional
    @Override
    public DirectoryPath getDirectoryPath(UUID directoryId) {
        // TODO: Group both methods in one
        List<UUID> directoryPathIds =  directoryDao.getDirectoryPathIds(directoryId);
        List<Directory> directories = directoryDao.findDirectoriesByIds(directoryPathIds);
        return new DirectoryPath(directories);
    }

    @Transactional
    @Override
    public void update(UUID directoryId, String name, boolean visible, String iconColor) {
        User currentUser = securityService.getCurrentUserOrThrow();
        Directory directory = directoryDao.getDirectoryById(directoryId, currentUser.getUserId()).orElseThrow(InvalidDirectoryException::new);
        if (!directory.getUser().equals(currentUser)) throw new InvalidDirectoryException(); // TODO: Forbidden exception
        directory.setName(name);
        directory.setVisible(visible);
        directory.setIconColor(iconColor);
    }

    @Transactional
    @Override
    public void delete(UUID[] directoryIds, String reason) {
        if (directoryIds.length == 0) return;

        // TODO: Propagate this to the Controller?
        List<UUID> directoryIdsList = Collections.unmodifiableList(Arrays.asList(directoryIds));

        User currentUser = securityService.getCurrentUserOrThrow();
        if (!currentUser.getIsAdmin()) {
            if (!directoryDao.delete(directoryIdsList, currentUser.getUserId()))
                throw new InvalidDirectoryException();
        } else {
            List<Directory> directories = directoryDao.findDirectoriesByIds(directoryIdsList);
            if (directories.size() != directoryIdsList.size() || !directoryDao.delete(directoryIdsList)) throw new InvalidDirectoryException();
            directories.forEach(d -> emailService.sendDeleteDirectoryEmail(d, reason));
        }
    }

    @Transactional
    @Override
    public List<Directory> getFavorites() {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        return directoryDao.getFavorites(currentUserId);
    }

    @Transactional
    @Override
    public void addFavorite(UUID directoryId) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        directoryDao.addFavorite(currentUserId, directoryId);
    }

    @Transactional
    @Override
    public void removeFavorite(UUID directoryId) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = directoryDao.removeFavorite(currentUserId, directoryId);
        if (!success) throw new InvalidDirectoryException();
    }
}
