package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

public class ApunteaUserDetails extends User {

    private final UUID userId;

    public ApunteaUserDetails(UUID userId, String email, String password,
                              boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                              Collection<? extends GrantedAuthority> authorities) {
        super(email, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    public ApunteaUserDetails(UUID userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}