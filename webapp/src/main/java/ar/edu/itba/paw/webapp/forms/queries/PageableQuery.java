package ar.edu.itba.paw.webapp.forms.queries;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public abstract class PageableQuery {
    @Min(value=1, message = "{validation.param.min}")
    @QueryParam("page")
    @DefaultValue("1")
    private int page;

    @Range(min = 1, max = 64, message = "{validation.param.range}")
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
