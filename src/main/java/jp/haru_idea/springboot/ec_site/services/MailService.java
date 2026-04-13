package jp.haru_idea.springboot.ec_site.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private MailSender mailSender;

    //TODO　URL管理確認 
    public Map<String, String> createTokenMailContent(String status, String token){
        Map<String, String> contents = new HashMap<>();
        String url = "";
        String subject = "";
        if(status.equals("register")){
            subject = "【XXXX】Create New Account";
            url = "http://localhost:8080/user/create/auth/verify?token=" + token;

        }else if(status.equals("resetPassword")){
            subject = "【XXXX】Reset Password";
            url = "http://localhost:8080/password/reset?token=" + token;
        }

        contents.put("subject", subject);
        contents.put("message",
                    "以下のリンクにアクセスしてください\r\n\r\n" + url + 
                    "\r\n\r\n" + " ※このリンクの有効期限は2時間です\r\n");
        return contents;
    }

    //TODO メール未達時の対応
    public void sendMail(String mail, String subject, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail);
        mailMessage.setFrom("u6b70co@gmail.com");
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        try{
            mailSender.send(mailMessage);
        }catch(MailException e){
            System.out.println("mail error");
        }
    }
}
