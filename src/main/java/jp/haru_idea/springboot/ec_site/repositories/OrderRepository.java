package jp.haru_idea.springboot.ec_site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
    Order findById(int id);
}
