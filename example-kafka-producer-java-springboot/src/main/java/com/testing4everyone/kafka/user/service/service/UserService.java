package com.testing4everyone.kafka.user.service.service;

import com.testing4everyone.kafka.user.service.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User saveUser(User User);
    public List<User> getAllUsers();
    public Optional<User> getUserById(String UserId);
    public Optional<User> getUserByName(String UserId);
    public User updateUserInfoById(String UserId, User UserName);
    public void deleteUserId(String UserId);
}
