package com.logicea.cards.service;

import com.logicea.cards.entity.User;
import com.logicea.cards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

