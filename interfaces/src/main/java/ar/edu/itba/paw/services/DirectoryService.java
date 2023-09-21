package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryService {
    Directory create(String name, UUID parentId);

    List<Directory> search(UUID institutionId, UUID careerId, UUID subjectId, String word, String sortBy, boolean ascending, Integer page, Integer pageSize);

    Optional<Directory> getDirectoryById(UUID directoryId);

    List<Directory> getChildren(UUID directoryId);

    DirectoryPath getDirectoryPath(UUID directoryId);

    void delete(UUID directoryId);
}
