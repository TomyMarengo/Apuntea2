package ar.edu.itba.paw.webapp.controller.utils;

import ar.edu.itba.paw.models.Page;

import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class ControllerUtils {

    public static <T> ResponseBuilder addPaginationLinks(final ResponseBuilder rb, final UriInfo uriInfo, final Page<T> page) {
        final UriBuilder ub = uriInfo.getRequestUriBuilder();
        return rb.link(ub.replaceQueryParam("page",  "1").build(), "first")
                .link(ub.replaceQueryParam("page",  page.getTotalPages()).build(), "last")
                .link(ub.replaceQueryParam("page",  Integer.toString( page.getPreviousPage())).build(), "prev")
                .link(ub.replaceQueryParam("page",  Integer.toString(page.getNextPage())).build(), "next")
                .header("X-Total-Count", page.getTotalResults())
                .header("X-Total-Pages", page.getTotalPages());
    }

    private ControllerUtils() {
    }
}

