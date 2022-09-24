package com.testing4everyone.kafka.user.service.controller;

import com.testing4everyone.kafka.user.service.model.User;

public class UserSignUpForm {
    private String name;
    private String phone;

    public UserSignUpForm(String name, String phone) {
        super();
        this.name = name;
        this.phone = phone;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
