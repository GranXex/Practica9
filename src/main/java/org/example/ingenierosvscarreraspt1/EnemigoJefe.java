package org.example.ingenierosvscarreraspt1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.effect.ColorAdjust;

/**
 * Jefe final de un piso. Extiende {@link Enemigo} con más vida,
 * mayor daño al jugador y velocidad de movimiento diferente.
 * Se identifica visualmente por un sprite de mayor tamaño con tono rojizo.
 */
public class EnemigoJefe extends Enemigo {

    /** Daño extra que inflige el jefe al jugador. */
    private int dañoEspecial;

    /**
     * Construye un jefe con 6 puntos de vida y daño especial aumentado.
     *
     * @param nombre    Nombre del jefe.
     * @param posicionX Posición inicial X.
     * @param posicionY Posición inicial Y.
     */
    public EnemigoJefe(String nombre, int posicionX, int posicionY) {
        super(nombre, posicionX, posicionY);
        // Sobreescribe la vida que pone Enemigo (2) dándole más
        setVida(6);
        this.dañoEspecial = 35;
    }

    /** @return Daño especial del jefe. */
    public int getDañoEspecial() { return dañoEspecial; }

    /**
     * Habilidad especial: el jefe ruge causando daño aumentado.
     *
     * @return El valor de daño especial aplicable al jugador.
     */
    public int rugido() {
        System.out.println("[Jefe] " + getNombre() + " usa RUGIDO — daño especial: " + dañoEspecial);
        return dañoEspecial;
    }

    @Override
    public void initGrafico() {
        var res = getClass().getResource("/org/example/ingenierosvscarreraspt1/fantasma.png");
        if (res != null) {
            setSprite(new ImageView(new Image(res.toExternalForm())));
            getSprite().setFitWidth(90);   // más grande que el enemigo normal
            getSprite().setPreserveRatio(true);
            getSprite().setX(getPosicionX());
            getSprite().setY(getPosicionY());
            // Tono rojizo para diferenciarlo
            ColorAdjust rojo = new ColorAdjust();
            rojo.setHue(-0.5);
            rojo.setSaturation(0.8);
            getSprite().setEffect(rojo);
        }
    }
}
