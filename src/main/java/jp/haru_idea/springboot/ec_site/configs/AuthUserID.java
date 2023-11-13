package jp.haru_idea.springboot.ec_site.configs;

import org.springframework.beans.factory.annotation.Autowired;

import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.services.UserService;

public class AuthUserID{
    @Autowired
    UserService userService;

    private boolean checkUserId(int userId){
        User user = new User();
        if (userId == user.getId()){
            return true;    
        }else{
            return false;
        }
    }
}
