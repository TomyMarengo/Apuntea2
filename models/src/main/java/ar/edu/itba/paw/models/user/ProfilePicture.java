package ar.edu.itba.paw.models.user;

import ar.edu.itba.paw.models.exceptions.user.UserNotFoundException;

import java.util.UUID;

public class ProfilePicture {
    private final UUID userId;
    private final byte[] picture;

    public ProfilePicture(String userId, Object picture) {
        if (userId == null) throw new UserNotFoundException();
        this.userId = UUID.fromString(userId);
        this.picture = (byte[]) picture;
    }

    public UUID getUserId() {
        return userId;
    }

    public byte[] getPicture() {
        return picture;
    }
}
