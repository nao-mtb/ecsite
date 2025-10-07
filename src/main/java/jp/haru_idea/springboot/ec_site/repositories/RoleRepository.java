package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Role;
import jp.haru_idea.springboot.ec_site.models.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findById(int id);
    Role findByRoleType(RoleType roleType);
    Collection<Role> findByRoleTypeNotIn(Collection<RoleType> roleType);    
}
