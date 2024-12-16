package com.lab.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class WebController {
	
    @GetMapping("/registrazione")
    public String mostraFormRegistrazione() {
        return "registrazione";
    }
    
    @GetMapping("/login")
    public String mostraFormLogin() {
        return "login";
    }
    
    @GetMapping("/cliente/ordine")
    public String mostraFormOrdine() {
        return "creaOrdine";
    }
	
}

