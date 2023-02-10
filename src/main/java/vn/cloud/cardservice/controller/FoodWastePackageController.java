	package vn.cloud.cardservice.controller;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import vn.cloud.cardservice.dto.InternalMessenger;
    import vn.cloud.cardservice.model.FoodWastePackage;
    import vn.cloud.cardservice.service.FoodWastePackageService;

    import java.util.List;

    @RestController
    @RequestMapping("/api/foodwastepackage")
    public class FoodWastePackageController {
        @Autowired
        FoodWastePackageService foodWastePackageService;

        //Create
        @PostMapping("/save")
        public ResponseEntity<FoodWastePackage> saveFood(@RequestBody FoodWastePackage foodWastePackageOther) {
            if(foodWastePackageOther != null){
                InternalMessenger<FoodWastePackage> internalMessenger = foodWastePackageService.saveFoodWastePackage(foodWastePackageOther);
                if(internalMessenger.isSuccess()) {
                    return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.CREATED); // if data gets saved
                }
                else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); //returning null to client to indicate server responded to request but unable to save data, e.g., due to validation exception
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends a null object to server
        }

        //Retrieve
        @GetMapping("/{id}")
        public ResponseEntity<FoodWastePackage> getFoodById (@PathVariable Long id) {
            if(id != null){
                InternalMessenger<FoodWastePackage> internalMessenger = foodWastePackageService.getFoodWastePackageById(id);
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
        public ResponseEntity<List<FoodWastePackage>> getAllFoodWastePackages() {
            InternalMessenger<List<FoodWastePackage>> internalMessenger = foodWastePackageService.getAllFoodWastePackages();
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
            }
            else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }

        @GetMapping("/get-list-with-deleted")
        public ResponseEntity<List<FoodWastePackage>> getAllFoodsWithDeleted() {
            InternalMessenger<List<FoodWastePackage>> internalMessenger = foodWastePackageService.getAllFoodWastePackagesWithDeleted();
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
            }
            else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }


        //Update
        @PutMapping("/update")
        public ResponseEntity<FoodWastePackage> updateFood(@RequestBody FoodWastePackage foodWastePackageOther) {
            if(foodWastePackageOther != null){
                InternalMessenger<FoodWastePackage> internalMessenger = foodWastePackageService.updateFoodWastePackage(foodWastePackageOther);
                if(internalMessenger.isSuccess()) {
                    return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
                }
                else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); // if unable to update, server problem
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends null, client problem
        }

//        @PutMapping("/update/pickup-status/{id}")
//        public ResponseEntity<Boolean> togglePickupStatus(@PathVariable Long id) {
//            if(id != null){
//                boolean isToggled = foodWastePackageService.togglePickupStatus(id);
//                if(isToggled) {
//                    return new ResponseEntity<>(true,HttpStatus.OK);
//                }
//                else return new ResponseEntity<>(false,HttpStatus.OK);
//            }
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        @PutMapping("/update/listed-status/{id}")
//        public ResponseEntity<Boolean> toggleListedStatus(@PathVariable Long id) {
//            if(id != null){
//                boolean isToggled = foodWastePackageService.toggleListedStatus(id);
//                if(isToggled) {
//                    return new ResponseEntity<>(true,HttpStatus.OK);
//                }
//                else return new ResponseEntity<>(false,HttpStatus.OK);
//            }
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
        @PutMapping("/update/status/{id}")
        public ResponseEntity<Boolean> updateCollectedStatus(@PathVariable Long id) {
            if(id != null){
                boolean isCollected = foodWastePackageService.updateCollectedStatus(id);
                if(isCollected) {
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
                boolean isDeleted = foodWastePackageService.deleteFoodWastePackageById(id);
                if(isDeleted) {
                    return new ResponseEntity<>(true,HttpStatus.OK);
                }
                else return new ResponseEntity<>(false,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
