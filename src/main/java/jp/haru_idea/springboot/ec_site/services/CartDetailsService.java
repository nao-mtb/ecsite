package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.models.CartDetail;
import jp.haru_idea.springboot.ec_site.repositories.CartDetailsRepository;
import jp.haru_idea.springboot.ec_site.repositories.CartRepository;

@Service
public class CartDetailsService {
    @Autowired
    CartDetailsRepository cartDetailsRepository;

    @Autowired
    CartRepository cartRepository;
    
    public Collection<CartDetail> getAll(){
        return cartDetailsRepository.findAll();
    }

    public void delete(int id){
        cartDetailsRepository.deleteById(id);
    }

    public void save(CartDetail cartDetail){
        cartDetailsRepository.save(cartDetail);
    }

    public CartDetail getByProductCartId(int productId, int cartId){
        return cartDetailsRepository.findByProductIdAndCartId(productId, cartId);
    }

    public CartDetail getById(int id){
        return cartDetailsRepository.findById(id);
    }

    // @Query("SELECT * FROM Carts INNER JOIN CartDetails on cart.id = cartDetails.cartId WHERE orderFlag = ?2")
    // public List<CartDetail> getByUserIdAndOrderFlag(int userId, int orderFlag){
    //     return cartDetailsRepository.findByUserIdAndOrderFlag(userId, orderFlag);
    // }

    // @Modifying
    // @Query("DELETE FROM CartDetails WHERE orderFlag = ? and ")
    // public List<CartDetail> getDeleteItem(int orderFlag){
    //     return cartDetailsRepository.delete(orderFlag);        
    // }

}
