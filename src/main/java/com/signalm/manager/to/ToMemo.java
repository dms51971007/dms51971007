package com.signalm.manager.to;

import com.signalm.manager.model.User;

import java.time.LocalDateTime;

public class ToMemo {
    private int id;

    private String memo;

    private User createdBy;

    private ToTask task;

    private LocalDateTime dateCreate;

    private String fileName;
    private byte[] filedata;
    private boolean isPicture;


    public ToMemo() {
    }

    public boolean isPicture() {
        return isPicture;
    }

    public void setPicture(boolean picture) {
        isPicture = picture;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFiledata() {
        return filedata;
    }

    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
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

    public ToTask getTask() {
        return task;
    }

    public void setTask(ToTask task) {
        this.task = task;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }
}
