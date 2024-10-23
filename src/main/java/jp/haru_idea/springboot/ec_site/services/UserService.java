package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    // @Autowired
    // BCryptPasswordEncoder passwordEncoder;

    // @Autowired
    // PasswordEncoder passwordEncoder;

    public Collection<User> getAll(){
        return userRepository.findAll();
    }

    // @Bean
    // public PasswordEncoder passwordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }
    
    public void save(User user){
        userRepository.save(user);
    }

    // public void save(User user, int passwordFlg){
    //     if(passwordFlg == 1){
    //         user.setPassword(passwordEncoder.encode(user.getPassword()));            
    //     }
    //     userRepository.save(user);
    // }

    public void delete(int id){
        userRepository.deleteById(id);
    }   

    public User getById(int id){
        return userRepository.findById(id);
    }

    public User getByMail(String mail){
        return userRepository.findByMail(mail);
    }

    public boolean isUserExists(String mail){
        return getByMail(mail) != null;
    }
}
