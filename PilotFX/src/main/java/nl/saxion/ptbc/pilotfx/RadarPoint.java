package nl.saxion.ptbc.pilotfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The RadarPoint class is the entry point for the Radar Point application.
 * It loads and displays the radar panel user interface.
 */
public class RadarPoint extends Application {

    /**
     * The main entry point for the JavaFX application.
     * This method is called when the application is launched.
     *
     * @param stage the primary stage for this application
     * @throws IOException if the FXML file cannot be loaded
     */
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RadarPoint.class.getResource("radar_panel.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 750, 750);
        stage.setTitle("Radar Point");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
