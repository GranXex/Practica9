package org.example.ingenierosvscarreraspt1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemigo extends Personaje {

    public Enemigo(String nombre, int x, int y) {
        super(nombre, 2, x, y); // 2 de vida
    }

    @Override
    public void initGrafico() {
        var res = getClass().getResource("/org/example/ingenierosvscarreraspt1/fantasma.png");
        if (res != null) {
            setSprite(new ImageView(new Image(res.toExternalForm())));

            // REDIMENSIONAR AQUÍ
            getSprite().setFitWidth(60);
            getSprite().minHeight(60);
            getSprite().setPreserveRatio(true);

            getSprite().setX(getPosicionX());
            getSprite().setY(getPosicionY());
        }
    }
}