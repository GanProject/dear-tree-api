package com.dear_tree.dear_tree.filter;

import com.dear_tree.dear_tree.common.ResponseCode;
import com.dear_tree.dear_tree.common.ResponseMessage;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;
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

        // Swagger 관련 요청은 JWT 검증 제외
        String uri = request.getRequestURI();

        if (uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui") || uri.startsWith("/swagger-resources") || uri.startsWith("/auth/sign-up") || uri.startsWith("/auth/sign-in") || uri.startsWith("/auth/check-username") || uri.startsWith("/auth//access-token/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

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

        if (accessToken == "") {
            log.error("액세스토큰 없음");
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(),
                    ResponseMessage.INVALID_ACCESS_TOKEN);
            return;
        }

        try {
            //블랙리스트에 있는 지 확인
            String redisKey = BLACKLIST_PREFIX + accessToken;
            Boolean isBlacklisted = redisTemplate.hasKey(redisKey);
            if (Boolean.TRUE.equals(isBlacklisted)) {
                log.error("Token is blacklisted");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(),
                        ResponseMessage.INVALID_ACCESS_TOKEN);
                return;
            }

            // 토큰 만료 여부 확인
            if (JwtUtil.isExpired(accessToken)) {
                log.error("Token is expired");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(),
                        ResponseMessage.INVALID_ACCESS_TOKEN);
                return;
            }

            // 토큰에서 username 추출
            String username = JwtUtil.getUsername(accessToken);
            log.info("Extracted Username : {}", username);

            // 토큰 검증
            if (!JwtUtil.validateToken(accessToken, username)) {
                log.error("Invalid token");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(),
                        ResponseMessage.INVALID_ACCESS_TOKEN);
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
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(),
                    ResponseMessage.INVALID_ACCESS_TOKEN);
            return;
        } catch (JwtException e) { // JWT 예외 처리
            log.error("JWT exception: {}", e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(),
                    ResponseMessage.INVALID_ACCESS_TOKEN);
            return;
        } catch (Exception e) { // 기타 예외 처리
            log.error("Unexpected error occurred while processing the JWT: {}", e.getMessage());
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ResponseMessage.INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseDto responseBody = new ResponseDto(status, message);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        response.getWriter().write(jsonResponse);
    }
}
