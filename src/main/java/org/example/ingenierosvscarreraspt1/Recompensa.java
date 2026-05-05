package org.example.ingenierosvscarreraspt1;
public class Recompensa implements Inventariable {
    private String nombre;
    private int valor;
    private String tipo;

    public Recompensa(String nombre, int valor, String tipo) {
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public int getValor() { return valor; }
    public String getTipo() { return tipo; }

    @Override
    public void registrar() {
        System.out.println("[Recompensa] " + nombre + " (Valor: " + valor + " " + tipo + ")");
    }

    @Override
    public void borrar() {
        System.out.println("La recompensa '" + nombre + "' ha sido consumida o perdida.");
    }
}