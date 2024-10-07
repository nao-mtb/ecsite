package jp.haru_idea.springboot.ec_site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>{
    Token findByUserId(int userId);
    Token findByToken(String token);
}