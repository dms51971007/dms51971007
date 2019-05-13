package com.signalm.manager.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "memolist")
public class Memo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "Kod")
    private int id;

    @Column(name = "Memo")
    private String memo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Person")
    private User createdBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KodTask")
    private Task task;

    @Column(name = "Date", columnDefinition="DATETIME")
    private LocalDateTime dateCreate;


    @Column(name = "filedata")
    private byte[] filedata;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "is_picture")
    private boolean isPicture;


    public Memo() {
    }

    public boolean isPicture() {
        return isPicture;
    }

    public void setPicture(boolean picture) {
        isPicture = picture;
    }

    public byte[] getFiledata() {
        return filedata;
    }
    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }
}
