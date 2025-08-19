
package com.example.system.configuration.filter;

import com.example.system.configuration.JwtUtils;
import com.example.system.service.impl.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userName;
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.replace("Bearer ", "");
                Claims claims = jwtUtils.parseToken(jwtToken);
                userName = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtils.validateToken(jwtToken, userDetails)) {
                        Authentication auth = new UsernamePasswordAuthenticationToken(userName, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        } catch (ExpiredJwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            log.info("Token expired");
            return;
        } catch (MalformedJwtException | SignatureException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token");
            log.info("Invalid JWT token");
            return;
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication error: " + ex.getMessage());
            log.info("Authentication error: {}", ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
