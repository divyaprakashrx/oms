package com.example.oms.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name
            , @RequestParam String phone, @RequestParam String address) {
        User n = new User();
        n.setName(name);
        n.setPhone(phone);
        n.setAddress(address);
        userRepository.save(n);
        return "Saved";
    }


    @GetMapping(path = "/{id}")
    public @ResponseBody User getUserById(@PathVariable("id") int id) {
        return userRepository.findById(id).get();
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping(path = "/update")
    //update user
    public @ResponseBody String updateUser (@RequestParam int id, @RequestParam String name
            , @RequestParam String phone, @RequestParam String address) {
        User n = userRepository.findById(id).get();
        n.setName(name);
        n.setPhone(phone);
        n.setAddress(address);
        userRepository.save(n);
        return "Updated";
    }

    @PostMapping(path="/delete")
    public @ResponseBody String deleteUser(@RequestParam int id) {
        User n = new User();
        n.setId(id);
        userRepository.delete(n);
        return "Deleted";
    }
}
