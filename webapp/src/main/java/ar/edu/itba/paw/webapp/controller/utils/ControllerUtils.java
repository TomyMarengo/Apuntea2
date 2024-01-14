package ar.edu.itba.paw.webapp.controller.utils;

import ar.edu.itba.paw.models.Page;

import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class ControllerUtils {

    public static <T> ResponseBuilder addPaginationLinks(ResponseBuilder rb, UriBuilder ub, Page<T> page) {
        return rb.link(ub.clone().queryParam("page",  "1").build(), "first")
                .link(ub.clone().queryParam("page",  page.getTotalPages()).build(), "last")
                .link(ub.clone().queryParam("page",  Integer.toString( page.getPreviousPage())).build(), "prev")
                .link(ub.clone().queryParam("page",  Integer.toString(page.getNextPage())).build(), "next");
    }

    private ControllerUtils() {
    }
}

