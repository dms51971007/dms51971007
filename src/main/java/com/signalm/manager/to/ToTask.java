package com.signalm.manager.to;

import com.signalm.manager.model.Task;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class ToTask {
    private int id;
    private String title;
    @DateTimeFormat(pattern = "dd.MM.yyyy H:mm")
    private LocalDateTime dateCreate;

    @DateTimeFormat(pattern = "dd.MM.yyyy H:mm")
    private LocalDateTime dateBegin;
    @DateTimeFormat(pattern = "dd.MM.yyyy H:mm")
    private LocalDateTime dateEnd;
    @DateTimeFormat(pattern = "dd.MM.yyyy H:mm")
    private LocalDateTime dateComplete;

    private ToUser createdBy;
    private ToUser responsible;
    private Boolean isDone;
    private Boolean isViewed;

    public ToTask() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public ToUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ToUser createdBy) {
        this.createdBy = createdBy;
    }

    public ToUser getResponsible() {
        return responsible;
    }

    public void setResponsible(ToUser responsible) {
        this.responsible = responsible;
    }

    public LocalDateTime getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(LocalDateTime dateBegin) {
        this.dateBegin = dateBegin;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LocalDateTime getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(LocalDateTime dateComplete) {
        this.dateComplete = dateComplete;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public boolean getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(boolean viewed) {
        isViewed = viewed;
    }
}
