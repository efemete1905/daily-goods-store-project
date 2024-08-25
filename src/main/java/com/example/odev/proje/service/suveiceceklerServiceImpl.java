package com.example.odev.proje.service;

import com.example.odev.proje.dao.suveiceceklerRepository;
import com.example.odev.proje.entity.suveicecekler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class suveiceceklerServiceImpl implements suveiceceklerService{

    private suveiceceklerRepository suveiceceklerRepository;

    @Autowired
    public suveiceceklerServiceImpl(suveiceceklerRepository thesuveiceceklerRepository) {
        this.suveiceceklerRepository = thesuveiceceklerRepository;
    }

    @Override
    public List<suveicecekler> findAll() {
        return suveiceceklerRepository.findAll();
    }

    @Override
    public void deleteById(int theId) {
 suveiceceklerRepository.deleteById(theId);
    }
}
