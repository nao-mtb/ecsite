package jp.haru_idea.springboot.ec_site.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

import jp.haru_idea.springboot.ec_site.models.Order;
import jp.haru_idea.springboot.ec_site.models.OrderDetail;
import jp.haru_idea.springboot.ec_site.repositories.OrderDetailsRepository;
import jp.haru_idea.springboot.ec_site.repositories.OrderRepository;

@Service
public class OrderDetailsService {
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Autowired
    OrderRepository orderRepository;

    @Transactional
    public void createOrder(){
        Order order = new Order();
        orderRepository.save(order);
        
        OrderDetail orderDetail = new OrderDetail();
        orderDetailsRepository.save(orderDetail);

    }

    public void save(OrderDetail orderDetail){
        orderDetailsRepository.save(orderDetail);
    }

}
