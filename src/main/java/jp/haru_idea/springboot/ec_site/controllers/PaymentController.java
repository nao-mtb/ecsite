package jp.haru_idea.springboot.ec_site.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.haru_idea.springboot.ec_site.configs.AuthUserID;
import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.models.CartDetail;
import jp.haru_idea.springboot.ec_site.models.CreditCard;
import jp.haru_idea.springboot.ec_site.models.Invoice;
import jp.haru_idea.springboot.ec_site.models.InvoiceDetail;
import jp.haru_idea.springboot.ec_site.models.Order;
import jp.haru_idea.springboot.ec_site.models.OrderDetail;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.services.CartDetailsService;
import jp.haru_idea.springboot.ec_site.services.CartService;
import jp.haru_idea.springboot.ec_site.services.CreditCardService;
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

    
    // TODO 支払指定するカードが有効期限切れの時はカード情報を再入力 
    @GetMapping("/{userId}/card")
    public String payment(@PathVariable int userId, Model model, RedirectAttributes attrs){
        // AuthUserID authUserID = new AuthUserID();
        // Boolean result = authUserID.checkUserId();
        
        Collection<CreditCard> creditCards = creditCardService.getUserIdOrderByCardDefaultDesc(userId);
        if (creditCards.size() == 0) {
            return "redirect:/user/credit-card/create/" + userId;
        }else{
            model.addAttribute("creditCards", creditCards);
            return "payments/card";    
        }
    } 

    // TODO 後で購入する商品を削除して決済
    @Transactional
    @PatchMapping("/{userId}/order")
    public String moveCartToOrderAndInvoice(
        @PathVariable int userId, Model model, 
        HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes attrs,
        User user
        ){
            Cart cart = cartService.getByUserId(userId);
            Collection<CartDetail> cartDetails = cart.getCartDetails();
            Order order = new Order();
            order.setUser(userService.getById(userId));
            order.setOrderDate(new Date());
            orderService.save(order);
            for(CartDetail cartDetail : cartDetails){
                if(cartDetail.getOrderFlag() == 1){
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cartDetail.getProduct());              
                    orderDetail.setPrice(cartDetail.getProduct().getSellingPrice());
                    orderDetail.setTax(0.1);
                    orderDetail.setNumber(cartDetail.getQuantity());
                    orderDetailsService.save(orderDetail);
                    System.out.println(cartDetail);
                }
            }

            Invoice invoice = new Invoice();
            invoice.setOrder(order);
            invoice.setCreditCard(creditCardService.getById(Integer.parseInt(request.getParameter("creditCard"))));
            invoiceService.save(invoice);
            for(CartDetail cartDetail : cartDetails){
                if(cartDetail.getOrderFlag() == 1){                
                    InvoiceDetail invoiceDetail = new InvoiceDetail();
                    invoiceDetail.setInvoice(invoice);
                    invoiceDetail.setProduct(cartDetail.getProduct());
                    invoiceDetail.setPrice(cartDetail.getProduct().getSellingPrice());
                    invoiceDetail.setTax(0.1);
                    invoiceDetail.setNumber(cartDetail.getQuantity());
                    invoiceDetailsService.save(invoiceDetail);
                }
            }
            for(CartDetail cartDetail : cartDetails){
                if(cartDetail.getOrderFlag() == 1){
                    cartDetailsService.delete(cartDetail.getId());
                }
            }

            attrs.addFlashAttribute("success", "商品の購入が完了しました");
            return "redirect:/cart/view/" + userId ;
        }


    // @PostMapping("/view/{userId}/pre-order")
    // public String  preOrder(@PathVariable int userId, HttpServletRequest request, Model model){
    //     HttpSession session = request.getSession();
    //     session.setAttribute("cart", cartService.getById(id));
    //     return "redirect:/" 
    // }

    // @GetMapping("/view/{userId}/payment")
    // public String payment(@PathVariable int userId, Model model){
    //     model.addAttribute("cart", cartService.getByUserId(userId));
    //     return "carts/buy";
    // }
    // @PostMapping("/{userId}/order")
    // public ResponseEntity<String> createOrder(){
    //     orderDetailsService.createOrder();
    //     return ResponseEntity.ok("");
    // }
            

        

}
