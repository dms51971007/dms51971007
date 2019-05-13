package com.signalm.manager.DAO;

import com.signalm.manager.model.Memo;

public interface MemoDAO {
    void addMemo(Memo memo);
    void deleteMemo(int id);
    Memo getMemo(int id);


}
