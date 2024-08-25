package com.example.odev.proje.log.entity.dto;


import java.time.LocalDateTime;

public class LogDto {
    private Long id;
    private LocalDateTime timestamp;
    private String logLevel;
    private String message;

    // Getter ve Setter metodları   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
