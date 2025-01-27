package jp.haru_idea.springboot.ec_site.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Order;
import jp.haru_idea.springboot.ec_site.models.User;
import jp.haru_idea.springboot.ec_site.repositories.OrderRepository;

@Service
public class OrderService {
    
    @Autowired
    OrderRepository orderRepository;

    public void save(Order order){
        orderRepository.save(order);
    }

    public Order getById(int id){
        return orderRepository.findById(id);
    }

    public void createOrder(Order order, User user){
        order.setUser(user);
        order.setOrderDate(new Date());
        save(order);
    }
}
