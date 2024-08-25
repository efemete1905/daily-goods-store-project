package com.example.odev.proje.service;

import com.example.odev.proje.entity.suveicecekler;

import java.util.List;

public interface suveiceceklerService {
    List<suveicecekler> findAll();

    void deleteById(int theId);
}
