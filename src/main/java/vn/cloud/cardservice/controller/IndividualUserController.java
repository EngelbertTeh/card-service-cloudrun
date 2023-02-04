package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.service.IndividualUserService;

@RestController
@RequestMapping("/api/individual")
public class IndividualUserController {
    @Autowired
    IndividualUserService individualUserService;

    @PostMapping("/save")
    public ResponseEntity<IndividualUser> saveIndividualUser (@RequestBody IndividualUser individualUser) {
        IndividualUser individualUser1 = individualUserService.saveIndividualUser(individualUser);
        if(individualUser1!=null) {
            return new ResponseEntity<>(individualUser1, HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
