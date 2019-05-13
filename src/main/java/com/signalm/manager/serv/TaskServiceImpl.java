package com.signalm.manager.serv;

import com.signalm.manager.DAO.TaskDAO;
import com.signalm.manager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.StyledEditorKit;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional

public class TaskServiceImpl implements TaskService {

    private final TaskDAO taskDAO;

    @Autowired
    public TaskServiceImpl(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Override
    @Transactional
    public List<Task> getTasks(int id, Integer filterID, int page) {
        return taskDAO.getTasks(id, filterID, page);
    }

    @Override
    @Transactional
    public List<Task> getMyTasks(int id, Integer filterID, int page) {
        return taskDAO.getMyTasks(id, filterID, page);
    }


    @Override
    @Transactional
    public Task getTask(int id) {
        return taskDAO.getTask(id);
    }


    @Override
    @Transactional
    public Task getTaskWithMemo(int id) {
        Task tmp = taskDAO.getTask(id);
        tmp.getMemoList().iterator();
        return tmp;
    }

    @Override
    public int addTask(Task task, int authUserID) {
        if (task.getCreatedBy().getId() == authUserID ) {
            return taskDAO.addTask(task);
        }
        return -1;
    }

    @Override
    public void deleteTask(int id, int authUserID) {
        if (taskDAO.getTask(id).getCreatedBy().getId() == authUserID) {
            taskDAO.deleteTask(id);
        }
    }

    @Override
    public void finishTask(int id, int authUserID) {
        Task task = getTask(id);
        if (task.getResponsible().getId() == authUserID) {

            task.setIsDone(!task.getIsDone());
            if (task.getIsDone()) {
                task.setDateComplete(LocalDateTime.now());
            } else {
                task.setDateComplete(null);
            }
            addTask(task, authUserID);
        }
    }

    @Override
    public void setViewedTask(Task task, int authUserID, Boolean viewed) {
        if (task.getIsViewed()==viewed) return;
        if (task.getResponsible().getId() == authUserID || !viewed) {
            task.setIsViewed(viewed);
            taskDAO.addTask(task);
        }
    }
}

