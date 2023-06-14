package com.demo.coffee_store_service.controller.Filter;

import com.demo.coffee_store_service.DTO.OperationResponse;
import com.demo.coffee_store_service.DTO.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecretKeyFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(SecretKeyFilter.class);

    final public static String SECRET_KEY_HEADER_NAME = "SECRET_KEY";
    final public static String SECRET_KEY_HEADER_VALUE = "MY_COFFEE_STORE_SECRET_KEY";


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        final  String ADMIN_API_PREFIX = "admin";
        String givenSecretKey = httpRequest.getHeader(SECRET_KEY_HEADER_NAME);

        if ( ((HttpServletRequest) req).getRequestURI().contains(ADMIN_API_PREFIX) &&
                (givenSecretKey==null || !givenSecretKey.equals(SECRET_KEY_HEADER_VALUE))) {
            log.error(String.format("Failed to consume Admin API %s, because not providing the secret key.",
                    ((HttpServletRequest) req).getRequestURI()));

            response.sendError (HttpStatus.UNAUTHORIZED.value(),"Not Allowed to Consume This API");
            return;
        }


        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}

