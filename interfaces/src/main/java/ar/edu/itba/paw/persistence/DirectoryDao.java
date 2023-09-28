package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Directory;
import ar.edu.itba.paw.models.DirectoryPath;

import java.util.List;
import java.util.UUID;

public interface DirectoryDao {
    UUID create(String name, UUID parentId, UUID userId, boolean visible, String iconColor);
    DirectoryPath getDirectoryPath(UUID directoryId);

    boolean update(Directory directory, UUID currentUserId);
    Directory getDirectoryById(UUID directoryId, UUID currentUserId);

    boolean deleteMany(UUID[] directoryIds, UUID currentUserId);

    boolean delete(UUID directoryId, UUID currentUserId);
}
