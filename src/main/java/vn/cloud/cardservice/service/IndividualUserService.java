package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.repository.IndividualUserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class IndividualUserService {

    Map<Boolean,IndividualUser> map;
    @Autowired
    IndividualUserRepository individualUserRepository;

    //Create
    public IndividualUser saveIndividualUser(IndividualUser individualUser) {
        try {
            return individualUserRepository.save(individualUser);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Retrieve
    public IndividualUser getUserById(Long id) {
        try {
            Optional<IndividualUser> individualUserOpt =  individualUserRepository.findById(id);
            if(individualUserOpt.isPresent()) {
                return individualUserOpt.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public IndividualUser getUserByEmail(String email) {
        try {
            Optional<IndividualUser> individualUserOpt =  individualUserRepository.findByEmail(email);
            if(individualUserOpt.isPresent()) {
                return individualUserOpt.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public List<IndividualUser> getAllIndividualUsers(){
        try {
            return individualUserRepository.findAll();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Update


    //Delete


    //Login Authentication
    public boolean authenticate(LoginDTO loginDTO, IndividualUser individualUserR) {
        return loginDTO.getEmail().equals(individualUserR.getEmail()) && loginDTO.getPassword().equals(individualUserR.getPassword());
    }

}
