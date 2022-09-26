package com.testing4everyone.kafka.fraud.service.model;

import javax.persistence.*;

@Entity
public class UserFraudInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique=true, length = 10, nullable=false)
    private String phone;
    private String creditInfo;

    private String updatedAt;

    public UserFraudInfo() {
    }

    public UserFraudInfo(String name, String phone, String creditInfo, String updatedAt) {
        super();
        this.phone = phone;
        this.creditInfo = creditInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreditInfo() {
        return creditInfo;
    }

    public void setCreditInfo(String creditInfo) {
        this.creditInfo = creditInfo;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String UpdateAt) {
        this.updatedAt = updatedAt;
    }
}
