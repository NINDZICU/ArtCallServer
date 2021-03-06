package ru.kpfu.itis.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Anatoly on 08.07.2017.
 */
@Entity
@Table
public class MyTasks implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO
    )
    @NotNull
    @Column(
            name = "id", unique = true
    )
    private Integer id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String dateFinish;
    @Column
    private String difficulty;
    @Column
    private String latitude;
    @Column
    private String longitude;
    @Column
    private Integer taskId;
    @Column
    private String address;


    @ManyToOne(

            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinColumn(
            name = "idCustomer"
    )
    private User customer;
    @Column
    private String state;

    @ManyToOne(
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}
    )
    @JoinColumn(
            name = "login"
    )
    private User user;

    public MyTasks(){}



    public MyTasks(String name, String description, String dateFinish, String difficulty, User customer, String state, User user, String latitude, String longitude, Integer taskId, String address) {
        this.name = name;
        this.description = description;
        this.dateFinish = dateFinish;
        this.difficulty = difficulty;
        this.customer = customer;
        this.state = state;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taskId = taskId;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
