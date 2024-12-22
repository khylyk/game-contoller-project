package nl.saxion.ptbc.groundcontrolfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The Main class is the entry point for the Ground Control application.
 * It sets up and displays the main window, terminal window, and radar window.
 */
public class Main extends Application {

    /**
     * The main entry point for the JavaFX application.
     * This method is called when the application is launched.
     *
     * @param stage the primary stage for this application
     * @throws IOException if the FXML files cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("map_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ground Control");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Stage terminalStage = new Stage();
        fxmlLoader = new FXMLLoader(Main.class.getResource("terminal_view.fxml"));
        Scene terminalScene = new Scene(fxmlLoader.load());
        terminalStage.setTitle("Terminal");
        terminalStage.setScene(terminalScene);
        terminalStage.setResizable(false);
        terminalStage.show();

        Stage radarStage = new Stage();
        fxmlLoader = new FXMLLoader(Main.class.getResource("radar_panel.fxml"));
        Scene radarScene = new Scene(fxmlLoader.load());
        radarStage.setTitle("Radar");
        radarStage.setScene(radarScene);
        radarStage.setResizable(false);
        radarStage.show();
    }
}