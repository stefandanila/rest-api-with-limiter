package com.stefandanila.api.filter;

import com.stefandanila.api.service.limiter.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RateLimiterFilter extends OncePerRequestFilter {
    private final RateLimiterService rateLimiterService;

    public RateLimiterFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //This applies after the authorization, should be safe to assume the header is correct
        String requestHeader = request.getHeader("Authorization");
        String bearer = "bearer ";
        String clientId = requestHeader.substring(bearer.length());

        if (rateLimiterService.isRequestAllowed(clientId, request.getRequestURI())) {
            doFilter(request, response, filterChain);
        } else {
            rateLimitReached(response);
        }
    }

    private static void rateLimitReached(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON.getType());
        response.getWriter().write("{\"error\": \"Rate limit exceeded\"}");
    }
}
