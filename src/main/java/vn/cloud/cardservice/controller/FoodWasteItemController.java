	package vn.cloud.cardservice.controller;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import vn.cloud.cardservice.dto.ImageDTO;
    import vn.cloud.cardservice.dto.InternalMessenger;
    import vn.cloud.cardservice.model.FoodWasteItem;
    import vn.cloud.cardservice.service.FoodWasteItemService;

    import java.util.List;

    @RestController
    @RequestMapping("/api/item")
    public class FoodWasteItemController {
        @Autowired
        FoodWasteItemService foodWasteItemService;

        //Create
        @PostMapping("/save")
        public ResponseEntity<FoodWasteItem> saveFoodWasteItem(@RequestBody FoodWasteItem foodWasteItemOther) {
            if(foodWasteItemOther != null && foodWasteItemOther.getId() == null){
                InternalMessenger<FoodWasteItem> internalMessenger = foodWasteItemService.saveFoodWasteItem(foodWasteItemOther);
                if(internalMessenger.isSuccess()) {
                    return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.CREATED); // if data gets saved
                }
                else return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.NO_CONTENT); //returning null to client to indicate server responded to request but unable to save data, e.g., due to validation exception
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends a null object to server
        }

        //Retrieve
        @GetMapping("/retrieve/{id}")
        public ResponseEntity<FoodWasteItem> getFoodWasteItemById (@PathVariable Long id) {
            if(id != null){
                InternalMessenger<FoodWasteItem> internalMessenger = foodWasteItemService.getFoodWasteItemById(id);
                if(internalMessenger.isSuccess()) {
                    return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
                }
                else if(internalMessenger.getErrorMessage().contains("element not found")) {
                    return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.NO_CONTENT);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        @GetMapping("/retrieve-name/{name}")
        public ResponseEntity<FoodWasteItem> getFoodWasteItemByName (@PathVariable String name) {
            if(name != null){
                InternalMessenger<FoodWasteItem> internalMessenger = foodWasteItemService.getFoodWasteItemByName(name);
                if(internalMessenger.isSuccess()) {
                    return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
                }
                else if(internalMessenger.getErrorMessage().contains("element not found")) {
                    return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.NO_CONTENT);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        @GetMapping("/get-all")
        public ResponseEntity<List<FoodWasteItem>> getAllFoodWasteItems() {
            InternalMessenger<List<FoodWasteItem>> internalMessenger = foodWasteItemService.getAllFoodWasteItems();
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
            }
            else return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.NO_CONTENT); //returns empty array as requested by client side
        }

        @GetMapping("/get-list/{biz_id}")
        public ResponseEntity<List<FoodWasteItem>> getFoodWasteItemsByBizId(@PathVariable Long biz_id) {
            if(biz_id != null){
            InternalMessenger<List<FoodWasteItem>> internalMessenger = foodWasteItemService.getFoodWasteItemsByBizId(biz_id);
            if(internalMessenger.isSuccess()) {
                return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
            }
            else return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.NO_CONTENT); //returns empty array as requested by client side
        }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //Update
        @PutMapping("/update")
        public ResponseEntity<FoodWasteItem> updateFood(@RequestBody FoodWasteItem foodWasteItemOther) {
            if(foodWasteItemOther != null){
                InternalMessenger<FoodWasteItem> internalMessenger = foodWasteItemService.updateFoodWasteItem(foodWasteItemOther);
                if(internalMessenger.isSuccess()) {
                    return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
                }
                else return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.NO_CONTENT); // if unable to update, server problem
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends null, client problem
        }

        @PutMapping("/image/upload")
        public ResponseEntity<HttpStatus> uploadImage(@RequestBody ImageDTO imageDTO){
            if (imageDTO != null) {
                InternalMessenger<FoodWasteItem> internalMessenger = foodWasteItemService.uploadImage(imageDTO);
                if(internalMessenger.isSuccess()) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                else if(!internalMessenger.getErrorMessage().equals("not found") && !internalMessenger.getErrorMessage().equals("invalid format") ){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // if it is due to server error when uploading image
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if imageDTO is null, or unable to retrieve object from repo due to invalid id (obj not created), or base64 string format invalid

        }


        //Delete
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Boolean> deleteFoodWasteItemById(@PathVariable Long id) {
            if(id != null){
                boolean isDeleted = foodWasteItemService.deleteFoodWasteItemById(id);
                if(isDeleted) {
                    return new ResponseEntity<>(true,HttpStatus.OK);
                }
                else return new ResponseEntity<>(false,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        @DeleteMapping("/delete-all/{biz_id}")
        public ResponseEntity<Boolean> deleteAllFoodWasteItemByBizId(@PathVariable Long biz_id) {
            if(biz_id != null){
                boolean isAllDeleted = foodWasteItemService.deleteAllFoodWasteItemByBizId(biz_id);
                if(isAllDeleted) {
                    return new ResponseEntity<>(true,HttpStatus.OK);
                }
                else return new ResponseEntity<>(false,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
