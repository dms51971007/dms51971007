package com.signalm.manager.serv;

import com.signalm.manager.DAO.RoleDAO;
import com.signalm.manager.DAO.UserDAO;
import com.signalm.manager.model.Role;
import com.signalm.manager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;


    @Override
   public List<User> getUsers() {

        return userDAO.getUsers();
    }

    @Override
     public List<User> getUsersWithCountTask() {
        return userDAO.getUsersWithCountTask();
    }

    @Override
    public User getUser(int id) {
        return userDAO.getUser(id);
    }

    @Override
    public User findByUserName(String userName) {
        // check the database if the user already exists
        return userDAO.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDAO.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        User u = prepareUser(user);
        userDAO.save(u);
    }

    User prepareUser(User user) {
        Collection<Role> res = new ArrayList<>();

        for (Role r :
                user.getRoles()) {
            if (r.getId() == null)
                res.add(roleDAO.findRoleByName(r.getName()));
        }
        user.setRoles(res);
        return user;
    }
}
