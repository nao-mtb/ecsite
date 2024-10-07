package jp.haru_idea.springboot.ec_site.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Token;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.repositories.TokenRepository;

@Service
public class TokenService {
    @Autowired
    TokenRepository tokenRepository;

    public void save(Token token){
        tokenRepository.save(token);
    }
    
    public Token getByUserId(int userId){
        return tokenRepository.findByUserId(userId);
    }

    public Token getByToken(String token){
        return tokenRepository.findByToken(token);
    }

    public void deleteById(int id){
        tokenRepository.deleteById(id);
    }   

    public boolean isExpirationDate(Date updateAt){
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String updateDate = dataFormat.format(updateAt); 
        LocalDateTime expireDateTime = LocalDateTime.parse(updateDate,formatter).plusHours(2);
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.isBefore(expireDateTime);
    }

    public Token findOrCreateUserToken(User user){
        Token token = getByUserId(user.getId());
        if (token == null){
            token = new Token();
            token.setUser(user);
        }
        return token;
    }

}
