package jp.haru_idea.springboot.ec_site.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.CartDetailsService;
import jp.haru_idea.springboot.ec_site.services.CartService;

@ControllerAdvice
public class CartModelAttributeAdvice {
    
    @Autowired
    private SecuritySession securitySession;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailsService cartDetailsService;

    @ModelAttribute("totalCartQuantity")
    public int totalCartQuantity(){
        int userId = securitySession.getUserId();
        Cart cart = cartService.getByUserId(userId);
        if(cart == null){
            return 0;
        }        
        return cartDetailsService.totalQuantity(cart.getId());        
    }
    
    @ModelAttribute("totalCartPrice")
    public int totalCartPrice(){
        int userId = securitySession.getUserId();
        Cart cart = cartService.getByUserId(userId);
        if(cart == null){
            return 0;
        }        
        return cartDetailsService.totalPrice(cart.getId());
    }
}
