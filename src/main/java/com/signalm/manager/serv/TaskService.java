package com.signalm.manager.serv;

import com.signalm.manager.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks(int id, Integer filterID,int page);
    List<Task> getMyTasks(int id,Integer filterID, int page);

    Task getTask(int id);

    Task getTaskWithMemo(int id);
    int addTask(Task task, int authUserID);

    void deleteTask(int id, int authUserID);

    void finishTask(int id, int authUserID);

    void setViewedTask(Task task, int authUserID, Boolean viewed);
}
