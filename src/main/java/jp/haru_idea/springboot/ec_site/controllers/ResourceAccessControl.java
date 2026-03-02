package jp.haru_idea.springboot.ec_site.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ResourceAccessControl {
    public boolean checkUserId(Authentication authentication, int userId){
        return true;
    }
}
