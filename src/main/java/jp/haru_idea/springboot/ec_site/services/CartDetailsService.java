package jp.haru_idea.springboot.ec_site.services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public int totalQuantity(int cartId){
        return cartDetailsRepository.findAllByCartId(cartId).size();
    }

    public int totalPrice(int cartId){        
        Collection<CartDetail> cartDetails = cartDetailsRepository.findAllByCartId(cartId);
        int price = 0;
        for(CartDetail cartDetail : cartDetails){
            double tax = cartDetail.getProduct().getTax().getRate();
            int product_price = cartDetail.getProduct().getSellingPrice();
            price = price + (product_price + (int)Math.floor(product_price * tax )) * cartDetail.getQuantity();
        }
        return price;
    }

    public Collection<CartDetail> purchaseCartDetails(Collection<Integer> cartDetailsIds){
        Collection<CartDetail> cartDetails = new ArrayList<CartDetail>();
        for(Integer cartDetailId : cartDetailsIds){
            cartDetails.add(getById(cartDetailId));
        }
        return cartDetails;
    }

    public void deletePurchasedCartDetails(Collection<CartDetail> cartDetails){
        for(CartDetail cartDetail : cartDetails){
            delete(cartDetail.getId());
        }
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
