package com.signalm.manager.DAO;

import com.signalm.manager.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDAO {

	private final SessionFactory sessionFactory;

	public RoleDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Role findRoleByName(String theRoleName) {

		Session currentSession = sessionFactory.getCurrentSession();

		Query<Role> theQuery = currentSession.createQuery("Select r from Role r where r.name=:roleName", Role.class);
		theQuery.setParameter("roleName", theRoleName);
		
		Role theRole = null;
		
		theRole = theQuery.getSingleResult();

		return theRole;
	}
}
