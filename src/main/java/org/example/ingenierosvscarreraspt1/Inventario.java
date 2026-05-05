package org.example.ingenierosvscarreraspt1;
import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private int capacidadMaxima;
    private List<Inventariable> items;

    public Inventario(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.items = new ArrayList<>();
    }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public List<Inventariable> getItems() { return items; }

    public boolean agregarItem(Inventariable item) {
        if (items.size() < capacidadMaxima) {
            items.add(item);
            return true;
        } else {
            System.out.println("El inventario está lleno.");
            return false;
        }
    }

    public boolean eliminarItem(Inventariable item) {
        return items.remove(item);
    }

    public void listarItems() {
        System.out.println("--- Inventario (" + items.size() + "/" + capacidadMaxima + ") ---");
        if (items.isEmpty()) {
            System.out.println("El inventario está vacío.");
        } else {
            for (Inventariable item : items) {
                // Llamamos al método de la interfaz para mostrar el item
                item.registrar();
            }
        }
    }
}