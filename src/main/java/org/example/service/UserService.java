package org.example.service;

import org.example.response.Command;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public interface UserService {

    Command signUp(String email, String password);

    Command login(String email, String password);
}
