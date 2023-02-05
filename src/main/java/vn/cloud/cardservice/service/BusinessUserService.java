package vn.cloud.cardservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.BusinessUser;
import vn.cloud.cardservice.repository.BusinessUserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BusinessUserService {
    @Autowired
    BusinessUserRepository businessUserRepository;

    //Create
    public BusinessUser saveBusinessUser(BusinessUser businessUser) {
        try {
            return businessUserRepository.save(businessUser);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Retrieve
    public BusinessUser getUserById(Long id) {
        try {
            Optional<BusinessUser> businessUserOpt =  businessUserRepository.findById(id);
            if(businessUserOpt.isPresent()) {
                return businessUserOpt.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public BusinessUser getUserByEmail(String email) {
        try {
            Optional<BusinessUser> businessUserOpt =  businessUserRepository.findByEmail(email);
            if(businessUserOpt.isPresent()) {
                return businessUserOpt.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    
    public List<BusinessUser> getAllBusinessUsers(){
        try {
            return businessUserRepository.findAll();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Update


    //Delete


    //Login Authentication
    public boolean authenticate(LoginDTO loginDTO, BusinessUser businessUserR) {
        return loginDTO.getEmail().equals(businessUserR.getEmail()) && loginDTO.getPassword().equals(businessUserR.getPassword());
    }

}

