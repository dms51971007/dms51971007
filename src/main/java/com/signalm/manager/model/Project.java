//package com.signalm.manager.model;
//
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "projects")
//public class Project {
//    @Id
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @Column(name = "ID")
//    private int id;
//    @Column(name = "Title")
//    private String title;
//
//    @Column(name = "DateCreate", columnDefinition="DATETIME")
//    private LocalDateTime dateCreate;
//
//    @OneToMany(targetEntity=Task.class,fetch = FetchType.LAZY, mappedBy = "pro")
//    @ElementCollection(targetClass=Task.class)
//    @OrderBy("dateCreate ASC")
//    private List<Memo> memoList;
//
//
//
//}
