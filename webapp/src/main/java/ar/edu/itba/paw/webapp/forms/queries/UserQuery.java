package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.EitherAttribute;
import ar.edu.itba.paw.webapp.validation.ValidUuid;
import org.hibernate.validator.constraints.Email;

import javax.ws.rs.QueryParam;
import java.util.UUID;

@EitherAttribute(fieldGroup1 = {"email"}, fieldGroup2 = {"followedBy", "query", "status"})
public class UserQuery extends PageableQuery {
    @ValidUuid
    @QueryParam("followedBy")
    private UUID followedBy;

    @ValidUuid
    @QueryParam("following")
    private UUID following;

    @QueryParam("query")
    private String query;

    @QueryParam("status")
    private String status;

    @QueryParam("email")
    @Email
    private String email;

    public UUID getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(UUID followedBy) {
        this.followedBy = followedBy;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getFollowing() {
        return following;
    }

    public void setFollowing(UUID following) {
        this.following = following;
    }
}
