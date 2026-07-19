package jp.haru_idea.springboot.ec_site.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public void save(Discount discount){
        discountRepository.save(discount);
    }

    public Discount getById(int id){
        return discountRepository.findById(id);
    }

    public void delete(int id){
        discountRepository.deleteById(id);
    }

    // public Optional<Discount> currentSale(){
    //     Date currentDate = new Date();
    //     Collection<Discount> discounts = getAll();
    //     return discounts.stream()
    //             .filter(discount -> currentDate.after(discount.getSaleFrom()) && currentDate.before(discount.getSaleTo()))
    //             .findFirst();
    // }

    public Discount currentSale(){
        LocalDateTime currentDate = LocalDateTime.now();
        Collection<Discount> discounts = getAll();
        Discount currentDiscount =  discounts.stream()
            .filter(discount -> currentDate.isAfter(discount.getSaleFrom()) && currentDate.isBefore(discount.getSaleTo()))
            .findFirst()
            .orElse(null);
        return currentDiscount;
    }
}
