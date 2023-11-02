package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Note_Favorites")
@IdClass(NoteFavorite.FavoriteKey.class)
public class NoteFavorite {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId
    User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "note_id")
    @MapsId
    Note note;

    /* package-private */ NoteFavorite() {}

    public NoteFavorite(User user, Note note) {
        this.user = user;
        this.note = note;
    }

    public static class FavoriteKey implements Serializable {
        private User user;
        private Note note;

        /* package-private */ FavoriteKey() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FavoriteKey that = (FavoriteKey) o;
            return Objects.equals(user, that.user) && Objects.equals(note, that.note);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, note);
        }
    }

}
