package com.signalm.manager.serv;

import com.signalm.manager.model.Memo;

public interface MemoService {

    void addMemo(Memo memo);

    Memo getMemo(int id);

    void deleteMemo(int id, int authUserID);


}
