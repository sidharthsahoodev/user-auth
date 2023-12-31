package org.example.controller;
import org.example.dto.RegisterUserDto;
import org.example.response.Command;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserAuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public Command signup(@Valid @RequestBody RegisterUserDto registerUserDto) {
        Command message = userService.signUp(registerUserDto.getEmail(),registerUserDto.getPassword());
        return message;
    }

}
