package org.example.repository;

import org.example.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    User findByEmail(String email);
}
