	package vn.cloud.cardservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.cloud.cardservice.model.Dummy;
import vn.cloud.cardservice.model.Test;
import vn.cloud.cardservice.repository.DummyRepository;

import java.util.Optional;


	@RestController
@RequestMapping("/api")
public class FoodController {
	
	@PostMapping("/hello")
	public ResponseEntity<String> sayHello(@RequestBody Test test){
		return new ResponseEntity<>(test.getEmail(), HttpStatus.OK);
	}

	@Autowired
	DummyRepository dummyRepository;

	@PostMapping("/dummy")
	public ResponseEntity<Dummy> sayHello(@RequestBody Dummy dummy){


		Dummy returnedObj = dummyRepository.save(dummy);

		if(returnedObj != null) {
			return new ResponseEntity<>(dummy,HttpStatus.OK);
		}
		else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/dummy/authenticate")
	public ResponseEntity<Dummy> authenticate(@RequestBody Dummy dummyOther){


		Optional<Dummy> dummyOpt = dummyRepository.findByEmail(dummyOther.getEmail());


		if(dummyOpt.isPresent()){
			Dummy dummyRepo = dummyOpt.get();
			if(dummyRepo.getEmail().equals(dummyOther.getEmail()) && dummyRepo.getPassword().equals(dummyOther.getPassword()) ) {
				return new ResponseEntity<>(dummyRepo,HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	}


}
