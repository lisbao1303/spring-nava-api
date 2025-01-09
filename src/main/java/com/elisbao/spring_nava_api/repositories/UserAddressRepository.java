package com.elisbao.spring_nava_api.repositories;

import com.elisbao.spring_nava_api.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    List<UserAddress> findByUser_Id(@Param("userId") Long userId);
}
