module com.example.serverchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.serverchat to javafx.fxml;
    exports com.example.serverchat;
}