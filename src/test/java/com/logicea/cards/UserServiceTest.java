package com.logicea.cards;

import com.logicea.cards.entity.UserEntity;
import com.logicea.cards.repository.UserRepository;
import com.logicea.cards.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authenticateUser_ValidCredentials_ReturnsUser() {
        String email = "abc@gmail.com";
        String password = "password123";
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword(password);
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
        UserEntity result = userService.authenticateUser(email, password);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(email, result.getEmail());
        Assertions.assertEquals(password, result.getPassword());
    }

    @Test
    public void authenticateUser_InvalidCredentials_ReturnsNull() {
        String email = "abc@gmail.com";
        String password = "password123";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
        UserEntity result = userService.authenticateUser(email, password);
        Assertions.assertNull(result);
    }
}
