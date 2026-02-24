package com.org.iopts.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Home Controller - Root endpoint & Health check
 */
@RestController
public class HomeController {

    @Hidden
    @GetMapping("/")
    public void home(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login.html");
    }

    @Hidden
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "application", "PICenter TOTALS API",
                "version", "v1.0.0"
        );
    }
}
