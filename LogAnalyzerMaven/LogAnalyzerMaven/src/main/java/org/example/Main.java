package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.Services.LogAnalyzerService;
import org.example.Tos.LogRecord;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws IOException {
        logger.log(Level.INFO, "Welcome to LogAnalyzer!");
        try {
            logger.log(Level.INFO, "Please place your file to Input folder!");
            logger.info("Please provide file Name with extenions");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String fileName=br.readLine();

            String[] file = fileName.split("\\.");
            if(file[1].equals("xlsx")){
                List<LogRecord> list = LogAnalyzerService.readLogRecord(System.getProperty("user.dir")+"/Input/"+ file[0]+".xlsx");
                logger.log(Level.INFO, "Data retrieved successfully");

                logger.log(Level.INFO, "Please enter your Choice !");
                getMenu(list);
            }else{
                throw new Exception("File extension not matching! ");
            }
            logger.log(Level.INFO, "Thank You for using LogAnalyzer!!!");
        }catch (Exception e){
            System.out.println(e.toString());
        }
        
    }
    public static void getMenu(List<LogRecord> list){
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Menu:");
            System.out.println("1. Most Recent Record ----");
            System.out.println("2. Last error occurred ----");
            System.out.println("3. Records matches for a given user input ----");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    Scanner scn=new Scanner(System.in);
                    System.out.print("Please enter logType! ");
                    String logType=scn.nextLine();
                    LogRecord mostRecent=LogAnalyzerService.fetchMostRecentRecord(list, logType);
                    System.out.println("Most Recent Record is: "+" Type: "+mostRecent.getLogType()+" TimeStamp: "+mostRecent.getTimestamp()+" Message: "+mostRecent.getMessage());
                    break;
                case 2:
                    LogRecord mostRecentErrorRecord=LogAnalyzerService.findMostRecentErrorRecord(list);
                     System.out.println("Most Recent Error Record is: "+" TimeStamp: "+mostRecentErrorRecord.getTimestamp()+" Error Message: "+mostRecentErrorRecord.getMessage());
                    break;
                case 3:
                    Scanner scn2=new Scanner(System.in);
                    System.out.print("Please enter errorMessage! ");
                    String message=scn2.nextLine();
                    List<LogRecord> recordList=LogAnalyzerService.getMatchingErrorRecords(list, message);
                    System.out.println("Print the Records");
                    for(LogRecord record:recordList){
                        System.out.println(" Type: "+record.getLogType()+" TimeStamp: "+record.getTimestamp()+" Error Message: "+record.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Exiting the program");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        } while (choice != 4);

        scanner.close();
    }
}