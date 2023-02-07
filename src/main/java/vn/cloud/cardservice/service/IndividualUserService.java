package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.repository.IndividualUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
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
            if(individualUserOpt.isPresent() && individualUserOpt.get().getIsDeactivated() == false) {
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
            if(individualUserOpt.isPresent() && individualUserOpt.get().getIsDeactivated() == false) {
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
            List<IndividualUser> activeUsers = new ArrayList<>();
            for(IndividualUser individualUser : individualUsers) {
                if(individualUser.getIsDeactivated() == false) {
                    activeUsers.add(individualUser);
                }
            }
            return new InternalMessenger<>(activeUsers,true); // only return non-deleted users
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    //Update
    public InternalMessenger<IndividualUser> updateIndividualUser(IndividualUser individualUserOther) {
        try {
            Optional<IndividualUser> individualUserOpt = individualUserRepository.findById(individualUserOther.getUserId());
            if(individualUserOpt.isPresent() && individualUserOpt.get().getIsDeactivated() == false) {
                IndividualUser individualUserS = individualUserRepository.saveAndFlush(individualUserOther); // save changes
                return new InternalMessenger<>(individualUserS,true);
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
                    IndividualUser individualUserS = individualUserRepository.saveAndFlush(individualUserR); // update the entity
                    return true;
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
