package com.signalm.manager.DAO;

import com.signalm.manager.model.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO {

    private final SessionFactory sessionFactory;
    private static final int PAGE_SIZE = 30;

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
    public List<Task> getMyTasks(int id, Integer filterID, String search, LocalDateTime dateFrom, LocalDateTime dateTo, int page) {
        Session session = sessionFactory.getCurrentSession();
        boolean hasSearch = search != null && !search.trim().isEmpty();
        String searchValue = hasSearch ? "%" + search.toLowerCase() + "%" : null;

        StringBuilder hql = new StringBuilder("SELECT t from Task t where t.createdBy.id=:id");
        if (filterID != null) {
            hql.append(" and t.responsible.id=:filter_id");
        }
        if (hasSearch) {
            hql.append(" and lower(t.title) like :search");
        }
        if (dateFrom != null) {
            hql.append(" and t.dateCreate >= :date_from");
        }
        if (dateTo != null) {
            hql.append(" and t.dateCreate <= :date_to");
        }
        hql.append(" order by t.isDone ASC, t.dateEnd DESC ");

        Query<Task> query = session.createQuery(hql.toString(), Task.class);
        if (filterID != null) {
            query.setParameter("filter_id", filterID);
        }
        if (hasSearch) {
            query.setParameter("search", searchValue);
        }
        if (dateFrom != null) {
            query.setParameter("date_from", dateFrom);
        }
        if (dateTo != null) {
            query.setParameter("date_to", dateTo);
        }

        query.setMaxResults(PAGE_SIZE);
        query.setFirstResult((page - 1) * PAGE_SIZE);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public List<Task> getTasks(int id, Integer filterID, String search, LocalDateTime dateFrom, LocalDateTime dateTo, int page) {
        Session session = sessionFactory.getCurrentSession();

        boolean hasSearch = search != null && !search.trim().isEmpty();
        String searchValue = hasSearch ? "%" + search.toLowerCase() + "%" : null;

        StringBuilder hql = new StringBuilder("SELECT t from Task t where t.responsible.id=:id");
        if (filterID != null) {
            hql.append(" and t.createdBy.id=:filter_id");
        }
        if (hasSearch) {
            hql.append(" and lower(t.title) like :search");
        }
        if (dateFrom != null) {
            hql.append(" and t.dateCreate >= :date_from");
        }
        if (dateTo != null) {
            hql.append(" and t.dateCreate <= :date_to");
        }
        hql.append(" order by t.isDone ASC, t.dateEnd DESC ");

        Query<Task> query = session.createQuery(hql.toString(), Task.class);
        if (filterID != null) {
            query.setParameter("filter_id", filterID);
        }
        if (hasSearch) {
            query.setParameter("search", searchValue);
        }
        if (dateFrom != null) {
            query.setParameter("date_from", dateFrom);
        }
        if (dateTo != null) {
            query.setParameter("date_to", dateTo);
        }
        query.setMaxResults(PAGE_SIZE);
        query.setFirstResult((page - 1) * PAGE_SIZE);
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
