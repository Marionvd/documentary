package com.mariodoumbanov.documentqa.repository;

import com.mariodoumbanov.documentqa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByEmail(String email);

    boolean existsUserByEmail(String email);

    boolean existsUserById(int id);
}
