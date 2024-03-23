package com.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelpController {

    @GetMapping("/help")
    public String helpPage() {
        return "help"; // This will return the index.html page
    }

}
