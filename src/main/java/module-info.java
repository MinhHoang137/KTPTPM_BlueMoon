module com.example.ktptpm_bluemoon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires mysql.connector.j;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens view_controller.fee           to javafx.fxml;
    opens view_controller.household     to javafx.fxml;
    opens view_controller.resident      to javafx.fxml;
    opens view_controller.temporary     to javafx.fxml;
    opens view_controller.notification  to javafx.fxml;
    opens view_controller               to javafx.fxml;

    opens controller.user               to javafx.fxml;
    opens controller.fee                to javafx.fxml;
    opens controller.household          to javafx.fxml;
    opens controller.resident           to javafx.fxml;
    opens controller.temporary          to javafx.fxml;
    opens controller.report             to javafx.fxml;
    opens controller.statistics         to javafx.fxml;

    opens com.example.ktptpm_bluemoon   to javafx.fxml;
    exports com.example.ktptpm_bluemoon;
}