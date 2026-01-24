package com.signalm.manager.DAO;

import com.signalm.manager.model.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MemoDAOImpl implements MemoDAO {

    private static final Logger logger = LoggerFactory.getLogger(MemoDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addMemo(Memo memo) {
        logger.info("MemoDAO.addMemo id={} taskId={}", memo.getId(),
                memo.getTask() != null ? memo.getTask().getId() : null);
        entityManager.persist(memo);
    }

    @Override
    public Memo getMemo(int id) {
        logger.info("MemoDAO.getMemo id={}", id);
        return entityManager.find(Memo.class, id);
    }

    @Override
    public void deleteMemo(int id) {
        logger.info("MemoDAO.deleteMemo id={}", id);
        Query query = entityManager.createQuery("delete from Memo where id=:id");
        query.setParameter("id",id);
        query.executeUpdate();
    }
}
