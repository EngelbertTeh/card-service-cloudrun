package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<BusinessUser> saveBusinessUser (@RequestBody BusinessUser businessUserOther) {
        if(businessUserOther != null){
            BusinessUser businessUserR = businessUserService.saveBusinessUser(businessUserOther);
            if(businessUserR != null) {
                return new ResponseEntity<>(businessUserR,HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Retrieve
    @GetMapping("/{id}")
    public ResponseEntity<BusinessUser> getUserById (@PathVariable Long id) {
        if(id != null){
            BusinessUser businessUserR= businessUserService.getUserById(id);
            if(businessUserR != null) {
                return new ResponseEntity<>(businessUserR,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-list")
    public ResponseEntity<List<BusinessUser>> getAllBusinessUsers(){
        List<BusinessUser> businessUsers = businessUserService.getAllBusinessUsers();
        if(businessUsers != null) {
            return new ResponseEntity<>(businessUsers, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Update



    //Delete



    //Login Authentication

    @PostMapping("/authenticate")
    public ResponseEntity<BusinessUser> authenticate(@RequestBody LoginDTO loginDTO) {

        if (loginDTO != null) {
            BusinessUser businessUserR = businessUserService.authenticate(loginDTO);
            if (businessUserR != null) {
                return new ResponseEntity<>(businessUserR, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
