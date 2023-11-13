package jp.haru_idea.springboot.ec_site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findById(int id);
    Role findByName(int roleId);    
}
