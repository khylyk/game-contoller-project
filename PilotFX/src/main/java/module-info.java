module nl.saxion.ptbc.pilotfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires SaSaCommunicator;
    requires opencsv;
    requires jdk.jdi;
    requires java.sql;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires org.apache.commons.lang3;

    opens nl.saxion.ptbc.pilotfx to javafx.fxml;
    exports nl.saxion.ptbc.pilotfx;
    exports nl.saxion.ptbc.pilotfx.Controller;
    opens nl.saxion.ptbc.pilotfx.Controller to javafx.fxml;
    exports nl.saxion.ptbc.pilotfx.Model;
    opens nl.saxion.ptbc.pilotfx.Model to javafx.fxml;
    exports nl.saxion.ptbc.pilotfx.utils;
}