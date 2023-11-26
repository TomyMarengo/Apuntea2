package ar.edu.itba.paw.models.user;

import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bans")
public class Ban {
    @Column(name = "reason")
    private String reason;

    @EmbeddedId
    private BanId banId;

    @Column(name = "end_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime endDate;


    /* package-private */ Ban() {
    }

    public Ban(User user, User admin, String reason, LocalDateTime endDate) {
        this.banId = new BanId(user, admin);
        this.reason = reason;
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getGetStartDate() {
        return banId.startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public User getUser() {
        return banId.user;
    }

    @Embeddable
    private static class BanId implements Serializable {
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne
        @JoinColumn(name = "admin_id", referencedColumnName = "user_id", nullable = false)
        private User admin;

        @Column(name = "start_date")
        @Convert(converter = LocalDateTimeConverter.class)
        private final LocalDateTime startDate = LocalDateTime.now();

        private BanId(User user, User admin) {
            this.user = user;
            this.admin = admin;
        }

        public User getUser() {
            return user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BanId banId = (BanId) o;
            return Objects.equals(user, banId.user) && Objects.equals(admin, banId.admin) && Objects.equals(startDate, banId.startDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, admin, startDate);
        }
    }
}
