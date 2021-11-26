package com.signalm.manager.controller;

import com.signalm.manager.model.Memo;
import com.signalm.manager.model.Task;
import com.signalm.manager.model.User;
import com.signalm.manager.serv.MemoService;
import com.signalm.manager.serv.TaskService;
import com.signalm.manager.serv.UserService;
import com.signalm.manager.to.ToMemo;
import com.signalm.manager.to.ToTask;
import com.signalm.manager.to.ToTaskFull;
import com.signalm.manager.to.ToUser;
import com.signalm.manager.util.MapperManager;
import com.signalm.manager.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final UserService userService;

    private final MapperManager mapperManager = new MapperManager();

    private final TaskService taskService;

    private final MemoService memoService;

    @Autowired
    public TaskController(UserService userService, TaskService taskService, MemoService memoService) {
        this.userService = userService;
        this.taskService = taskService;
        this.memoService = memoService;
        //      this.commonsMultipartResolver = commonsMultipartResolver;
    }

    @RequestMapping("/")
    public String defaultTask() {
        return "redirect:/task/tasklist?user_id=-1&page=1";
    }


    @GetMapping("/tasklist")
    public String showTaskList(@RequestParam("user_id") int user_id,
                               @RequestParam("page") int page,
                               @RequestParam(value = "filter_user_id", required = false) Integer filterUserID,
                               Model theModel,
                               HttpServletRequest request) {

        if (filterUserID != null) {
            HttpSession session = request.getSession();
            if (filterUserID != -1)
                session.setAttribute("filter", filterUserID);
            else
                session.setAttribute("filter", null);

        }

        prepareModelTaskList(user_id, page, theModel, request);

        return "tasklist";
    }


    @GetMapping("/deletetask")
    public String delTask(@RequestParam("user_id") int user_id,
                          @RequestParam("page") int page,
                          @RequestParam("task_id") int task_id,
                          Model theModel,
                          HttpServletRequest request) {

        taskService.deleteTask(task_id, getAuthUser(request).getId());
        prepareModelTaskList(user_id, page, theModel, request);

        return "redirect:tasklist";

    }

    @GetMapping("/finishtask")
    public String finishTask(@RequestParam("user_id") int user_id,
                             @RequestParam("page") int page,
                             @RequestParam("task_id") int task_id,
                             Model theModel,
                             HttpServletRequest request) {

        taskService.finishTask(task_id, getAuthUser(request).getId());
        prepareModelTaskList(user_id, page, theModel, request);

        return "redirect:tasklist";

    }


    @GetMapping("/edittask")
    public String addTask(@RequestParam int user_id,
                          @RequestParam int page,
                          @RequestParam int task_id,
                          Model theModel,
                          HttpServletRequest request) {

        prepareModelShowTask(user_id, page, task_id, theModel, request);

        return "edittask";

    }

    @GetMapping("/addtask")
    public String addTask(@RequestParam("user_id") int user_id,
                          @RequestParam("page") int page,
                          Model theModel,
                          HttpServletRequest request) {
        prepareModelShowTask(user_id, page, 0, theModel, request);
        ToTaskFull tm = new ToTaskFull();
        tm.setDateCreate(LocalDateTime.now());
        tm.setDateBegin(LocalDateTime.now());
        tm.setDateEnd(LocalDateTime.now());
        tm.setDateComplete(null);
        tm.setResponsible(new ToUser(userService.getUser(user_id)));
        tm.setIsViewed(false);
        theModel.addAttribute("task_to", tm);
        // theModel.addAttribute("responsible_id", user_id);
        return "edittask";

    }


    @GetMapping("/imagedisplay")
    public void showImage(@RequestParam("id") Integer memo_id, HttpServletResponse response) throws IOException {
        Memo item = memoService.getMemo(memo_id);
        String filename = URLEncoder.encode(item.getFileName().replace(' ', '_'), java.nio.charset.StandardCharsets.UTF_8.toString());

        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.getOutputStream().write(item.getFiledata());
        response.getOutputStream().close();
    }


    @GetMapping("/deletememo")
    public String deleteMemo(@RequestParam("user_id") int user_id,
                             @RequestParam("page") int page,
                             @RequestParam("task_id") int task_id,
                             @RequestParam("memo_id") int memo_id,
                             Model theModel,
                             HttpServletRequest request) {

        memoService.deleteMemo(memo_id, getAuthUser(request).getId());

        prepareModelShowTask(user_id, page, task_id, theModel, request);

        return "showtask";
    }

    @GetMapping("/showtask")
    public String showTask(@RequestParam("user_id") int user_id,
                           @RequestParam("page") int page,
                           @RequestParam("task_id") int task_id,
                           Model theModel,
                           HttpServletRequest request) {


        prepareModelShowTask(user_id, page, task_id, theModel, request);
        ToMemo tm = new ToMemo();
        theModel.addAttribute("memo_to", tm);

        return "showtask";
    }

    @RequestMapping(value = "/savetask", method = RequestMethod.POST)
    public String saveTask(@ModelAttribute("task_to") ToTaskFull toTask,
                           @RequestParam int user_id,
                           @RequestParam int page,
                           @RequestParam int responsible_id,
                           Model theModel,
                           HttpServletRequest request) {

        Task task = mapperManager.map(toTask, Task.class);
        if (task.getId() == 0) {
            task.setCreatedBy(getAuthUser(request));
            task.setResponsible(userService.getUser(responsible_id));
        } else {
            Task oldTask = taskService.getTask(task.getId());
            task.setCreatedBy(oldTask.getCreatedBy());
            task.setResponsible(oldTask.getResponsible());
        }
        int task_id = taskService.addTask(task, getAuthUser(request).getId());
        prepareModelShowTask(user_id, page, task_id, theModel, request);

        return "showtask";
    }


    @RequestMapping(value = "/savememo", method = RequestMethod.POST)
    public String handleFileUpload(@ModelAttribute("memo_to") ToMemo toMemo,
                                   @RequestParam MultipartFile[] fileUpload,
                                   @RequestParam int user_id,
                                   @RequestParam int page,
                                   @RequestParam int task_id,
                                   Model theModel,
                                   HttpServletRequest request) {
        User authUser = getAuthUser(request);

        if (fileUpload != null && fileUpload.length > 0) {
            for (MultipartFile aFile : fileUpload) {
                toMemo.setFileName(aFile.getOriginalFilename());

                try {
                    toMemo.setFiledata(aFile.getBytes());
                    if (Objects.requireNonNull(aFile.getContentType()).contains("image")) toMemo.setPicture(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Task task = taskService.getTaskWithMemo(task_id);
        ToTaskFull toTaskFull = mapperManager.map(task, ToTaskFull.class);

        toMemo.setDateCreate(LocalDateTime.now());
        toMemo.setCreatedBy(authUser);
        toMemo.setTask(toTaskFull);

        memoService.addMemo(mapperManager.map(toMemo, Memo.class));
        taskService.setViewedTask(task, authUser.getId(), false);
        prepareModelShowTask(user_id, page, task_id, theModel, request);

        return "showtask";
    }


    private void prepareModelShowTask(int user_id,
                                      int page,
                                      int task_id,
                                      Model theModel,
                                      HttpServletRequest request) {
        ToUser authUser = mapperManager.map(request.getSession().getAttribute("user"), ToUser.class);

        List<ToUser> userList = userService.getUsersWithCountTask().stream().map(ToUser::new).collect(Collectors.toList());
        if (task_id != 0) {
            Task task = taskService.getTaskWithMemo(task_id);
            ToTaskFull toTaskFull = mapperManager.map(task, ToTaskFull.class);
            theModel.addAttribute("task_to", toTaskFull);
            taskService.setViewedTask(task, authUser.getId(), true);
        }
            Integer filterUserID = (Integer) request.getSession().getAttribute("filter");

            theModel.addAttribute("userList", userList);
            theModel.addAttribute("user_id", user_id);
            theModel.addAttribute("auth_user", authUser);
            theModel.addAttribute("page", page);
            theModel.addAttribute("filter_user_id", filterUserID);

    }

    private void prepareModelTaskList(int user_id,
                                      int page,
                                      Model theModel,
                                      HttpServletRequest request) {
        ToUser authUser = new ToUser(getAuthUser(request));
        Integer filterUserID = (Integer) request.getSession().getAttribute("filter");
        List<ToUser> userList = userService.getUsersWithCountTask().stream().map(ToUser::new).collect(Collectors.toList());
        List<ToUser> userListFilter = userService.getUsers().stream().map(ToUser::new).collect(Collectors.toList());

        List<ToTask> taskList;
        if (user_id == -1) {
            taskList = taskService.getMyTasks(authUser.getId(), filterUserID, page).stream().map(post -> mapperManager.map(post, ToTask.class)).collect(Collectors.toList());
        } else {
            taskList = taskService.getTasks(user_id, filterUserID, page).stream().map(post -> mapperManager.map(post, ToTask.class)).collect(Collectors.toList());
        }
        List<Integer> pageList = Utils.getPagin(page);
        theModel.addAttribute("taskList", taskList);
        theModel.addAttribute("userList", userList);
        theModel.addAttribute("userListFilter", userListFilter);
        theModel.addAttribute("pageList", pageList);
        theModel.addAttribute("user_id", user_id);
        theModel.addAttribute("auth_user", authUser);
        theModel.addAttribute("page", page);
        theModel.addAttribute("filter_user_id", filterUserID);
        if (filterUserID != null)
            theModel.addAttribute("filter_user_name", (userService.getUser(filterUserID).getFullName()));
        else
            theModel.addAttribute("filter_user_name", "Фильтр");


    }

    private User getAuthUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }
}
