package org.example.ingenierosvscarreraspt1;

/**
 * Representa un arma que el jugador puede equipar y usar en combate.
 * Implementa {@link Inventariable} para poder ser almacenada en el inventario.
 */
public class Arma implements Inventariable {

    /** Nombre del arma. */
    private String nombre;

    /** Puntos de daño que inflige esta arma. */
    private int daño;

    /** Alcance en píxeles (no usado en colisión, pero disponible para extensiones). */
    private int alcance;

    /**
     * Construye un arma con los atributos indicados.
     *
     * @param nombre  Nombre del arma.
     * @param daño    Daño que inflige.
     * @param alcance Alcance del arma.
     */
    public Arma(String nombre, int daño, int alcance) {
        this.nombre  = nombre;
        this.daño    = daño;
        this.alcance = alcance;
    }

    /** @return Nombre del arma. */
    public String getNombre() { return nombre; }

    /** @return Puntos de daño del arma. */
    public int getDaño() { return daño; }

    /** @return Alcance del arma. */
    public int getAlcance() { return alcance; }

    /** Fábrica: espada ligera (daño 1, alcance 50). */
    public static Arma espada() {
        return new Arma("Espada", 1, 50);
    }

    /** Fábrica: mazo pesado (daño 2, alcance 40). */
    public static Arma mazo() {
        return new Arma("Mazo", 2, 40);
    }

    /** Fábrica: lanza larga (daño 1, alcance 80). */
    public static Arma lanza() {
        return new Arma("Lanza", 1, 80);
    }

    @Override
    public void registrar() {
        System.out.println("[Arma] " + nombre + " | Daño: " + daño + " | Alcance: " + alcance);
    }

    @Override
    public void borrar() {
        System.out.println("[Arma] " + nombre + " ha sido descartada del inventario.");
    }
}
