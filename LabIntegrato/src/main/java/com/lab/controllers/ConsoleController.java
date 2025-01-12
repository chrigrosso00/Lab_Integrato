package com.lab.controllers;

import com.lab.entities.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("console")
public class ConsoleController {

    @PostMapping("giveAutoDiscount")
    public ResponseEntity<?> applicaSconto(@RequestParam Long clienteId,@RequestParam int percentuale){

        //TODO
        //applica degli sconti automaticamente,raggiunti determinati livelli di fedeltà

        return ResponseEntity.ok().build();
    }
    @PostMapping("createAutoReport")
    public ResponseEntity<?> createAutoReport(@RequestParam Report report){

        //TODO
        //crea dei report automatici,in base agli incidenti randomici che ci sono durante l'attività,
        // senza nessun supervisore,ogni volta che questo verrà inviato un supervisore lo dovrà aprire


        return ResponseEntity.ok().build();
    }


}
