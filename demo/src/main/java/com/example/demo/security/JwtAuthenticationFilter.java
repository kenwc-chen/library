package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 從請求中獲取 JWT token
        String token = getTokenFromRequest(request);

        // 檢查 SecurityContext 中是否已經有身份驗證
        if (token != null && jwtTokenProvider.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 如果沒有身份驗證，則從 token 中解析 phoneNumber 並設置身份
            String phoneNumber = jwtTokenProvider.getPhoneNumberFromToken(token);

            // 創建使用者詳細信息
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(phoneNumber, "", new ArrayList<>());

            // 創建身份驗證對象
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 設置詳細信息
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 將身份驗證設置到 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 繼續過濾器鏈
        filterChain.doFilter(request, response);
    }

    // 從請求頭中獲取 JWT token
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // 去掉 "Bearer " 前綴
        }
        return null;
    }
}
