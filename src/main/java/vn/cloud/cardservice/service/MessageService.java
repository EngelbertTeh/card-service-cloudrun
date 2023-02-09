package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.Message;
import vn.cloud.cardservice.repository.MessageRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    //Create
    public InternalMessenger<Message> saveMessage(Message messageOther) {
        try {
            Message messageWasteBundleR = messageRepository.save(messageOther);
            return new InternalMessenger<>(messageWasteBundleR, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Retrieve
    public InternalMessenger<Message> getMessageById(Long id) {
        try {
            Optional<Message> messageWasteBundleOpt = messageRepository.findById(id);
            if (messageWasteBundleOpt.isPresent()) {
                return new InternalMessenger<>(messageWasteBundleOpt.get(), true);
            } else return new InternalMessenger<>(null, false, "element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }


    public InternalMessenger<List<Message>> getAllMessages() {
        try {
            List<Message> messages = messageRepository.findAll();
            if(!messages.isEmpty()) {
                return new InternalMessenger<>(messages, true);
            }
            else return new InternalMessenger<>(null, false, "list empty");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Update
    public InternalMessenger<Message> updateMessage(Message messageOther) {
        try {
            Optional<Message> messageWasteBundleOpt = messageRepository.findById(messageOther.getId());
            if (messageWasteBundleOpt.isPresent()) { // if such user exists
                Message messageWasteBundleR = messageRepository.saveAndFlush(messageOther); // save changes
                return new InternalMessenger<>(messageWasteBundleR, true);
            } else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Delete
    public Boolean deleteMessageById(Long id) { // hard delete
        try {
            Optional<Message> messageWasteBundleOpt = messageRepository.findById(id);
            if (messageWasteBundleOpt.isPresent()) { // make sure bundle exists
                Message messageWasteBundleR = messageWasteBundleOpt.get();
                messageRepository.delete(messageWasteBundleR);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




}
