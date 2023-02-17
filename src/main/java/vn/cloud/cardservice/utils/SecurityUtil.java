package vn.cloud.cardservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import vn.cloud.cardservice.dto.LoginDTO;
import vn.cloud.cardservice.model.BaseUserModel;

@Service
public class SecurityUtil<T extends BaseUserModel> {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public  T encryptPassword(T t) {
        try {
            t.setPassword(passwordEncoder.encode(t.getPassword()));
            return t;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean authenticate(LoginDTO loginDTO, T t) {
        try {
            String emailFromLogin = loginDTO.getEmail().toLowerCase();
            String emailFromRepo = t.getEmail().toLowerCase();

            return emailFromLogin.equals(emailFromRepo) && passwordEncoder.matches(loginDTO.getPassword(),t.getPassword());
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
