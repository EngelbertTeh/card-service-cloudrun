package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.cloud.cardservice.dto.InternalMessenger;
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
    public ResponseEntity<IndividualUser> saveIndividualUser(@RequestBody IndividualUser individualUserOther) {
        if(individualUserOther != null){
            InternalMessenger<IndividualUser> internalMessenger = individualUserService.saveIndividualUser(individualUserOther);
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.CREATED); // if data gets saved
            }
            else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); //returning null to client to indicate server responded to request but unable to save data, e.g., due to validation exception
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends a null object to server
    }

    //Retrieve
    @GetMapping("/{id}")
    public ResponseEntity<IndividualUser> getUserById (@PathVariable Long id) {
        if(id != null){
            InternalMessenger<IndividualUser> internalMessenger = individualUserService.getUserById(id);
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
            }
            else if(internalMessenger.getErrorMessage().contains("element not found")) {
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-list")
    public ResponseEntity<List<IndividualUser>> getAllIndividualUsers() {
        InternalMessenger<List<IndividualUser>> internalMessenger = individualUserService.getAllIndividualUsers();
        if(internalMessenger.isSuccess()) {
            return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }

    //Update
    @PutMapping("/update")
    public ResponseEntity<IndividualUser> updateIndividualUser(@RequestBody IndividualUser individualUserOther) {
        if(individualUserOther != null){
            InternalMessenger<IndividualUser> internalMessenger = individualUserService.updateIndividualUser(individualUserOther);
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
            }
            else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); // if unable to update, server problem
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends null, client problem
    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteIndividualUser(Long id) {
        if(id != null){
            boolean isDeleted = individualUserService.deleteIndividualUserById(id);
            if(isDeleted) {
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            else return new ResponseEntity<>(false,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Login Authentication
    @PostMapping("/authenticate")
    public ResponseEntity<IndividualUser> authenticate(@RequestBody LoginDTO loginDTO) {
        if (loginDTO != null) {
            InternalMessenger<IndividualUser> internalMessenger = individualUserService.getUserByEmail(loginDTO.getEmail());
            if (internalMessenger.isSuccess()) {
                IndividualUser individualUserR = internalMessenger.getData();
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
