package org.example.ingenierosvscarreraspt1;

import javafx.scene.paint.Color;

/**
 * Tesoro especial que extiende {@link Recompensa}.
 * Además de curar, otorga puntos de experiencia al jugador al ser recogido.
 */
public class Tesoro extends Recompensa {

    /** Puntos de experiencia que otorga al ser recogido. */
    private int experiencia;

    /**
     * Crea un tesoro en la posición indicada.
     *
     * @param nombre      Nombre del tesoro.
     * @param valorSalud  HP que restaura.
     * @param experiencia Puntos de experiencia que otorga.
     * @param x           Posición X.
     * @param y           Posición Y.
     */
    public Tesoro(String nombre, int valorSalud, int experiencia, int x, int y) {
        super(nombre, valorSalud, "Salud+EXP", x, y);
        this.experiencia = experiencia;
        // Color distinto al de Recompensa simple
        getSprite().setFill(Color.MEDIUMPURPLE);
        getSprite().setStroke(Color.INDIGO);
        getSprite().setWidth(25);
        getSprite().setHeight(25);
    }

    /** @return Puntos de experiencia que otorga. */
    public int getExperiencia() { return experiencia; }

    @Override
    public void registrar() {
        System.out.println("[Tesoro] " + getNombre()
                + " | +" + getValor() + " HP | +" + experiencia + " EXP");
    }
}
