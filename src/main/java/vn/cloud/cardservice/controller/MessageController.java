	package vn.cloud.cardservice.controller;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import vn.cloud.cardservice.dto.InternalMessenger;
    import vn.cloud.cardservice.model.Message;
    import vn.cloud.cardservice.service.MessageService;

    import java.util.List;

    @RestController
    @RequestMapping("/api/message")
    public class MessageController {
            @Autowired
            MessageService messageService;
    
            //Create
            @PostMapping("/save")
            public ResponseEntity<Message> saveMessage(@RequestBody Message messageWasteBundleOther) {
                if(messageWasteBundleOther != null && messageWasteBundleOther.getId() == null){
                    InternalMessenger<Message> internalMessenger = messageService.saveMessage(messageWasteBundleOther);
                    if(internalMessenger.isSuccess()) {
                        return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.CREATED); // if data gets saved
                    }
                    else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); //returning null to client to indicate server responded to request but unable to save data, e.g., due to validation exception
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends a null object to server
            }
    
            //Retrieve
            @GetMapping("/{id}")
            public ResponseEntity<Message> getMessageById (@PathVariable Long id) {
                if(id != null){
                    InternalMessenger<Message> internalMessenger = messageService.getMessageById(id);
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
            public ResponseEntity<List<Message>> getAllMessages() {
                InternalMessenger<List<Message>> internalMessenger = messageService.getAllMessages();
                if(internalMessenger.isSuccess()) {
                    return new ResponseEntity<>(internalMessenger.getData(), HttpStatus.OK);
                }
                else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
    
            //Update
            @PutMapping("/update")
            public ResponseEntity<Message> updateMessage(@RequestBody Message messageWasteBundleOther) {
                if(messageWasteBundleOther != null){
                    InternalMessenger<Message> internalMessenger = messageService.updateMessage(messageWasteBundleOther);
                    if(internalMessenger.isSuccess()) {
                        return new ResponseEntity<>(internalMessenger.getData(),HttpStatus.OK);
                    }
                    else return new ResponseEntity<>(null,HttpStatus.NO_CONTENT); // if unable to update, server problem
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // if client sends null, client problem
            }
    
            //Delete
            @DeleteMapping("/delete/{id}")
            public ResponseEntity<Boolean> deleteMessage(@PathVariable Long id) {
                if(id != null){
                    boolean isDeleted = messageService.deleteMessageById(id);
                    if(isDeleted) {
                        return new ResponseEntity<>(true,HttpStatus.OK);
                    }
                    else return new ResponseEntity<>(false,HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }
