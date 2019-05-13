package com.signalm.manager.DAO;

import com.signalm.manager.model.Memo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemoDAOImpl implements MemoDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public MemoDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addMemo(Memo memo) {
        Session session = sessionFactory.getCurrentSession();
        session.save(memo);
    }

    @Override
    public Memo getMemo(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.find(Memo.class, id);
    }

    @Override
    public void deleteMemo(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete from Memo where id=:id");
        query.setParameter("id",id);
        query.executeUpdate();
    }
}
