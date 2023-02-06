	package vn.cloud.cardservice.controller;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.*;
	import vn.cloud.cardservice.dto.InternalMessenger;
	import vn.cloud.cardservice.model.FoodWasteBundle;
	import vn.cloud.cardservice.service.FoodWasteBundleService;
	import java.util.List;

@RestController
@RequestMapping("/api/food-waste-bundle")
public class FoodWasteBundleController {
		@Autowired
		FoodWasteBundleService foodWasteBundleService;

		//Create
		@PostMapping("/save")
		public ResponseEntity<FoodWasteBundle> saveFoodWasteBundle(@RequestBody FoodWasteBundle foodWasteBundleOther) {
			if(foodWasteBundleOther != null){
				InternalMessenger<FoodWasteBundle> internalMessenger = foodWasteBundleService.saveFoodWasteBundle(foodWasteBundleOther);
				if(internalMessenger.isSuccess()) {
					return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.CREATED); // if data gets saved
				}
				else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); //returning null to client to indicate server responded to request but unable to save data, e.g., due to validation exception
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends a null object to server
		}

		//Retrieve
		@GetMapping("/{id}")
		public ResponseEntity<FoodWasteBundle> getUserById (@PathVariable Long id) {
			if(id != null){
				InternalMessenger<FoodWasteBundle> internalMessenger = foodWasteBundleService.getUserById(id);
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
		public ResponseEntity<List<FoodWasteBundle>> getAllFoodWasteBundles() {
			InternalMessenger<List<FoodWasteBundle>> internalMessenger = foodWasteBundleService.getAllFoodWasteBundles();
			if(internalMessenger.isSuccess()) {
				return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
			}
			else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
		}

		//Update
		@PutMapping("/update")
		public ResponseEntity<FoodWasteBundle> updateFoodWasteBundle(@RequestBody FoodWasteBundle foodWasteBundleOther) {
			if(foodWasteBundleOther != null){
				InternalMessenger<FoodWasteBundle> internalMessenger = foodWasteBundleService.updateFoodWasteBundle(foodWasteBundleOther);
				if(internalMessenger.isSuccess()) {
					return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
				}
				else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); // if unable to update, server problem
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends null, client problem
		}

		//Delete
		@DeleteMapping("/delete/{id}")
		public ResponseEntity<Boolean> deleteFoodWasteBundle(@PathVariable Long id) {
			if(id != null){
				boolean isDeleted = foodWasteBundleService.deleteFoodWasteBundleById(id);
				if(isDeleted) {
					return new ResponseEntity<>(true,HttpStatus.OK);
				}
				else return new ResponseEntity<>(false,HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
}
