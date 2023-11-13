package jp.haru_idea.springboot.ec_site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.jdbc.core.JdbcTemplate;

import jp.haru_idea.springboot.ec_site.repositories.CartRepository;
import jp.haru_idea.springboot.ec_site.repositories.InvoiceDetailsRepository;
import jp.haru_idea.springboot.ec_site.repositories.InvoiceRepository;
import jp.haru_idea.springboot.ec_site.repositories.OrderDetailsRepository;
import jp.haru_idea.springboot.ec_site.repositories.OrderRepository;

@Service
public class PaymentService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private  JdbcTemplate jdbcTemplate;

    
}
