package jp.haru_idea.springboot.ec_site.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.models.CartDetail;
import jp.haru_idea.springboot.ec_site.models.Product;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.CartDetailsService;
import jp.haru_idea.springboot.ec_site.services.CartService;
import jp.haru_idea.springboot.ec_site.services.ProductService;
import jp.haru_idea.springboot.ec_site.services.UserService;

@RequestMapping("/cart")
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailsService cartDetailsService;

    @Autowired
    private SecuritySession securitySession;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/view")
    public String viewUser(Model model){
        int userId = securitySession.getUserId();
        model.addAttribute("cart", cartService.getByUserId(userId));
        return "carts/view";
    }

    //確認用テストコード
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

    @PatchMapping("/shoppingcart")
    public String addCart(
            @RequestParam("quantity") int quantity,
            @RequestParam("id") int productId,
            RedirectAttributes attrs){
        int userId = securitySession.getUserId();
        User user = userService.getById(userId);
        Cart cart = cartService.getByUserId(userId);
        if (cart == null){
            cart = new Cart();
            cart.setUser(user);
        }
        cartService.save(cart);

        Product product = productService.getById(productId);
        CartDetail cartDetail = cartDetailsService.getByProductCartId(productId, cart.getId());
        if (cartDetail == null){
            cartDetail = new CartDetail();
            cartDetail.setQuantity(quantity);
            cartDetail.setProduct(product);
            cartDetail.setCart(cart);
        }else{
            cartDetail.setQuantity(quantity + cartDetail.getQuantity());
        }
        cartDetailsService.save(cartDetail);
        attrs.addFlashAttribute("success", "商品を追加しました");
        return "redirect:/cart/view";
    }    
}
