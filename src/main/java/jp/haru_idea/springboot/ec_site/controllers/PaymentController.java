package jp.haru_idea.springboot.ec_site.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.models.CartDetail;
import jp.haru_idea.springboot.ec_site.models.CreditCard;
import jp.haru_idea.springboot.ec_site.models.Discount;
import jp.haru_idea.springboot.ec_site.models.Invoice;
import jp.haru_idea.springboot.ec_site.models.InvoiceDetail;
import jp.haru_idea.springboot.ec_site.models.Order;
import jp.haru_idea.springboot.ec_site.models.OrderDetail;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.CartDetailsService;
import jp.haru_idea.springboot.ec_site.services.CartService;
import jp.haru_idea.springboot.ec_site.services.CreditCardService;
import jp.haru_idea.springboot.ec_site.services.DiscountService;
import jp.haru_idea.springboot.ec_site.services.InvoiceDetailsService;
import jp.haru_idea.springboot.ec_site.services.InvoiceService;
import jp.haru_idea.springboot.ec_site.services.OrderDetailsService;
import jp.haru_idea.springboot.ec_site.services.OrderService;
import jp.haru_idea.springboot.ec_site.services.UserService;


@RequestMapping("/payment")
@Controller
public class PaymentController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartDetailsService cartDetailsService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private InvoiceDetailsService invoiceDetailsService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private SecuritySession securitySession;
    
    //TODO Javascriptでカート画面操作を作成
    @GetMapping("/card")
    public String payment(@Valid
            Model model, RedirectAttributes attrs, HttpSession session,
            @RequestParam(name="cartDetailId", required = false) Collection<Integer> cartDetailsIds
        ){
        int userId = securitySession.getUserId();
        if (cartDetailsIds != null){
            Collection<CreditCard> creditCards = creditCardService.getUserIdOrderByCardDefaultDesc(userId);
            if (creditCards.size() == 0) {
                return "redirect:/user/credit-card/create/";
            }else{
                model.addAttribute("creditCards", creditCards);
                model.addAttribute("cartDetailsIds", cartDetailsIds);
                return "payments/card";
            }
        }else{
            attrs.addFlashAttribute("success", "購入商品がありません");
            return "redirect:/cart/view/";
        }
    }

    @Transactional
    @PatchMapping("/order")
    public String moveCartToOrderAndInvoice(
            @RequestParam("cartDetailsId") Collection<Integer> cartDetailsIds,
            HttpServletRequest request,
            HttpServletResponse response, RedirectAttributes attrs,
            User user){
        int userId = securitySession.getUserId();

        Collection<CartDetail> cartDetails = cartDetailsService.purchaseCartDetails(cartDetailsIds);
        Order order = new Order();
        orderService.createOrder(order, userService.getById(userId));
        Discount discount = discountService.currentSale();
        orderDetailsService.copyFromCartDetail(cartDetails, order, discount);

        Invoice invoice = new Invoice();
        CreditCard  creditCard = creditCardService.getById(Integer.parseInt(request.getParameter("creditCard")));
        invoiceService.createInvoice(invoice, order, creditCard);
        invoiceDetailsService.copyFromCartDetail(cartDetails, invoice, discount);  
        cartDetailsService.deletePurchasedCartDetails(cartDetails);
        attrs.addFlashAttribute("success", "商品の購入が完了しました");
        return "redirect:/cart/view/";
    }
}
