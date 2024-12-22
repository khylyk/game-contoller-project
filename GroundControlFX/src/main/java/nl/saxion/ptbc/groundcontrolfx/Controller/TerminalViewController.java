package nl.saxion.ptbc.groundcontrolfx.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.saxion.ptbc.pilotfx.Model.Pilot;
import nl.saxion.ptbc.pilotfx.utils.Logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The TerminalViewController class manages the interaction between the user interface and the Terminal model.
 * It handles user inputs, command execution, status and radar updates, and logging.
 */
public class TerminalViewController {
    @FXML
    private TextArea terminalText;
    @FXML
    private TextField inputField;
    @FXML
    private Button toggleStatusButton;
    @FXML
    private Button toggleRadarButton;

    private final Pilot pilot = Pilot.getInstance();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private LocalTime lastPrintTime = LocalTime.MIN;
    private LocalTime lastRadarPrintTime = LocalTime.MIN;

    private String lastStatus;
    private boolean displayStatusMessages = true;
    private boolean displayRadarMessages = true;

    /**
     * Initializes the controller by setting up listeners for status and radar updates,
     * and configuring the terminal text area to auto-scroll to the bottom.
     */
    public void initialize() {
        System.out.println("Initialize method called");
        pilot.addStatusListener(this::onStatusReceived);
        pilot.addRadarListener(this::onRadarReceived);

        Platform.runLater(() -> {
            terminalText.textProperty().addListener((observableValue, s, t1) -> {
                terminalText.setScrollTop(Double.MAX_VALUE);
            });
        });
    }

    /**
     * Handles the event when the Enter key is pressed in the input field.
     * Validates and executes the command entered by the user.
     *
     * @param event the key event
     */
    @FXML
    protected void onEnterPressed(KeyEvent event) {
        Platform.runLater(() -> {
            if (event.getCode() == KeyCode.ENTER) {
                String text = inputField.getText();

                if (pilot.isRunning() && !text.contains("STATUS") && !text.contains("RADAR")) {
                    raiseAlertWindow("Pilot is already running. Wait for current operation to finish or stop it manually");
                }

                try {

                    validateLine(text);
                    pilot.executeCommand(text);
                    terminalText.appendText(LocalTime.now().format(timeFormatter) + ": " + text + "\n");
                    Logger.addCommandLog(LocalTime.now(), text);
                } catch (IllegalArgumentException e) {
                    raiseAlertWindow(e.getMessage());
                }
                inputField.clear();
            }
        });
    }

    private void validateLine(String line) {
        if (line.isBlank()) {
            throw new IllegalArgumentException("Command can not be empty");
        }

        String[] lineParts = line.split(" ");
        switch (lineParts[0]) {
            case "STATUS":
                validateStatusCommand(lineParts);
                break;
            case "RADAR":
                validateRadarCommand(lineParts);
                break;
            case "DRIVE":
                validateDriveCommand(lineParts);
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + line);
        }
    }

    /**
     * Validates the format of a STATUS command.
     *
     * @param lineParts the parts of the STATUS command
     * @throws IllegalArgumentException if the STATUS command is invalid
     */
    private void validateStatusCommand(String[] lineParts) {
        if (lineParts.length != 2 || (!lineParts[1].equals("REQUEST") && !lineParts[1].equals("ON") && !lineParts[1].equals("OFF"))) {
            throw new IllegalArgumentException("Wrong STATUS command format");
        }
    }

    /**
     * Validates the format of a RADAR command.
     *
     * @param lineParts the parts of the RADAR command
     * @throws IllegalArgumentException if the RADAR command is invalid
     */
    private void validateRadarCommand(String[] lineParts) {
        if (lineParts.length != 2 || (!lineParts[1].equals("ON") && !lineParts[1].equals("OFF"))) {
            throw new IllegalArgumentException("Wrong RADAR command format");
        }
    }

    /**
     * Validates the format of a DRIVE command.
     *
     * @param lineParts the parts of the DRIVE command
     * @throws IllegalArgumentException if the DRIVE command is invalid
     */
    private void validateDriveCommand(String[] lineParts) {
        if (lineParts.length != 4) {
            throw new IllegalArgumentException("Wrong DRIVE command format");
        }

        double power = Double.parseDouble(lineParts[1]);
        int angle = Integer.parseInt(lineParts[2]);
        double duration = Double.parseDouble(lineParts[3]);
        if (power < -1 || power > 1) throw new IllegalArgumentException("Wrong DRIVE command format");
        if (angle < -30 || angle > 30) throw new IllegalArgumentException("Wrong DRIVE command format");
        if (duration < 0 || duration > 5) throw new IllegalArgumentException("Wrong DRIVE command format");
    }

    /**
     * Saves collision points to the database and logs the action.
     */
    @FXML
    public void saveCollisionPoints() {
        new Thread(() -> {
            terminalText.appendText(LocalTime.now().format(timeFormatter) + ": " + "Collision points have been successfully saved." + "\n");
            Logger.uploadCollisionPointsToDatabase();
        }).start();
    }

    /**
     * Saves way points to the database and logs the action.
     */
    @FXML
    public void saveWayPoints() {
        new Thread(() -> {
            terminalText.appendText(LocalTime.now().format(timeFormatter) + ": " + "Way points have been successfully saved." + "\n");
            Logger.uploadWayPointsToDatabase();
        }).start();
    }

    /**
     * Saves terminal logs to the database and logs the action.
     */
    @FXML
    public void saveTerminalLog() {
        terminalText.appendText(LocalTime.now().format(timeFormatter) + ": " + "Terminal log have been successfully saved." + "\n");
        Logger.uploadCommandLogToDatabase();
    }

    /**
     * Handles status messages received from the Pilot.
     *
     * @param status the status message
     */
    public void onStatusReceived(String status) {
        if (!displayStatusMessages) {
            return;
        }

        Platform.runLater(() -> {
            LocalTime now = LocalTime.now();

            if (status.equals(lastStatus)) {
                return;
            }

            if (lastPrintTime.plusSeconds(2).isBefore(now)) {
                terminalText.appendText(now.format(timeFormatter) + ": " + status + "\n");

                lastPrintTime = now;
                lastStatus = status;
            }
        });
    }

    /**
     * Handles radar messages received from the Pilot and displays them in the terminal.
     *
     * @param x the x-coordinate of the radar point
     * @param y the y-coordinate of the radar point
     */
    public void onRadarReceived(double x, double y) {
        if (!displayRadarMessages) {
            return;
        }

        Platform.runLater(() -> {
            LocalTime now = LocalTime.now();

            if (lastRadarPrintTime.plusSeconds(2).isBefore(now)) {
                terminalText.appendText(now.format(timeFormatter) + ": RADAR x=" + x + " y=" + y + "\n");
                lastRadarPrintTime = now;
            }
        });
    }

    /**
     * Toggles the display of status messages in the terminal.
     */
    @FXML
    public void toggleStatus() {
        System.out.println("Toggle Status button clicked");
        displayStatusMessages = !displayStatusMessages;
        toggleStatusButton.setText(displayStatusMessages ? "Hide Status" : "Show Status");
    }

    /**
     * Toggles the display of radar messages in the terminal.
     */
    @FXML
    public void toggleRadar() {
        System.out.println("Toggle Radar button clicked");
        displayRadarMessages = !displayRadarMessages;
        toggleRadarButton.setText(displayRadarMessages ? "Hide Radar" : "Show Radar");
    }

    /**
     * Raises an alert window with the given message.
     *
     * @param message the message to display in the alert window
     */
    private void raiseAlertWindow(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
