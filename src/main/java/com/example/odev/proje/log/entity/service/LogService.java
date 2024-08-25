package com.example.odev.proje.log.entity.service;

import com.example.odev.proje.log.entity.Log;
import com.example.odev.proje.log.entity.dto.LogDto;

public interface LogService {
    LogDto save(Log theLog, String username);
}
