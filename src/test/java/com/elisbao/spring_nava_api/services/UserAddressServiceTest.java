package com.elisbao.spring_nava_api.services;

import com.elisbao.spring_nava_api.models.User;
import com.elisbao.spring_nava_api.models.UserAddress;
import com.elisbao.spring_nava_api.models.dto.UserAddressDTO;
import com.elisbao.spring_nava_api.models.enums.ProfileEnum;
import com.elisbao.spring_nava_api.repositories.UserAddressRepository;
import com.elisbao.spring_nava_api.security.UserSpringSecurity;
import com.elisbao.spring_nava_api.services.exceptions.DataBindingViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAddressServiceTest {

    @InjectMocks
    private UserAddressService userAddressService;

    @Mock
    private UserAddressRepository userAddressRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserSpringSecurity userSpringSecurity;

    @BeforeAll
    static void setUp() {
        UserSpringSecurity userSpringSecurity = new UserSpringSecurity(1L, "test", "password", new HashSet<>(List.of(ProfileEnum.ADMIN)));
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userSpringSecurity);
        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    void setUpEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllByUser() {
        User user = new User();
        user.setId(1L);

        List<UserAddressDTO> listAddress = new ArrayList<>();
        UserAddressDTO address = new UserAddressDTO();
        listAddress.add(address);

        when(userSpringSecurity.getId()).thenReturn(1L);
        when(userAddressRepository.findByUser_Id(1L)).thenReturn(listAddress);

        List<UserAddressDTO> result = userAddressService.findAllByUser();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userAddressRepository, times(1)).findByUser_Id(1L);
    }

    @Test
    void testCreate() {
        UserAddressDTO dto = new UserAddressDTO();
        dto.setCep("12345");
        dto.setLogradouro("Street 1");

        User user = new User();
        user.setId(1L);

        when(userService.findById(1L)).thenReturn(user);
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAddress result = userAddressService.create(dto);

        assertNotNull(result);
        assertEquals(dto.getCep(), result.getCep());
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    void testUpdate() {
        UserAddressDTO dto = new UserAddressDTO();
        dto.setCep("54321");
        dto.setLogradouro("Street 2");

        UserAddress existingAddress = new UserAddress();
        existingAddress.setId(1L);
        existingAddress.setCep("12345");
        existingAddress.setLogradouro("Street 1");

        when(userAddressRepository.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAddress result = userAddressService.update(dto, 1L);

        assertNotNull(result);
        assertEquals(dto.getCep(), result.getCep());
        assertEquals(dto.getLogradouro(), result.getLogradouro());
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    void testDelete() {
        UserAddress address = new UserAddress();
        address.setId(1L);

        when(userAddressRepository.findById(1L)).thenReturn(Optional.of(address));
        doNothing().when(userAddressRepository).deleteById(1L);

        assertDoesNotThrow(() -> userAddressService.delete(1L));
        verify(userAddressRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteThrowsExceptionIfRelatedEntitiesExist() {
        UserAddress address = new UserAddress();
        address.setId(1L);

        when(userAddressRepository.findById(1L)).thenReturn(Optional.of(address));
        doThrow(new RuntimeException("Foreign key violation")).when(userAddressRepository).deleteById(1L);

        assertThrows(DataBindingViolationException.class, () -> userAddressService.delete(1L));
        verify(userAddressRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFromDTO() {
        UserAddressDTO dto = new UserAddressDTO();
        dto.setCep("98765");
        dto.setLogradouro("Street 3");

        UserAddress result = userAddressService.fromDTO(dto);

        assertNotNull(result);
        assertEquals(dto.getCep(), result.getCep());
        assertEquals(dto.getLogradouro(), result.getLogradouro());
    }
}
