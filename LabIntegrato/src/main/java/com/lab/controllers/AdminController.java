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

import java.util.ArrayList;
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
    public ResponseEntity<?> getStoricoAllOrdini(@RequestHeader("Authorizarion") String authorizationHeader){
        try{
            validaTokenAdmin(authorizationHeader);
            List<Ordine> ordini = ordineDAO.findAll();
            return ResponseEntity.ok().body(ordini);
        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/storicoOrdini/completed")
    public ResponseEntity<?> getStoricoAllOrdinCompletati(@RequestHeader("Authorizarion") String authorizationHeader){
        try{
            validaTokenAdmin(authorizationHeader);
            List<Ordine> ordini = ordineDAO.findAll();
            List<Ordine> ordiniCompletati = new ArrayList<Ordine>();
            for(Ordine ordin : ordini){
                if(ordin.getStato().equals("COMPLETATO")){
                    ordiniCompletati.add(ordin);
                }
            }
            return ResponseEntity.ok().body(ordiniCompletati);
        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/storicoOrdini/pending")
    public ResponseEntity<?> getStoricoAllOrdiniDaCompletare(@RequestHeader("Authorizarion") String authorizationHeader){

        try{
            validaTokenAdmin(authorizationHeader);
            List<Ordine> ordini = ordineDAO.findAll();
            List<Ordine> ordiniInAttesa = List.of();
            for(Ordine ordin : ordini){

                if(ordin.getStato().equals("COMPLETATO")){
                    ordiniInAttesa.add(ordin);
                }
            }
            return ResponseEntity.ok().body(ordiniInAttesa);
        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/storicoOrdini/cliente")
    public ResponseEntity<?> getStoricoAllOrdiniCliente(@RequestHeader("Authorizarion") String authorizationHeader,
                                                        @RequestParam(required = false) Long idCliente, @RequestParam(required = false) String partitaIVA){

        try{

            validaTokenAdmin(authorizationHeader);

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

        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/userList")
    public ResponseEntity<?> getUserList(@RequestHeader("Authorizarion") String authorizationHeader){

        try{
            validaTokenAdmin(authorizationHeader);
            return ResponseEntity.ok().body(userDAO.findAll());
        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //TODO

    @GetMapping("/priceList")
    public ResponseEntity<?> getPriceList(@RequestHeader("Authorizarion") String authorizationHeader){

        try{
            validaTokenAdmin(authorizationHeader);
            return ResponseEntity.ok().body("todo");
        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //TODO

    @GetMapping("/priceList/mats")
    public ResponseEntity<?> getPriceListMats(@RequestHeader("Authorizarion") String authorizationHeader){

        try{
            validaTokenAdmin(authorizationHeader);
            return ResponseEntity.ok().body("todo");
        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("cambiaRuolo")
    public ResponseEntity<?> promuoviUtente(@RequestHeader("Authorizarion") String authorizationHeader,
                                            @RequestParam Long utenteId,@RequestParam  String ruolo){

        try{
            validaTokenAdmin(authorizationHeader);
            User user = userDAO.findById(utenteId).
                    orElseThrow(()-> new RuntimeException("User not found"));
            Role role = roleRepository.findByName(RoleName.valueOf(ruolo))
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            user.getRoles().add(role);
            userDAO.save(user);

            return ResponseEntity.ok().body("Ruolo dell'user modificato con successo");
        }catch ( ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //TODO

    @PostMapping("applica/sconto")
    public ResponseEntity<?> applicaSconto(@RequestHeader("Authorizarion") String authorizationHeader,
                                           @RequestParam Long clienteId,@RequestParam int percentuale){
        try{
            validaTokenAdmin(authorizationHeader);
            return ResponseEntity.ok().body("todo");
        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //TODO

    @PostMapping("applica/premio")
    public ResponseEntity<?> applicaPremio(@RequestHeader("Authorizarion") String authorizationHeader,
                                           @RequestParam Long clienteId,@RequestParam int premio){
        try{
            validaTokenAdmin(authorizationHeader);
            return ResponseEntity.ok().body("todo");
        }catch (ForbiddenException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



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




}
