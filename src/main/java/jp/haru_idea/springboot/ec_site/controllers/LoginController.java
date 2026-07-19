package jp.haru_idea.springboot.ec_site.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/user/login")
    public String login(){
        return "users/login";
    }

    @GetMapping("/backoffice/login")
    public String backofficeLogin(){
        return "backoffices/login";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/backoffice/home")
    public String backofficeHome() {
        return "backoffices/home";
    }
}
