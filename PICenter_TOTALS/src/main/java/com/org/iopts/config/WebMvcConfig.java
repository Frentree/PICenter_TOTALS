package com.org.iopts.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Login & Root
        registry.addViewController("/").setViewName("forward:/login.html");
        registry.addViewController("/login").setViewName("forward:/login.html");

        // Existing pages
        registry.addViewController("/picenter_dashboard").setViewName("forward:/picenter_dashboard.html");
        registry.addViewController("/picenter_target").setViewName("forward:/picenter_target.html");
        registry.addViewController("/picenter_detection").setViewName("forward:/picenter_detection.html");
        registry.addViewController("/picenter_scan").setViewName("forward:/picenter_scan.html");
        registry.addViewController("/picenter_report").setViewName("forward:/picenter_report.html");
        registry.addViewController("/picenter_statistics").setViewName("forward:/picenter_statistics.html");
        registry.addViewController("/picenter_user").setViewName("forward:/picenter_user.html");
        registry.addViewController("/picenter_notice").setViewName("forward:/picenter_notice.html");
        registry.addViewController("/picenter_setting").setViewName("forward:/picenter_setting.html");

        // New pages - Target sub-menus
        registry.addViewController("/picenter_target_group").setViewName("forward:/picenter_target_group.html");
        registry.addViewController("/picenter_global_filters").setViewName("forward:/picenter_global_filters.html");

        // New pages - Detection sub-menus
        registry.addViewController("/picenter_exception").setViewName("forward:/picenter_exception.html");
        registry.addViewController("/picenter_approval").setViewName("forward:/picenter_approval.html");

        // New pages - Scan sub-menus
        registry.addViewController("/picenter_search_regist").setViewName("forward:/picenter_search_regist.html");
        registry.addViewController("/picenter_search_list").setViewName("forward:/picenter_search_list.html");

        // New pages - Report sub-menus
        registry.addViewController("/picenter_report_except").setViewName("forward:/picenter_report_except.html");

        // New pages - User sub-menus
        registry.addViewController("/picenter_userlog").setViewName("forward:/picenter_userlog.html");
        registry.addViewController("/picenter_user_lockdown").setViewName("forward:/picenter_user_lockdown.html");

        // New pages - Notice sub-menus
        registry.addViewController("/picenter_faq").setViewName("forward:/picenter_faq.html");
        registry.addViewController("/picenter_download").setViewName("forward:/picenter_download.html");

        // New pages - Setting sub-menus
        registry.addViewController("/picenter_node").setViewName("forward:/picenter_node.html");
        registry.addViewController("/picenter_interlock").setViewName("forward:/picenter_interlock.html");

        // Data Type page (개인정보 유형)
        registry.addViewController("/picenter_data_type").setViewName("forward:/picenter_data_type.html");
        registry.addViewController("/search/data_type").setViewName("forward:/picenter_data_type.html");
    }
}
