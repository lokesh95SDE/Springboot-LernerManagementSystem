package com.springAi.LernerManagementSystem.filters;

import com.springAi.LernerManagementSystem.Util.JwtUtil;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class jwtFilter extends OncePerRequestFilter {

//    check token is available
//    if present and valid check is expired
//    If not expired then request will go to Filter chain or 401 unauthorized
//    we should not check token for /Register and /Signin and /verify and /h2-console/** else we will get 401 unauthorized for these endpoints as well because we are not sending token in these requests

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No JWT present -> continue normal Spring Security flow
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            Claims claims = JwtUtil.VerifyToken(token);

            String role = claims.get("roles", String.class);

            List<SimpleGrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority(role));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities);

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

        } catch (ExpiredJwtException |
                 SignatureException |
                 MalformedJwtException |
                 UnsupportedJwtException ex) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid JWT Token");
            return;
        }

        filterChain.doFilter(request, response);
    }


    public boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.contains("/Register") || path.contains("/Signin") || path.contains("/verify") || path.contains("/h2-console");
    }



//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
////    if(request.getRequestURL().toString().contains("/Register") || request.getRequestURL().toString().contains("/Signin") || request.getRequestURL().toString().contains("/verify") || request.getRequestURL().toString().contains("/h2-console")){
////        filterChain.doFilter(request,response);
////        return;
////    } ------> insted f this we can use shouldNotFilter method which is more elegant and clean way to achieve the same result
//        String authHeader = request.getHeader("Authorization");
//
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Unauthorized: Missing or invalid token");
//            return;
//        }
//        String token = authHeader.substring(7);
//    try{
//        Claims generatedClaims = JwtUtil.VerifyToken(token);
//        String role = generatedClaims.get("roles", String.class);
//        List<SimpleGrantedAuthority> authorityLis = List.of(new SimpleGrantedAuthority(role));
//        System.out.println("Role from JWT = " + role);
//        System.out.println("Authority List = " + authorityLis);
//        SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(generatedClaims.getSubject(), null, authorityLis));
//    }catch (ExpiredJwtException | SignatureException exception){
//        exception.printStackTrace();
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Unauthorized: Invalid token");
//        return;
//    } catch (io.jsonwebtoken.MalformedJwtException exception){
//        exception.printStackTrace();
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Unauthorized: Invalid token");
//        return;
//    }catch (io.jsonwebtoken.UnsupportedJwtException exception){
//        exception.printStackTrace();
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Unauthorized: Invalid token");
//        return;
//    }
//    filterChain.doFilter(request,response);
//    }



}
