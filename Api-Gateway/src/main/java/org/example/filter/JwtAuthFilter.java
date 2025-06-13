//package org.example.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import org.example.dto.AuthUser;
//
//import org.example.util.JwtUtil;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter  {
//
//    private final JwtUtil jwtUtil;
//
//    public JwtAuthFilter(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        final String token;
//        String username;
//        List<SimpleGrantedAuthority> roleDtoList;
//        if (authHeader == null || !authHeader.startsWith("Bearer")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        token = authHeader.substring(7);
//        username = jwtUtil.extractUsername(token);
//
//        roleDtoList = jwtUtil.extractRoles(token);
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            AuthUser authUser =  new AuthUser(username,roleDtoList, null);
//            if (jwtUtil.validateToken(token)) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        authUser, null, authUser.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//
//
////    @Override
////    public GatewayFilter apply(Config config) {
////
////        return (exchange, chain) -> {
////            HttpMethod method = exchange.getRequest().getMethod();
////            String path = exchange.getRequest().getURI().getPath();
////            System.out.println("🔥 [JwtAuthFilter] Incoming request: " + method + " " + path);
////            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
////                System.out.println("✅ [JwtAuthFilter] Preflight request passed through");
////                return chain.filter(exchange);
////            }
////
////            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
////                System.out.println("❌ [JwtAuthFilter] Missing or invalid Authorization header");
////                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
////                return exchange.getResponse().setComplete();
////            }
////
////            String token = authHeader.substring(7);
////            if (!jwtUtil.validateToken(token)) {
////                System.out.println("❌ [JwtAuthFilter] Invalid JWT token");
////                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
////                return exchange.getResponse().setComplete();
////            }
////            System.out.println("✅ [JwtAuthFilter] Valid JWT token, proceeding");
////            return chain.filter(exchange);
////        };
////    }
//
////    public static class Config {
////        // 빈 설정 클래스 (필요하면 값 전달 가능)
////    }
//}
