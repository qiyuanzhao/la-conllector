package com.lavector.collector.entity.user;

import com.lavector.collector.entity.BaseAuditableSqlEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;

/**
 * @author tao
 */
@Entity
@Table(indexes = {@Index(name = "email", columnList = "email")})
public class User extends BaseAuditableSqlEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String username;

    private String avatar;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String avatarData;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(length = 200, nullable = false)
    private String email;

    @Column(name = "job_title")
    private String jobTitle;

    private String role;

    protected User() {
    }

    public String getUsername() {
        if (username == null) {
            username = email;
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean hasRole(String role) {
        boolean hasRole = false;
        if (this.role != null && this.role.equals(role)) {
            hasRole = true;
        }
        return hasRole;
    }


    public String getAvatarData() {
        return avatarData;
    }

    public void setAvatarData(String avatarData) {
        this.avatarData = avatarData;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", email=" + email + "]";
    }

}
