package org.example.ingenierosvscarreraspt1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public static class Arma implements Inventariable {
        private String nombre;
        private int daño;
        private double alcance;

        public Arma(String nombre, int daño, double alcance) {
            this.nombre = nombre;
            this.daño = daño;
            this.alcance = alcance;
        }

        public String getNombre() { return nombre; }
        public int getDaño() { return daño; }
        public double getAlcance() { return alcance; }

        @Override
        public void registrar() {
            System.out.println("[org.example.ingenierosvscarreraspt1.HelloController.Arma] " + nombre + " (Daño: " + daño + ", Alcance: " + alcance + ")");
        }

        @Override
        public void borrar() {
            System.out.println("El arma '" + nombre + "' ha sido destruida o descartada.");
        }
    }
}
