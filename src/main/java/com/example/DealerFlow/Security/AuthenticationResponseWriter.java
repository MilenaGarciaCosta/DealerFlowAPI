package com.example.DealerFlow.Security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class AuthenticationResponseWriter {

    public static final String AUTHENTICATION_REQUIRED_MESSAGE = "Authentication required";

    private AuthenticationResponseWriter() {
    }

    public static void writeUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"message\":\"" + AUTHENTICATION_REQUIRED_MESSAGE + "\"}");
    }
}
