open module com.example.parallel_game_server {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires java.naming;
    requires org.postgresql.jdbc;

//    opens com.example.parallel_game_server to javafx.fxml, com.google.gson;
    exports com.example.parallel_game_server;
}