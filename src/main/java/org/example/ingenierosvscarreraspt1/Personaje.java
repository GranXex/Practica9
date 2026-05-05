package org.example.ingenierosvscarreraspt1;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class Personaje implements ElementoDinamico, Destruible {
    private String nombre;
    private int vida;
    private int posicionX;
    private int posicionY;

    public Personaje(String nombre, int vida, int posicionX, int posicionY) {
        this.nombre = nombre;
        this.vida = vida;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }

    public void setPosicionX(int x) { this.posicionX = x; }
    public void setPosicionY(int y) { this.posicionY = y; }

    public void recibirDaño(int daño) {
        this.vida -= daño;
        System.out.println(this.nombre + " ha recibido " + daño + " puntos de daño. Vida restante: " + this.vida);

        // Si la vida cae a 0 o menos, el personaje se destruye
        if (this.vida <= 0) {
            this.vida = 0; // Opcional: para que no muestre vida en números negativos
            this.destruye();
        }
    }

    @Override
    public void destruye() {
        System.out.println("¡" + this.nombre + " ha perdido toda su vida y ha sido destruido (Game Over)!");
    }

    @Override
    public void mover(String direccion, int distancia) {
        switch (direccion.toLowerCase()) {
            case "norte": posicionY -= distancia; break;
            case "sur":   posicionY += distancia; break;
            case "este":  posicionX += distancia; break;
            case "oeste": posicionX -= distancia; break;
        }
        if (sprite != null) {
            sprite.setX(posicionX);
            sprite.setY(posicionY);
        }
    }


    private ImageView sprite;

    public void initGrafico() {
        var res = getClass().getResource("/org/example/ingenierosvscarreraspt1/Personaje.png");
        if (res != null) {
            sprite = new ImageView(new Image(res.toExternalForm()));

            // REDIMENSIONAR AQUÍ
            sprite.setFitWidth(64);  // Ajusta el ancho
            sprite.setFitHeight(64); // Ajusta el alto
            sprite.setPreserveRatio(true); // Mantiene la proporción para que no se vea estirado

            sprite.setX(posicionX);
            sprite.setY(posicionY);
        }
    }
    // Añade esto dentro de la clase Personaje
    public void atacar(java.util.List<Enemigo> enemigos, javafx.scene.layout.Pane root) {
        java.util.List<Enemigo> muertos = new java.util.ArrayList<>();

        for (Enemigo e : enemigos) {
            if (this.sprite.getBoundsInParent().intersects(e.getSprite().getBoundsInParent())) {
                e.recibirDaño(1);

                // EFECTO DE RETROCESO: Empuja al enemigo lejos del jugador
                if (this.posicionX < e.getPosicionX()) e.setPosicionX(e.getPosicionX() + 40);
                else e.setPosicionX(e.getPosicionX() - 40);

                e.getSprite().setX(e.getPosicionX());

                if (e.getVida() <= 0) {
                    root.getChildren().remove(e.getSprite());
                    muertos.add(e);
                }
            }
        }
        enemigos.removeAll(muertos);
    }

    // Para que el hijo pueda setear el sprite
    protected void setSprite(ImageView sprite) { this.sprite = sprite; }

    public ImageView getSprite() {
        return sprite;
    }

}