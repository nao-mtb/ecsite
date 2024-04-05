package jp.haru_idea.springboot.ec_site.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.PasswordResetToken;
import jp.haru_idea.springboot.ec_site.repositories.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenService {
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    public void save(PasswordResetToken passwordResetToken){
        passwordResetTokenRepository.save(passwordResetToken);
    }
    
    public PasswordResetToken getByUserId(int userId){
        return passwordResetTokenRepository.findByUserId(userId);
    }

    public PasswordResetToken getByToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }

    public boolean checkExpiration(Date updateAt){
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String updateDate = dataFormat.format(updateAt); 
        LocalDateTime expireDateTime = LocalDateTime.parse(updateDate,formatter).plusHours(2);
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.isBefore(expireDateTime);
    }
}
