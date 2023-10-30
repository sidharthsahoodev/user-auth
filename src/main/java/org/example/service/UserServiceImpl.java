package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.response.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Override
    public Command signUp(String email, String password) {
        User existingUser = userRepository.findByEmail(email);

        if (existingUser != null) {
            return new Command("User already exists", false);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return new Command("User successfully registered", true);
    }
}
