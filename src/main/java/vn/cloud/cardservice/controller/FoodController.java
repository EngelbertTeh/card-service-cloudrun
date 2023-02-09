	package vn.cloud.cardservice.controller;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.*;
	import vn.cloud.cardservice.dto.InternalMessenger;
	import vn.cloud.cardservice.model.Food;
	import vn.cloud.cardservice.service.FoodService;
	import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {
	@Autowired
	FoodService foodService;

	//Create
	@PostMapping("/save")
	public ResponseEntity<Food> saveFood(@RequestBody Food foodWasteBundleOther) {
		if(foodWasteBundleOther != null){
			InternalMessenger<Food> internalMessenger = foodService.saveFood(foodWasteBundleOther);
			if(internalMessenger.isSuccess()) {
				return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.CREATED); // if data gets saved
			}
			else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); //returning null to client to indicate server responded to request but unable to save data, e.g., due to validation exception
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends a null object to server
	}

	//Retrieve
	@GetMapping("/{id}")
	public ResponseEntity<Food> getFoodById (@PathVariable Long id) {
		if(id != null){
			InternalMessenger<Food> internalMessenger = foodService.getFoodById(id);
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
	public ResponseEntity<List<Food>> getAllFoods() {
		InternalMessenger<List<Food>> internalMessenger = foodService.getAllFoods();
		if(internalMessenger.isSuccess()) {
			return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
		}
		else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
	}

	//Update
	@PutMapping("/update")
	public ResponseEntity<Food> updateFood(@RequestBody Food foodWasteBundleOther) {
		if(foodWasteBundleOther != null){
			InternalMessenger<Food> internalMessenger = foodService.updateFood(foodWasteBundleOther);
			if(internalMessenger.isSuccess()) {
				return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
			}
			else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); // if unable to update, server problem
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends null, client problem
	}

	@PutMapping("/update/pickup-status/{id}")
	public ResponseEntity<Boolean> togglePickupStatus(@PathVariable Long id) {
		if(id != null){
			boolean isToggled = foodService.togglePickupStatus(id);
			if(isToggled) {
				return new ResponseEntity<>(true,HttpStatus.OK);
			}
			else return new ResponseEntity<>(false,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/update/listed-status/{id}")
	public ResponseEntity<Boolean> toggleListedStatus(@PathVariable Long id) {
		if(id != null){
			boolean isToggled = foodService.toggleListedStatus(id);
			if(isToggled) {
				return new ResponseEntity<>(true,HttpStatus.OK);
			}
			else return new ResponseEntity<>(false,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/update/collected-status/{id}")
	public ResponseEntity<Boolean> updateCollectedStatus(@PathVariable Long id) {
		if(id != null){
			boolean isUpdated = foodService.updateCollectedStatus(id);
			if(isUpdated) {
				return new ResponseEntity<>(true,HttpStatus.OK);
			}
			else return new ResponseEntity<>(false,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	//Delete
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Boolean> deleteFood(@PathVariable Long id) {
		if(id != null){
			boolean isDeleted = foodService.deleteFoodById(id);
			if(isDeleted) {
				return new ResponseEntity<>(true,HttpStatus.OK);
			}
			else return new ResponseEntity<>(false,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
