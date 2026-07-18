package jp.haru_idea.springboot.ec_site.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.haru_idea.springboot.ec_site.models.Tax;
import jp.haru_idea.springboot.ec_site.repositories.TaxRepository;

@Service
public class TaxService {
    @Autowired
    TaxRepository taxRepository;

    public Collection<Tax> getAll(){
        return taxRepository.findAll();
    }

    public Tax getById(int id){
        return taxRepository.findById(id);
    }

    public void save(Tax tax){
        taxRepository.save(tax);
    }

    public void delete(int id){
        taxRepository.deleteById(id);
    }
}    
