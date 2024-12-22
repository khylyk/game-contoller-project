package nl.saxion.ptbc.pilotfx.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Database class provides methods to interact with a database.
 * It supports adding waypoints, collision points, and command logs to the database.
 */
public class Database {

    private PreparedStatement pstmt = null;
    private Connection conn = null;
    private final String url;

    private final boolean isGroundControlDatabase;

    public Database(String url, boolean isGroundControlDatabase) {
        this.url = url;
        this.isGroundControlDatabase = isGroundControlDatabase;
        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (isGroundControlDatabase) {
            createTable("create table if not exists commandLog(time text not null,command text not null);");
            createTable("create table if not exists collisionPoints (xcoordinate real not null, zcoordinate real not null)");
        } else {
            createTable("create table if not exists wayPoints (xcoordinate real not null, zcoordinate real not null)");
        }
    }

    /**
     * Adds waypoints to the database.
     *
     * @param x the x-coordinate of the waypoint
     * @param z the z-coordinate of the waypoint
     */
    public void addWayPoints(double x, double z) {
        String insertWayPoints = "INSERT INTO wayPoints(xcoordinate, zcoordinate) VALUES(?,?)";
        insertPoints(insertWayPoints, x, z);
    }

    /**
     * Adds collision points to the database.
     *
     * @param x the x-coordinate of the collision point
     * @param z the z-coordinate of the collision point
     */
    public void addCollisionPoints(double x, double z) {
        String insertCollisionPoints = "INSERT INTO collisionPoints(xcoordinate, zcoordinate) VALUES(?,?)";
        insertPoints(insertCollisionPoints, x, z);
    }

    /**
     * Adds a command log to the database.
     *
     * @param time    the time the command was issued
     * @param command the command issued
     */
    public void addCommandLog(LocalTime time, String command) {
        String insertCommandLog = "INSERT INTO commandLog(time, command) VALUES(?,?)";
        insertCommand(insertCommandLog, String.valueOf(time), command);
    }

    /**
     * Inserts points into the database using the specified SQL statement.
     *
     * @param sql the SQL statement to execute
     * @param x   the x-coordinate to insert
     * @param z   the z-coordinate to insert
     */
    private void insertPoints(String sql, double x, double z) {
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, x);
            pstmt.setDouble(2, z);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't find Database. File path is not specified correctly. Need to specify it in Logger class.");
        }
    }

    /**
     * Inserts a command log into the database using the specified SQL statement.
     *
     * @param sql     the SQL statement to execute
     * @param time    the time the command was issued
     * @param command the command issued
     */
    private void insertCommand(String sql, String time, String command) {
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, time);
            pstmt.setString(2, command);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't find Database. File path is not specified correctly. Need to specify it in Logger class.");
        }
    }

    private void createTable(String sql) {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Can't find Database. File path is not specified correctly. Need to specify it in Logger class.");
        }
    }

    /**
     * Exports saved radar points from collisionPoints table in Ground Control database
     * @throws IllegalAccessException when user tries to call this method on Pilot database
     * @return list of Strings of the following format: x-coordinate z-coordinate
     */
    public List<String> exportRadarPoints() throws IllegalAccessException {
        if (!isGroundControlDatabase) {
            throw new IllegalAccessException("This database is not a Ground Control database.");
        }

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM collisionPoints");
            List<String> radarPoints = new ArrayList<>();

            while (resultSet.next()) {
                radarPoints.add(resultSet.getString("xcoordinate") + " " + resultSet.getInt("zcoordinate"));
            }

            return radarPoints;
        } catch (SQLException e) {
            System.err.println("Can't find Database. File path is not specified correctly. Need to specify it in Logger class.");
        }

        throw new IllegalStateException("Database does not contain any points.");
    }

    public void exportToCSV(String tableName, String csvFilePath) {
        String sql = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             FileWriter csvWriter = new FileWriter(csvFilePath)) {
            int columnCount = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                try {
                    csvWriter.append(rs.getMetaData().getColumnName(i));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (i < columnCount) csvWriter.append(",");
            }
            csvWriter.append("\n");

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    csvWriter.append(rs.getString(i));
                    if (i < columnCount) csvWriter.append(",");
                }
                csvWriter.append("\n");
            }

        } catch (SQLException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
