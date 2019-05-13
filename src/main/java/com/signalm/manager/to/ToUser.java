package com.signalm.manager.to;

import com.signalm.manager.model.Role;
import com.signalm.manager.model.User;

import java.util.ArrayList;


public class ToUser {
    private int id;
    private String fullName;
    private String name;
    private String surname;
    private String userName;
    private String email;
    private long numActiveTask;

    private Boolean isAdmin = false;
    private Boolean isActive = false;
    private Boolean isList = false;

    private String password;

    public ToUser() {
    }

    public ToUser(int id, String fullName, long numActiveTask) {
        this.id = id;
        this.fullName = fullName;
        this.numActiveTask = numActiveTask;
    }

    public ToUser(int id, String fullName, String name, String surname, String userName, String email, long numActiveTask) {
        this.id = id;
        this.fullName = fullName;
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.email = email;
        this.numActiveTask = numActiveTask;
    }

    public ToUser(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.numActiveTask = user.getNumActiveTask();
        this.password = user.getPassword();
        this.isList = user.isIslist();
        for (Role r : user.getRoles()) {
            if (r.getName().equals("ROLE_ADMIN")) isAdmin = true;
            if (r.getName().equals("ROLE_USER")) isActive = true;
        }
    }

    public User getUser() {
        User u = new User();
        u.setId(this.id);
        u.setName(this.name);
        u.setUserName(this.userName);
        u.setSurname(this.getSurname());
        u.setUserName(this.getUserName());
        u.setEmail(this.getEmail());
        u.setNumActiveTask(this.getNumActiveTask());
        u.setPassword(this.getPassword());
        u.setIslist(this.isList);
        u.setRoles(new ArrayList<>());
        if (this.isAdmin) u.getRoles().add(new Role("ROLE_ADMIN"));
        if (this.isActive) u.getRoles().add(new Role("ROLE_USER"));

        return u;
    }

    public Boolean getList() {
        return isList;
    }

    public void setList(Boolean list) {
        isList = list;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getNumActiveTask() {
        return numActiveTask;
    }

    public void setNumActiveTask(long numActiveTask) {
        this.numActiveTask = numActiveTask;
    }


}
