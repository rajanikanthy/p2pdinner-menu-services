package com.p2pdinner.filters;

import com.p2pdinner.services.ProfileServicesProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rajaniy on 11/4/16.
 */
@Component
public class ProfileValidatorFilter extends OncePerRequestFilter {

    private static final Logger _logger = LoggerFactory.getLogger(ProfileValidatorFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        PathMatcher pathMatcher = new AntPathMatcher();
        if (pathMatcher.match("/**/menuitem/**", httpServletRequest.getRequestURI())) {
            _logger.info("Matches menuitem path....");
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
