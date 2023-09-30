package org.example.Tos;

import org.example.Enums.LogType;

import java.sql.Timestamp;

public class LogRecord {

    private String logType;
    private Timestamp timestamp; 
    private String message;

    public LogRecord(String logType, Timestamp timestamp, String message) {
        this.logType = logType;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getLogType() {
        return logType;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LogRecord{" +
                "logType='" + logType + '\'' +
                ", timestamp=" + timestamp + // Change the timestamp representation
                ", message='" + message + '\'' +
                '}';
    }
}
