package com.example.odev.proje.dao;

import com.example.odev.proje.entity.users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface usersRepository extends JpaRepository<users,Integer> {
    Optional<users> findByUsername(String username);
    Optional<users> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}
