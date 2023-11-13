package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import jp.haru_idea.springboot.ec_site.models.Cart;
import jp.haru_idea.springboot.ec_site.models.CartDetail;

public interface CartDetailsRepository extends JpaRepository<CartDetail, Integer>{
    // List<CartDetail> findByUserIdAndOrderFlag(int userid, int orderFlag);
    // List<CartDetail> delete(int orderFlag);

}
