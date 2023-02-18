package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.model.Message;
import vn.cloud.cardservice.repository.MessageRepository;

import java.util.ArrayList;
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
            Message messageR = messageRepository.saveAndFlush(messageOther);
            return new InternalMessenger<>(messageR, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Retrieve
    public InternalMessenger<Message> getMessageById(Long id) {
        try {
            Optional<Message> messageOpt = messageRepository.findById(id);
            if (messageOpt.isPresent()) {
                return new InternalMessenger<>(messageOpt.get(), true);
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
            else return new InternalMessenger<>(new ArrayList<>(), false, "list empty");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Update
    public InternalMessenger<Message> updateMessage(Message messageOther) {
        try {
            Optional<Message> messageOpt = messageRepository.findById(messageOther.getId());
            if (messageOpt.isPresent()) {
                Message messageR = messageRepository.saveAndFlush(messageOther); // save changes
                return new InternalMessenger<>(messageR, true);
            } else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null, false, e.toString());
        }
    }

    //Delete
    public Boolean deleteMessageById(Long id) { // hard delete
        try {
            Optional<Message> messageOpt = messageRepository.findById(id);
            if (messageOpt.isPresent()) { // make sure bundle exists
                Message messageR = messageOpt.get();
                messageRepository.delete(messageR);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




}
