package com.elisbao.spring_nava_api.repositories;

import com.elisbao.spring_nava_api.models.UserAddress;
import com.elisbao.spring_nava_api.models.dto.UserAddressDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    @Query("SELECT new com.elisbao.spring_nava_api.models.dto.UserAddressDTO(u.cep, u.logradouro, u.numero, u.complemento, u.bairro, u.localidade, u.uf, u.regiao) FROM UserAddress u WHERE u.user.id = :userId")
    List<UserAddressDTO> findByUser_Id(@Param("userId") Long userId);
}
