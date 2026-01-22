package com.signalm.manager.serv;

import com.signalm.manager.DAO.MemoDAO;
import com.signalm.manager.model.Memo;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemoServiceImpl implements MemoService {
    private final MemoDAO memoDAO;

    public MemoServiceImpl(MemoDAO memoDAO) {
        this.memoDAO = memoDAO;
    }

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
        Memo memo = memoDAO.getMemo(id);
        if (memo != null && memo.getTask() != null
                && memo.getTask().getCreatedBy() != null
                && memo.getTask().getCreatedBy().getId() == authUserID) {
            memoDAO.deleteMemo(id);
        }
    }
}
