package org.example.ingenierosvscarreraspt1;
import java.util.ArrayList;
import java.util.List;

public class Nivel {
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    private String nombre;
    private int numero;
    private String dificultad;
    private List<Obstaculo> obstaculos;
    private List<CheckPoint> checkPoints;
    private List<ElementoDinamico> elementosDinamicos;
    private Inventario inventario;

    public Nivel(String nombre, int numero, String dificultad, Inventario inventario) {
        this.nombre = nombre;
        this.numero = numero;
        this.dificultad = dificultad;
        this.inventario = inventario;

        this.obstaculos = new ArrayList<>();
        this.checkPoints = new ArrayList<>();
        this.elementosDinamicos = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public int getNumero() { return numero; }
    public String getDificultad() { return dificultad; }

    public void agregarObstaculo(Obstaculo obstaculo) {
        this.obstaculos.add(obstaculo);
    }

    public void agregarCheckPoint(CheckPoint checkPoint) {
        this.checkPoints.add(checkPoint);
    }

    public void agregarElementoDinamico(ElementoDinamico elemento) {
        this.elementosDinamicos.add(elemento);
    }

    public void moverElementosDinamicos() {
        String[] direcciones = {"Norte", "Sur", "Este", "Oeste"};
        java.util.Random random = new java.util.Random();

        for (ElementoDinamico elemento : elementosDinamicos) {
            String direccionAleatoria = direcciones[random.nextInt(direcciones.length)];
            int distanciaAleatoria = random.nextInt(5) + 1;

            elemento.mover(direccionAleatoria, distanciaAleatoria);
        }
    }

    public void mostrarEstado() {
        System.out.println(ANSI_YELLOW + "--- Estado del Nivel " + numero + " ---" + ANSI_RESET);
        System.out.println("Nombre del Nivel: " + nombre);
        System.out.println("Dificultad: " + dificultad);
        System.out.println("Obstáculos: " + obstaculos.size());
        System.out.println("CheckPoints: " + checkPoints.size());
        System.out.println("Elementos Dinámicos: " + elementosDinamicos.size());
    }
}