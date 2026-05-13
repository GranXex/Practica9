
package org.example.ingenierosvscarreraspt1;

public class Utileria implements ElementoDinamico {

    private String nombre;
    private String descripcion;
    private int posicionX;
    private int posicionY;
    private boolean usada;

    // NUEVO
    private int daño;

    public Utileria(String nombre,
                    String descripcion,
                    int posicionX,
                    int posicionY,
                    int daño) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.daño = daño;

        this.usada = false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public boolean isUsada() {
        return usada;
    }

    public int getDaño() {
        return daño;
    }

    public void usar() {

        this.usada = true;

        System.out.println("Se usó: " + nombre);
    }

    // ==================================
    // ARMAS
    // ==================================

    // Espada normal
    public static Utileria espada() {

        return new Utileria(
                "Espada",
                "Arma básica",
                0,
                0,
                1
        );
    }

    // Mazo doble daño
    public static Utileria mazo() {

        return new Utileria(
                "Mazo",
                "Hace el doble de daño",
                0,
                0,
                2
        );
    }

    @Override
    public void mover(String direccion, int distancia) {

        System.out.println(
                nombre
                        + " se mueve hacia "
                        + direccion
                        + " "
                        + distancia
                        + " unidades.");
    }
}