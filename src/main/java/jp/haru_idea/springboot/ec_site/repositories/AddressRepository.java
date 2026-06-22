package jp.haru_idea.springboot.ec_site.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findById(int id);
    void deleteById(int id);
    Collection<Address> findByUserId(int userId);
    Collection<Address> findByUserIdAndAddressTypeNot(int userId, int addressType);
}
