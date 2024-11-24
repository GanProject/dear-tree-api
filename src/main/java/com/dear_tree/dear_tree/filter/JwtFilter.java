package com.dear_tree.dear_tree.filter;

import com.dear_tree.dear_tree.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "Blacklist:";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        //쿠키들 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.error("쿠키 없음");
            filterChain.doFilter(request, response);
            return;
        }

        //액세스토큰 찾기
       var accessToken = "";
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("accessToken")) {
                accessToken = cookies[i].getValue();
            }
        }
        log.info("access token : {}", accessToken);
        if (accessToken == "") {
            log.error("액세스토큰 없음");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //블랙리스트에 있는 지 확인
            String redisKey = BLACKLIST_PREFIX + accessToken;
            Boolean isBlacklisted = redisTemplate.hasKey(redisKey);
            if (Boolean.TRUE.equals(isBlacklisted)) {
                log.error("Token is blacklisted");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted.");
                return;
            }

            // 토큰 만료 여부 확인
            if (JwtUtil.isExpired(accessToken)) {
                log.error("Token is expired");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired.");
                return;
            }

            // 토큰에서 username 추출
            String username = JwtUtil.getUsername(accessToken);
            log.info("Extracted Username : {}", username);

            // 토큰 검증
            if (!JwtUtil.validateToken(accessToken, username)) {
                log.error("Invalid token");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
                return;
            }

            // 권한 부여
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("USER")));

            //detail 넣어주기
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (ExpiredJwtException e) { // 토큰 만료 예외 처리
            log.error("Token expired exception: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired.");
            return;
        } catch (JwtException e) { // JWT 예외 처리
            log.error("JWT exception: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
            return;
        } catch (Exception e) { // 기타 예외 처리
            log.error("Unexpected error occurred while processing the JWT: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
