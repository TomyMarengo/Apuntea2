package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Directory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryService {
    Optional<Directory> create(String name, String parentId, String userId);

    List<Directory> search(UUID institutionId, UUID careerId, UUID subjectId, String sortBy, boolean ascending, Integer page, Integer pageSize);

    Optional<Directory> getDirectoryById(String id);

    List<Directory> getChildren(String id);

}
