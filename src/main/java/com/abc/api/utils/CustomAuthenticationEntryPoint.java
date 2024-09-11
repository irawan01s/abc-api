package com.abc.api.utils;

import com.abc.api.payload.response.WebResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        WebResponse<Object> webResponse = WebResponse.builder()
                .status(false)
                .errors("Unauthorized access - " + authException.getMessage())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(webResponse));
//        Map<String, Object> data = new HashMap<>();
//        data.put("status", HttpServletResponse.SC_UNAUTHORIZED);
//        String errorMessage = (String) request.getAttribute("error_message");
//        data.put("message", Objects.requireNonNullElse(errorMessage, "Unauthorized access"));
//
//        response.getOutputStream().println(new ObjectMapper().writeValueAsString(data));


    }
}
