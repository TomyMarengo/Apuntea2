package ar.edu.itba.paw.webapp.helpers;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class LocaleHelper {
    private LocaleHelper() {
    }
    public static Locale getLocale() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request == null) {
            return Locale.getDefault();
        }
        return request.getLocale();
    }
}
