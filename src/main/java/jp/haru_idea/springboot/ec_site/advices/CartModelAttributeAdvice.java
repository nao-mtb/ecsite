package jp.haru_idea.springboot.ec_site.advices;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.haru_idea.springboot.ec_site.components.CartTotalCalculator;
import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.models.CartDetail;
import jp.haru_idea.springboot.ec_site.models.CartDetailView;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.CartDetailsService;
import jp.haru_idea.springboot.ec_site.services.CartService;
import jp.haru_idea.springboot.ec_site.services.DiscountService;

@ControllerAdvice
public class CartModelAttributeAdvice {
    
    @Autowired
    private SecuritySession securitySession;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailsService cartDetailsService;

    @Autowired
    private CartTotalCalculator cartTotalCalculator;

    @Autowired
    private DiscountService discountService;

    @ModelAttribute("totalCartQuantity")
    public int totalCartQuantity(){
        int userId = securitySession.getUserId();
        Cart cart = cartService.getByUserId(userId);
        if(cart == null){
            return 0;
        }
        Collection<CartDetail> cartDetails = cartDetailsService.getAllCartDetails(cart.getId());
        Collection<CartDetailView> cartDetailViews = cartDetailsService.createCartDetailViews(cartDetails);
        return cartTotalCalculator.totalQuantity(cartDetailViews);
    }
    
    @ModelAttribute("totalCartPrice")
    public int totalCartPrice(){
        int userId = securitySession.getUserId();
        Cart cart = cartService.getByUserId(userId);
        if(cart == null){
            return 0;
        }
        Collection<CartDetail> cartDetails = cartDetailsService.getAllCartDetails(cart.getId());
        double discountRate = 0;
        if(discountService.currentSale() != null){
            discountRate = discountService.currentSale().getRate();
        }
        Collection<CartDetailView> cartDetailViews = cartDetailsService.createCartDetailViews(cartDetails, discountRate);
        return cartTotalCalculator.totalPrice(cartDetailViews);
    }
}
