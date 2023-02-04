package vn.cloud.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.model.IndividualUser;
import vn.cloud.cardservice.repository.IndividualUserRepository;

@Service
public class IndividualUserService {
    @Autowired
    IndividualUserRepository individualUserRepository;

    public IndividualUser saveIndividualUser(IndividualUser individualUser) {
        try {
            return individualUserRepository.save(individualUser);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
