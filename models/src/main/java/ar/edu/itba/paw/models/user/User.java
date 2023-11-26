package ar.edu.itba.paw.models.user;

import ar.edu.itba.paw.models.directory.Directory;
import ar.edu.itba.paw.models.institutional.Career;
import ar.edu.itba.paw.models.note.Note;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @JoinColumn(name = "career_id")
    private Career career;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private Collection<Role> roles;
    @Column(nullable = false)
    private String locale;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "notifications_enabled", nullable = false)
    private Boolean notificationsEnabled;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id")
    private Image profilePicture;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    @JoinTable(
            name = "Note_Favorites",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "note_id" )}
    )
    private Set<Note> noteFavorites = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    @JoinTable(
            name = "Directory_Favorites",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "directory_id" )}
    )
    private Set<Directory> directoryFavorites = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("email ASC")
    @JoinTable(
            name = "Follows",
            joinColumns = { @JoinColumn(name = "follower_id") },
            inverseJoinColumns = { @JoinColumn(name = "followed_id")}
    )
    private Set<User> usersFollowing = new HashSet<>();

    /* package-private */ User() {
    }

    private User(UserBuilder builder) {
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.career = builder.career;
        this.roles = builder.roles;
        this.locale = builder.locale;
        this.status = builder.status;
        this.notificationsEnabled = true;
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
    public Locale getLocale() {
        return new Locale(locale);
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

    public Image getProfilePicture() {
        return profilePicture;
    }

    public UUID getInstitutionId() {
        return career.getInstitutionId();
    }

    public Boolean hasNotificationsEnabled() {
        return notificationsEnabled;
    }

    public String getDisplayName() {
        if (username != null) {
            return username;
        } else if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    public Set<Note> getNoteFavorites() {
        return noteFavorites;
    }

    public Set<Directory> getDirectoryFavorites() {
        return directoryFavorites;
    }

    public Set<User> getUsersFollowing() {
        return usersFollowing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    // TODO: Remove
    public static class UserBuilder {
        private UUID userId;
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private String password;
        private Career career;
        private Collection<Role> roles;
        private String locale;
        private UserStatus status;

        public UserBuilder() {
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

        public UserBuilder career(Career career) {
            this.career = career;
            return this;
        }

        public UserBuilder roles(Collection<Role> roles) {
            this.roles = roles;
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

        public User build() {
            return new User(this);
        }
    }
}
