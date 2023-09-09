package ar.edu.itba.apuntea.services;

import ar.edu.itba.apuntea.models.Directory;

import java.util.List;
import java.util.Optional;

public interface DirectoryService {
    Optional<Directory> create(String name, String parentId, String userId);

    List<Directory> search(String institutionId, String careerId, String subjectId, String sortBy, boolean ascending, Integer page, Integer pageSize);

    Optional<Directory> getDirectoryById(String id);

    List<Directory> getChildren(String id);

}
