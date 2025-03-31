package com.examly.springapp.logging;


import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class loggingService {
    private static final String LOG_FOLDER = "logs";
    private static final String LOG_FILE = LOG_FOLDER + "/app.log";

    public loggingService() {
        ensureLogFileExists();
    }

    private void ensureLogFileExists() {
        File folder = new File(LOG_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File logFile = new File(LOG_FILE);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLog(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n");
            System.out.println("LOGGED: " + message); // Print in console too
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}