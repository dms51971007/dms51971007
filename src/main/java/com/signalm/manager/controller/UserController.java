package com.signalm.manager.controller;

import com.signalm.manager.model.User;
import com.signalm.manager.serv.UserService;
import com.signalm.manager.to.ToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String userList(Model theModel) {

        List<ToUser> userList = userService.getUsers().stream().map(post -> new ToUser(post)).collect(Collectors.toList());

        theModel.addAttribute("userList", userList);


        return "userlist";
    }



    @PostMapping("/edit")
    public String saveUser(@RequestParam("id") int id,
                           @RequestParam("email") String email,
                           @RequestParam("name") String name,
                           @RequestParam("surname") String surname,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "isactive", required = false) String isactive,
                           @RequestParam(value = "isadmin", required = false) String isadmin,
                           @RequestParam(value = "islist", required = false) String islist,
                           HttpServletRequest request,
                           Model theModel) {
        ToUser u = new ToUser();
        u.setId(id);
        u.setEmail(email);
        u.setName(name);
        u.setSurname(surname);
        u.setUserName(username);
        u.setPassword(password);
        if (isactive != null) u.setActive(true);
        if (isadmin != null) u.setAdmin(true);
        if (islist != null) u.setList(true);

        userService.save(u.getUser());
        List<ToUser> userList = userService.getUsers().stream().map(post -> new ToUser(post)).collect(Collectors.toList());
        theModel.addAttribute("userList", userList);
        ToUser authUser = new ToUser((User) request.getSession().getAttribute("user"));
        theModel.addAttribute("auth_user", authUser);

        return "userlist";
    }
}
