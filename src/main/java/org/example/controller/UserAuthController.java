package org.example.controller;
import org.example.dto.UserAuthDto;
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
    public Command signup(@Valid @RequestBody UserAuthDto userAuthDto) {
        Command message = userService.signUp(userAuthDto.getEmail(), userAuthDto.getPassword());
        return message;
    }
    @PostMapping("/login")
    public Command login(@Valid @RequestBody UserAuthDto loginRequestDto) {
        return userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
    }

}
