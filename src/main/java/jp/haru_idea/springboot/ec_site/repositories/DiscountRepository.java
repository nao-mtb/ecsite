package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer>{

}
