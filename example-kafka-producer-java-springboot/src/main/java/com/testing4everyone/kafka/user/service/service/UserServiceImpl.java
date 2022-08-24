package com.testing4everyone.kafka.user.service.service;

import com.testing4everyone.kafka.user.service.model.User;
import com.testing4everyone.kafka.user.service.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
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

//    @Override
//    public User updateUserInfoById(String UserId, User UserName){
//        User UserUpdate =  UserRepository.getOne(Integer.valueOf(UserId));
//        String UserNameStr = UserName.getName();
//        String UserAddressStr = UserName.getAddress();
//        UserUpdate.setName(UserNameStr);
//        UserUpdate.setAddress(UserAddressStr);
//        return UserRepository.save(UserUpdate);
//    }

//    @Override
//    public void deleteUserId(String UserId){
//        UserRepository.deleteById(Integer.valueOf(UserId));
//    }
}
