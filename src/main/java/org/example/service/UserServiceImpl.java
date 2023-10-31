package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.response.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Command signUp(String email, String password) {
//
//        Command validationCommand = validateEmailAndPassword(email, password);
//        if (!validationCommand.isSuccessful()) {
//            return validationCommand;
//        }


        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return new Command("User already exists", false);
        }

        User user = new User();
        user.setEmail(email);
    //    user.setPassword(password);

        // Hash the password before saving it
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return new Command("User successfully registered", true);
    }

    private Command validateEmailAndPassword(String email, String password) {
        // Email regex pattern
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        // Password regex pattern
        // The following regex ensures that the password is at least 8 characters long,
        // contains at least one digit, one lower case letter, one upper case letter,
        // and one special character.
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);

        if (!emailPattern.matcher(email).matches()) {
            return new Command("Invalid email format", false);
        }

        if (!passwordPattern.matcher(password).matches()) {
            return new Command("Password must be at least 8 characters long, "
                    + "contain at least one digit, one lower case letter, "
                    + "one upper case letter, and one special character", false);
        }

        return new Command("Validation successful", true);
    }


}
