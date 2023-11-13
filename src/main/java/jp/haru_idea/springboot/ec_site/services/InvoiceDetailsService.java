package jp.haru_idea.springboot.ec_site.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.InvoiceDetail;
import jp.haru_idea.springboot.ec_site.repositories.InvoiceDetailsRepository;

@Service
public class InvoiceDetailsService {
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;

    public void save(InvoiceDetail invoiceDetail){
        invoiceDetailsRepository.save(invoiceDetail);
    }
    
}
