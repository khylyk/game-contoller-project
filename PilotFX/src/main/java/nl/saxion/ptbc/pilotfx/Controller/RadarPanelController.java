package nl.saxion.ptbc.pilotfx.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import nl.saxion.ptbc.pilotfx.Model.Pilot;
import nl.saxion.ptbc.pilotfx.utils.Logger;
import org.sqlite.jdbc3.JDBC3DatabaseMetaData;

import java.util.HashSet;
import java.util.Set;

/**
 * The RadarPanelController class manages the radar and frog position displays on the user interface.
 * It handles radar updates and frog position updates, and displays them on the corresponding canvases.
 */
public class RadarPanelController {
    @FXML
    private Canvas radarCanvas;
    @FXML
    private Canvas frogCanvas;
    @FXML
    private Label statusLabel;

    private final Pilot pilot = Pilot.getInstance();
    private final Set<String> processedRadarPoints = new HashSet<>();

    /**
     * Initializes the controller by setting up radar and frog location listeners, and setting the initial status label.
     */
    @FXML
    public void initialize() {
        pilot.addRadarListener(this::onRadarUpdate);
        pilot.addFrogLocation(this::updateFrogPosition);
        statusLabel.setText("Status: Waiting for radar data...");
    }

    /**
     * Updates the frog's position on the canvas and displays an arrow indicating its direction.
     *
     * @param x the x-coordinate of the frog
     * @param z the z-coordinate of the frog
     * @param frogAngle the angle of the frog
     */
    public void updateFrogPosition(double x, double z, double frogAngle) {
        Platform.runLater(() -> {
            GraphicsContext gc = frogCanvas.getGraphicsContext2D();
            clearFrog(gc);

            // Translate and scale coordinates
            double canvasWidth = frogCanvas.getWidth();
            double canvasHeight = frogCanvas.getHeight();
            double scaledX = (x + 500) * (canvasWidth / 1000);
            double scaledZ = canvasHeight - (z + 500) * (canvasHeight / 1000);

            // Ensure the points are within the visible area
            if (scaledX >= 0 && scaledX <= canvasWidth && scaledZ >= 0 && scaledZ <= canvasHeight) {
                gc.setFill(Color.BLUE);
                gc.fillOval(scaledX, scaledZ, 10, 10);  // Draw frog position as a small circle

                double arrowLength = 20;
                double angleInRadians = Math.toRadians(-frogAngle + 80);
                double arrowX = scaledX + arrowLength * Math.cos(angleInRadians);
                double arrowZ = scaledZ - arrowLength * Math.sin(angleInRadians);

                gc.setStroke(Color.BLUE);
                gc.setLineWidth(4);
                gc.strokeLine(scaledX + 5, scaledZ + 5, arrowX, arrowZ);
                statusLabel.setText("Status: Frog position updated");
            } else {
                statusLabel.setText("Status: Frog position out of bounds");
            }
        });
    }

    /**
     * Handles radar updates and draws radar points on the canvas.
     *
     * @param x the x-coordinate of the radar point
     * @param z the z-coordinate of the radar point
     */
    public void onRadarUpdate(double x, double z) {
        Platform.runLater(() -> {
            String radarPointKey = x + "," + z;
            if (!processedRadarPoints.contains(radarPointKey)) {
                Logger.addCollisionPoints(x,z);
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
                    gc.fillOval(scaledX + 10, scaledZ + 10, 3, 3);  // Draw radar point as a small circle
                    statusLabel.setText("Status: Radar data received");
                } else {
                    statusLabel.setText("Status: Radar point out of bounds");
                }
            }
        });
    }

    /**
     * Clears the radar canvas.
     *
     * @param gc the graphics context of the radar canvas to clear
     */
    private void clearRadar(GraphicsContext gc) {
        gc.clearRect(0, 0, radarCanvas.getWidth(), radarCanvas.getHeight());
    }

    /**
     * Clears the frog canvas.
     *
     * @param gc the graphics context of the frog canvas to clear
     */
    private void clearFrog(GraphicsContext gc) {
        gc.clearRect(0, 0, frogCanvas.getWidth(), frogCanvas.getHeight());
    }
}
