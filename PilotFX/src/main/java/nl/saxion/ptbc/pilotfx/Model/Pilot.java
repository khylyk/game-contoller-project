package nl.saxion.ptbc.pilotfx.Model;

import nl.saxion.ptbc.SaSaCommunicator;
import nl.saxion.ptbc.pilotfx.Controller.RadarPanelController;
import nl.saxion.ptbc.pilotfx.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The Pilot class manages communication with the SaSaCommunicator and controls the movement of the frog.
 * It includes methods for driving the frog, handling status and radar updates, and detecting if the frog is stuck.
 */
public class Pilot {
    private static Pilot instance;

    private final SaSaCommunicator saSaCommunicator;
    private double currentX;
    private double currentZ;
    private double targetX;
    private double targetZ;
    private double currentAngle;
    private volatile boolean running = false;
    private volatile boolean obstacleDetected = false;

    private final List<StatusListener> statusListeners = new ArrayList<>();
    private final List<FrogLocation> frogLocations = new ArrayList<>();
    private final List<RadarListener> radarListeners = new ArrayList<>();
    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the SaSaCommunicator and starts the obstacle detection thread.
     */
    private Pilot() {
        saSaCommunicator = new SaSaCommunicator("PILOT");
        saSaCommunicator.send("STATUS ON");
        saSaCommunicator.send("RADAR ON");
        sleep(5000);
        startObstacleDetection();
    }

    /**
     * Returns the singleton instance of the Pilot class.
     *
     * @return the singleton instance
     */
    public static Pilot getInstance() {
        if (instance == null) {
            instance = new Pilot();
        }

        return instance;
    }

    /**
     * Checks if the frog is currently running.
     *
     * @return true if the frog is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets the running state of the frog.
     *
     * @param running true to set the frog running, false to stop
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Interface for status update listeners.
     */
    public interface StatusListener {
        void onStatusUpdate(String status);
    }

    /**
     * Interface for radar update listeners.
     */
    public interface RadarListener {
        void onRadarUpdate(double x, double y);
    }

    /**
     * Interface for frog location update listeners.
     */
    public interface FrogLocation {
        void onFrogUpdate(double x, double y, double frogAngle);
    }

    /**
     * Adds a status listener.
     *
     * @param listener the status listener to add
     */
    public void addStatusListener(StatusListener listener) {
        statusListeners.add(listener);
    }

    /**
     * Adds a radar listener.
     *
     * @param listener the radar listener to add
     */
    public void addRadarListener(RadarListener listener) {
        radarListeners.add(listener);
    }

    /**
     * Adds a frog location listener.
     *
     * @param locator the frog location listener to add
     */
    public void addFrogLocation(FrogLocation locator) {
        frogLocations.add(locator);
    }

    /**
     * Notifies all status listeners with the given status message.
     *
     * @param status the status message to notify
     */
    private void notifyStatusListeners(String status) {
        for (StatusListener listener : statusListeners) {
            listener.onStatusUpdate(status);
        }
    }

    /**
     * Notifies all radar listeners with the given coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    private void notifyRadarListeners(double x, double y) {
        for (RadarListener listener : radarListeners) {
            listener.onRadarUpdate(x, y);
        }
    }

    /**
     * Notifies all frog location listeners with the given coordinates and angle.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param frogAngle the angle of the frog
     */
    private void notifyFrogLocation(double x, double y, double frogAngle) {
        for (FrogLocation locator : frogLocations) {
            locator.onFrogUpdate(x, y, frogAngle);
        }
    }

    /**
     * Drives the frog to the specified coordinates.
     *
     * @param x the x-coordinate to drive to
     * @param z the z-coordinate to drive to
     */
    public void driveFrogToCoordinate(double x, double z) {
        targetX = x;
        targetZ = z;

        double tolerance = 3;
        double maxSteeringAngle = 30;
        double motorPower = 1;

        running = true;

        while (distance(currentX, currentZ, x, z) > tolerance && running) {
            if (obstacleDetected) {
                avoidObstacle();
            } else {
                double targetAngle = calculateAngle(currentX, currentZ, x, z);
                double angleDifference = normalizeAngle(targetAngle - currentAngle);
                double steering = 0;

                if (Math.abs(targetAngle) != 0) {
                    steering = Math.max(-maxSteeringAngle, Math.min(maxSteeringAngle, angleDifference));
                }
                sendDriveCommand(motorPower, steering, 0.1);
            }
            sleep(100);
            fetchMessages();
        }

        running = false;
    }

    /**
     * Calculates the distance between two points.
     *
     * @param x1 the x-coordinate of the first point
     * @param z1 the z-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param z2 the z-coordinate of the second point
     * @return the distance between the two points
     */
    private double distance(double x1, double z1, double x2, double z2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
    }

    /**
     * Calculates the angle between the current position and the target position.
     *
     * @param currentX the current x-coordinate
     * @param currentZ the current z-coordinate
     * @param targetX the target x-coordinate
     * @param targetZ the target z-coordinate
     * @return the angle between the current position and the target position
     */
    private double calculateAngle(double currentX, double currentZ, double targetX, double targetZ) {
        double deltaX = targetX - currentX;
        double deltaZ = targetZ - currentZ;
        double angleInRadians = Math.atan2(deltaX, deltaZ);
        return Math.toDegrees(angleInRadians);
    }

    /**
     * Normalizes an angle to the range [-180, 180] degrees.
     *
     * @param angle the angle to normalize
     * @return the normalized angle
     */
    private double normalizeAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    /**
     * Sends a drive command to the SaSaCommunicator.
     *
     * @param motorPower the motor power
     * @param steeringAngle the steering angle
     * @param duration the duration of the drive command
     */
    public void sendDriveCommand(double motorPower, double steeringAngle, double duration) {
        String command = String.format("DRIVE %.2f %.2f %.2f", motorPower, steeringAngle, duration).replace(",", ".");
        saSaCommunicator.send(command);
    }

    /**
     * Executes a command by sending it to the SaSaCommunicator.
     *
     * @param command the command to execute
     */
    public void executeCommand(String command) {
        saSaCommunicator.send(command);
    }

    /**
     * Pauses the current thread for the specified number of milliseconds.
     *
     * @param ms the number of milliseconds to sleep
     */
    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches messages from the SaSaCommunicator and processes them.
     */
    public void fetchMessages() {
        for (String message : saSaCommunicator.getMessages()) {
            processMessage(message);
        }
    }

    /**
     * Starts a thread for detecting obstacles in front of the frog.
     */
    private void startObstacleDetection() {
        Thread obstacleDetectionThread = new Thread(() -> {
            while (true) {
                fetchMessages();
                sleep(200);
            }
        });
        obstacleDetectionThread.setDaemon(true);
        obstacleDetectionThread.start();
    }

    /**
     * Processes a message from the SaSaCommunicator.
     *
     * @param message the message to process
     */
    private void processMessage(String message) {
        String[] parts = message.replace(",", ".").split(" ");
        if (parts[0].equals("FROG")) {
            switch (parts[1]) {
                case "STATUS" -> {
                    currentX = Double.parseDouble(parts[2]);
                    currentZ = Double.parseDouble(parts[4]);
                    currentAngle = Double.parseDouble(parts[5]);
                    Logger.addWayPoints(currentX, currentZ);
                    notifyStatusListeners(message);
                    notifyFrogLocation(currentX, currentZ, currentAngle);
                }
                case "RADAR" -> {
                    if (parts[2].equals("STOP")) {
                        return;
                    }
                    double relativeX = Double.parseDouble(parts[3]);
                    double relativeZ = Double.parseDouble(parts[5]);

                    double angleInRadians = Math.toRadians(currentAngle);
                    double absoluteX = currentX + relativeX * Math.cos(angleInRadians) + relativeZ * Math.sin(angleInRadians);
                    double absoluteZ = currentZ + relativeZ * Math.cos(angleInRadians) - relativeX * Math.sin(angleInRadians);
                    notifyRadarListeners(absoluteX, absoluteZ);

                    // Obstacle detection logic
                    // if current frog direction is closer to Z-axis (0 or 360 degrees)
                    if ((currentAngle >= 0 && currentAngle <= 45) || (currentAngle > 315 && currentAngle <= 360) ||
                            (currentAngle >= 135 && currentAngle <= 225)) {
                        if (Math.abs(relativeZ) < 10 && Math.abs(relativeX) < 5) {
                            obstacleDetected = true;
                        }
                    } else {
                        if (Math.abs(relativeX) < 10 && Math.abs(relativeZ) < 5) {
                            obstacleDetected = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * Attempts to avoid an obstacle by reversing and steering.
     */
    private void avoidObstacle() {
        // Reverse a bit and steer to avoid the obstacle
        sendDriveCommand(-0.5, 0, 1);
        sleep(1000);

        double targetAngle = calculateAngle(currentX, currentZ, targetX, targetZ);
        int turnAngle = (normalizeAngle(targetAngle - currentAngle) > 0) ? -30 : 30;

        for (int i = 0; i < 3; i++) {
            sendDriveCommand(-0.5, turnAngle, 1);
            sleep(1000);
            fetchMessages();
        }

        // Move forward after avoiding
        sendDriveCommand(1, 0, 5);
        sleep(5000);

        // Clear obstacle flag after avoidance maneuver
        obstacleDetected = false;
    }
}
