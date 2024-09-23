package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.CreditCard;
import jp.haru_idea.springboot.ec_site.repositories.CreditCardRepository;

@Service
public class CreditCardService {
    @Autowired CreditCardRepository creditCardRepository;

    public void save(CreditCard creditCard){
        creditCardRepository.save(creditCard);
    }

    public void delete(int id){
        creditCardRepository.deleteById(id);
    }

    public CreditCard getById(int id){
        return creditCardRepository.findById(id);
    }

    public Collection<CreditCard> getUserId(int userId){
        return creditCardRepository.findByUserId(userId);
    }

    //発生日付をソートして表示する場合
    // public Collection<HouseholdAccount> getAll(){
    //     Sort sortOrder = Sort.by("eventDate");
    //     return householdAccountRepository.findAll(sortOrder);
    // }
    public Collection<CreditCard> getUserIdOrderByCardDefaultDesc(int userId){
        return creditCardRepository.findByUserIdOrderByCardDefaultDesc(userId);
    }

    public CreditCard getDefaultCreditCards(int userId){
        Collection<CreditCard> creditCards = getUserId(userId);
        CreditCard defaultCreditCard = creditCards.stream()
            .filter(creditCard -> creditCard.getCardDefault() == 1)
            .findFirst()
            .orElse(null);
        return defaultCreditCard;
    }

    public int getCardDefault(int userId){
        return creditCardRepository.findByCardDefault(userId);
    }    
}
