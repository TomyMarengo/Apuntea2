package ar.edu.itba.paw.models;

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

}
