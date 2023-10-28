package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

public interface Searchable {
    UUID getId();

    String getName();

    User getUser();

    UUID getParentId();

    //Subject getSubject();

    LocalDateTime getCreatedAt();

    LocalDateTime getLastModifiedAt();

    boolean isVisible();

    Category getCategory();

    float getAvgScore();

    String getFileType();

    String getIconColor();

    boolean getFavorite();
}
