package jp.haru_idea.springboot.ec_site.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/admin")
@Controller
public class AdminController{
    @GetMapping("/index")
    @ResponseBody
    public String index(){
        return "認証に成功しました。<a href='/logout'>ログアウト</a>";
    }
}