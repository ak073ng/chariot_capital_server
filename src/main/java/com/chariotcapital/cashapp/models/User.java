package com.chariotcapital.cashapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonProperty("id")
    @Column(length = 11)
    private Integer id;

    @JsonProperty("userToken")
    @Column(length = 8)
    private String userToken;

    @JsonProperty("fullName")
    @Column(length = 25)
    private String fullName;

    @JsonProperty("userName")
    @Column(length = 15)
    private String userName;

    @JsonProperty("email")
    @Column(length = 25)
    private String email;

    @JsonProperty("password")
    @Column(length = 75)
    private String password;

    @JsonProperty("newPassword")
    @Transient
    private String newPassword;

    @JsonProperty("sessionStatus")
    @Column(length = 25)
    private String sessionStatus;

    @CreationTimestamp
    @JsonProperty("createdDateTime")
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @JsonProperty("updatedDateTime")
    private LocalDateTime updatedDateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String SessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
}
