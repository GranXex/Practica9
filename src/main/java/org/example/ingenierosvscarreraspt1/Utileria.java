    package org.example.ingenierosvscarreraspt1;
    public class Utileria implements ElementoDinamico {
        private String nombre;
        private String descripcion;
        private int posicionX;
        private int posicionY;
        private boolean usada;

        public Utileria(String nombre, String descripcion, int posicionX, int posicionY) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.posicionX = posicionX;
            this.posicionY = posicionY;
            this.usada = false;
        }

        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
        public int getPosicionX() { return posicionX; }
        public int getPosicionY() { return posicionY; }
        public boolean isUsada() { return usada; }

        public void usar() {
            this.usada = true;
            System.out.println("Se usó: " + nombre);
        }

        public void espada(){

        }

        @Override
        public void mover(String direccion, int distancia) {
            System.out.println(nombre + " se mueve hacia " + direccion + " " + distancia + " unidades.");
        }
    }