package com.lab.controllers;

import com.lab.DTO.UtenteRegistrationDTO;
import com.lab.customException.ForbiddenException;
import com.lab.customException.UnauthorizedException;
import com.lab.entities.Ordine;
import com.lab.entities.Role;
import com.lab.entities.RoleName;
import com.lab.entities.User;
import com.lab.repos.*;
import com.lab.services.OrdineService;
import com.lab.services.UserService;
import com.lab.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    OrdineDAO ordineDAO;
    @Autowired
    OrdineService ordineService;
    @Autowired
    ClienteDAO clienteDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    CostiDAO costiDAO;
    @Autowired
    RoleDAO roleRepository;
    @Autowired
    private JwtUtil jwtUtil;


    private Integer validaTokenAdmin(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Autenticazione richiesta.");
        }
        String token = authorizationHeader.substring(7);
        Integer idAdmin = jwtUtil.extractIdUtente(token);
        List<String> roles = jwtUtil.extractRoles(token);
        if (roles == null || !roles.contains("ROLE_ADMIN")) {
            throw new ForbiddenException("Accesso negato. Ruolo non autorizzato.");
        }
        return idAdmin;
    }


    @GetMapping("/dashboard")
    public String dashboard(@RequestHeader("Authorizarion") String authorizationHeader) {
        try{
            validaTokenAdmin(authorizationHeader);
            return "Welcome Admin!";
        }catch (ForbiddenException e){
            return "Accesso negato";
        }

    }

    @GetMapping("/storicoOrdini/all")
    public ResponseEntity<?> getStoricoAllOrdini(){

        List<Ordine> ordini = ordineDAO.findAll();
        return ResponseEntity.ok().body(ordini);
    }
    @GetMapping("/storicoOrdini/completed")
    public ResponseEntity<?> getStoricoAllOrdinCompletati(){
        //TODO
        //lista di tutti gli ordini,completati

            List<Ordine> ordini = ordineDAO.findAll();
            List<Ordine> ordiniCompletati = List.of();
            for(Ordine ordin : ordini){

                if(ordin.getStato().equals("COMPLETATO")){
                    ordiniCompletati.add(ordin);
                }
            }
        return ResponseEntity.ok().body(ordiniCompletati);
    }
    @GetMapping("/storicoOrdini/pending")
    public ResponseEntity<?> getStoricoAllOrdiniDaCompletare(){
        //TODO
        //lista di tutti gli ordini,non completati
        List<Ordine> ordini = ordineDAO.findAll();
        List<Ordine> ordiniInAttesa = List.of();
        for(Ordine ordin : ordini){

            if(ordin.getStato().equals("COMPLETATO")){
                ordiniInAttesa.add(ordin);
            }
        }
        return ResponseEntity.ok().body(ordiniInAttesa);
    }

    @GetMapping("/storicoOrdini/cliente")
    public ResponseEntity<?> getStoricoAllOrdiniCliente(@RequestParam(required = false) Long idCliente, @RequestParam(required = false) String partitaIVA){
        //TODO
        //lista di tutti gli ordini effettuati da un certo cliente
        if(idCliente != null && partitaIVA == null){
            List<Ordine> ordiniByCliente = ordineService.getOrdiniByCliente(Math.toIntExact(idCliente));
            return ResponseEntity.ok().body(ordiniByCliente);
        } else if (idCliente == null && partitaIVA != null) {

                Long id = clienteDAO.findByPartitaIva(partitaIVA).get().getId();
                List<Ordine> ordiniByCliente = ordineService.getOrdiniByCliente(Math.toIntExact(idCliente));
                return ResponseEntity.ok().body(ordiniByCliente);
        }
        else{
            Long id = clienteDAO.findByPartitaIva(partitaIVA).get().getId();
            if(id == idCliente){
                List<Ordine> ordiniByCliente = ordineService.getOrdiniByCliente(Math.toIntExact(id));
                return ResponseEntity.ok().body(ordiniByCliente);
            }
            else {
                return ResponseEntity.badRequest().body("ID del cliente o partita Iva errati,oppure incongruenti");
            }
        }
    }

    @GetMapping("/userList")
    public ResponseEntity<?> getUserList(){
        //TODO
        //lista di tutti i dipendenti,con info di BI // info di BI ancora da implementare
        return ResponseEntity.ok().body(userDAO.findAll());
    }

    @GetMapping("/priceList")
    public ResponseEntity<?> getPriceList(){
        //TODO
        //lista di tutti i prezzi in generale

        return ResponseEntity.ok().body("todo");
    }

    @GetMapping("/priceList/mats")
    public ResponseEntity<?> getPriceListMats(){

        //TODO
        //lista di tutti prezzi dei materiali nel tempo

        return ResponseEntity.ok().body("todo");
    }

    @PostMapping("cambiaRuolo")
    public ResponseEntity<?> promuoviUtente(@RequestParam Long utenteId,@RequestParam  String ruolo){

        User user = userDAO.findById(utenteId).
                orElseThrow(()-> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(RoleName.valueOf(ruolo))
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userDAO.save(user);

        return ResponseEntity.ok().body("Ruolo dell'user modificato con successo");
    }

    @PostMapping("applica/sconto")
    public ResponseEntity<?> applicaSconto(@RequestParam Long clienteId,@RequestParam int percentuale){

        //TODO //Logica per capire wuanti soldi sono stati spesi
        //permette di applicare lo sconto ad un cliente
        return ResponseEntity.ok().body("todo");
    }

    @PostMapping("applica/premio")
    public ResponseEntity<?> applicaPremio(@RequestParam Long clienteId,@RequestParam int premio){

        //TODO
        //permette di dare premi a degli user in base ai report / punteggi dei supervisori

        return ResponseEntity.ok().body("todo");
    }




}
