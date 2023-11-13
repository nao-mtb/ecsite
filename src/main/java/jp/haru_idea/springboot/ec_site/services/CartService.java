package jp.haru_idea.springboot.ec_site.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.repositories.CartRepository;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    private JdbcTemplate JdbcTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager; 

    public Collection<Cart> getAll(){
        return cartRepository.findAll();
    }

    public void save(Cart cart){
        cartRepository.save(cart);
    }

    public Cart getById(int id){
        return cartRepository.findById(id);
    }

    // public Collection<Cart> getAllById(int id){
    //     return cartRepository.findAllById(id);
    // }
    
    public Cart getByUserId(int userId){
        return cartRepository.findByUserId(userId);
    }
    
    public void delete(int userId){
        cartRepository.deleteByUserId(userId);
    }

    public int addParentRecordAndGetPrimaryKey(String parentName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        JdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO parent(name) VALUE(?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,parentName);
            return ps;
        },
        keyHolder);
        return keyHolder.getKey().intValue();
    }



    // public void moveData(int userId){
    //     Cart cart = getByUserId(userId);
    //     String sql = "INSERT INTO cart_details(productId, quantity, productId.)"
    //     Query query = EntityManager.createQuery();
    // }

}
