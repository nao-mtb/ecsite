package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import jp.haru_idea.springboot.ec_site.models.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer>{
    CreditCard findById(int id);
    Collection<CreditCard> findByUserId(int userId);
    Collection<CreditCard> findByUserIdOrderByCardDefaultDesc(int userId);
    CreditCard deleteById(int id);
    int findByCardDefault(int userId);
}
