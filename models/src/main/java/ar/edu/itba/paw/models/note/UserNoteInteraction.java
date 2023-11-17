package ar.edu.itba.paw.models.note;

import ar.edu.itba.paw.models.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "User_Note_Interactions")
@IdClass(UserNoteInteraction.InteractionKey.class)
public class UserNoteInteraction {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "note_id")
    @MapsId
    private Note note;

    // TODO?: Create enum for different interaction types
    // private InteractionType type;

    /* package-private */ UserNoteInteraction() {}

    public UserNoteInteraction(final User user, final Note note) {
        this.user = user;
        this.note = note;
    }

    public static class InteractionKey implements Serializable {
        private User user;
        private Note note;

        /* package-private */ InteractionKey() {}

        public InteractionKey(User user, Note note) {
            this.user = user;
            this.note = note;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InteractionKey that = (InteractionKey) o;
            return Objects.equals(user, that.user) && Objects.equals(note, that.note);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, note);
        }
    }

}
