package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.InternalMessenger;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.BusinessUser;
import vn.cloud.cardservice.repository.BusinessUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BusinessUserService {

    @Autowired
    BusinessUserRepository businessUserRepository;

    //Create
    public InternalMessenger<BusinessUser> saveBusinessUser(BusinessUser businessUserOther) {
        try {
                BusinessUser businessUserR = businessUserRepository.save(businessUserOther);
                return new InternalMessenger<>(businessUserR,true);
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    //Retrieve
    public InternalMessenger<BusinessUser> getUserById(Long id) {
        try {
            Optional<BusinessUser> businessUserOpt =  businessUserRepository.findById(id);
            if(businessUserOpt.isPresent() && businessUserOpt.get().getIsDeactivated() == false) {
                return new InternalMessenger<>(businessUserOpt.get(),true);
            }
            else return new InternalMessenger<>(null,false,"element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    public InternalMessenger<BusinessUser> getUserByEmail(String email) {
        try {
            Optional<BusinessUser> businessUserOpt =  businessUserRepository.findByEmail(email);
            if(businessUserOpt.isPresent() && businessUserOpt.get().getIsDeactivated() == false) {
                return new InternalMessenger<>(businessUserOpt.get(),true);
            }
            else return new InternalMessenger<>(null,false,"element not found");
        } catch (Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }
    
    public InternalMessenger<List<BusinessUser>> getAllBusinessUsers(){
        try {
            List<BusinessUser> businessUsers = businessUserRepository.findAll();
            if(!businessUsers.isEmpty()) {
                List<BusinessUser> activeUsers = new ArrayList<>();
                for(BusinessUser businessUser : businessUsers) {
                    if(businessUser.getIsDeactivated() == false) {
                        activeUsers.add(businessUser);
                    }
                }
                return new InternalMessenger<>(activeUsers,true); // only return non-deleted users
            }
            else return new InternalMessenger<>(null,false,"list empty");
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    //Update
    public InternalMessenger<BusinessUser> updateBusinessUser(BusinessUser businessUserOther) {
        try {
            Optional<BusinessUser> businessUserOpt = businessUserRepository.findById(businessUserOther.getId());
            if(businessUserOpt.isPresent() && businessUserOpt.get().getIsDeactivated() == false) {
                BusinessUser businessUserS = businessUserRepository.saveAndFlush(businessUserOther); // save changes
                return new InternalMessenger<>(businessUserS,true);
            }
            else throw new NoSuchElementException(); // will not save as new instance if it is not found in db
        } catch(Exception e) {
            e.printStackTrace();
            return new InternalMessenger<>(null,false,e.toString());
        }
    }

    //Delete
    public Boolean deleteBusinessUserById(Long id) { // soft delete
        try {
            Optional<BusinessUser> businessUserOpt = businessUserRepository.findById(id);
            if(businessUserOpt.isPresent()) { // make sure such user exists
                BusinessUser businessUserR = businessUserOpt.get();
                if(businessUserR.getIsDeactivated()==false) { // make sure user was not previously "soft deleted", if it was previously "deleted", the `isDeactivated` returns true
                    businessUserR.setIsDeactivated(true); // "soft delete" the user by changing `isDeactivated` to true
                    BusinessUser businessUserS = businessUserRepository.saveAndFlush(businessUserR); // update the entity
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
    public Boolean authenticate(LoginDTO loginDTO, BusinessUser businessUserR) {
        String emailFromLogin = loginDTO.getEmail().toLowerCase();
        String emailFromRepo = businessUserR.getEmail().toLowerCase();

        return emailFromLogin.equals(emailFromRepo) && loginDTO.getPassword().equals(businessUserR.getPassword());
    }


}

