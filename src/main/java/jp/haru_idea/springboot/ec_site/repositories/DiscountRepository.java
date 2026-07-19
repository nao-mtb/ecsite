package jp.haru_idea.springboot.ec_site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer>{
    Discount findById(int id);
}
