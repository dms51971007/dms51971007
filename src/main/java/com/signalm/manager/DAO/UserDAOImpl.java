package com.signalm.manager.DAO;

import com.signalm.manager.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("UserDAO")
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getUsers() {
        logger.info("UserDAO.getUsers");
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles", User.class);
        return query.getResultList();
    }

    @Override
    public List<User> getUsersWithCountTask() {
        logger.info("UserDAO.getUsersWithCountTask");
        // Get users with task counts - using subquery to avoid grouping issues with JOIN FETCH
        Query query = entityManager.createQuery(
                "SELECT u.id, SUM(CASE WHEN t.isDone = true THEN 0 ELSE 1 END) as num_task " +
                "FROM User u LEFT OUTER JOIN Task t ON t.responsible.id = u.id " +
                "WHERE u.islist = true " +
                "GROUP BY u.id");

        List<Object[]> results = query.getResultList();
        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> userIds = new ArrayList<>();
        for (Object[] row : results) {
            userIds.add((Integer) row[0]);
        }

        // Fetch users with roles in a single optimized query
        TypedQuery<User> userQuery = entityManager.createQuery(
                "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id IN :ids", User.class);
        userQuery.setParameter("ids", userIds);
        List<User> users = userQuery.getResultList();

        // Create a map for quick lookup
        java.util.Map<Integer, Long> taskCountMap = new java.util.HashMap<>();
        for (Object[] row : results) {
            taskCountMap.put((Integer) row[0], ((Number) row[1]).longValue());
        }

        // Set task counts on users
        for (User user : users) {
            Long count = taskCountMap.get(user.getId());
            if (count != null) {
                user.setNumActiveTask(count);
            }
        }

        return users;
    }

    @Override
    public User getUser(int id) {
        logger.info("UserDAO.getUser id={}", id);
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUserName(String theUserName) {
        logger.info("UserDAO.findByUserName username={}", theUserName);
        TypedQuery<User> theQuery = entityManager.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.userName = :uName", User.class);
        theQuery.setParameter("uName", theUserName);
        List<User> results = theQuery.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public void save(User theUser) {
        logger.info("UserDAO.save id={} username={}", theUser.getId(), theUser.getUserName());
        entityManager.merge(theUser);
    }


}
