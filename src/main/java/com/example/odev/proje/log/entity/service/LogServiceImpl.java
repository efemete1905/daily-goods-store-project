package com.example.odev.proje.log.entity.service;

import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.dto.LogDto;
import com.example.odev.proje.log.entity.mapper.LogMapper;
import com.example.odev.proje.log.entity.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogServiceImpl implements LogService{

    @Autowired
    private LogRepository logRepository;
    @Override
    public LogDto save(Log theLog,String username){
        Log savedLog = logRepository.save(theLog);

        log.info("Log saved with id: {} by user: {}", savedLog.getId(), username);
        log.info(theLog.getMessage());

        return LogMapper.INSTANCE.logToLogDTO(savedLog);

    }
}
