package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
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
        UUID userId = securityService.getCurrentUserOrThrow().getUserId();
        return directoryDao.create(name, parentId, userId, visible, iconColor);
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
    public void delete(UUID[] directoryIds, String reason) {
        if (directoryIds.length == 0) return;

        User currentUser = securityService.getCurrentUserOrThrow();
        if (!currentUser.getIsAdmin()) {
            if (!directoryDao.delete(directoryIds, currentUser.getUserId()))
                throw new InvalidDirectoryException();
        } else {
            List<Directory> dir = directoryDao.delete(directoryIds);
            if (dir.isEmpty()) throw new InvalidDirectoryException();
            dir.forEach(d -> emailService.sendDeleteDirectoryEmail(d, reason));
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
        boolean success = directoryDao.addFavorite(currentUserId, directoryId);
        if (!success) throw new InvalidDirectoryException();
    }

    @Transactional
    @Override
    public void removeFavorite(UUID directoryId) {
        UUID currentUserId = securityService.getCurrentUserOrThrow().getUserId();
        boolean success = directoryDao.removeFavorite(currentUserId, directoryId);
        if (!success) throw new InvalidDirectoryException();
    }
}
