package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.BusinessUser;
import vn.cloud.cardservice.repository.BusinessUserRepository;
import vn.cloud.cardservice.service.BusinessUserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/business")
public class BusinessUserController {

    @Autowired
    BusinessUserService businessUserService;

    @GetMapping("/{id}")
    public ResponseEntity<BusinessUser> getUserById (@PathVariable Long id) {
        if(id!=null){
            BusinessUser bUser= businessUserService.findById(id);
            if(bUser != null) {
                return new ResponseEntity<>(bUser,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // get list
    @GetMapping("/get-list")
    public ResponseEntity<List<BusinessUser>> getAllBusinessUsers(){
        List<BusinessUser> bUsers = businessUserService.getAllBusinessUsers();
        if(bUsers!=null) {
            return new ResponseEntity<>(bUsers, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //registration
    @PostMapping("/save")
    public ResponseEntity<BusinessUser> saveBusinessUser (@RequestBody BusinessUser businessUser) {
      BusinessUser businessUser1 = businessUserService.saveBusinessUser(businessUser);
      if(businessUser1!=null) {
          return new ResponseEntity<>(businessUser1,HttpStatus.CREATED);
      }
      else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //login authentication
    @Autowired
    BusinessUserRepository businessUserRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<BusinessUser> authenticatePost(@RequestBody LoginDTO loginDTO){

        if(loginDTO!=null){
            Optional<BusinessUser> bUserOpt = businessUserRepository.findByEmail(loginDTO.getEmail());

            if(bUserOpt.isPresent()){
                BusinessUser bUserFromRepo = bUserOpt.get();
                if(bUserFromRepo.getEmail().equals(loginDTO.getEmail()) && bUserFromRepo.getPassword().equals(loginDTO.getPassword()) ) {
                    return new ResponseEntity<>(bUserFromRepo,HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
