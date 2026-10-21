package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.haru_idea.springboot.ec_site.components.CartViewHelper;
import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.models.CartDetail;
import jp.haru_idea.springboot.ec_site.models.CartDetailView;
import jp.haru_idea.springboot.ec_site.models.CartView;
import jp.haru_idea.springboot.ec_site.models.Product;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.CartDetailsService;
import jp.haru_idea.springboot.ec_site.services.CartService;
import jp.haru_idea.springboot.ec_site.services.DiscountService;
import jp.haru_idea.springboot.ec_site.services.ProductService;
import jp.haru_idea.springboot.ec_site.services.UserService;

@RequestMapping("/cart")
@Controller
public class CartController{
    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailsService cartDetailsService;

    @Autowired 
    private CartViewHelper cartViewHelper;

    @Autowired
    private SecuritySession securitySession;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscountService discountService;

    @GetMapping("/view")
    public String viewUser(Model model){
        int userId = securitySession.getUserId();
        Cart cart = cartService.getByUserId(userId);
        if(cart == null){
            model.addAttribute("cartDetailViews", null);
            model.addAttribute("totalCartPrice", 0);
            return "carts/view";
        }
        Collection<CartDetail> cartDetails = cartDetailsService.getAllCartDetails(cart.getId());
        double discountRate = 0;
        if(discountService.currentSale() != null){
            discountRate = discountService.currentSale().getRate();
        }
        Collection<CartDetailView> cartDetailViews = cartDetailsService.createCartDetailViews(cartDetails, discountRate);
        CartView cartView = cartViewHelper.createCartView(cartDetailViews);
        model.addAttribute("cartView", cartView);
        return "carts/view";
    }

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
