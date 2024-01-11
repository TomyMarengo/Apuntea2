package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.directory.DirectoryFavoriteGroups;
import ar.edu.itba.paw.models.directory.DirectoryPath;
import ar.edu.itba.paw.models.exceptions.UserNotOwnerException;
import ar.edu.itba.paw.models.exceptions.directory.DirectoryNotFoundException;
import ar.edu.itba.paw.models.search.SearchArguments;
import ar.edu.itba.paw.models.user.User;
import ar.edu.itba.paw.models.exceptions.directory.InvalidDirectoryException;
import ar.edu.itba.paw.persistence.DirectoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DirectoryServiceImpl implements DirectoryService {
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

    @Transactional(readOnly = true)
    @Override
    public Optional<Directory> getDirectoryById(UUID directoryId) {
        UUID currentUserId = securityService.getCurrentUser().map(User::getUserId).orElse(null);
        return directoryDao.getDirectoryById(directoryId, currentUserId);
    }

    @Transactional(readOnly = true)
    @Override
    public DirectoryPath getDirectoryPath(UUID directoryId) {
        List<Directory> directories =  directoryDao.getDirectoryPath(directoryId);
        return new DirectoryPath(directories);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Directory> getDirectories(UUID parentId, UUID userId, UUID favBy, String word, String sortBy, boolean ascending, int page, int pageSize) {
        final SearchArguments.SearchArgumentsBuilder sab = new SearchArguments.SearchArgumentsBuilder()
                .userId(userId)
                .parentId(parentId)
                .favBy(favBy)
                .word(word)
                .sortBy(sortBy)
                .ascending(ascending);
        final Optional<User> maybeUser = securityService.getCurrentUser();
        maybeUser.ifPresent(u -> sab.currentUserId(u.getUserId()));

        boolean navigate = parentId != null || favBy != null; // Maybe this should be an additional parameter

        SearchArguments searchArgumentsWithoutPaging = sab.build();
        int countTotalResults = navigate? directoryDao.countNavigationResults(searchArgumentsWithoutPaging) : directoryDao.countSearchResults(searchArgumentsWithoutPaging);
        int safePage = Page.getSafePagePosition(page, countTotalResults, pageSize);

        sab.page(safePage).pageSize(pageSize);
        SearchArguments sa = sab.build();

        return new Page<>(
                navigate? directoryDao.navigate(sa) : directoryDao.search(sa),
                sa.getPage(),
                sa.getPageSize(),
                countTotalResults
        );
    }

    @Transactional
    @Override
    public void update(UUID directoryId, String name, Boolean visible, String iconColor) {
        User currentUser = securityService.getCurrentUserOrThrow();
        Directory directory = directoryDao
                .getDirectoryById(directoryId, currentUser.getUserId())
                .orElseThrow(DirectoryNotFoundException::new);
        if (!currentUser.equals(directory.getUser()))
            throw new UserNotOwnerException();
        if (name != null)
            directory.setName(name);
        if (visible != null)
            directory.setVisible(visible);
        if (iconColor != null)
            directory.setIconColor(iconColor);
    }

    @Transactional
    @Override
    public void delete(UUID directoryId, String reason) {
        User currentUser = securityService.getCurrentUserOrThrow();
        if (!currentUser.getIsAdmin()) {
            if (!directoryDao.delete(directoryId, currentUser.getUserId()))
                throw new InvalidDirectoryException();
        } else {
            Optional<Directory> maybeDirectory = directoryDao.getDirectoryById(directoryId, currentUser.getUserId());
            if (!maybeDirectory.isPresent()) throw new DirectoryNotFoundException();
            if (!directoryDao.delete(directoryId)) throw new InvalidDirectoryException();
            emailService.sendDeleteDirectoryEmail(maybeDirectory.get(), reason);
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
