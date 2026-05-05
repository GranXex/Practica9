module org.example.ingenierosvscarreraspt1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ingenierosvscarreraspt1 to javafx.fxml;
    exports org.example.ingenierosvscarreraspt1;
}