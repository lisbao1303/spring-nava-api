package com.elisbao.spring_nava_api.services;

import com.elisbao.spring_nava_api.models.User;
import com.elisbao.spring_nava_api.models.enums.ProfileEnum;
import com.elisbao.spring_nava_api.repositories.UserRepository;
import com.elisbao.spring_nava_api.security.UserSpringSecurity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameWhenUserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setProfiles(new HashSet<>(List.of(ProfileEnum.ADMIN.getCode())));  // Defina os perfis conforme necessário

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Act
        UserSpringSecurity result = (UserSpringSecurity) userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsernameWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistentuser"));
        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }
}
