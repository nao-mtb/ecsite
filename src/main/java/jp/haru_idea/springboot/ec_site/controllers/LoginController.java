package jp.haru_idea.springboot.ec_site.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(){
        return "users/login";    
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
