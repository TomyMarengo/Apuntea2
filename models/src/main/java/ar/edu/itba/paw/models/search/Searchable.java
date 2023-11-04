package ar.edu.itba.paw.models.search;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.institutional.Subject;
import ar.edu.itba.paw.models.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Searchable {
    UUID getId();

    String getName();

    User getUser();

    UUID getParentId();

    Subject getSubject();

    LocalDateTime getCreatedAt();

    LocalDateTime getLastModifiedAt();

    boolean isVisible();

    Category getCategory();

    float getAvgScore();

    String getFileType();

    String getIconColor();

    boolean getFavorite();

    default boolean isDirectory() {
        return this.getCategory() == Category.DIRECTORY;
    }
}
