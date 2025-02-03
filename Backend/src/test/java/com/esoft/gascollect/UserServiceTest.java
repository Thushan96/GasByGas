package com.esoft.gascollect;

import com.esoft.gascollect.dto.UserDTO;
import com.esoft.gascollect.entity.Role;
import com.esoft.gascollect.entity.User;
import com.esoft.gascollect.repository.RoleRepository;
import com.esoft.gascollect.repository.UserRepository;
import com.esoft.gascollect.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder; // Ensure this is mocked

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO(
                0,
                "John Doe",
                "john.doe@example.com",
                1234567890,
                "password123",
                Set.of("ROLE_USER")
        );

        Role userRole = new Role(1, "ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User(
                1,
                userDTO.getName(),
                userDTO.getContactNo(),
                userDTO.getEmail(),
                "encodedPassword",
                Set.of(userRole),
                null
        );
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.addUser(userDTO);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getName(), result.getName());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getContactNo(), result.getContactNo());
        assertTrue(result.getRoles().contains("ROLE_USER"));

        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUser_Success() {
        // Arrange
        int userId = 1;
        Role userRole = new Role();
        userRole.setId(1);
        userRole.setName("USER");

        User user = new User(
                userId,
                "John Doe",
                1234567890,
                "john.doe@example.com",
                "encodedPassword",
                Set.of(userRole),
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.getUser(userId);

        assertTrue(result.isPresent());
        UserDTO userDTO = result.get();
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getContactNo(), userDTO.getContactNo());
        assertTrue(userDTO.getRoles().contains("USER"));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUser_UserNotFound() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUser(userId);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

}

