package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.CartDetail;
import jp.haru_idea.springboot.ec_site.models.Discount;
import jp.haru_idea.springboot.ec_site.models.Invoice;
import jp.haru_idea.springboot.ec_site.models.InvoiceDetail;
import jp.haru_idea.springboot.ec_site.repositories.InvoiceDetailsRepository;

@Service
public class InvoiceDetailsService {
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;

    public void save(InvoiceDetail invoiceDetail){
        invoiceDetailsRepository.save(invoiceDetail);
    }

    public void copyFromCartDetail(Collection<CartDetail> cartDetails, Invoice invoice, Discount discount){
        for(CartDetail cartDetail : cartDetails){
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setInvoice(invoice);
            invoiceDetail.setProduct(cartDetail.getProduct());
            invoiceDetail.setPrice(cartDetail.getProduct().getSellingPrice());
            invoiceDetail.setTax(cartDetail.getProduct().getTax().getRate());
            invoiceDetail.setNumber(cartDetail.getQuantity());
            invoiceDetail.setDiscount(discount);
            save(invoiceDetail);
        }
    }
}
