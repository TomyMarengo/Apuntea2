package ar.edu.itba.paw.models.user;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Follows")
@IdClass(Follow.FollowKey.class)
public class Follow {
    @Id
    @ManyToOne
    @JoinColumn(name = "follower_id")
    @MapsId
    private User follower;

    @Id
    @ManyToOne
    @JoinColumn(name = "followed_id")
    @MapsId
    private User followed;

    // TODO?: Create enum for different interaction types
    // private InteractionType type;

    /* package-private */ Follow() {}

    public Follow(final User follower, final User followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public static class FollowKey implements Serializable {
        private User follower;
        private User followed;

        /* package-private */ FollowKey() {}

        public FollowKey(User follower, User followed) {
            this.follower = follower;
            this.followed = followed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FollowKey that = (FollowKey) o;
            return Objects.equals(follower, that.follower) && Objects.equals(followed, that.followed);
        }

        @Override
        public int hashCode() {
            return Objects.hash(follower, followed);
        }
    }

}
