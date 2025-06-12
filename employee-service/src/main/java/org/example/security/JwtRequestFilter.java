package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("🛠️ [EmployeeService] Request received: " + request.getRequestURI());
        String path = request.getRequestURI();
        String method = request.getMethod();
        System.out.println("🔥 JwtRequestFilter called for path: " + path + " method: " + method);

        // ✅ 1. OPTIONS 요청은 바로 통과
        if ("OPTIONS".equalsIgnoreCase(method)) {
            System.out.println("✅ Preflight request allowed");
            response.setStatus(HttpServletResponse.SC_OK);
            return; // ❗주의: 여기서 filterChain.doFilter() 호출 안 함
        }

        // ✅ 2. 로그인/회원가입 요청은 필터 건너뜀
        if (path.startsWith("/auth/register") || path.startsWith("/auth/login")) {
            System.out.println("✅ register and login filter skipped");
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 3. 토큰 검사
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, "", userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
