package jp.haru_idea.springboot.ec_site.services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import jp.haru_idea.springboot.ec_site.models.CartDetail;
import jp.haru_idea.springboot.ec_site.models.Discount;
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

    public void copyFromCartDetail(Collection<CartDetail> cartDetails, Order order, Discount discount){
        for(CartDetail cartDetail : cartDetails){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartDetail.getProduct());              
            orderDetail.setSellingPrice(cartDetail.getProduct().getSellingPrice());
            orderDetail.setTax(cartDetail.getProduct().getTax().getRate());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setDiscount(discount);
            save(orderDetail);
        }
    }

    public Collection<OrderDetail> getAllByOrderDetails(Collection<Order> orders){
        Collection<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        if(orders != null){
            for(Order order : orders){
                orderDetails.addAll(orderDetailsRepository.findAllByOrder(order));
            }
        }
        return orderDetails;
    }

}
