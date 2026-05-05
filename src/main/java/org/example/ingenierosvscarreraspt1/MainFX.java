package org.example.ingenierosvscarreraspt1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javafx.scene.input.KeyCode;

public class MainFX extends Application {
    private int pisoActual = 2;
    private List<Enemigo> enemigos = new ArrayList<>();
    private Personaje heroe;
    private Pane root;
    private boolean juegoTerminado = false;
    private final Set<KeyCode> teclasPresionadas = new HashSet<>();
    private static final int PASO = 12;

    // CONTROL DE DAÑO
    private long ultimoDañoRecibido = 0;
    private final long TIEMPO_INVULNERABILIDAD = 1_500_000_000L; // 1.5 segundos en nanosegundos

    @Override
    public void start(Stage stage) {
        root = new Pane();

        // 1. ESCENARIO
        String rutaEscenario = getClass().getResource("/org/example/ingenierosvscarreraspt1/escenario.png").toExternalForm();
        ImageView fondo = new ImageView(new Image(rutaEscenario));
        fondo.setFitWidth(800);
        fondo.setFitHeight(600);

        // 2. COLISIONES
        Rectangle muroCentral = new Rectangle(285, 215, 230, 170);
        muroCentral.setFill(Color.TRANSPARENT);

        Rectangle salida = new Rectangle(680, 480, 80, 80);
        salida.setFill(Color.TRANSPARENT);

        // 3. PERSONAJES
        heroe = new Personaje("Miguel", 100, 100, 100);
        heroe.initGrafico();
        heroe.getSprite().setFitWidth(50);
        heroe.getSprite().setPreserveRatio(true);

        root.getChildren().addAll(fondo, muroCentral, salida, heroe.getSprite());
        generarEnemigos(root, new Random().nextInt(3) + 1);

        Scene scene = new Scene(root, 800, 600);

        // 4. CONTROL DE TECLAS - Registrar teclas presionadas y soltadas
        scene.setOnKeyPressed(e -> {
            teclasPresionadas.add(e.getCode());
            if (e.getCode() == KeyCode.SPACE) {
                if (!juegoTerminado) heroe.atacar(enemigos, root);
            }
        });

        scene.setOnKeyReleased(e -> {
            teclasPresionadas.remove(e.getCode());
        });

        // 5. TIMER DE MOVIMIENTO Y DAÑO
        long[] ultimoMovimiento = {0};
        final long INTERVALO_MOVIMIENTO = 16_000_000L; // ~60 FPS para movimiento (16ms)

        javafx.animation.AnimationTimer timer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                if (juegoTerminado) return;

                // --- MOVIMIENTO DIAGONAL (procesado a intervalos fijos) ---
                if (now - ultimoMovimiento[0] >= INTERVALO_MOVIMIENTO) {
                    ultimoMovimiento[0] = now;

                    int oldX = heroe.getPosicionX();
                    int oldY = heroe.getPosicionY();
                    int dx = 0, dy = 0;

                    if (teclasPresionadas.contains(KeyCode.UP))    dy -= PASO;
                    if (teclasPresionadas.contains(KeyCode.DOWN))  dy += PASO;
                    if (teclasPresionadas.contains(KeyCode.LEFT))  dx -= PASO;
                    if (teclasPresionadas.contains(KeyCode.RIGHT)) dx += PASO;

                    // Aplicar movimiento solo si hay teclas presionadas
                    if (dx != 0 || dy != 0) {
                        heroe.setPosicionX(oldX + dx);
                        heroe.setPosicionY(oldY + dy);
                        heroe.getSprite().setX(heroe.getPosicionX());
                        heroe.getSprite().setY(heroe.getPosicionY());
                    }

                    // Verificar colisiones
                    if (heroe.getSprite().getBoundsInParent().intersects(muroCentral.getBoundsInParent()) ||
                            heroe.getPosicionX() < 15 || heroe.getPosicionX() > 735 ||
                            heroe.getPosicionY() < 15 || heroe.getPosicionY() > 535) {
                        heroe.setPosicionX(oldX);
                        heroe.setPosicionY(oldY);
                        heroe.getSprite().setX(oldX);
                        heroe.getSprite().setY(oldY);
                    }

                    // Verificar salida
                    if (heroe.getSprite().getBoundsInParent().intersects(salida.getBoundsInParent())) {
                        cambiarDePiso(heroe, stage, root);
                    }
                }

                // Manejo visual de la invulnerabilidad (Parpadeo)
                if (now - ultimoDañoRecibido < TIEMPO_INVULNERABILIDAD) {
                    heroe.getSprite().setOpacity(0.4); // Se vuelve semi-transparente
                } else {
                    heroe.getSprite().setOpacity(1.0); // Normal
                }

                for (int i = 0; i < enemigos.size(); i++) {
                    Enemigo e = enemigos.get(i);
                    e.setPosicionX(e.getPosicionX() + 1);
                    e.getSprite().setX(e.getPosicionX());

                    if (e.getPosicionX() > 800) {
                        e.setPosicionX(-50);
                        e.setPosicionY(new Random().nextInt(500) + 50);
                        e.getSprite().setY(e.getPosicionY());
                    }

                    // LÓGICA DE DAÑO CON COOLDOWN
                    if (heroe.getSprite().getBoundsInParent().intersects(e.getSprite().getBoundsInParent())) {
                        if (now - ultimoDañoRecibido > TIEMPO_INVULNERABILIDAD) {
                            heroe.recibirDaño(20); // Daño más significativo
                            ultimoDañoRecibido = now;

                            if (heroe.getVida() <= 0) {
                                juegoTerminado = true;
                                mostrarPantallaFinal(root, "GAME OVER", Color.RED);
                                this.stop();
                            }
                        }
                    }
                }
            }
        };
        timer.start();

        stage.setOnCloseRequest(event -> {
            javafx.application.Platform.exit();
            System.exit(0);
        });

        stage.setScene(scene);
        stage.setTitle("Piso: " + pisoActual);
        stage.show();
    }

    private void cambiarDePiso(Personaje heroe, Stage stage, Pane root) {
        pisoActual--;
        if (pisoActual > 0) {
            heroe.setPosicionX(50);
            heroe.setPosicionY(50);
            heroe.getSprite().setX(50);
            heroe.getSprite().setY(50);
            generarEnemigos(root, new Random().nextInt(3) + 1);
            stage.setTitle("Piso: " + pisoActual);
        } else {
            juegoTerminado = true;
            mostrarPantallaFinal(root, "YOU WIN", Color.GOLD);
        }
    }

    private void generarEnemigos(Pane root, int cantidad) {
        for(Enemigo e : enemigos) root.getChildren().remove(e.getSprite());
        enemigos.clear();
        Random rand = new Random();
        for (int i = 0; i < cantidad; i++) {
            int x, y;
            do {
                x = rand.nextInt(700) + 50;
                y = rand.nextInt(500) + 50;
            } while (x > 270 && x < 530 && y > 200 && y < 400);
            Enemigo nuevo = new Enemigo("Fantasma " + i, x, y);
            nuevo.initGrafico();
            nuevo.getSprite().setFitWidth(45);
            nuevo.getSprite().setPreserveRatio(true);
            enemigos.add(nuevo);
            root.getChildren().add(nuevo.getSprite());
        }
    }

    private void mostrarPantallaFinal(Pane root, String mensaje, Color colorTexto) {
        Rectangle fondoFin = new Rectangle(0, 0, 800, 600);
        fondoFin.setFill(Color.BLACK);
        fondoFin.setOpacity(0.85);
        Text texto = new Text(mensaje);
        texto.setFont(Font.font("Verdana", 90));
        texto.setFill(colorTexto);
        texto.setX(150);
        texto.setY(320);
        root.getChildren().addAll(fondoFin, texto);
        fondoFin.toFront();
        texto.toFront();
    }

    public static void main(String[] args) { launch(); }
}