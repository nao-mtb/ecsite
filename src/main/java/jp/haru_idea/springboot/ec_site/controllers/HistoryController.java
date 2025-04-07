package jp.haru_idea.springboot.ec_site.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jp.haru_idea.springboot.ec_site.models.Order;
import jp.haru_idea.springboot.ec_site.models.OrderDetail;
import jp.haru_idea.springboot.ec_site.securities.SecuritySession;
import jp.haru_idea.springboot.ec_site.services.OrderDetailsService;
import jp.haru_idea.springboot.ec_site.services.OrderService;
import jp.haru_idea.springboot.ec_site.services.UserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RequestMapping("/order-history")
@Controller
public class HistoryController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private SecuritySession securitySession;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String history(Model model){
        int userId = securitySession.getUserId();
        Collection<Order> orders = orderService.getAllByUser(userService.getById(userId));
        Collection<OrderDetail> orderDetails = orderDetailsService.getAllByOrderDetails(orders);
        model.addAttribute("orderDetails", orderDetails);

        //TODO orderIDごとに表示変更
        return "histories/index";
    }
    
    
    
    

}
