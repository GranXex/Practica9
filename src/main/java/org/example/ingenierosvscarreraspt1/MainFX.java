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

/**
 * Clase principal del juego IngenierosVsCarrerasPT1.
 *
 * Cambios para Práctica 10 (Persistencia):
 *  - Al iniciar: intenta cargar partida guardada desde "partida.txt".
 *    Si no existe, inicia partida nueva desde el principio.
 *  - Tecla G: guarda manualmente el estado actual (piso, héroe, enemigos).
 *  - Al cambiar de piso: guarda automáticamente la partida.
 *  - Al terminar el juego (Game Over / You Win): guarda el estado final.
 */
public class MainFX extends Application {

    // Ruta del archivo de guardado (se crea junto al .jar o en el directorio de trabajo)
    private static final String ARCHIVO_GUARDADO = "partida.txt";

    private int pisoActual = 2;
    private List<Enemigo> enemigos = new ArrayList<>();
    private Personaje heroe;
    private Pane root;
    private boolean juegoTerminado = false;
    private final Set<KeyCode> teclasPresionadas = new HashSet<>();
    private static final int PASO = 12;

    private long ultimoDañoRecibido = 0;
    private final long TIEMPO_INVULNERABILIDAD = 1_500_000_000L;

    @Override
    public void start(Stage stage) {
        root = new Pane();

        // 1. FONDO
        String rutaEscenario = getClass()
                .getResource("/org/example/ingenierosvscarreraspt1/escenario.png")
                .toExternalForm();
        ImageView fondo = new ImageView(new Image(rutaEscenario));
        fondo.setFitWidth(800);
        fondo.setFitHeight(600);

        // 2. COLISIONES
        Rectangle muroCentral = new Rectangle(285, 215, 230, 170);
        muroCentral.setFill(Color.TRANSPARENT);
        Rectangle salida = new Rectangle(680, 480, 80, 80);
        salida.setFill(Color.TRANSPARENT);

        // 3. HÉROE (valores por defecto; se sobreescriben si hay guardado)
        heroe = new Personaje("Miguel", 100, 100, 100);
        heroe.initGrafico();
        heroe.getSprite().setFitWidth(50);
        heroe.getSprite().setPreserveRatio(true);

        root.getChildren().addAll(fondo, muroCentral, salida, heroe.getSprite());

        // ----------------------------------------------------------------
        // R01 — Cargar partida guardada
        // ----------------------------------------------------------------
        GestorPersistencia.EstadoJuego estadoCargado =
                GestorPersistencia.cargarPartida(ARCHIVO_GUARDADO);

        if (estadoCargado != null) {
            pisoActual = estadoCargado.pisoActual;

            // Restaurar posición del héroe
            heroe.setPosicionX(estadoCargado.heroePosX);
            heroe.setPosicionY(estadoCargado.heroePosY);
            heroe.getSprite().setX(estadoCargado.heroePosX);
            heroe.getSprite().setY(estadoCargado.heroePosY);

            // Restaurar vida del héroe (aplicar el daño ya recibido)
            int dañoPrevio = 100 - estadoCargado.heroeVida;
            if (dañoPrevio > 0) heroe.recibirDaño(dañoPrevio);

            // Restaurar enemigos guardados
            restaurarEnemigos(estadoCargado);
            System.out.println("[Inicio] Partida cargada — Piso: " + pisoActual);
        } else {
            // Sin guardado: generar enemigos aleatorios
            generarEnemigos(root, new Random().nextInt(3) + 1);
            System.out.println("[Inicio] Nueva partida — Piso: " + pisoActual);
        }

        stage.setTitle("Piso: " + pisoActual);

        // ----------------------------------------------------------------
        // 4. INPUT
        // ----------------------------------------------------------------
        Scene scene = new Scene(root, 800, 600);

        scene.setOnKeyPressed(e -> {
            teclasPresionadas.add(e.getCode());

            if (e.getCode() == KeyCode.SPACE && !juegoTerminado) {
                heroe.atacar(enemigos, root);
            }

            // G → Guardar manualmente (R05)
            if (e.getCode() == KeyCode.G && !juegoTerminado) {
                GestorPersistencia.guardarPartida(
                        ARCHIVO_GUARDADO, pisoActual, heroe, enemigos);
                mostrarMensajeTemporal("Partida guardada  [G]", Color.LIGHTGREEN);
            }
        });

        scene.setOnKeyReleased(e -> teclasPresionadas.remove(e.getCode()));

        // ----------------------------------------------------------------
        // 5. GAME LOOP
        // ----------------------------------------------------------------
        long[] ultimoMovimiento = {0};
        final long INTERVALO = 16_000_000L;

        javafx.animation.AnimationTimer timer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                if (juegoTerminado) return;

                if (now - ultimoMovimiento[0] >= INTERVALO) {
                    ultimoMovimiento[0] = now;

                    int oldX = heroe.getPosicionX();
                    int oldY = heroe.getPosicionY();
                    int dx = 0, dy = 0;

                    if (teclasPresionadas.contains(KeyCode.UP))    dy -= PASO;
                    if (teclasPresionadas.contains(KeyCode.DOWN))  dy += PASO;
                    if (teclasPresionadas.contains(KeyCode.LEFT))  dx -= PASO;
                    if (teclasPresionadas.contains(KeyCode.RIGHT)) dx += PASO;

                    if (dx != 0 || dy != 0) {
                        heroe.setPosicionX(oldX + dx);
                        heroe.setPosicionY(oldY + dy);
                        heroe.getSprite().setX(heroe.getPosicionX());
                        heroe.getSprite().setY(heroe.getPosicionY());
                    }

                    if (heroe.getSprite().getBoundsInParent()
                            .intersects(muroCentral.getBoundsInParent())
                            || heroe.getPosicionX() < 15 || heroe.getPosicionX() > 735
                            || heroe.getPosicionY() < 15 || heroe.getPosicionY() > 535) {
                        heroe.setPosicionX(oldX);
                        heroe.setPosicionY(oldY);
                        heroe.getSprite().setX(oldX);
                        heroe.getSprite().setY(oldY);
                    }

                    if (heroe.getSprite().getBoundsInParent()
                            .intersects(salida.getBoundsInParent())) {
                        cambiarDePiso(heroe, stage, root);
                    }
                }

                // Parpadeo invulnerabilidad
                heroe.getSprite().setOpacity(
                        (now - ultimoDañoRecibido < TIEMPO_INVULNERABILIDAD) ? 0.4 : 1.0);

                // Mover enemigos y detectar colisión con héroe
                for (int i = 0; i < enemigos.size(); i++) {
                    Enemigo e = enemigos.get(i);
                    e.setPosicionX(e.getPosicionX() + 1);
                    e.getSprite().setX(e.getPosicionX());

                    if (e.getPosicionX() > 800) {
                        e.setPosicionX(-50);
                        e.setPosicionY(new Random().nextInt(500) + 50);
                        e.getSprite().setY(e.getPosicionY());
                    }

                    if (heroe.getSprite().getBoundsInParent()
                            .intersects(e.getSprite().getBoundsInParent())) {
                        if (now - ultimoDañoRecibido > TIEMPO_INVULNERABILIDAD) {
                            heroe.recibirDaño(20);
                            ultimoDañoRecibido = now;

                            if (heroe.getVida() <= 0) {
                                juegoTerminado = true;
                                // R05 — Guardar estado de derrota
                                GestorPersistencia.guardarPartida(
                                        ARCHIVO_GUARDADO, pisoActual, heroe, enemigos);
                                mostrarPantallaFinal("GAME OVER", Color.RED);
                                this.stop();
                            }
                        }
                    }
                }
            }
        };
        timer.start();

        // Guardar al cerrar la ventana
        stage.setOnCloseRequest(event -> {
            if (!juegoTerminado) {
                GestorPersistencia.guardarPartida(
                        ARCHIVO_GUARDADO, pisoActual, heroe, enemigos);
            }
            javafx.application.Platform.exit();
            System.exit(0);
        });

        stage.setScene(scene);
        stage.show();
    }

    // ---------------------------------------------------------------
    // Cambio de piso con autoguardado
    // ---------------------------------------------------------------
    private void cambiarDePiso(Personaje heroe, Stage stage, Pane root) {
        pisoActual--;
        if (pisoActual > 0) {
            heroe.setPosicionX(50);
            heroe.setPosicionY(50);
            heroe.getSprite().setX(50);
            heroe.getSprite().setY(50);
            generarEnemigos(root, new Random().nextInt(3) + 1);
            stage.setTitle("Piso: " + pisoActual);

            // R05 — Autoguardado al pasar de piso
            GestorPersistencia.guardarPartida(
                    ARCHIVO_GUARDADO, pisoActual, heroe, enemigos);
            System.out.println("[Auto-Guardado] Cambiaste al Piso " + pisoActual);
        } else {
            juegoTerminado = true;
            GestorPersistencia.guardarPartida(
                    ARCHIVO_GUARDADO, pisoActual, heroe, enemigos);
            mostrarPantallaFinal("YOU WIN", Color.GOLD);
        }
    }

    // ---------------------------------------------------------------
    // Generar enemigos nuevos (partida sin guardado)
    // ---------------------------------------------------------------
    private void generarEnemigos(Pane root, int cantidad) {
        for (Enemigo e : enemigos) root.getChildren().remove(e.getSprite());
        enemigos.clear();

        Random rand = new Random();
        for (int i = 0; i < cantidad; i++) {
            int x, y;
            do {
                x = rand.nextInt(700) + 50;
                y = rand.nextInt(500) + 50;
            } while (x > 270 && x < 530 && y > 200 && y < 400);

            Enemigo nuevo = new Enemigo("Fantasma" + i, x, y);
            nuevo.initGrafico();
            nuevo.getSprite().setFitWidth(45);
            nuevo.getSprite().setPreserveRatio(true);
            enemigos.add(nuevo);
            root.getChildren().add(nuevo.getSprite());
        }
    }

    // ---------------------------------------------------------------
    // Restaurar enemigos desde archivo (R01)
    // ---------------------------------------------------------------
    private void restaurarEnemigos(GestorPersistencia.EstadoJuego estado) {
        for (Enemigo e : enemigos) root.getChildren().remove(e.getSprite());
        enemigos.clear();

        for (int i = 0; i < estado.enemigosDatos.size(); i++) {
            int[] datos = estado.enemigosDatos.get(i);
            String nombre = estado.enemigosNombres.get(i);

            Enemigo e = new Enemigo(nombre, datos[0], datos[1]);
            e.initGrafico();
            e.getSprite().setFitWidth(45);
            e.getSprite().setPreserveRatio(true);

            // Aplicar daño previo si la vida guardada es menor a la base (2)
            int dañoPrevio = 2 - datos[2];
            if (dañoPrevio > 0) e.recibirDaño(dañoPrevio);

            enemigos.add(e);
            root.getChildren().add(e.getSprite());
        }
    }

    // ---------------------------------------------------------------
    // Pantalla final
    // ---------------------------------------------------------------
    private void mostrarPantallaFinal(String mensaje, Color colorTexto) {
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

    // ---------------------------------------------------------------
    // Mensaje temporal en pantalla (confirmar guardado con G)
    // ---------------------------------------------------------------
    private void mostrarMensajeTemporal(String mensaje, Color color) {
        Text texto = new Text(mensaje);
        texto.setFont(Font.font("Verdana", 18));
        texto.setFill(color);
        texto.setX(20);
        texto.setY(580);
        root.getChildren().add(texto);

        javafx.animation.PauseTransition pausa =
                new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
        pausa.setOnFinished(e -> root.getChildren().remove(texto));
        pausa.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
