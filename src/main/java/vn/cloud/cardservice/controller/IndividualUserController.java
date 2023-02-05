package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.cloud.cardservice.dto.AndroidDTO;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.service.IndividualUserService;

@RestController
@RequestMapping("/api/individual")
public class IndividualUserController {
    @Autowired
    IndividualUserService individualUserService;

    @PostMapping("/save")
    public ResponseEntity<IndividualUser> saveIndividualUser (@RequestBody AndroidDTO androidDTO) {

        IndividualUser individualUser1 = new IndividualUser();
        individualUser1.setName(androidDTO.getUsername());
        individualUser1.setEmail(androidDTO.getEmail());
        individualUser1.setPassword(androidDTO.getPwd());
        individualUser1.setPhone(androidDTO.getPhone());
        individualUser1.setSalary(Double.parseDouble(androidDTO.getSalary()));
        individualUser1.setRole(androidDTO.getRole());
        IndividualUser savedInstance = individualUserService.saveIndividualUser(individualUser1);
        if(savedInstance!=null) {

            return new ResponseEntity<>(individualUser1, HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/test")
    public ResponseEntity<IndividualUser> test(){

        return new ResponseEntity<>(HttpStatus.OK);

    }



}
