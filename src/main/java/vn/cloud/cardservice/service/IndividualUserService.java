package vn.cloud.cardservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.repository.IndividualUserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class IndividualUserService {

    @Autowired
    IndividualUserRepository individualUserRepository;


    //Create
    public InternalMessenger<IndividualUser> saveIndividualUser(IndividualUser individualUserOther) {
        try {
            IndividualUser individualUserR = individualUserRepository.save(individualUserOther);
            return new InternalMessenger<>(individualUserR,true);
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    //Retrieve
    public InternalMessenger<IndividualUser> getUserById(Long id) {
        try {
            Optional<IndividualUser> individualUserOpt =  individualUserRepository.findById(id);
            if(individualUserOpt.isPresent()) {
                return new InternalMessenger<>(individualUserOpt.get(),true);
            }
            else return new InternalMessenger<>(null,false,"element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    public InternalMessenger<IndividualUser> getUserByEmail(String email) {
        try {
            Optional<IndividualUser> individualUserOpt =  individualUserRepository.findByEmail(email);
            if(individualUserOpt!=null && individualUserOpt.isPresent()) {
                return new InternalMessenger<>(individualUserOpt.get(),true);
            }
            else return new InternalMessenger<>(null,false,"element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    public InternalMessenger<List<IndividualUser>> getAllIndividualUsers(){
        try {
            List<IndividualUser> individualUsers = individualUserRepository.findAll();
            return new InternalMessenger<>(individualUsers,true);
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    //Update
    public InternalMessenger<IndividualUser> updateIndividualUser(IndividualUser individualUserOther) {
        try {
            Optional<IndividualUser> individualUserOpt = individualUserRepository.findById(individualUserOther.getId());
            if(individualUserOpt.isPresent()) { // if such user exists
                IndividualUser individualUserR = individualUserRepository.save(individualUserOther); // save changes
                return new InternalMessenger<>(individualUserR,true);
            }
            else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    //Delete
    public Boolean deleteIndividualUserById(Long id) { // soft delete
        try {
            Optional<IndividualUser> individualUserOpt = individualUserRepository.findById(id);
            if(individualUserOpt.isPresent()) { // make sure such user exists
                IndividualUser individualUserR = individualUserOpt.get();
                if(individualUserR.getIsDeactivated()==false) { // make sure user was not previously "soft deleted", if it was previously "deleted", the `isDeactivated` returns true
                    individualUserR.setIsDeactivated(true); // "soft delete" the user by changing `isDeactivated` to true
                    InternalMessenger<IndividualUser> internalMessenger = updateIndividualUser(individualUserR); // update the entity
                    if(internalMessenger.isSuccess()) {
                        return true;
                    }
                }
            }
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //Login Authentication
    public Boolean authenticate(LoginDTO loginDTO, IndividualUser individualUserR) {
        return loginDTO.getEmail().equals(individualUserR.getEmail()) && loginDTO.getPassword().equals(individualUserR.getPassword());
    }

}
