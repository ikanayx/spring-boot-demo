package space.itzkana.interceptor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

public class HttpReqHeaderLocaleChangeInterceptor extends LocaleChangeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(HttpReqHeaderLocaleChangeInterceptor.class);

    /**
     * try to get locale from header, if not exist then get it from request parameter.
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return bool
     * @throws ServletException ServletException
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws ServletException {
        String newLocale = request.getHeader(getParamName());
        if (newLocale != null) {
            if (checkHttpMethods(request.getMethod())) {
                LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
                if (localeResolver == null) {
                    throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
                }
                try {
                    localeResolver.setLocale(request, response, parseLocaleValue(newLocale));
                } catch (IllegalArgumentException ex) {
                    if (isIgnoreInvalidLocale()) {
                        log.debug("Ignoring invalid locale value [{}]: {}", newLocale, ex.getMessage());
                    } else {
                        throw ex;
                    }
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    private boolean checkHttpMethods(String currentMethod) {
        String[] configuredMethods = getHttpMethods();
        if (ObjectUtils.isEmpty(configuredMethods)) {
            return true;
        }
        for (String configuredMethod : configuredMethods) {
            if (configuredMethod.equalsIgnoreCase(currentMethod)) {
                return true;
            }
        }
        return false;
    }
}
