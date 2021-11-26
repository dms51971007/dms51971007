package com.signalm.manager.DAO;

import com.signalm.manager.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserDAO {

    List<User> getUsers();

    List<User> getUsersWithCountTask();

    User getUser(int id);

    User findByUserName(String userName);

    void save(User user);


}
