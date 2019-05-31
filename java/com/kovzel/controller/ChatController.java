package com.kovzel.controller;

import com.kovzel.entity.*;
import com.kovzel.repository.ChatRepository;
import com.kovzel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/chat")
public class ChatController {
    private Chat generalChat = new Chat(0);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserController userController;

    @GetMapping(path = "/show/{id}")
    public @ResponseBody
    List<Message> showChat(@PathVariable("id") int id) throws IllegalAccessException {
        if(chatRepository.findById(id).isPresent()) {
            Integer userId = userController.getUserId();
            if (userId != null) {
                Chat chat = chatRepository.findById(id).get();
                if(chat.getUsers().contains(userRepository.findById(userId).get())){
                    return chat.getMessages();
                }
                throw new IllegalAccessException("You aren't a member of this chat");
            }
            throw new IllegalAccessException("Invalid current user");
        }
        throw new IllegalAccessException("Invalid chat");
    }

    @GetMapping(path = "/send/{id}")
    public @ResponseBody
    List<Message> sendMessage(@PathVariable("id") int id, @RequestParam String text) throws IllegalAccessException {
        if (chatRepository.existsById(id)) {
            Chat chat = chatRepository.findById(id).get();
            Integer userId = userController.getUserId();
            if (userId != null) {
                if(chat.getUsers().contains(userRepository.findById(userId).get())){
                    User user = userRepository.findById(userId).get();
                    Message message = new Message();
                    Date date = new Date();
                    message.setUser(user);
                    message.setMessage(text);
                    message.setDate(date);
                    message.setNameUser(user.getFirstName());
                    chat.addMessage(message);
                    return chat.getMessages();
                }
                throw new IllegalAccessException("You aren't a member of this chat");
            }
            throw new IllegalAccessException("Invalid current user");
        }
        throw new IllegalAccessException("Invalid chat");
    }

    @GetMapping("/all")
    public String getAllChats(Model model) {
        model.addAttribute("chats", chatRepository.findAll());
        return "chats";
    }

    @GetMapping(path = "/save")
    public @ResponseBody
    String addChat(@RequestParam String name,
                     @RequestParam int user_id) throws IllegalAccessException {
        Optional<User> user1 = userRepository.findById(user_id);
        Optional<User> user2 = userRepository.findById(userController.getUserId());
        if (user1.isPresent() && user2.isPresent() && user1 != user2) {
            Chat chat = new Chat();
            chat.setName(name);
            chat.addUser(user1.get());
            chat.addUser(user2.get());
            chatRepository.save(chat);
            return "Successfully";
        }
        throw new IllegalAccessException("Invalid current user or user_id");
    }


    @GetMapping(path = "/addUser/{id}")
    public @ResponseBody
    String addToChat(@PathVariable("id") int id_chat,
                     @RequestParam int id_user) throws IllegalAccessException {
        Optional<Chat> chat = chatRepository.findById(id_chat);
        Optional<User> user = userRepository.findById(id_user);
        if (user.isPresent() && chat.isPresent()) {
            chat.get().addUser(user.get());
            chatRepository.save(chat.get());
            return "Add" + user.get().getLastName() + " " + user.get().getFirstName() + " Into " + chat.get().getName();
        }
        throw new IllegalAccessException("Invalid user or chat");
    }
}
