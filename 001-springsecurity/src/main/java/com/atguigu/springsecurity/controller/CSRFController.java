package com.atguigu.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CSRFController {

    @GetMapping("/to-update")
    public String test(Model model){
        return "csrfTest";
    }
}
