package ar.edu.itba.paw.webapp.forms.queries;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public abstract class PageableQuery {
    @Min(1)
    @QueryParam("page")
    @DefaultValue("1")
    private int page;

    @Range(min = 4, max = 24)
    @QueryParam("pageSize")
    @DefaultValue("12")
    private int pageSize;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
