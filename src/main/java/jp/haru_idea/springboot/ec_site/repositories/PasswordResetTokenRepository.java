package jp.haru_idea.springboot.ec_site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer>{
    PasswordResetToken findByUserId(int userId);
    PasswordResetToken findByToken(String token);  
}