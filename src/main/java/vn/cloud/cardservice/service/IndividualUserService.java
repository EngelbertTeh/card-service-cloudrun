package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.repository.IndividualUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class IndividualUserService {
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
    public IndividualUser authenticate(LoginDTO loginDTO) {
        try {
            Optional<IndividualUser> individualUserOpt = individualUserRepository.findByEmail(loginDTO.getEmail());
            if (individualUserOpt.isPresent()) {
                IndividualUser individualUserR = individualUserOpt.get();
                if (individualUserR.getEmail().equals(loginDTO.getEmail()) && individualUserR.getPassword().equals(loginDTO.getPassword())) {
                    return individualUserR;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
