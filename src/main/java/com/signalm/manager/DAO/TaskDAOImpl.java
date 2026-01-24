package com.signalm.manager.DAO;

import com.signalm.manager.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO {

    private static final Logger logger = LoggerFactory.getLogger(TaskDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    private static final int PAGE_SIZE = 30;

    @Override
    public Task getTask(int id) {
        logger.info("TaskDAO.getTask id={}", id);
        return entityManager.find(Task.class, id);
    }

    @Override
    public List<Task> getMyTasks(int id, Integer filterID, String search, LocalDateTime dateFrom, LocalDateTime dateTo, int page) {
        logger.info("TaskDAO.getMyTasks userId={} filterId={} search={} dateFrom={} dateTo={} page={}",
                id, filterID, search, dateFrom, dateTo, page);
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

        TypedQuery<Task> query = entityManager.createQuery(hql.toString(), Task.class);
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
        logger.info("TaskDAO.getTasks userId={} filterId={} search={} dateFrom={} dateTo={} page={}",
                id, filterID, search, dateFrom, dateTo, page);
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

        TypedQuery<Task> query = entityManager.createQuery(hql.toString(), Task.class);
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
        logger.info("TaskDAO.addTask id={} title={}", task.getId(), task.getTitle());
        entityManager.merge(task);
        return task.getId();
    }

    @Override
    public void deleteTask(int id) {
        logger.info("TaskDAO.deleteTask id={}", id);
        Query query = entityManager.createQuery("delete from Task where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();

    }

}
