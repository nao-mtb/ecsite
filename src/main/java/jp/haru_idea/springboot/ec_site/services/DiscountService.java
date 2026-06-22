package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Discount;
import jp.haru_idea.springboot.ec_site.repositories.DiscountRepository;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;

    public Collection<Discount> getAll(){
        return discountRepository.findAll();
    }

    // public Optional<Discount> currentSale(){
    //     Date currentDate = new Date();
    //     Collection<Discount> discounts = getAll();
    //     return discounts.stream()
    //             .filter(discount -> currentDate.after(discount.getSaleFrom()) && currentDate.before(discount.getSaleTo()))
    //             .findFirst();
    // }

    public Discount currentSale(){
        Date currentDate = new Date();
        Collection<Discount> discounts = getAll();
        Discount currentDiscount =  discounts.stream()
            .filter(discount -> currentDate.after(discount.getSaleFrom()) && currentDate.before(discount.getSaleTo()))
            .findFirst()
            .orElse(null);
        return currentDiscount;
    }

}
