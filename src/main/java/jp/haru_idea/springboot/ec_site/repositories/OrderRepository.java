package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Order;
import jp.haru_idea.springboot.ec_site.models.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
    Order findById(int id);
    Collection<Order> findAllByUser(User user);
}
