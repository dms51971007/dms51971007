package com.signalm.manager.DAO;


import com.signalm.manager.model.Task;

import java.time.LocalDateTime;

import java.util.List;


public interface TaskDAO  {
    List<Task> getTasks(int id, Integer filterID, String search, LocalDateTime dateFrom, LocalDateTime dateTo, int page);
    List<Task> getMyTasks(int id, Integer filterID, String search, LocalDateTime dateFrom, LocalDateTime dateTo, int page);
    Task getTask(int id);

    int addTask(Task memo);

    void deleteTask(int id);



}
