package jp.haru_idea.springboot.ec_site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
    User findById(int id);
    void deleteById(int id);
    
    
}
