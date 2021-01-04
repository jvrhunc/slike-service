package si.fri.rso.slike.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class SlikeInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SlikeInterceptor.class);
    private static final String REQUEST_ID = "";

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${spring.profiles.active}")
    String environment;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UUID reqId = UUID.randomUUID();
        request.setAttribute(REQUEST_ID, reqId);

        logger.info(applicationName + ", " + environment + " - ENTRY (" + reqId + ") - " + request.getMethod() + " - "
                + request.getRequestURI());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        logger.info(applicationName + ", " + environment + " - EXIT (" + request.getAttribute(REQUEST_ID) + ") - "
                + request.getMethod() + " - " + request.getRequestURI());
    }

}
