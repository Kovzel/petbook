package com.kovzel.controller;

import com.kovzel.entity.User;
import com.kovzel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    private Integer userId;

    @Autowired
    private UserRepository userRepository;

    Integer getUserId() {
        return userId;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user) throws IllegalAccessException {
        if (userId == null) {
            User user1 = userRepository.findByEmail(user.getEmail());
            if (user1.getEmail() != null) {
                if (user1.getPassword().equals(user.getPassword())) {
                    userId = user1.getId();
                    return "userInfo"; //        <----------- fix it!!!
                }
                throw new IllegalAccessException("Wrong password");
            }
            throw new IllegalAccessException("Wrong email");
        }
        throw new IllegalAccessException("You are already logged in. If you want to choose another account please log out");
    }

    @GetMapping("/register")
    public String registerForm(Model model) throws IllegalAccessException {
        if (userId == null) {
            model.addAttribute("user", new User());
            return "register";
        }
        throw new IllegalAccessException("You are already logged in. If you want to choose another account please log out");
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) throws IllegalAccessException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            userRepository.save(user);
            return "userInfo";
        }
        throw new IllegalAccessException("This email is already taken");
    }

    @GetMapping("/all")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/info")
    public @ResponseBody
    String getInfoUser(Model model) throws IllegalAccessException {
        if (userId != null) {
            model.addAttribute("user", userRepository.findById(getUserId()));
            return "userInfo";
        }
        throw new IllegalAccessException("Invalid current user");
    }

    @GetMapping("/findUser/{id}")
    public @ResponseBody
    String getUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userRepository.findById(id).get());
        return "userInfo";
    }

    @GetMapping("/logout")
    public @ResponseBody
    String logout() throws IllegalAccessException {
        if (userId != null) {
            userId = null;
            return "Successfully";
        }
        throw new IllegalAccessException("Invalid current user");
    }

    @GetMapping("/delete")
    public @ResponseBody
    String deleteValidUser(@PathVariable("id") int id) throws IllegalAccessException {
        if (userId != null) {
            userRepository.deleteById(userId);
            return "index";
        }
        throw new IllegalAccessException("Invalid current user");
    }

    @GetMapping(value = "/update")
    public @ResponseBody
    String updateUser(@RequestParam String password,
                      @RequestParam(required = false) String firstName,
                      @RequestParam(required = false) String lastName,
                      @RequestParam(required = false) String email,
                      @RequestParam(required = false) String newPassword) throws IllegalAccessException {
        if (userId != null) {
            User user = userRepository.findById(userId).get();
            if (user.getPassword().equals(password)) {
                if (firstName != null) {
                    user.setFirstName(firstName);
                }
                if (lastName != null) {
                    user.setLastName(lastName);
                }
                if (email != null) {
                    user.setEmail(email);
                }
                if (newPassword != null) {
                    user.setPassword(password);
                }
                userRepository.save(user);
                return "Successfully";
            }
            throw new IllegalAccessException("Wrong password");
        }
        throw new IllegalAccessException("Invalid current user");
    }
}