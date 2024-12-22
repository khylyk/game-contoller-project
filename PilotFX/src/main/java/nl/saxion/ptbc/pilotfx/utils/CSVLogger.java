package nl.saxion.ptbc.pilotfx.utils;

import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * The CSVLogger class provides functionality to log data to a CSV file.
 * It supports inserting XZ coordinates and command logs with timestamps.
 */
public class CSVLogger {
    private String filePath = "";

    /**
     * Constructs a CSVLogger with the specified file path.
     * Clears the CSV file if it exists.
     *
     * @param filePath the path to the CSV file
     */
    public CSVLogger(String filePath) {
        this.filePath = filePath;
        clearCSV();
    }

    /**
     * Clears the CSV file by creating a new empty file at the specified path.
     */
    private void clearCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(new File(filePath), false))) {
        } catch (IOException e) {
            System.err.println("Can't find CSV file. File path is not specified correctly. Need to specify it in Logger class.");
        }
    }

    /**
     * Checks if the CSV file contains the specified header.
     *
     * @param header the header to check for
     * @return true if the header is found, false otherwise
     */
    private boolean checkForHeader(String[] header) {
        try (com.opencsv.CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (Arrays.equals(nextLine, header)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Can't find CSV file. File path is not specified correctly. Need to specify it in Logger class.");
        }
        return false;
    }

    /**
     * Inserts XZ coordinates into the CSV file.
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     */
    public void insertXZPoints(double x, double z) {
        String[] header = {"X", "Z"};
        String[] xzCoordinate = {String.valueOf(x), String.valueOf(z)};
        addToCSV(header,xzCoordinate);
    }

    /**
     * Inserts a command log with a timestamp into the CSV file.
     *
     * @param time the time of the command
     * @param command the command to log
     */
    public void insertCommandLog(LocalTime time, String command) {
        String trueTime = String.valueOf(time);
        String[] header = {"Time", "Command"};
        String[] commandLog = {trueTime, command};
        addToCSV(header,commandLog);
    }

    /**
     * Adds data to the CSV file, ensuring the header is written if it does not already exist.
     *
     * @param header the header to ensure in the CSV file
     * @param data the data to add to the CSV file
     */
    private void addToCSV(String[] header, String[] data){
        try (CSVWriter writer = new CSVWriter(new FileWriter(new File(filePath), true))) {
            if (!checkForHeader(header)) {
                writer.writeNext(header);
                writer.writeNext(data);
            } else writer.writeNext(data);
        } catch (IOException e) {
            System.err.println("Can't find CSV file. File path is not specified correctly. Need to specify it in Logger class.");
        }
    }
}
