package com.signalm.manager.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasklist")
public class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "Kod")
    private int id;
    @Column(name = "Title")
    private String title;

    @Column(name = "Memo")
    private String memo;

    @Column(name = "DateCreate", columnDefinition="DATETIME")
    private LocalDateTime dateCreate;
    @Column(name = "DateBegin", columnDefinition="DATETIME")
    private LocalDateTime dateBegin;
    @Column(name = "DateEnd", columnDefinition="DATETIME")
    private LocalDateTime dateEnd;
    @Column(name = "DateComplite", columnDefinition="DATETIME")
    private LocalDateTime dateComplete;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Owner")
    private User createdBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KodPerson")
    private User responsible;

    @OneToMany(targetEntity=Memo.class,fetch = FetchType.LAZY, mappedBy = "task")
    @ElementCollection(targetClass=Memo.class)
    @OrderBy("dateCreate ASC")
    private List<Memo> memoList;

    @Column(name = "isDone")
    private boolean isDone;

    @Column(name = "isViewed")
    private boolean isViewed;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "project_id")
//    private Task project;

    public Task() {
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
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

    public List<Memo> getMemoList() {
        return memoList;
    }

    public void setMemoList(List<Memo> memoList) {
        this.memoList = memoList;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean done) {
        isDone = done;
    }

    public boolean getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(boolean viewed) {
        isViewed = viewed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
//                ", createdBy=" + createdBy +
                '}';
    }
}
