package jp.haru_idea.springboot.ec_site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Invoice;
import jp.haru_idea.springboot.ec_site.repositories.InvoiceRepository;

@Service
public class InvoiceService {
    @Autowired
    InvoiceRepository invoiceRepository;

    public void save(Invoice invoice){
        invoiceRepository.save(invoice);
    }

}
