package jp.haru_idea.springboot.ec_site.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.UserService;

@ControllerAdvice
public class UserModelAttributeAdvice {
    
    // @Autowired
    // private SecuritySession securitySession;
    
    // @Autowired
    // private UserService userService;

    // @ModelAttribute("lastName")
    // public String lastName(){
    //     int userId = securitySession.getUserId();
    //     User user = userService.getById(userId);
    //     if(user == null){
    //         return "";
    //     }        
    //     return user.getLastName();
    // }

    // @ModelAttribute("firstName")
    // public String firstName(){
    //     int userId = securitySession.getUserId();
    //     User user = userService.getById(userId);
    //     if(user == null){
    //         return "";
    //     }        
    //     return user.getFirstName();
    // }
}
