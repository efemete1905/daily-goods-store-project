package com.example.odev.proje.log.entity.repository;

import com.example.odev.proje.entity.Category;
import com.example.odev.proje.log.entity.Log;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface LogRepository extends JpaRepository<Log,Integer> {
}
