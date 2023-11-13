package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    Product findById(int id);
    Product findByName(String name);  
    Product findByCode(String code);
    void deleteById(int id);
    Collection<Product> findByDiscontinuedFlag(int flag);
}
