package com.org.iopts.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Home Controller - Root endpoint, Health check & Page routes
 *
 * Provides root redirect, health check endpoint, and page routing
 * for all SPA (Single Page Application) front-end pages.
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

    /* ============================================================
     * Page routes - redirect SPA page paths to their HTML files.
     * These support both the legacy JSP-style URL paths and
     * the new clean URL paths used by the front-end router.
     * ============================================================ */

    @Hidden
    @GetMapping("/dashboard")
    public void dashboard(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_dashboard.html");
    }

    @Hidden
    @GetMapping("/detection")
    public void detection(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_detection.html");
    }

    @Hidden
    @GetMapping("/approval")
    public void approval(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_approval.html");
    }

    @Hidden
    @GetMapping("/exception")
    public void exception(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_exception.html");
    }

    @Hidden
    @GetMapping("/search")
    public void search(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_search.html");
    }

    @Hidden
    @GetMapping("/remediation")
    public void remediation(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_remediation.html");
    }

    @Hidden
    @GetMapping("/report")
    public void report(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_report.html");
    }

    @Hidden
    @GetMapping("/statistics")
    public void statistics(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_statistics.html");
    }

    @Hidden
    @GetMapping("/targets")
    public void targets(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_target.html");
    }

    @Hidden
    @GetMapping("/users")
    public void users(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_user.html");
    }

    @Hidden
    @GetMapping("/notice")
    public void notice(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_notice.html");
    }

    @Hidden
    @GetMapping("/faq")
    public void faq(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_faq.html");
    }

    @Hidden
    @GetMapping("/settings")
    public void settings(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_setting.html");
    }

    @Hidden
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_download.html");
    }

    @Hidden
    @GetMapping("/scan")
    public void scan(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_scan.html");
    }

    @Hidden
    @GetMapping("/user-log")
    public void userLog(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_userlog.html");
    }

    @Hidden
    @GetMapping("/lockdown")
    public void lockdown(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_lockdown.html");
    }

    @Hidden
    @GetMapping("/interlock")
    public void interlock(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_interlock.html");
    }

    @Hidden
    @GetMapping("/license")
    public void license(HttpServletResponse response) throws IOException {
        response.sendRedirect("/picenter_license.html");
    }
}
