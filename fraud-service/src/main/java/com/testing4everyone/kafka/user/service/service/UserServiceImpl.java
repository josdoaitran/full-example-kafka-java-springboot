package com.testing4everyone.kafka.user.service.service;

import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User User) {
         return userRepository.save(User);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(String UserId){
        return userRepository.findById(Integer.valueOf(UserId));
    }

    @Override
    public Optional<User>getUserByName(String UserName){
        return userRepository.findByName(UserName);
    }

    @Override
    public User getUserByPhone(String Phone){
        return userRepository.findByPhone(Phone);
    }

    @Override
    public User updateUserInfoById(String UserId, User user){
        User UserUpdate =  userRepository.getOne(Integer.valueOf(UserId));
        String UserNameStr = user.getName();
        String UserAddressStr = user.getStatus();
        UserUpdate.setName(UserNameStr);
        UserUpdate.setStatus(UserAddressStr);
        return userRepository.save(UserUpdate);
    }

    @Override
    public void updateUserStatus(String UserId, String status){
        User UserUpdate =  userRepository.findUserById(Integer.valueOf(UserId));
        UserUpdate.setStatus(status);
        userRepository.save(UserUpdate);
        System.out.println("Done");
    }

    @Override
    public void deleteUserId(String UserId){
        userRepository.deleteById(Integer.valueOf(UserId));
    }
}
