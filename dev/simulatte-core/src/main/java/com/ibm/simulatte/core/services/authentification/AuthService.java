package com.ibm.simulatte.core.services.authentification;

import com.ibm.simulatte.core.datamodels.user.User;
import com.ibm.simulatte.core.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    public User signIn(User knownUser) {
        return knownUser;
    }
}
