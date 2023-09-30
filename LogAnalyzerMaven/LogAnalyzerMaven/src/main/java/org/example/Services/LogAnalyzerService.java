package org.example.Services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Enums.LogType;
import org.example.Tos.LogRecord;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.FileInputStream;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class LogAnalyzerService {
    private static final Logger logger = Logger.getLogger(LogAnalyzerService.class.getName());
    public static List<LogRecord> readLogRecord(String filePath) throws Exception {
        List<LogRecord> logRecords = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // Skip the header row
                    continue;
                }

                String logType =  row.getCell(0).getStringCellValue();
                // Read the timestamp as a Date object
                Cell timestampCell = row.getCell(1);
                Date date = timestampCell.getDateCellValue();
                Timestamp timestamp = new Timestamp(date.getTime());

                String message = row.getCell(2).getStringCellValue();

                logRecords.add(new LogRecord(logType, timestamp, message));
            }
        }catch (Exception e) {
            throw new Exception("Error occurred while processing the sheet: " + e.toString(), e);
        }
        return logRecords;
    }
    

    public static LogRecord fetchMostRecentRecord(List<LogRecord> logRecords, String logType) {
        LogRecord mostRecentRecord = null;
         if (logRecords.isEmpty() || logRecords == null) {
                logger.log(Level.WARNING, "The logRecords list is either null or empty.");
            }
        try{
            for (LogRecord record : logRecords) {
            if (record.getLogType().equals(logType) &&
                    (mostRecentRecord == null || record.getTimestamp().compareTo(mostRecentRecord.getTimestamp()) > 0)) {
                mostRecentRecord = record;
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mostRecentRecord;
    }

    public static List<LogRecord> getMatchingErrorRecords(List<LogRecord> logRecords, String errorMessage) {
        List<LogRecord> matchingRecords = new ArrayList<>();
        if (logRecords.isEmpty() || logRecords == null) {
                logger.log(Level.WARNING, "The logRecords list is either null or empty.");
            }
        try{
            for (LogRecord record : logRecords) {
            if (record.getLogType().equalsIgnoreCase(LogType.ERROR.toString()) && record.getMessage().contains(errorMessage)) {
                matchingRecords.add(record);
            }
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
       return matchingRecords;
    }

    public static LogRecord findMostRecentErrorRecord(List<LogRecord> logRecords) {
        LogRecord mostRecentErrorRecord = null;
    if (logRecords.isEmpty() || logRecords == null) {
                logger.log(Level.WARNING, "The logRecords list is either null or empty.");
            }
    try{
        for (LogRecord record : logRecords) {
            if (LogType.ERROR.toString().equalsIgnoreCase(record.getLogType()) &&
                (mostRecentErrorRecord == null || record.getTimestamp().after(mostRecentErrorRecord.getTimestamp()))) {
                mostRecentErrorRecord = record;
            }
        }
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return mostRecentErrorRecord;
    }
}
