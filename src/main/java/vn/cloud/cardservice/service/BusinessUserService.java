package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.model.BusinessUser;
import vn.cloud.cardservice.repository.BusinessUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessUserService {
    @Autowired
    BusinessUserRepository businessUserRepository;
    public List<BusinessUser> getAllBusinessUsers(){
       return businessUserRepository.findAll();
    }

    public BusinessUser saveBusinessUser(BusinessUser businessUser) {
        try {
            return businessUserRepository.save(businessUser);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BusinessUser findById(Long id) {
        Optional<BusinessUser> bUserOpt =  businessUserRepository.findById(id);
        return bUserOpt.get();
    }
}

