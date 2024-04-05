package jp.haru_idea.springboot.ec_site.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.CartService;

@RequestMapping("/cart")
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private SecuritySession securitySession;

    @GetMapping("/view")
    public String viewUser(Model model){
        int userId = securitySession.getUserId();
        if (userId == 0){
            return "users/login";
        }
        model.addAttribute("cart", cartService.getByUserId(userId));
        return "carts/view";
    }

    @GetMapping("/view-test/{id}")
    public String view(@PathVariable int id, Model model){
        model.addAttribute("cart", cartService.getById(id));
        return "carts/view-test";
    }


    // @GetMapping("/view/{cacheString}")
    // public String view(@PathVariable String cacheString, Model model){
    //     // Collection<Cart> carts = cartService.getAllByCash(cashString);
    //     model.addAttribute("cart", carts);
    //     return "carts/view";
    // }

    @PatchMapping("/shopping-cart/{productId}")
    public String addCart(@PathVariable int productId, RedirectAttributes attrs){
        Cart cart = cartService.getById(productId);
        cartService.save(cart);
        attrs.addFlashAttribute("success", "商品を追加しました");
        // if(){
        //     return "redirect:/view/" + cart.getById();
        // }
        return "redirect:/view/" ;
    }

    // @PatchMapping("/detail/{productId}")
    // public String addDetailCart(@PathVariable int productId, @PathVariable int num, Model model, RedirectAttributes attrs){
    //     Cart cart = cartService.getById(productId);
    //     quantity = cartService.getByQuantity(num);
    //     cartService.save(cart);
    //     attrs.addFlashAttribute("success", "商品を追加しました");
    //     if(){
    //         return "redirect:/view/" + cart.getById();
    //     }
    //     return "redirect:/product/shopping/index";
    // }
    
}
