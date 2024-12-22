module nl.saxion.ptbc.groundcontrolfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires SaSaCommunicator;
    requires nl.saxion.ptbc.pilotfx;
    requires opencsv;

    opens nl.saxion.ptbc.groundcontrolfx to javafx.fxml;
    exports nl.saxion.ptbc.groundcontrolfx;
    opens nl.saxion.ptbc.groundcontrolfx.Controller to javafx.fxml;
    exports nl.saxion.ptbc.groundcontrolfx.Controller;
}