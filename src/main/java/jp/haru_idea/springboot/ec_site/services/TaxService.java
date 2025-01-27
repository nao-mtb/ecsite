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

}    
