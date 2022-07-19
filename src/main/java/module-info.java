module javafx.qqplayer {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires rxcontrols;
    requires java.sql;
    requires mysql.connector.java;
    //   requires javafx.web;


    exports MyQQPlayer;

    opens MyQQPlayer to javafx.fxml;
}