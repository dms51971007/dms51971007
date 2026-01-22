package com.signalm.manager.DAO;

import com.signalm.manager.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("UserDAO")
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getUsers() {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery(
                "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles", User.class);
        return query.getResultList();
    }

    @Override
    public List<User> getUsersWithCountTask() {
        Session session = sessionFactory.getCurrentSession();
        // Get users with task counts - using subquery to avoid grouping issues with JOIN FETCH
        Query query = session.createQuery(
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
        Query<User> userQuery = session.createQuery(
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
        Session session = sessionFactory.getCurrentSession();
        return session.find(User.class, id);
    }

    @Override
    public User findByUserName(String theUserName) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<User> theQuery = currentSession.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.userName = :uName", User.class);
        theQuery.setParameter("uName", theUserName);
        List<User> results = theQuery.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public void save(User theUser) {
        // get current hibernate session
        Session currentSession = sessionFactory.getCurrentSession();

        // create the user ... finally LOL
        currentSession.saveOrUpdate(theUser);
    }


}
