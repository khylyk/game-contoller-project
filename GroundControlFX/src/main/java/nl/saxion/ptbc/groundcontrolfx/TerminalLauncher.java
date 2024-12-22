package nl.saxion.ptbc.groundcontrolfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.saxion.ptbc.pilotfx.Model.Pilot;

/**
 * The TerminalLauncher class is the entry point for the Terminal application.
 * It initializes the Pilot instance and loads the terminal view user interface.
 */
public class TerminalLauncher extends Application {

    /**
     * The main entry point for the JavaFX application.
     * This method is called when the application is launched.
     *
     * @param stage the primary stage for this application
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        Pilot pilot = Pilot.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(TerminalLauncher.class.getResource("terminal_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Terminal");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
