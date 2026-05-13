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
 */
public class MainFX extends Application {

    private static final String ARCHIVO_GUARDADO = "partida.txt";

    private int pisoActual = 10;

    private List<Enemigo> enemigos = new ArrayList<>();

    private Personaje heroe;

    private Pane root;

    private boolean juegoTerminado = false;

    private final Set<KeyCode> teclasPresionadas =
            new HashSet<>();

    private static final int PASO = 12;

    private long ultimoDañoRecibido = 0;

    private final long TIEMPO_INVULNERABILIDAD =
            1_500_000_000L;

    // =========================================================
    // ARMAS
    // =========================================================

    private Utileria espada =
            Utileria.espada();

    private Utileria mazo =
            Utileria.mazo();

    private Utileria armaActual;

    @Override
    public void start(Stage stage) {

        root = new Pane();

        // =====================================================
        // FONDO
        // =====================================================

        String rutaEscenario = getClass()
                .getResource("/org/example/ingenierosvscarreraspt1/escenario.png")
                .toExternalForm();

        ImageView fondo =
                new ImageView(new Image(rutaEscenario));

        fondo.setFitWidth(800);
        fondo.setFitHeight(600);

        // =====================================================
        // COLISIONES
        // =====================================================

        Rectangle muroCentral =
                new Rectangle(285, 215, 230, 170);

        muroCentral.setFill(Color.TRANSPARENT);

        Rectangle salida =
                new Rectangle(680, 480, 80, 80);

        salida.setFill(Color.TRANSPARENT);

        // =====================================================
        // HEROE
        // =====================================================

        heroe = new Personaje(
                "Miguel",
                100,
                100,
                100);

        heroe.initGrafico();

        heroe.getSprite().setFitWidth(50);

        heroe.getSprite().setPreserveRatio(true);

        // ARMA INICIAL
        armaActual = espada;

        root.getChildren().addAll(
                fondo,
                muroCentral,
                salida,
                heroe.getSprite());

        // =====================================================
        // NUEVA PARTIDA
        // =====================================================

        generarEnemigos(
                root,
                new Random().nextInt(3) + 1);

        GestorPersistencia.guardarPartida(
                ARCHIVO_GUARDADO,
                pisoActual,
                heroe,
                enemigos);

        System.out.println(
                "[Inicio] Nueva partida — Piso: "
                        + pisoActual);

        stage.setTitle("Piso: " + pisoActual);

        // =====================================================
        // INPUT
        // =====================================================

        Scene scene =
                new Scene(root, 800, 600);

        scene.setOnKeyPressed(e -> {

            teclasPresionadas.add(
                    e.getCode());

            // =================================================
            // ATAQUE
            // =================================================

            if (e.getCode() == KeyCode.SPACE
                    && !juegoTerminado) {

                atacarConArma();
            }

            // =================================================
            // GUARDAR
            // =================================================

            if (e.getCode() == KeyCode.G
                    && !juegoTerminado) {

                GestorPersistencia.guardarPartida(
                        ARCHIVO_GUARDADO,
                        pisoActual,
                        heroe,
                        enemigos);

                mostrarMensajeTemporal(
                        "Partida guardada [G]",
                        Color.LIGHTGREEN);
            }

            // =================================================
            // CARGAR
            // =================================================

            if (e.getCode() == KeyCode.P
                    && !juegoTerminado) {

                GestorPersistencia.EstadoJuego estado =
                        GestorPersistencia.cargarPartida(
                                ARCHIVO_GUARDADO);

                if (estado != null) {

                    pisoActual =
                            estado.pisoActual;

                    heroe.setPosicionX(
                            estado.heroePosX);

                    heroe.setPosicionY(
                            estado.heroePosY);

                    heroe.getSprite().setX(
                            estado.heroePosX);

                    heroe.getSprite().setY(
                            estado.heroePosY);

                    heroe.setVida(
                            estado.heroeVida);

                    restaurarEnemigos(estado);

                    stage.setTitle(
                            "Piso: " + pisoActual);

                    mostrarMensajeTemporal(
                            "Partida cargada [P]",
                            Color.CYAN);

                } else {

                    mostrarMensajeTemporal(
                            "No hay guardado disponible",
                            Color.ORANGE);
                }
            }

            // =================================================
            // CAMBIAR A ESPADA
            // =================================================

            if (e.getCode() == KeyCode.DIGIT1) {

                armaActual = espada;

                mostrarMensajeTemporal(
                        "Arma actual: ESPADA",
                        Color.LIGHTBLUE);

                System.out.println(
                        "Arma cambiada a ESPADA");
            }

            // =================================================
            // CAMBIAR A MAZO
            // =================================================

            if (e.getCode() == KeyCode.DIGIT2) {

                armaActual = mazo;

                mostrarMensajeTemporal(
                        "Arma actual: MAZO",
                        Color.ORANGE);

                System.out.println(
                        "Arma cambiada a MAZO");
            }
        });

        scene.setOnKeyReleased(
                e -> teclasPresionadas.remove(
                        e.getCode()));

        // =====================================================
        // GAME LOOP
        // =====================================================

        long[] ultimoMovimiento = {0};

        final long INTERVALO = 16_000_000L;

        javafx.animation.AnimationTimer timer =
                new javafx.animation.AnimationTimer() {

                    @Override
                    public void handle(long now) {

                        if (juegoTerminado)
                            return;

                        if (now - ultimoMovimiento[0]
                                >= INTERVALO) {

                            ultimoMovimiento[0] = now;

                            int oldX =
                                    heroe.getPosicionX();

                            int oldY =
                                    heroe.getPosicionY();

                            int dx = 0;

                            int dy = 0;

                            if (teclasPresionadas.contains(
                                    KeyCode.UP))
                                dy -= PASO;

                            if (teclasPresionadas.contains(
                                    KeyCode.DOWN))
                                dy += PASO;

                            if (teclasPresionadas.contains(
                                    KeyCode.LEFT))
                                dx -= PASO;

                            if (teclasPresionadas.contains(
                                    KeyCode.RIGHT))
                                dx += PASO;

                            if (dx != 0 || dy != 0) {

                                heroe.setPosicionX(
                                        oldX + dx);

                                heroe.setPosicionY(
                                        oldY + dy);

                                heroe.getSprite().setX(
                                        heroe.getPosicionX());

                                heroe.getSprite().setY(
                                        heroe.getPosicionY());
                            }

                            if (heroe.getSprite()
                                    .getBoundsInParent()
                                    .intersects(
                                            muroCentral.getBoundsInParent())

                                    || heroe.getPosicionX() < 15
                                    || heroe.getPosicionX() > 735
                                    || heroe.getPosicionY() < 15
                                    || heroe.getPosicionY() > 535) {

                                heroe.setPosicionX(oldX);

                                heroe.setPosicionY(oldY);

                                heroe.getSprite().setX(oldX);

                                heroe.getSprite().setY(oldY);
                            }

                            if (heroe.getSprite()
                                    .getBoundsInParent()
                                    .intersects(
                                            salida.getBoundsInParent())) {

                                cambiarDePiso(
                                        heroe,
                                        stage,
                                        root);
                            }
                        }

                        // =================================================
                        // INVULNERABILIDAD
                        // =================================================

                        heroe.getSprite().setOpacity(
                                (now - ultimoDañoRecibido
                                        < TIEMPO_INVULNERABILIDAD)
                                        ? 0.4 : 1.0);

                        // =================================================
                        // ENEMIGOS
                        // =================================================

                        for (int i = 0;
                             i < enemigos.size();
                             i++) {

                            Enemigo e =
                                    enemigos.get(i);

                            e.setPosicionX(
                                    e.getPosicionX() + 1);

                            e.getSprite().setX(
                                    e.getPosicionX());

                            if (e.getPosicionX() > 800) {

                                e.setPosicionX(-50);

                                e.setPosicionY(
                                        new Random()
                                                .nextInt(500) + 50);

                                e.getSprite().setY(
                                        e.getPosicionY());
                            }

                            if (heroe.getSprite()
                                    .getBoundsInParent()
                                    .intersects(
                                            e.getSprite()
                                                    .getBoundsInParent())) {

                                if (now - ultimoDañoRecibido
                                        > TIEMPO_INVULNERABILIDAD) {

                                    heroe.recibirDaño(20);

                                    ultimoDañoRecibido = now;

                                    if (heroe.getVida() <= 0) {

                                        juegoTerminado = true;

                                        GestorPersistencia
                                                .guardarPartida(
                                                        ARCHIVO_GUARDADO,
                                                        pisoActual,
                                                        heroe,
                                                        enemigos);

                                        mostrarPantallaFinal(
                                                "GAME OVER",
                                                Color.RED);

                                        this.stop();
                                    }
                                }
                            }
                        }
                    }
                };

        timer.start();

        // =====================================================
        // CERRAR JUEGO
        // =====================================================

        stage.setOnCloseRequest(event -> {

            if (!juegoTerminado) {

                GestorPersistencia.guardarPartida(
                        ARCHIVO_GUARDADO,
                        pisoActual,
                        heroe,
                        enemigos);
            }

            javafx.application.Platform.exit();

            System.exit(0);
        });

        stage.setScene(scene);

        stage.show();
    }

    // =========================================================
    // ATAQUE CON ARMA
    // =========================================================

    private void atacarConArma() {

        int daño =
                armaActual.getDaño();

        List<Enemigo> eliminados =
                new ArrayList<>();

        for (Enemigo e : enemigos) {

            if (heroe.getSprite()
                    .getBoundsInParent()
                    .intersects(
                            e.getSprite()
                                    .getBoundsInParent())) {

                e.recibirDaño(daño);

                System.out.println(
                        armaActual.getNombre()
                                + " hizo "
                                + daño
                                + " de daño");

                if (e.getVida() <= 0) {

                    eliminados.add(e);

                    root.getChildren()
                            .remove(e.getSprite());

                    System.out.println(
                            e.getNombre()
                                    + " eliminado");
                }
            }
        }

        enemigos.removeAll(eliminados);
    }

    // =========================================================
    // CAMBIAR DE PISO
    // =========================================================

    private void cambiarDePiso(Personaje heroe,
                               Stage stage,
                               Pane root) {

        pisoActual--;

        if (pisoActual > 0) {

            heroe.setPosicionX(50);

            heroe.setPosicionY(50);

            heroe.getSprite().setX(50);

            heroe.getSprite().setY(50);

            generarEnemigos(
                    root,
                    new Random().nextInt(3) + 1);

            stage.setTitle(
                    "Piso: " + pisoActual);

            GestorPersistencia.guardarPartida(
                    ARCHIVO_GUARDADO,
                    pisoActual,
                    heroe,
                    enemigos);

            System.out.println(
                    "[Auto-Guardado] Cambiaste al Piso "
                            + pisoActual);

        } else {

            juegoTerminado = true;

            GestorPersistencia.guardarPartida(
                    ARCHIVO_GUARDADO,
                    pisoActual,
                    heroe,
                    enemigos);

            mostrarPantallaFinal(
                    "YOU WIN",
                    Color.GOLD);
        }
    }

    // =========================================================
    // GENERAR ENEMIGOS
    // =========================================================

    private void generarEnemigos(Pane root,
                                 int cantidad) {

        for (Enemigo e : enemigos)
            root.getChildren()
                    .remove(e.getSprite());

        enemigos.clear();

        Random rand = new Random();

        for (int i = 0; i < cantidad; i++) {

            int x, y;

            do {

                x = rand.nextInt(700) + 50;

                y = rand.nextInt(500) + 50;

            } while (x > 270
                    && x < 530
                    && y > 200
                    && y < 400);

            Enemigo nuevo =
                    new Enemigo(
                            "Fantasma" + i,
                            x,
                            y);

            nuevo.initGrafico();

            nuevo.getSprite().setFitWidth(45);

            nuevo.getSprite().setPreserveRatio(true);

            enemigos.add(nuevo);

            root.getChildren()
                    .add(nuevo.getSprite());
        }
    }

    // =========================================================
    // RESTAURAR ENEMIGOS
    // =========================================================

    private void restaurarEnemigos(
            GestorPersistencia.EstadoJuego estado) {

        for (Enemigo e : enemigos)
            root.getChildren()
                    .remove(e.getSprite());

        enemigos.clear();

        for (int i = 0;
             i < estado.enemigosDatos.size();
             i++) {

            int[] datos =
                    estado.enemigosDatos.get(i);

            String nombre =
                    estado.enemigosNombres.get(i);

            Enemigo e =
                    new Enemigo(
                            nombre,
                            datos[0],
                            datos[1]);

            e.initGrafico();

            e.getSprite().setFitWidth(45);

            e.getSprite().setPreserveRatio(true);

            int dañoPrevio =
                    2 - datos[2];

            if (dañoPrevio > 0)
                e.recibirDaño(dañoPrevio);

            enemigos.add(e);

            root.getChildren()
                    .add(e.getSprite());
        }
    }

    // =========================================================
    // PANTALLA FINAL
    // =========================================================

    private void mostrarPantallaFinal(
            String mensaje,
            Color colorTexto) {

        Rectangle fondoFin =
                new Rectangle(0, 0, 800, 600);

        fondoFin.setFill(Color.BLACK);

        fondoFin.setOpacity(0.85);

        Text texto = new Text(mensaje);

        texto.setFont(
                Font.font("Verdana", 90));

        texto.setFill(colorTexto);

        texto.setX(150);

        texto.setY(320);

        root.getChildren()
                .addAll(fondoFin, texto);

        fondoFin.toFront();

        texto.toFront();
    }

    // =========================================================
    // MENSAJE TEMPORAL
    // =========================================================

    private void mostrarMensajeTemporal(
            String mensaje,
            Color color) {

        Text texto = new Text(mensaje);

        texto.setFont(
                Font.font("Verdana", 18));

        texto.setFill(color);

        texto.setX(20);

        texto.setY(580);

        root.getChildren().add(texto);

        javafx.animation.PauseTransition pausa =
                new javafx.animation.PauseTransition(
                        javafx.util.Duration.seconds(2));

        pausa.setOnFinished(
                e -> root.getChildren().remove(texto));

        pausa.play();
    }

    public static void main(String[] args) {

        launch();
    }
}
