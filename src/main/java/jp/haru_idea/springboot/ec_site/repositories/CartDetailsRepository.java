package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.CartDetail;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetail, Integer>{
    // List<CartDetail> findByUserIdAndOrderFlag(int userid, int orderFlag);
    // List<CartDetail> delete(int orderFlag);
    CartDetail findByProductIdAndCartId(int productId, int cartId);
    int countByQuantity(int id);
    CartDetail findById(int id);
}
