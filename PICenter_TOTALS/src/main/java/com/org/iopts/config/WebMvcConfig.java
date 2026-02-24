package com.org.iopts.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login.html");
        registry.addViewController("/login").setViewName("forward:/login.html");
        registry.addViewController("/picenter_target").setViewName("forward:/picenter_target.html");
        registry.addViewController("/picenter_dashboard").setViewName("forward:/picenter_dashboard.html");
        registry.addViewController("/picenter_detection").setViewName("forward:/picenter_detection.html");
        registry.addViewController("/picenter_scan").setViewName("forward:/picenter_scan.html");
        registry.addViewController("/picenter_report").setViewName("forward:/picenter_report.html");
        registry.addViewController("/picenter_statistics").setViewName("forward:/picenter_statistics.html");
        registry.addViewController("/picenter_user").setViewName("forward:/picenter_user.html");
        registry.addViewController("/picenter_notice").setViewName("forward:/picenter_notice.html");
        registry.addViewController("/picenter_setting").setViewName("forward:/picenter_setting.html");
    }
}
