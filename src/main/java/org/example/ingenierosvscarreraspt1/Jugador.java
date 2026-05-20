package org.example.ingenierosvscarreraspt1;

/**
 * Clase que representa al jugador principal del juego.
 * Extiende {@link Personaje} y añade atributos de experiencia,
 * arma equipada e inventario personal.
 */
public class Jugador extends Personaje {

    /** Puntos de experiencia acumulados. */
    private int nivelExperiencia;

    /** Arma actualmente equipada. */
    private Arma armaEquipada;

    /** Inventario personal del jugador. */
    private Inventario inventario;

    /**
     * Construye un jugador con los parámetros dados.
     *
     * @param nombre    Nombre del jugador.
     * @param posicionX Posición inicial X.
     * @param posicionY Posición inicial Y.
     */
    public Jugador(String nombre, int posicionX, int posicionY) {
        super(nombre, 100, posicionX, posicionY);
        this.nivelExperiencia = 0;
        this.armaEquipada     = Arma.espada();
        this.inventario       = new Inventario(8);
    }

    /** @return Nivel de experiencia del jugador. */
    public int getNivelExperiencia() { return nivelExperiencia; }

    /** @return Arma actualmente equipada. */
    public Arma getArmaEquipada()    { return armaEquipada; }

    /** @return Inventario del jugador. */
    public Inventario getInventario() { return inventario; }

    /**
     * Equipa un arma nueva.
     *
     * @param arma Arma a equipar.
     */
    public void equiparArma(Arma arma) {
        this.armaEquipada = arma;
        System.out.println("[Jugador] Arma equipada: " + arma.getNombre());
    }

    /**
     * Aumenta la experiencia del jugador al eliminar enemigos.
     *
     * @param puntos Puntos de experiencia a sumar.
     */
    public void ganarExperiencia(int puntos) {
        this.nivelExperiencia += puntos;
        System.out.println("[Jugador] Experiencia: " + nivelExperiencia);
    }

    @Override
    public void initGrafico() {
        // Reutiliza el sprite base de Personaje
        super.initGrafico();
    }
}
