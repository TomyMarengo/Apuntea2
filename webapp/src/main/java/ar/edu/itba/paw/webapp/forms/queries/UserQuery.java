package ar.edu.itba.paw.webapp.forms.queries;

import ar.edu.itba.paw.webapp.validation.ValidUuid;

import javax.ws.rs.QueryParam;
import java.util.UUID;

public class UserQuery extends PageableQuery {
    @ValidUuid
    @QueryParam("followedBy")
    private UUID followedBy;

    @QueryParam("query")
    private String query;

    @QueryParam("status")
    private String status;

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
}
