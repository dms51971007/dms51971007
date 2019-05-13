package com.signalm.manager.serv;

import com.signalm.manager.DAO.MemoDAO;
import com.signalm.manager.model.Memo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class MemoServiceImpl implements MemoService {
    @Autowired
    private MemoDAO memoDAO;

    @Override
    public void addMemo(Memo memo) {
        memoDAO.addMemo(memo);
    }

    @Override
    public Memo getMemo(int id) {
        return memoDAO.getMemo(id);
    }

    @Override
    public void deleteMemo(int id, int authUserID ) {
        if (memoDAO.getMemo(id).getTask().getCreatedBy().getId() == authUserID)
        memoDAO.deleteMemo(id);
    }
}
