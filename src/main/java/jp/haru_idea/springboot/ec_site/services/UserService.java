package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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
    private MailSender mailSender;

    @Autowired
    public UserService(MailSender mailSender){
        this.mailSender = mailSender;

    }

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

    public void sendTokenMail(String status, String mail, String token){
        String url = "";
        String subject = "";
        if(status.equals("register")){
            url = "http://localhost:8080/user/create/auth/verify?token=" + token;
            subject = "【XXXX】Create New Account";
        }else if(status.equals("resetPassword")){
            url = "http://localhost:8080/user/profile/password/reset?token=" + token;
            subject = "【XXXX】Reset Password";
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail);
        mailMessage.setFrom("u6b70co@gmail.com");
        mailMessage.setSubject(subject);
        mailMessage.setText(
            "以下のリンクにアクセスしてください\r\n\r\n" + 
            url + "\r\n\r\n" + " ※このリンクの有効期限は2時間です\r\n" 
            );
        try{
            mailSender.send(mailMessage);
        }catch(MailException e){
            System.out.println("mail error");
        }
    }
    
    // public void sendPasswordResetMail(String mail, String token){
    //     String url = "http://localhost:8080/user/profile/password/reset?token=" + token;
    //     SimpleMailMessage mailMessage = new SimpleMailMessage();
    //     mailMessage.setTo(mail);
    //     mailMessage.setFrom("u6b70co@gmail.com");
    //     mailMessage.setSubject("【XXXX】パスワードリセット");
    //     mailMessage.setText(
    //         "以下のリンクにアクセスして新しいパスワードを設定してください\r\n\r\n" + 
    //         url + "\r\n\r\n" + " ※このリンクの有効期限は2時間です\r\n" 
    //         );
    //     try{
    //         mailSender.send(mailMessage);
    //     }catch(MailException e){
    //         System.out.println("mail error");
    //     }
    // }
    
}
