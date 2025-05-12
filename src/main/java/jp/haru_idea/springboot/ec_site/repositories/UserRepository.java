package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
    User findById(int id);
    void deleteById(int id);
    User findByMail(String mail);
    Collection<User> findByLastName(String lastName);
    @Query("SELECT DISTINCT ru.user FROM RoleUser ru WHERE (ru.role.id = :roleId) AND (ru.user.lastName LIKE :lastName) AND (ru.user.firstName LIKE :firstName)")
    Collection<User> findUsersByRoleAndName(@Param("roleId") int roleId, @Param("lastName") String lastName, @Param("firstName") String firstName);
    @Query("SELECT DISTINCT ru.user FROM RoleUser ru WHERE (ru.role.id = :roleId) AND (ru.user.lastName LIKE %:lastName%)")
    Collection<User> findUsersByRoleAndLastName(@Param("roleId") int roleId, @Param("lastName") String lastName);
}
