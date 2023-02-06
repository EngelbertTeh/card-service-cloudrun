package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.BusinessUser;
import vn.cloud.cardservice.service.BusinessUserService;

import java.util.List;

@RestController
@RequestMapping("/api/business")
public class BusinessUserController {
    @Autowired
    BusinessUserService businessUserService;

    //Create
    @PostMapping("/save")
    public ResponseEntity<BusinessUser> saveBusinessUser(@RequestBody BusinessUser businessUserOther) {
        if(businessUserOther != null){
            InternalMessenger<BusinessUser> internalMessenger = businessUserService.saveBusinessUser(businessUserOther);
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.CREATED); // if data gets saved
            }
            else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); //returning null to client to indicate server responded to request but unable to save data, e.g., due to validation exception
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends a null object to server
    }

    //Retrieve
    @GetMapping("/{id}")
    public ResponseEntity<BusinessUser> getUserById (@PathVariable Long id) {
        if(id != null){
            InternalMessenger<BusinessUser> internalMessenger = businessUserService.getUserById(id);
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
    public ResponseEntity<List<BusinessUser>> getAllBusinessUsers() {
        InternalMessenger<List<BusinessUser>> internalMessenger = businessUserService.getAllBusinessUsers();
        if(internalMessenger.isSuccess()) {
            return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }

    //Update
    @PutMapping("/update")
    public ResponseEntity<BusinessUser> updateBusinessUser(@RequestBody BusinessUser businessUserOther) {


            return new ResponseEntity<>(businessUserOther,HttpStatus.OK);

//            InternalMessenger<BusinessUser> internalMessenger = businessUserService.updateBusinessUser(businessUserOther);
//            if(internalMessenger.isSuccess()) {
//                return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
//            }
//            else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); // if unable to update, server problem
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends null, client problem

    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteBusinessUser(@PathVariable Long id) {
        if(id != null){
            boolean isDeleted = businessUserService.deleteBusinessUserById(id);
            if(isDeleted) {
                return new ResponseEntity<>(true,HttpStatus.OK);
            }
            else return new ResponseEntity<>(false,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Login Authentication
    @PostMapping("/authenticate")
    public ResponseEntity<BusinessUser> authenticate(@RequestBody LoginDTO loginDTO) {
        if (loginDTO != null) {
            InternalMessenger<BusinessUser> internalMessenger = businessUserService.getUserByEmail(loginDTO.getEmail());
            if (internalMessenger.isSuccess()) {
                BusinessUser businessUserR = internalMessenger.getData();
                boolean isAuthenticated = businessUserService.authenticate(loginDTO,businessUserR);
                if(isAuthenticated) {
                    return new ResponseEntity<>(businessUserR,HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(null,HttpStatus.OK); // status OK even if email/password wrong, to indicate to client that credentials had been checked
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
