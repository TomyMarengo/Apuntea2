package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryFavoriteGroups;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.persistence.DirectoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        List<Directory> directories =  directoryDao.getDirectoryPath(directoryId);
        return new DirectoryPath(directories);
    }

    @Transactional
    @Override
    public void update(UUID directoryId, String name, boolean visible, String iconColor) {
        User currentUser = securityService.getCurrentUserOrThrow();
        Directory directory = directoryDao
                .getDirectoryById(directoryId, currentUser.getUserId())
                .filter(d -> d.getUser().equals(currentUser))
                .orElseThrow(InvalidDirectoryException::new);
        directory.setName(name);
        directory.setVisible(visible);
        directory.setIconColor(iconColor);
    }

    @Transactional
    @Override
    public void delete(UUID[] directoryIds, String reason) {
        if (directoryIds.length == 0) return;

        List<UUID> directoryIdsList = Arrays.asList(directoryIds);

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
    public DirectoryFavoriteGroups getFavorites() {
        User currentUser = securityService.getCurrentUserOrThrow();
        Map<Boolean, List<Directory>> directories = currentUser.getDirectoryFavorites().stream().collect(Collectors.partitioningBy(Directory::isRootDirectory));
        return new DirectoryFavoriteGroups(directories.get(true), directories.get(false));
    }

    @Transactional
    @Override
    public void addFavorite(UUID directoryId) {
        User currentUser = securityService.getCurrentUserOrThrow();
        directoryDao.addFavorite(currentUser, directoryId);
    }

    @Transactional
    @Override
    public void removeFavorite(UUID directoryId) {
        User currentUser = securityService.getCurrentUserOrThrow();
        boolean success = directoryDao.removeFavorite(currentUser, directoryId);
        if (!success) throw new InvalidDirectoryException();
    }
}
