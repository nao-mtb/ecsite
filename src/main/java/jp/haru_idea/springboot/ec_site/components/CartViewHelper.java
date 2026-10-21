package jp.haru_idea.springboot.ec_site.components;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.haru_idea.springboot.ec_site.models.CartDetailView;
import jp.haru_idea.springboot.ec_site.models.CartView;

@Component 
public class CartViewHelper {
    @Autowired 
    CartTotalCalculator cartTotalCalculator;

    public CartView createCartView(Collection<CartDetailView> cartDetailViews){
        CartView cartView = new CartView();
        cartView.setCartDetailViews(cartDetailViews);
        cartView.setTotalPrice(cartTotalCalculator.totalPrice(cartDetailViews));
        return cartView;
    }
}
