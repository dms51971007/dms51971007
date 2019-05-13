package com.signalm.manager.DAO;

import com.signalm.manager.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public TaskDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Task getTask(int id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Task.class, id);
    }

    @Override
    public List<Task> getMyTasks(int id, Integer filterID, int page) {
        Session session = sessionFactory.getCurrentSession();
        Query<Task> query;
        if (filterID != null) {
            query = session.createQuery("SELECT t from Task t where t.createdBy.id=:id and t.responsible.id=:filter_id order by t.isDone ASC, t.dateEnd DESC ", Task.class);
            query.setParameter("filter_id", filterID);
        } else
            query = session.createQuery("SELECT t from Task t where t.createdBy.id=:id order by t.isDone ASC, t.dateEnd DESC ", Task.class);

        query.setMaxResults(30);
        query.setFirstResult((page - 1) * 30);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Task> getTasks(int id, Integer filterID, int page) {
        Session session = sessionFactory.getCurrentSession();

        Query<Task> query;
        if (filterID != null) {
            query = session.createQuery("SELECT t from Task t where t.responsible.id=:id and t.createdBy.id=:filter_id order by t.isDone ASC, t.dateEnd DESC ", Task.class);
            query.setParameter("filter_id", filterID);
        } else
            query = session.createQuery("SELECT t from Task t where t.responsible.id=:id order by t.isDone ASC, t.dateEnd DESC ", Task.class);
        query.setMaxResults(30);
        query.setFirstResult((page - 1) * 30);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public int addTask(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(task);
        return task.getId();
    }

    @Override
    public void deleteTask(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from Task where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();

    }

}
