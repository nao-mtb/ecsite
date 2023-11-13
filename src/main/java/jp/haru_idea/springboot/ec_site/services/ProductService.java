package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Product;
import jp.haru_idea.springboot.ec_site.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    public Collection<Product> getAll(){
        return productRepository.findAll();
    }

    public void save(Product product){
        productRepository.save(product);
    }

    public void delete(int id){
        productRepository.deleteById(id);
    }

    public Product getById(int id){
        return productRepository.findById(id);
    }

    public Product getByCode(String code){
        return productRepository.findByCode(code);
    }

    public Collection<Product> getByDiscontinuedFlag(int flag){
        return productRepository.findByDiscontinuedFlag(flag);
    } 
}
