package com.signalm.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
class LoginController {


    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(HttpServletResponse response)
    {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        return "fancy-login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied()
    {
        return "access-denied";
    }

    @PostMapping("/access-denied")
    public String showAccessDeniedPost()
    {
        return "access-denied";
    }

}
