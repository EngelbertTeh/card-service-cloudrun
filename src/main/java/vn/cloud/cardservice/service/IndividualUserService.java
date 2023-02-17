package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.repository.IndividualUserRepository;
import vn.cloud.cardservice.utils.SecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class IndividualUserService {

    @Autowired
    SecurityUtil<IndividualUser> securityUtil;
    @Autowired
    IndividualUserRepository individualUserRepository;


    //Create
    public InternalMessenger<IndividualUser> saveIndividualUser(IndividualUser individualUserOther) {
        try {
            IndividualUser individualUserPasswordEncrypted = securityUtil.encryptPassword(individualUserOther);
            IndividualUser individualUserR = individualUserRepository.saveAndFlush(individualUserPasswordEncrypted);
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
            List<IndividualUser> individualUsers = individualUserRepository.findAllActive();
            if(!individualUsers.isEmpty()) {
                return new InternalMessenger<>(individualUsers,true); // only return non-deleted users
            }
            else return new InternalMessenger<>(new ArrayList<>(),false,"list empty");
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    //Update
    public InternalMessenger<IndividualUser> updateIndividualUser(IndividualUser individualUserOther) {
        try {
            Optional<IndividualUser> individualUserOpt = individualUserRepository.findById(individualUserOther.getId());
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
                    individualUserRepository.saveAndFlush(individualUserR); // update the entity
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
        return securityUtil.authenticate(loginDTO,individualUserR);
    }

}
