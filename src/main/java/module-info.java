module com.tabled.millioner {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.google.gson;

    opens com.tabled.millioner to javafx.fxml;
    exports com.tabled.millioner;
    exports com.tabled.millioner.controller;
    opens com.tabled.millioner.controller to javafx.fxml;
}