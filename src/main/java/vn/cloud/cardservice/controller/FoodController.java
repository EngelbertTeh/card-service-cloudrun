	package vn.cloud.cardservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.cloud.cardservice.model.Test;


	@RestController
@RequestMapping("/api")
public class FoodController {
	
	@PostMapping("/hello")
	public ResponseEntity<String> sayHello(@RequestBody Test test){
		return new ResponseEntity<>(test.getEmail(), HttpStatus.OK);
	}

}
