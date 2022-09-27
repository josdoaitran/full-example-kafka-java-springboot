package com.testing4everyone.kafka.user.service.repository;

import com.testing4everyone.kafka.user.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByPhone(String phone);
    Optional<User> findByName(String studentName);

    User findUserById(int id);
}
