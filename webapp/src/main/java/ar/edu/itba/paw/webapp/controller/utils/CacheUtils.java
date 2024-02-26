package ar.edu.itba.paw.webapp.controller.utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

public class CacheUtils {
    public static final int MAX_TIME_UNCONDITIONAL = 31536000; // 1 year
    public static final int MAX_TIME_CONDITIONAL = 86400; // 1 day
    private CacheUtils() {
    }

    public static Response.ResponseBuilder conditionalCache(final Response.ResponseBuilder responseBuilder, final Request request, final int entityHashCode) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(MAX_TIME_CONDITIONAL);
        final EntityTag tag = new EntityTag(Integer.toString(entityHashCode));
        final Response.ResponseBuilder builder = request.evaluatePreconditions(tag);
        if (builder != null) return builder.cacheControl(cacheControl).tag(tag);
        return responseBuilder.cacheControl(cacheControl).tag(tag);
    }

    public static Response.ResponseBuilder unconditionalCache(final Response.ResponseBuilder responseBuilder) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(MAX_TIME_UNCONDITIONAL);
        responseBuilder.cacheControl(cacheControl);
        return responseBuilder;
    }
}

