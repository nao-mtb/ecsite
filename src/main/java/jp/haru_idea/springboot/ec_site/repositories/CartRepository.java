package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{
    Cart findById(int id);
    Cart findByUserId(int userId);    
    void deleteByUserId(int userId);
}
