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
    
    @GetMapping("/cliente/prova")
    public String mostraProva() {
        return "prova";
    }
    
    @GetMapping("/cliente/dashboard")
    public String mostraDashboardCliente() {
        return "dashboardCliente";
    }
    
    @GetMapping("/admin/dashboard")
    public String mostraDashboardAdmin() {
        return "dashboardAdmin";
    }
    
    @GetMapping("/cliente/crea/ordine")
    public String mostraFormOrdine() {
        return "creaOrdine";
    }
    
    @GetMapping("/cliente/storico/ordine")
    public String mostraStoricoOrdine() {
        return "storicoOrdiniCliente";
    }
	
}
