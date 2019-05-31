package com.kovzel.repository;

import com.kovzel.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    default User findByEmail(String email) throws IllegalAccessException {
        for (User user : this.findAll()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        throw new IllegalAccessException("Invalid chat");
    }
}