package jp.haru_idea.springboot.ec_site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.CartDetailsService;
import jp.haru_idea.springboot.ec_site.services.CartService;
import jp.haru_idea.springboot.ec_site.services.UserService;

@ControllerAdvice
public class CommonController {
    
    @Autowired
    private SecuritySession securitySession;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailsService cartDetailsService;

    @Autowired
    private UserService userService;

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

    @ModelAttribute("lastName")
    public String lastName(){
        int userId = securitySession.getUserId();
        User user = userService.getById(userId);
        if(user == null){
            return "";
        }        
        return user.getLastName();
    }

    @ModelAttribute("firstName")
    public String firstName(){
        int userId = securitySession.getUserId();
        User user = userService.getById(userId);
        if(user == null){
            return "";
        }        
        return user.getFirstName();
    }
}
