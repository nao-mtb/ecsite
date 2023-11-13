package jp.haru_idea.springboot.ec_site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{
    
}
