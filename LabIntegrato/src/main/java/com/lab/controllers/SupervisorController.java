package com.lab.controllers;

import com.lab.DTO.ReportDTO;
import com.lab.entities.Report;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/supervisor")
public class SupervisorController {

    @PostMapping("createReport")
    public ResponseEntity<?> makeReport(@RequestBody Report report) {
        //TODO
        //permette di postare un report da parte di un supervisore

        return ResponseEntity.ok().body("TODO");
    }
   @PostMapping("updateReport")
   public ResponseEntity<?> updateReport(@RequestBody Report report) {


        return ResponseEntity.ok().body("todo");
   }

    @PutMapping("setScore")
    public ResponseEntity<?> updateUserScore(@RequestParam Long userId, @RequestParam Long score) {

        //TODO
        //permette di aggiornare lo score di un determinato user,valore in base a cui verranno dati premi
        //O DECURTATI SOLDI A QUESTO INFAME

        return ResponseEntity.ok().body("TODO");
    }
}
