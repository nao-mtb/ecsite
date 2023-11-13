package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.RoleUser;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Integer>{
    RoleUser findById(int id);
    Collection<RoleUser> findAllById(int id);
    List<RoleUser> findByUserId(int userId);
    // RoleUser findByUserId(int userId);

}
