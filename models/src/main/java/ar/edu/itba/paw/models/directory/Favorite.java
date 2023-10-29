package ar.edu.itba.paw.models.directory;


import ar.edu.itba.paw.models.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "favorites")
@IdClass(Favorite.FavoriteKey.class)
public class Favorite {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId
    User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "directory_id")
    @MapsId
    Directory directory;

    /* package-private */ Favorite() {}

    public Favorite(User user, Directory directory) {
        this.user = user;
        this.directory = directory;
    }

    public static class FavoriteKey implements Serializable {
        private User user;
        private Directory directory;

        /* package-private */ FavoriteKey() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FavoriteKey that = (FavoriteKey) o;
            return Objects.equals(user, that.user) && Objects.equals(directory, that.directory);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, directory);
        }
    }

}
