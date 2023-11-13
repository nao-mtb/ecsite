package jp.haru_idea.springboot.ec_site.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jp.haru_idea.springboot.ec_site.repositories.UserRepository;

@Component
public class SecuritySession {

    @Autowired
    public UserRepository userRepository;

    public int getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            Object principal = auth.getPrincipal();
            if(principal instanceof UserDetails){
                String mail = ((UserDetails)principal).getUsername();
                int userId = userRepository.findByMail(mail).getId();
                return userId;                
            }
        }
        return 0;
    }
}
