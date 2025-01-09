package com.elisbao.spring_nava_api.unit.services;

import com.elisbao.spring_nava_api.models.User;
import com.elisbao.spring_nava_api.models.dto.UserCreateDTO;
import com.elisbao.spring_nava_api.models.dto.UserUpdateDTO;
import com.elisbao.spring_nava_api.models.enums.ProfileEnum;
import com.elisbao.spring_nava_api.repositories.UserRepository;
import com.elisbao.spring_nava_api.security.UserSpringSecurity;
import com.elisbao.spring_nava_api.services.UserService;
import com.elisbao.spring_nava_api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
    void testFindByIdWhenUserExists() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> userService.findById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate() {
        User user = new User();
        user.setPassword("password");

        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.create(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.getProfiles().contains(ProfileEnum.USER));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setId(1L);
        user.setPassword("newPassword");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(bCryptPasswordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update(user);

        assertNotNull(result);
        assertEquals("encodedNewPassword", result.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.delete(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFromCreateDTO() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("testUser");
        dto.setPassword("testPassword");

        User user = userService.fromDTO(dto);

        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    void testFromUpdateDTO() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setId(1L);
        dto.setPassword("updatedPassword");

        User user = userService.fromDTO(dto);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("updatedPassword", user.getPassword());
    }
}
