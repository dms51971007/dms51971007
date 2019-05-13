package com.signalm.manager.config;

import com.signalm.manager.model.Task;
import com.signalm.manager.model.User;
import com.signalm.manager.serv.MemoService;
import com.signalm.manager.serv.TaskService;
import com.signalm.manager.serv.UserService;
import com.signalm.manager.to.ToMemo;
import com.signalm.manager.to.ToTask;
import com.signalm.manager.to.ToTaskFull;
import com.signalm.manager.util.MapperManager;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "/persistence-mysql_test.properties")
@ContextConfiguration(classes = {DemoSecurityConfig.class, DemoAppConfig.class})
@WebAppConfiguration

public class DemoAppConfigTest extends TestCase {
    private MockMvc mockMvc;

    private MapperManager mapperManager = new MapperManager();

    @Autowired
    protected UserService userService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected MemoService memoService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
    @Test
    public void testAuth() throws Exception {
        User user = userService.getUser(90);
        mockMvc.perform(formLogin("/authenticateTheUser").user("username", user.getUserName()).password("password", user.getPassword()))
                .andExpect(authenticated().withRoles("USER", "ADMIN"));
        mockMvc.perform(get("/user/list").with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()))))
                .andExpect(status().is2xxSuccessful());

        user = userService.getUser(107);
        mockMvc.perform(get("/user/list").with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()))))
                .andExpect(status().is4xxClientError());


    }

    @Test
    public void testTaskList() throws Exception {
        User user = userService.getUser(107);

        Map<String, Object> sessionattr = new HashMap<>();
        sessionattr.put("user", user);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/task/tasklist?user_id=-1&page=1")
                        .sessionAttrs(sessionattr)
                        .with(SecurityMockMvcRequestPostProcessors.
                                authentication(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()))))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status()
                        .is2xxSuccessful())
                .andReturn();
        Assert.assertEquals(((List) result.getModelAndView().getModel().get("taskList")).size(), 30);

    }

    @Test
    public void testTaskViewed() throws Exception {
        User user1 = userService.getUser(90);
        User user2 = userService.getUser(107);
        Task task = new Task();
        task.setTitle("Проверка!!");
        task.setMemo("Проверка!!");
        task.setCreatedBy(user1);
        task.setResponsible(user2);
        int id = taskService.addTask(task, user1.getId());
        Task taskFromBase;
        taskFromBase = taskService.getTask(id);
        Assert.assertEquals(task.toString(), taskFromBase.toString());
        Assert.assertFalse(taskFromBase.getIsViewed());

        Map<String, Object> sessionattr = new HashMap<>();
        sessionattr.put("user", user1);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/task/showtask?user_id=" + user1.getId() + "&page=1" + "&task_id=" + id)
                        .sessionAttrs(sessionattr)
                        .with(SecurityMockMvcRequestPostProcessors.
                                authentication(new UsernamePasswordAuthenticationToken(user1.getUserName(), user1.getPassword()))))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status()
                        .is2xxSuccessful())
                .andReturn();
        taskFromBase = taskService.getTask(id);
        Assert.assertFalse(taskFromBase.getIsViewed());

        sessionattr.put("user", user2);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/task/showtask?user_id=" + user2.getId() + "&page=1" + "&task_id=" + id)
                .sessionAttrs(sessionattr)
                .with(SecurityMockMvcRequestPostProcessors.
                        authentication(new UsernamePasswordAuthenticationToken(user2.getUserName(), user2.getPassword()))))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status()
                        .is2xxSuccessful())
                .andReturn();
        taskFromBase = taskService.getTask(id);
        Assert.assertTrue(taskFromBase.getIsViewed());

    }

    @Test
    public void testAddMemoWithFile() throws Exception{
        User user1 = userService.getUser(90);
        User user2 = userService.getUser(107);
        Task task = new Task();
        task.setTitle("Проверка MEMO!!");
        task.setMemo("Проверка MEMO!!");
        task.setCreatedBy(user1);
        task.setResponsible(user2);
        int id = taskService.addTask(task, user1.getId());

        ToTask toTask = mapperManager.map(taskService.getTask(id), ToTask.class);

        ToMemo toMemo = new ToMemo();
        toMemo.setTask(toTask);
        toMemo.setMemo("Memo with file");


        File f =new File(getClass().getClassLoader().getResource("testUpload.jpg").getFile());

        System.out.println(f.isFile()+"  "+f.getName()+f.exists());
        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile fstmp = new MockMultipartFile("fileUpload", f.getName(), "multipart/form-data",fi1);


        Map<String, Object> sessionattr = new HashMap<>();
        sessionattr.put("user", user1);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/task/savememo")
                .file(fstmp)
                .param("user_id","90")
                .param("page","1")
                .param("task_id", String.valueOf(task.getId()))
                .flashAttr("memo_to", toMemo)
                .sessionAttrs(sessionattr))
                .andExpect(status().isOk());

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/task/showtask?user_id=" + user1.getId() + "&page=1" + "&task_id=" + id)
                        .sessionAttrs(sessionattr)
                        .with(SecurityMockMvcRequestPostProcessors.
                                authentication(new UsernamePasswordAuthenticationToken(user1.getUserName(), user1.getPassword()))))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status()
                        .is2xxSuccessful())
                .andReturn();

        toTask = (ToTaskFull) result.getModelAndView().getModel().get("task_to");
        Assert.assertEquals(((ToTaskFull) toTask).getMemoList().get(0).getFiledata().length,42240);

    }


}