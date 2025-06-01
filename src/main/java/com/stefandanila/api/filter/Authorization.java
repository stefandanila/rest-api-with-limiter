package com.stefandanila.api.filter;

import com.stefandanila.api.service.client.ClientService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class Authorization extends OncePerRequestFilter {
    private final ClientService clientService;

    @Autowired
    public Authorization(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        String bearer = "bearer ";
        if (requestHeader != null && requestHeader.toLowerCase().startsWith(bearer)) {
            String clientId = requestHeader.substring(bearer.length());
            if (clientService.isValid(clientId)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        unauthorizedResponse(response);
    }

    private static void unauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.getType());
        response.getWriter().write("{\"error\": \"Unauthorized\"}");
    }
}
