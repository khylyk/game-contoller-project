package nl.saxion.ptbc.pilotfx.utils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Logger class is responsible for logging waypoints, collision points, and command logs.
 * It provides methods to add log entries and upload them to a database and CSV files.
 */
public class Logger {

    /**
     * A record to represent XZ coordinates.
     */
    private record XZCoordinate(double x, double z) {
    }

    /**
     * A record to represent a command log entry with time and command.
     */
    private record CommandLog(LocalTime time, String command) {
    }

    private static ArrayList<XZCoordinate> wayPoints = new ArrayList<>();
    private static ArrayList<XZCoordinate> collisionPoints = new ArrayList<>();
    private static ArrayList<CommandLog> commandLogs = new ArrayList<>();

    private static final Database groundControlDatabase = new Database("jdbc:sqlite:" + System.getProperty("user.dir") + "/shared/logs/groundControlDB.sqlite", true);
    private static final Database pilotDatabase = new Database("jdbc:sqlite:" + System.getProperty("user.dir") + "/shared/logs/pilotDB.sqlite", false);
    private static final CSVLogger commandCSV = new CSVLogger("shared/logs/MissionLog.csv");
    private static final CSVLogger wayPointsCSV = new CSVLogger("shared/logs/FrogCoordinates.csv");
    private static final CSVLogger collisionPointCSV = new CSVLogger("shared/logs/RadarPoints.csv");

    private static boolean isUploading = false;

    /**
     * Adds collision points to the log.
     *
     * @param x the x-coordinate of the collision point
     * @param z the z-coordinate of the collision point
     */
    public static void addCollisionPoints(double x, double z) {
        if (!isUploading) {
            collisionPoints.add(new XZCoordinate(x, z));
        }
    }

    /**
     * Adds waypoints to the log.
     *
     * @param x the x-coordinate of the waypoint
     * @param z the z-coordinate of the waypoint
     */
    public static void addWayPoints(double x, double z) {
        if (!isUploading) {
            wayPoints.add(new XZCoordinate(x, z));
        }
    }

    /**
     * Adds a command log entry to the log.
     *
     * @param time    the time the command was issued
     * @param command the command issued
     */
    public static void addCommandLog(LocalTime time, String command) {
        if (!isUploading) {
            commandLogs.add(new CommandLog(time, command));
        }
    }

    /**
     * Uploads waypoints to the database and the corresponding CSV file.
     */
    public static void uploadWayPointsToDatabase() {
        isUploading = true;
        for (XZCoordinate coordinate : wayPoints) {
            pilotDatabase.addWayPoints(coordinate.x, coordinate.z);
            wayPointsCSV.insertXZPoints(coordinate.x, coordinate.z);
        }
        isUploading = false;
    }

    /**
     * Uploads collision points to the database and the corresponding CSV file.
     */
    public static void uploadCollisionPointsToDatabase() {
        isUploading = true;
        for (XZCoordinate coordinate : collisionPoints) {
            groundControlDatabase.addCollisionPoints(coordinate.x, coordinate.z);
            collisionPointCSV.insertXZPoints(coordinate.x, coordinate.z);
        }
        isUploading = false;
    }

    /**
     * Uploads command logs to the database and the corresponding CSV file.
     */
    public static void uploadCommandLogToDatabase() {
        isUploading = true;
        for (CommandLog log : commandLogs) {
            groundControlDatabase.addCommandLog(log.time, log.command);
            commandCSV.insertCommandLog(log.time, log.command);
        }
        isUploading = false;
    }

    /**
     * @return list of strings of the following format: x-coordinate z-coordinate
     */

    public static List<String> exportRadarPointsFromDB() {
        try {
            return groundControlDatabase.exportRadarPoints();
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        return List.of();
    }
}
