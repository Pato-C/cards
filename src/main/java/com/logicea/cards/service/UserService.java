package com.logicea.cards.service;

import com.logicea.cards.entity.UserEntity;
import com.logicea.cards.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public static Logger logger = LogManager.getLogger("com.logicea.cards");

    public UserEntity authenticateUser(String email, String password) {
        logger.info("Going to fetch user with email: "+email);
        try {
            UserEntity user = userRepository.findByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
        } catch (Exception e)
        {
            logger.error("Error fetching user "+e);
        }

        return null;
    }



}

