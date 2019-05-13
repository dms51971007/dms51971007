package com.signalm.manager.controller;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {

    @RequestMapping("/")
    public String defaultTask() {
        return "redirect:/task/tasklist?user_id=-1&page=1";
    }
}
