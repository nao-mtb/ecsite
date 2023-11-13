package jp.haru_idea.springboot.ec_site.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.haru_idea.springboot.ec_site.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findById(int id);
    void deleteById(int id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Address address set shipping_default = 0 where address.user.id = :userId")
    void resetShippingDefault(@Param("userId") int userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Address address set billing_default = 0 where address.user.id = :userId")
    void resetBillingDefault(@Param("userId") int userId);

}
