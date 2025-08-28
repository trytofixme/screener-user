package ru.screener.user.integrations.telegram;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TelegramSecretFilter extends OncePerRequestFilter {

    @Value("${telegram.webhook-secret}")
    private String webhookSecret;

    private static final String HEADER = "X-Telegram-Bot-Api-Secret-Token";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/telegram/webhook");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String secret = request.getHeader(HEADER);
        if (secret == null || !secret.equals(webhookSecret)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("forbidden");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
