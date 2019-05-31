package com.kovzel.controller;

import com.kovzel.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

public class MainController {
    @GetMapping("/home")
    public String getHome(@ModelAttribute User user) {
        return "index";
    }
}
