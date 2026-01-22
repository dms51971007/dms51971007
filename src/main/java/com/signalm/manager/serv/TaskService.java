package com.signalm.manager.serv;

import com.signalm.manager.model.Task;

import java.time.LocalDateTime;

import java.util.List;

public interface TaskService {

    List<Task> getTasks(int id, Integer filterID, String search, LocalDateTime dateFrom, LocalDateTime dateTo, int page);
    List<Task> getMyTasks(int id, Integer filterID, String search, LocalDateTime dateFrom, LocalDateTime dateTo, int page);

    Task getTask(int id);

    Task getTaskWithMemo(int id);
    int addTask(Task task, int authUserID);

    void deleteTask(int id, int authUserID);

    void finishTask(int id, int authUserID);

    void setViewedTask(Task task, int authUserID, Boolean viewed);
}
