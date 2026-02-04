package com.org.iopts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/swagger")
public class SwaggerController {

    @GetMapping({"/", "/index.html"})
    public String swaggerUi() {
        return "forward:/swagger-ui/index.html";
    }
}
