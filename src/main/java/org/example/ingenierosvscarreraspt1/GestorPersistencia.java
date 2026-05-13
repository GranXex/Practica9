package org.example.ingenierosvscarreraspt1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GestorPersistencia {


    public static void borrarPartida(String archivoGuardado) {

    }

    public static class EstadoJuego {
        public int pisoActual;
        public String nombrePersonaje;
        public int heroePosX;
        public int heroePosY;
        public int heroeVida;


        public List<int[]> enemigosDatos;    // posX, posY, vida
        public List<String> enemigosNombres; // nombre

        public EstadoJuego() {
            enemigosDatos    = new ArrayList<>();
            enemigosNombres  = new ArrayList<>();
        }
    }



    public static void guardarPartida(String nombreArchivo, int pisoActual, Personaje heroe, List<Enemigo> enemigos) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(nombreArchivo));

            // Guardar piso actual
            writer.write("Piso " + pisoActual);
            writer.newLine();

            // Guardar héroe: Personaje <nombre> <posX> <posY> <vida>
            writer.write("Personaje "
                    + heroe.getNombre()   + " "
                    + heroe.getPosicionX() + " "
                    + heroe.getPosicionY() + " "
                    + heroe.getVida());
            writer.newLine();

            // Guardar cada enemigo vivo: Enemigo <nombre> <posX> <posY> <vida>
            for (Enemigo e : enemigos) {
                writer.write("Enemigo "
                        + e.getNombre()    + " "
                        + e.getPosicionX() + " "
                        + e.getPosicionY() + " "
                        + e.getVida());
                writer.newLine();
            }

            System.out.println("[Guardado] Partida guardada en: " + nombreArchivo);

        } catch (IOException ex) {
            System.err.println("[Error] No se pudo guardar la partida: " + ex.getMessage());
        } finally {
            if (writer != null) {
                try { writer.close(); } catch (IOException ex) {
                    System.err.println("[Error] Al cerrar el archivo: " + ex.getMessage());
                }
            }
        }
    }


    public static EstadoJuego cargarPartida(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            System.out.println("[Carga] No se encontró archivo de guardado: " + nombreArchivo);
            return null;
        }

        EstadoJuego estado = new EstadoJuego();
        BufferedReader reader = null;
        int numLinea = 0;

        try {
            reader = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = reader.readLine()) != null) {
                numLinea++;
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split("\\s+");

                switch (partes[0]) {

                    case "Piso":
                        // Formato: Piso <pisoActual>
                        if (partes.length < 2) break;
                        estado.pisoActual = Integer.parseInt(partes[1]);
                        break;

                    case "Personaje":
                        // Formato: Personaje <nombre> <posX> <posY> <vida>
                        if (partes.length < 5) break;
                        estado.nombrePersonaje = partes[1];
                        estado.heroePosX = Integer.parseInt(partes[2]);
                        estado.heroePosY = Integer.parseInt(partes[3]);
                        estado.heroeVida = Integer.parseInt(partes[4]);
                        break;

                    case "Enemigo":
                        // Formato: Enemigo <nombre> <posX> <posY> <vida>
                        if (partes.length < 5) break;
                        estado.enemigosNombres.add(partes[1]);
                        estado.enemigosDatos.add(new int[]{
                                Integer.parseInt(partes[2]), // posX
                                Integer.parseInt(partes[3]), // posY
                                Integer.parseInt(partes[4])  // vida
                        });
                        break;

                    default:
                        System.err.println("[Carga] Línea " + numLinea
                                + ": tipo desconocido '" + partes[0] + "'");
                }
            }

            System.out.println("[Carga] Partida cargada desde: " + nombreArchivo
                    + " — Piso " + estado.pisoActual
                    + ", Héroe vida=" + estado.heroeVida
                    + ", Enemigos=" + estado.enemigosDatos.size());

        } catch (FileNotFoundException ex) {
            System.err.println("[Error] Archivo no encontrado: " + ex.getMessage());
            return null;
        } catch (NumberFormatException ex) {
            System.err.println("[Error] Número inválido en línea " + numLinea + ": " + ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.err.println("[Error] Leyendo archivo: " + ex.getMessage());
            return null;
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException ex) {
                    System.err.println("[Error] Al cerrar el archivo: " + ex.getMessage());
                }
            }
        }

        return estado;
    }
}
