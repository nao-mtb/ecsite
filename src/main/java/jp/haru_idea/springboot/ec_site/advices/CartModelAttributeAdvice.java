package jp.haru_idea.springboot.ec_site.advices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.haru_idea.springboot.ec_site.models.Cart;
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
    private DiscountService discountService;

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
        double discountRate = 0.0;
        if(discountService.currentSale() != null){
            discountRate = discountService.currentSale().getRate();
        }
        //TODO 端数調整・Javascriptを使用してチェックしたアイテムのみの合計金額を表示
        int totalCartPrice = (int)(cartDetailsService.totalPrice(cart.getId()) * (1 - discountRate));
        return totalCartPrice;
    }
}
