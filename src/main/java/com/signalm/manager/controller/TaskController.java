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
import com.signalm.manager.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final UserService userService;

    private final TaskService taskService;

    private final MemoService memoService;

    @Autowired
    public TaskController(UserService userService, TaskService taskService, MemoService memoService) {
        this.userService = userService;
        this.taskService = taskService;
        this.memoService = memoService;
        //      this.commonsMultipartResolver = commonsMultipartResolver;
    }

    @ModelAttribute("_csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @RequestMapping("/")
    public String defaultTask() {
        return "redirect:/task/tasklist?user_id=-1&page=1";
    }


    @GetMapping("/tasklist")
    public String showTaskList(@RequestParam(value = "user_id", defaultValue = "-1") int user_id,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "filter_user_id", required = false) Integer filterUserID,
                               @RequestParam(value = "date_from", required = false) String dateFrom,
                               @RequestParam(value = "date_to", required = false) String dateTo,
                               @RequestParam(value = "search", required = false) String search,
                               Model theModel,
                               HttpServletRequest request) {

        if (filterUserID != null) {
            HttpSession session = request.getSession();
            if (filterUserID != -1)
                session.setAttribute("filter", filterUserID);
            else
                session.setAttribute("filter", null);

        }
        if (dateFrom != null || dateTo != null) {
            HttpSession session = request.getSession();
            session.setAttribute("date_from", normalizeDateParam(dateFrom));
            session.setAttribute("date_to", normalizeDateParam(dateTo));
        }
        if (search != null) {
            String trimmedSearch = search.trim();
            if (trimmedSearch.isEmpty()) {
                request.getSession().setAttribute("search", null);
            } else {
                request.getSession().setAttribute("search", trimmedSearch);
            }
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

        Task task = taskService.getTask(task_id);
        if (task == null) {
            return "redirect:/task/tasklist?user_id=" + user_id + "&page=" + page;
        }
        int authUserId = getAuthUser(request).getId();
        if (task.getCreatedBy().getId() != authUserId) {
            return "redirect:/task/showtask?user_id=" + user_id + "&page=" + page + "&task_id=" + task_id;
        }
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
        // Автоматически устанавливаем заказчика как текущего пользователя
        tm.setCreatedBy(new ToUser(getAuthUser(request)));
        tm.setIsViewed(false);
        theModel.addAttribute("task_to", tm);
        // theModel.addAttribute("responsible_id", user_id);
        return "edittask";

    }


    @GetMapping("/imagedisplay")
    public void showImage(@RequestParam("id") Integer memo_id, HttpServletResponse response) throws IOException {
        Memo item = memoService.getMemo(memo_id);
        if (item == null || item.getFiledata() == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = item.getFileName() != null ? item.getFileName() : "file";
        String lowerName = fileName.toLowerCase();
        boolean isImage = lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")
                || lowerName.endsWith(".gif") || lowerName.endsWith(".webp");

        String contentType = isImage ? "image/" + (lowerName.endsWith(".png") ? "png"
                : (lowerName.endsWith(".gif") ? "gif" : "jpeg")) : "application/octet-stream";
        response.setContentType(contentType);

        String filenameSafe = URLEncoder.encode(fileName.replace(' ', '_'), java.nio.charset.StandardCharsets.UTF_8.toString());
        // Inline for images to allow preview, attachment otherwise
        String disposition = (isImage ? "inline" : "attachment") + "; filename=\"" + filenameSafe + "\"";
        response.setHeader("Content-Disposition", disposition);
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
                           @RequestParam(value = "date_begin", required = false) String dateBegin,
                           @RequestParam(value = "date_end", required = false) String dateEnd,
                           Model theModel,
                           HttpServletRequest request) {

        User authUser = getAuthUser(request);
        Task task = toTaskEntity(toTask);
        LocalDateTime dateBeginValue = parseDateStart(dateBegin);
        LocalDateTime dateEndValue = parseDateStart(dateEnd);
        User responsibleUser = userService.getUser(responsible_id);
        if (responsibleUser == null) {
            responsibleUser = authUser;
        }
        if (task.getId() == 0) {
            task.setCreatedBy(authUser);
            task.setResponsible(responsibleUser);
            task.setDateCreate(LocalDateTime.now());
            task.setDateBegin(dateBeginValue != null ? dateBeginValue : LocalDateTime.now());
            task.setDateEnd(dateEndValue != null ? dateEndValue : LocalDateTime.now());
        } else {
            Task oldTask = taskService.getTask(task.getId());
            if (oldTask == null) {
                return "redirect:/task/tasklist?user_id=" + user_id + "&page=" + page;
            }
            if (oldTask.getCreatedBy().getId() != authUser.getId()) {
                return "redirect:/task/showtask?user_id=" + user_id + "&page=" + page + "&task_id=" + task.getId();
            }
            task.setCreatedBy(oldTask.getCreatedBy() != null ? oldTask.getCreatedBy() : authUser);
            task.setResponsible(responsibleUser != null ? responsibleUser
                    : (oldTask.getResponsible() != null ? oldTask.getResponsible() : authUser));
            task.setDateCreate(oldTask.getDateCreate());
            task.setDateBegin(dateBeginValue != null ? dateBeginValue : oldTask.getDateBegin());
            task.setDateEnd(dateEndValue != null ? dateEndValue : oldTask.getDateEnd());
        }
        int task_id = taskService.addTask(task, authUser.getId());
        prepareModelShowTask(user_id, page, task_id, theModel, request);

        return "showtask";
    }


    @PostMapping("/savememo")
    public String handleFileUpload(@RequestParam String memo,
                                   @RequestParam(required = false) MultipartFile[] fileUpload,
                                   @RequestParam int user_id,
                                   @RequestParam int page,
                                   @RequestParam int task_id,
                                   Model theModel,
                                   HttpServletRequest request) {
        User authUser = getAuthUser(request);

        // Create Memo object
        Memo memoObj = new Memo();
        memoObj.setMemo(memo);
        memoObj.setDateCreate(LocalDateTime.now());
        memoObj.setCreatedBy(authUser);

        // Handle file upload
        if (fileUpload != null && fileUpload.length > 0) {
            MultipartFile aFile = fileUpload[0]; // Take first file
            memoObj.setFileName(aFile.getOriginalFilename());

            try {
                memoObj.setFiledata(aFile.getBytes());
                if (Objects.requireNonNull(aFile.getContentType()).contains("image")) {
                    memoObj.setPicture(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Get task and set relationship
        Task task = taskService.getTask(task_id);
        memoObj.setTask(task);

        // Save memo
        memoService.addMemo(memoObj);

        // Mark task as not viewed for other users
        taskService.setViewedTask(task, authUser.getId(), false);

        // Redirect back to task view
        return "redirect:/task/showtask?user_id=" + user_id + "&page=" + page + "&task_id=" + task_id;
    }


    private void prepareModelShowTask(int user_id,
                                      int page,
                                      int task_id,
                                      Model theModel,
                                      HttpServletRequest request) {
        ToUser authUser = new ToUser((User) request.getSession().getAttribute("user"));

        List<ToUser> userList = userService.getUsersWithCountTask().stream().map(ToUser::new).collect(Collectors.toList());
        if (task_id != 0) {
            Task task = taskService.getTaskWithMemo(task_id);
            ToTaskFull toTaskFull = toTaskFull(task);
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
        String dateFrom = (String) request.getSession().getAttribute("date_from");
        String dateTo = (String) request.getSession().getAttribute("date_to");
        String search = (String) request.getSession().getAttribute("search");
        List<ToUser> userList = userService.getUsersWithCountTask().stream().map(ToUser::new).collect(Collectors.toList());
        List<ToUser> userListFilter = userService.getUsers().stream().map(ToUser::new).collect(Collectors.toList());
        LocalDateTime dateFromValue = parseDateStart(dateFrom);
        LocalDateTime dateToValue = parseDateEnd(dateTo);

        List<ToTask> taskList;
        if (user_id == -1) {
            taskList = taskService.getMyTasks(authUser.getId(), filterUserID, search, dateFromValue, dateToValue, page)
                    .stream()
                    .map(this::toTask)
                    .collect(Collectors.toList());
        } else {
            taskList = taskService.getTasks(user_id, filterUserID, search, dateFromValue, dateToValue, page)
                    .stream()
                    .map(this::toTask)
                    .collect(Collectors.toList());
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
        theModel.addAttribute("search", search);
        theModel.addAttribute("date_from", dateFrom);
        theModel.addAttribute("date_to", dateTo);
        if (filterUserID != null)
            theModel.addAttribute("filter_user_name", (userService.getUser(filterUserID).getFullName()));
        else
            theModel.addAttribute("filter_user_name", "Фильтр");

        // Add CSRF token for Thymeleaf forms
        theModel.addAttribute("_csrf", request.getAttribute("_csrf"));


    }

    private String normalizeDateParam(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private LocalDateTime parseDateStart(String value) {
        if (value == null) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
            return date.atStartOfDay();
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private LocalDateTime parseDateEnd(String value) {
        if (value == null) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
            return date.atTime(LocalTime.MAX);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private ToTask toTask(Task task) {
        if (task == null) {
            return null;
        }
        ToTask toTask = new ToTask();
        toTask.setId(task.getId());
        toTask.setTitle(task.getTitle());
        toTask.setDateCreate(task.getDateCreate());
        toTask.setDateBegin(task.getDateBegin());
        toTask.setDateEnd(task.getDateEnd());
        toTask.setDateComplete(task.getDateComplete());
        toTask.setIsDone(task.getIsDone());
        toTask.setIsViewed(task.getIsViewed());
        if (task.getCreatedBy() != null) {
            toTask.setCreatedBy(new ToUser(task.getCreatedBy()));
        }
        if (task.getResponsible() != null) {
            toTask.setResponsible(new ToUser(task.getResponsible()));
        }
        return toTask;
    }

    private ToTaskFull toTaskFull(Task task) {
        if (task == null) {
            return null;
        }
        ToTaskFull toTaskFull = new ToTaskFull();
        toTaskFull.setId(task.getId());
        toTaskFull.setTitle(task.getTitle());
        toTaskFull.setMemo(task.getMemo());
        toTaskFull.setDateCreate(task.getDateCreate());
        toTaskFull.setDateBegin(task.getDateBegin());
        toTaskFull.setDateEnd(task.getDateEnd());
        toTaskFull.setDateComplete(task.getDateComplete());
        toTaskFull.setIsDone(task.getIsDone());
        toTaskFull.setIsViewed(task.getIsViewed());
        if (task.getMemoList() != null) {
            toTaskFull.setMemoList(task.getMemoList());
        } else {
            toTaskFull.setMemoList(java.util.Collections.emptyList());
        }
        if (task.getCreatedBy() != null) {
            toTaskFull.setCreatedBy(new ToUser(task.getCreatedBy()));
        }
        if (task.getResponsible() != null) {
            toTaskFull.setResponsible(new ToUser(task.getResponsible()));
        }
        return toTaskFull;
    }

    private Task toTaskEntity(ToTaskFull source) {
        Task task = new Task();
        task.setId(source.getId());
        task.setTitle(source.getTitle());
        task.setMemo(source.getMemo());
        task.setDateCreate(source.getDateCreate());
        task.setDateBegin(source.getDateBegin());
        task.setDateEnd(source.getDateEnd());
        task.setDateComplete(source.getDateComplete());
        task.setIsDone(Boolean.TRUE.equals(source.getIsDone()));
        task.setIsViewed(source.getIsViewed());
        if (source.getCreatedBy() != null) {
            task.setCreatedBy(source.getCreatedBy().getUser());
        }
        if (source.getResponsible() != null) {
            task.setResponsible(source.getResponsible().getUser());
        }
        return task;
    }

    private User getAuthUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }
}
