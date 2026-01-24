package com.signalm.manager.DAO;

import com.signalm.manager.model.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDAO {

	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Role findRoleByName(String theRoleName) {
		logger.info("RoleDAO.findRoleByName name={}", theRoleName);

		TypedQuery<Role> theQuery = entityManager.createQuery(
				"Select r from Role r where r.name=:roleName", Role.class);
		theQuery.setParameter("roleName", theRoleName);
		
		Role theRole = null;
		
		theRole = theQuery.getSingleResult();
		return theRole;
	}
}
