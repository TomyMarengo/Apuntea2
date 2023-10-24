package ar.edu.itba.paw.models.user;

import ar.edu.itba.paw.models.converter.LocalDateTimeConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "verification_codes")
@IdClass(VerificationCode.VerificationCodeKey.class)
public class VerificationCode {
    @Id
    private String code;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @Column(name = "expires_at")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime expirationDate;

    /* package-private */ VerificationCode() {}
    public VerificationCode(String code, User user, LocalDateTime expirationDate) {
        this.code = code;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public static class VerificationCodeKey implements Serializable {
        private String code;
        private User user;
        private LocalDateTime expirationDate;

        /* package-private */ VerificationCodeKey() {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VerificationCodeKey that = (VerificationCodeKey) o;
            return Objects.equals(code, that.code) && Objects.equals(user, that.user) && Objects.equals(expirationDate, that.expirationDate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, user, expirationDate);
        }
    }


}
