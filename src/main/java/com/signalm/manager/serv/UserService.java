package com.signalm.manager.serv;

import com.signalm.manager.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {
    List<User> getUsers();
    User getUser(int id);
    User findByUserName(String userName);
    List<User> getUsersWithCountTask();
//    void save(CrmUser crmUser);
    void save(User user);

    }
