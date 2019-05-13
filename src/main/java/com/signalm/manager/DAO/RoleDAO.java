package com.signalm.manager.DAO;

import com.signalm.manager.model.Role;

public interface RoleDAO {

    public Role findRoleByName(String theRoleName);

}