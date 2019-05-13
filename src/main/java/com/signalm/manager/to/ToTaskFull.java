package com.signalm.manager.to;


import com.signalm.manager.model.Memo;

import java.util.List;

public class ToTaskFull extends ToTask{
    private List<Memo> memoList;
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<Memo> getMemoList() {
        return memoList;
    }

    public void setMemoList(List<Memo> memoList) {
        this.memoList = memoList;
    }
}
