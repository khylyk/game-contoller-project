package nl.saxion.ptbc.groundcontrolfx.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import nl.saxion.ptbc.pilotfx.Model.Pilot;
import nl.saxion.ptbc.pilotfx.utils.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The MapController class handles the interaction between the Frog and the Ground Control Map.
 * It manages button actions, mouse clicks on the canvas, and updates the canvas with radar and frog positions.
 */
public class MapController {

    @FXML
    private Button buttonForward;
    @FXML
    private Button buttonRight;
    @FXML
    private Button buttonLeft;
    @FXML
    private Button buttonBack;
    @FXML
    private Canvas radarCanvas;
    @FXML
    private Canvas frogCanvas;
    @FXML
    private Canvas trailCanvas;
    @FXML
    private Canvas clickCanvas;

    private final Pilot pilot = Pilot.getInstance();
    private final Set<String> processedRadarPoints = new HashSet<>();
    private volatile boolean running = false;
    private volatile Thread currentThread;

    /**
     * Initializes the MapController by adding radar and frog location listeners to the Pilot instance.
     */
    public void initialize() {
        pilot.addRadarListener(this::onRadarUpdate);
        pilot.addFrogLocation(this::updateFrogPosition);
    }

    /**
     * Handles mouse click events on the canvas, draws the click location, and drives the frog to the clicked coordinates.
     *
     * @param event the mouse event
     * @throws InterruptedException if the current thread is interrupted
     */
    @FXML
    public void handleMouseClick(MouseEvent event) throws InterruptedException {
        double x = event.getX() * 2 - 500;
        double z = -(event.getY() * 2 - 500);
        handleDraw(event.getX(), event.getY());
        if ((running || pilot.isRunning()) && currentThread.isAlive()) {
            running = false;
            pilot.setRunning(false);
            currentThread.join();
            currentThread.interrupt();
        }

        driveToCoordinate(x, z);
    }

    /**
     * Draws a marker on the canvas at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param z the z-coordinate
     */
    @FXML
    public void handleDraw(double x, double z) {
        Platform.runLater(() -> {
            GraphicsContext gc = clickCanvas.getGraphicsContext2D();
            clearCanvas(gc);

            gc.setFill(Color.LIME);
            gc.fillOval(x - 5, z - 5, 10, 10);
        });
    }

    /**
     * Handles button click events for driving the frog in different directions or stopping it.
     *
     * @param event the action event
     * @throws InterruptedException if the current thread is interrupted
     */
    @FXML
    public void buttonClicked(ActionEvent event) throws InterruptedException {
        if ((running || pilot.isRunning()) && currentThread.isAlive()) {
            running = false;
            pilot.setRunning(false);
            currentThread.join();
            currentThread.interrupt();
        }

        if (event.getSource().equals(buttonForward)) {
            driveForward();
        } else if (event.getSource().equals(buttonBack)) {
            driveBackwards();
        } else if (event.getSource().equals(buttonRight)) {
            driveRight();
        } else if (event.getSource().equals(buttonLeft)) {
            driveLeft();
        } else {
            stop();
        }
    }

    /**
     * Drives the frog forward.
     */
    @FXML
    public void driveForward() {
        drive(1, 0, 0.5);
    }

    /**
     * Drives the frog to the left.
     */
    @FXML
    public void driveLeft() {
        drive(1, -30, 0.5);
    }

    /**
     * Drives the frog to the right.
     */
    @FXML
    public void driveRight() {
        drive(1, 30, 0.5);
    }

    /**
     * Stops the frog and interrupts the current driving thread.
     *
     * @throws InterruptedException if the current thread is interrupted
     */
    @FXML
    public void stop() throws InterruptedException {
        if (currentThread == null) {
            return;
        }

        running = false;
        currentThread.join();
        currentThread.interrupt();
    }

    /**
     * Drives the frog backward.
     */
    @FXML
    public void driveBackwards() {
        drive(-1, 0, 0.5);
    }

    /**
     * Drives the frog with the specified power, angle, and duration.
     *
     * @param power    the driving power
     * @param angle    the driving angle
     * @param duration the driving duration
     */
    public void drive(double power, int angle, double duration) {
        running = true;
        currentThread = new Thread(() -> {
            while (running) {
                pilot.sendDriveCommand(power, angle, duration);
                pilot.fetchMessages();
                pilot.sleep(500);
            }
        });
        currentThread.start();
    }

    /**
     * Updates the frog's position on the canvas.
     *
     * @param x         the x-coordinate
     * @param z         the z-coordinate
     * @param frogAngle the frog's angle
     */
    public void updateFrogPosition(double x, double z, double frogAngle) {
        Platform.runLater(() -> {
            GraphicsContext gc = frogCanvas.getGraphicsContext2D();
            GraphicsContext trail = trailCanvas.getGraphicsContext2D();
            clearCanvas(gc);

            // Translate and scale coordinates
            double canvasWidth = frogCanvas.getWidth();
            double canvasHeight = frogCanvas.getHeight();
            double scaledX = (x + 500) * (canvasWidth / 1000) - 5;
            double scaledZ = canvasHeight - (z + 500) * (canvasHeight / 1000) - 5;

            // Ensure the points are within the visible area
            if (scaledX >= 0 && scaledX <= canvasWidth && scaledZ >= 0 && scaledZ <= canvasHeight) {
                gc.setFill(Color.BLUE);
                gc.fillOval(scaledX, scaledZ, 10, 10);  // Draw frog position as a small circle
                trail.setFill(Color.GRAY);
                trail.fillOval(scaledX, scaledZ, 3, 3);
            }
        });
    }

    /**
     * Handles radar updates and draws radar points on the canvas.
     *
     * @param x the x-coordinate
     * @param z the z-coordinate
     */
    public void onRadarUpdate(double x, double z) {
        Platform.runLater(() -> {
            String radarPointKey = x + "," + z;
            if (!processedRadarPoints.contains(radarPointKey)) {
                processedRadarPoints.add(radarPointKey);
                GraphicsContext gc = radarCanvas.getGraphicsContext2D();

                // Translate and scale coordinates
                double canvasWidth = radarCanvas.getWidth();
                double canvasHeight = radarCanvas.getHeight();
                double scaledX = (x + 500) * (canvasWidth / 1000);
                double scaledZ = canvasHeight - (z + 500) * (canvasHeight / 1000);

                // Ensure the points are within the visible area
                if (scaledX >= 0 && scaledX <= canvasWidth && scaledZ >= 0 && scaledZ <= canvasHeight) {
                    gc.setFill(Color.RED);
                    gc.fillOval(scaledX, scaledZ, 3, 3);  // Draw radar point as a small circle
                }
            }
        });
    }

    /**
     * Clears the specified canvas.
     *
     * @param gc the graphics context of the canvas to clear
     */
    private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0, 0, frogCanvas.getWidth(), frogCanvas.getHeight());
    }

    /**
     * Drives frog to the coordinate where TNT is located
     */
    @FXML
    public void onButtonFindTNT() {
        driveToCoordinate(-223, -345);
    }

    /**
     * Drives frog to the coordinate where Detonator is located
     */
    @FXML
    public void onButtonFindDetonator() {
        driveToCoordinate(-147, 241);
    }

    /**
     * Drives frog back to the base entrance which is blocked with rocks
     */
    @FXML
    public void onButtonDestroyTheRocks() {
        driveToCoordinate(1, -100);
    }

    /**
     * handles export of radar points from the database
     * for each received point draws a point on the map
     */
    @FXML
    public void exportRadarPoints() {
        List<String> receivedRadarPoints = Logger.exportRadarPointsFromDB();
        for (String radarPoint : receivedRadarPoints) {
            String[] coordinates = radarPoint.split(" ");
            onRadarUpdate(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
        }
    }

    /**
     * Drives frog to the specified coordinate
     *
     * @param x the x-coordinate
     * @param z the-z coordinate
     */
    private void driveToCoordinate(double x, double z) {
        running = true;
        currentThread = new Thread(() -> {
            pilot.driveFrogToCoordinate(x, z);
        });
        currentThread.start();
    }
}
