package com.elisbao.spring_nava_api.services;

import com.elisbao.spring_nava_api.models.UserAddress;
import com.elisbao.spring_nava_api.models.User;
import com.elisbao.spring_nava_api.models.dto.UserAddressDTO;
import com.elisbao.spring_nava_api.repositories.UserAddressRepository;
import com.elisbao.spring_nava_api.security.UserSpringSecurity;
import com.elisbao.spring_nava_api.services.exceptions.AuthorizationException;
import com.elisbao.spring_nava_api.services.exceptions.DataBindingViolationException;
import com.elisbao.spring_nava_api.services.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;

    private final UserService userService;

    public UserAddressService(UserAddressRepository userAddressRepository, UserService userService) {
        this.userAddressRepository = userAddressRepository;
        this.userService = userService;
    }

    public List<UserAddress> findAllByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        return this.userAddressRepository.findByUser_Id(userSpringSecurity.getId());
    }

    @Transactional
    public UserAddress create(UserAddressDTO dto) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        User user = this.userService.findById(userSpringSecurity.getId());

        UserAddress obj = fromDTO(dto);

        obj.setId(null);
        obj.setUser(user);
        obj = this.userAddressRepository.save(obj);
        return obj;
    }

    @Transactional
    public UserAddress update(UserAddressDTO obj, Long id)  {

        UserAddress existingAddress = findById(id);

        if (obj.getCep() != null && !obj.getCep().equals(existingAddress.getCep())) {
            existingAddress.setCep(obj.getCep());
        }
        if (obj.getLogradouro() != null && !obj.getLogradouro().equals(existingAddress.getLogradouro())) {
            existingAddress.setLogradouro(obj.getLogradouro());
        }
        if (obj.getNumero() != null && !obj.getNumero().equals(existingAddress.getNumero())) {
            existingAddress.setNumero(obj.getNumero());
        }
        if (obj.getComplemento() != null && !obj.getComplemento().equals(existingAddress.getComplemento())) {
            existingAddress.setComplemento(obj.getComplemento());
        }
        if (obj.getBairro() != null && !obj.getBairro().equals(existingAddress.getBairro())) {
            existingAddress.setBairro(obj.getBairro());
        }
        if (obj.getLocalidade() != null && !obj.getLocalidade().equals(existingAddress.getLocalidade())) {
            existingAddress.setLocalidade(obj.getLocalidade());
        }
        if (obj.getUf() != null && !obj.getUf().equals(existingAddress.getUf())) {
            existingAddress.setUf(obj.getUf());
        }
        if (obj.getRegiao() != null && !obj.getRegiao().equals(existingAddress.getRegiao())) {
            existingAddress.setRegiao(obj.getRegiao());
        }

        return userAddressRepository.save(existingAddress);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userAddressRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
        }
    }

    private UserAddress findById(Long id) {
        return this.userAddressRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Endereço não encontrado! Id: " + id + ", Tipo: " + UserAddress.class.getName()));
    }

    public UserAddress fromDTO(@Valid UserAddressDTO dto) {
        UserAddress entity = new UserAddress();
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setNumero(dto.getNumero());
        entity.setComplemento(dto.getComplemento());
        entity.setBairro(dto.getBairro());
        entity.setLocalidade(dto.getLocalidade());
        entity.setUf(dto.getUf());
        entity.setRegiao(dto.getRegiao());
        return entity;
    }
}
