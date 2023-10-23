package ar.edu.itba.paw.models.user;

import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.institutional.Institution;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(length = 30, unique = true)
    private String username;
    @Column(length = 320, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;
    @ManyToOne(fetch = FetchType.LAZY)
    private Career career;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private Collection<Role> roles;
    @Column(nullable = false)
    private String locale;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /* package-private */ User() {
    }

    private User(UserBuilder builder) {
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.institution = builder.institution;
        this.career = builder.career;
        this.roles = builder.roles;
        this.locale = builder.locale;
        this.status = builder.status;
    }

    public String getEmail() {
        return email;
    }
    public UUID getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public Collection<Role> getRoles() {
        return roles;
    }
    public String getLocale() {
        return locale;
    }
    public Institution getInstitution() {
        return institution;
    }

    public Career getCareer() {
        return career;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public boolean getIsAdmin() {
        return roles != null && roles.stream().anyMatch(role -> role == Role.ROLE_ADMIN);
    }

    public boolean isBanned() {
        return status == UserStatus.BANNED;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getDisplayName() {
        if (username != null) {
            return username;
        } else if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return email;
    }

    public static class UserBuilder {
        private UUID userId;
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private String password;
        private Institution institution;
        private Career career;
        private Collection<Role> roles;
        private String locale;
        private UserStatus status;

        public UserBuilder() {
        }

        private UserBuilder(User user) {
            this.userId = user.userId;
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.username = user.username;
            this.email = user.email;
            this.password = user.password;
            this.institution = user.institution;
            this.career = user.career;
            this.roles = user.roles;
            this.locale = user.locale;
            this.status = user.status;
        }

        public UserBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder institution(Institution institution) {
            this.institution = institution;
            return this;
        }

        public UserBuilder career(Career career) {
            this.career = career;
            return this;
        }

        public UserBuilder roles(String[] roles) {
            // TODO: Make roles a Collection
            this.roles = Arrays.stream(roles).map(Role::getRole).collect(Collectors.toList());
            return this;
        }

        public UserBuilder locale(String locale) {
            this.locale = locale;
            return this;
        }

        public UserBuilder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public static UserBuilder from(User user) {
            return new UserBuilder(user);
        }

        public User build() {
            return new User(this);
        }
    }
}
