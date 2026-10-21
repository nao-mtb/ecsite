package jp.haru_idea.springboot.ec_site.components;

import java.util.Collection;

import org.springframework.stereotype.Component;

import jp.haru_idea.springboot.ec_site.models.CartDetailView;

@Component 
public class CartTotalCalculator {
    public int totalPrice(Collection<CartDetailView> cartDetailViews){
        int totalPrice = 0;
        for(CartDetailView cartDetailView: cartDetailViews){
            totalPrice = totalPrice + cartDetailView.getSellingPrice() * cartDetailView.getQuantity(); 
        }
        return totalPrice;
    }

    public int totalQuantity(Collection<CartDetailView> cartDetailViews){
        int totalQuantity = 0;
        for(CartDetailView cartDetailView: cartDetailViews){
            totalQuantity = totalQuantity + cartDetailView.getQuantity();
        }
        return totalQuantity;
    }
    
}
