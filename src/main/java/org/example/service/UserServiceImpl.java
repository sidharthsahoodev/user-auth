package org.example.service;

import io.jsonwebtoken.Claims;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.response.Command;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Command signUp(String email, String password) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return new Command("User already exists", false);
        }

        User user = new User();
        user.setEmail(email);
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        //userRepository.save(user);
        executorService.submit(() -> userRepository.save(user));

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

    @Override
    public Command login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String jwtToken = jwtUtil.generateToken(email);
            return new Command("Login successful", true, jwtToken);
        }
        return new Command("Invalid credentials", false);
    }
    public Command verifyJwt(String jwtToken) {
        if (jwtUtil.validateToken(jwtToken)) {
            Claims claims = jwtUtil.extractClaims(jwtToken);
            String email = claims.getSubject();
            User user = userRepository.findByEmail(email);
            if (user != null) {
                return new Command("Token valid", true);
            } else {
                return new Command("User not found", false);
            }
        } else {
            return new Command("Token invalid or expired", false);
        }
    }
    public Command refreshJwt(String oldJwtToken) {
        if (jwtUtil.validateToken(oldJwtToken)) {
            String newJwtToken = jwtUtil.refreshToken(oldJwtToken);
            return new Command("Token refreshed", true, newJwtToken);
        } else {
            return new Command("Token invalid or expired. LOGOUT", false, null);
        }
    }

}
