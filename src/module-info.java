module itss.ktpm_bluemoon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;

    opens itss.ktpm_bluemoon to javafx.fxml;
    exports itss.ktpm_bluemoon;
    opens view_controller.household to javafx.fxml;
    exports view_controller.household;
    opens view_controller.resident to javafx.fxml;
    exports view_controller.resident;
    opens entity.resident to javafx.base;
    exports view_controller;
    opens view_controller to javafx.fxml;
    opens view_controller.notification to javafx.fxml;
    exports view_controller.notification;
    opens controller.household to javafx.base;
    exports controller.household;
    opens controller.fee to javafx.fxml;
    exports controller.fee;
    opens view_controller.fee to javafx.fxml;
    opens view_controller.temporary to javafx.fxml;
    exports view_controller.temporary;
    opens controller.temporary to javafx.base;
    exports controller.temporary;
    opens entity.temporary to javafx.base;
    exports entity.temporary;
}