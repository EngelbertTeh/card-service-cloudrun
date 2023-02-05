package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.service.IndividualUserService;

import java.util.List;

@RestController
@RequestMapping("/api/individual")
public class IndividualUserController {
    @Autowired
    IndividualUserService individualUserService;

    //Create
    @PostMapping("/save")
    public ResponseEntity<IndividualUser> saveIndividualUser (@RequestBody IndividualUser individualUserOther) {
        if(individualUserOther != null){
            IndividualUser individualUserR = individualUserService.saveIndividualUser(individualUserOther);
            if(individualUserR != null) {
                return new ResponseEntity<>(individualUserR,HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Retrieve
    @GetMapping("/{id}")
    public ResponseEntity<IndividualUser> getUserById (@PathVariable Long id) {
        if(id != null){
            IndividualUser individualUserR= individualUserService.getUserById(id);
            if(individualUserR != null) {
                return new ResponseEntity<>(individualUserR,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-list")
    public ResponseEntity<List<IndividualUser>> getAllIndividualUsers(){
        List<IndividualUser> individualUsers = individualUserService.getAllIndividualUsers();
        if(individualUsers != null) {
            return new ResponseEntity<>(individualUsers, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Update



    //Delete



    //Login Authentication
    @PostMapping("/authenticate")
    public ResponseEntity<IndividualUser> authenticate(@RequestBody LoginDTO loginDTO) {
        if (loginDTO != null) {
            IndividualUser individualUserR = individualUserService.getUserByEmail(loginDTO.getEmail());
            if (individualUserR != null) {
                boolean isAuthenticated = individualUserService.authenticate(loginDTO,individualUserR);
                if(isAuthenticated) {
                    return new ResponseEntity<>(individualUserR,HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(null,HttpStatus.OK); // status OK even if email/password wrong, to indicate to client that credentials had been checked
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
