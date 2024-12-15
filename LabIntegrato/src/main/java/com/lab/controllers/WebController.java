package com.lab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
	
    @GetMapping("/registrazione")
    public String mostraFormRegistrazione() {
        return "registrazione";
    }
	
}

