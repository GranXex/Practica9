# IngenierosVsCarrerasPT1

Juego de descenso de pisos desarrollado en Java 21 + JavaFX 21.  
El jugador debe eliminar enemigos (fantasmas y jefes) y llegar a la salida de cada piso para bajar del piso 10 al 1 y ganar la partida.

## Requisitos

- Java 21+
- Maven 3.8+

## Ejecución

```bash
mvn clean javafx:run
```

## Controles

| Tecla   | Acción                          |
|---------|---------------------------------|
| ↑↓←→   | Mover al jugador (diagonal OK)  |
| ESPACIO | Atacar con el arma equipada     |
| 1       | Equipar Espada (daño 1)         |
| 2       | Equipar Mazo (daño 2)           |
| 3       | Equipar Lanza (daño 1, alcance) |
| I       | Listar inventario (consola)     |
| G       | Guardar partida                 |
| P       | Cargar partida                  |

## Mecánicas

- **Enemigos**: fantasmas que se mueven automáticamente; en pisos ≤ 3 pueden aparecer **Jefes** con más vida y daño especial.
- **Obstáculos**: rocas que dañan al jugador al contacto y se destruyen.
- **Checkpoints**: al pisarlos guardan automáticamente el progreso y cambian de color.
- **Recompensas**: pociones que restauran HP; **Tesoros** que además otorgan experiencia.
- **Inventario**: capacidad 8. Se llena con armas equipadas y recompensas recogidas.
- **HUD**: barra inferior siempre visible con ❤ vida, piso, arma, inventario y EXP.

## Estructura del proyecto

```
src/main/java/org/example/ingenierosvscarreraspt1/
├── Interfaces
│   ├── ElementoDinamico.java   — mover(String, int)
│   ├── Destruible.java         — destruye()
│   └── Inventariable.java      — registrar() / borrar()
├── Modelo base
│   ├── Personaje.java          — clase base de personajes
│   ├── Obstaculo.java          — implementa Destruible
│   ├── CheckPoint.java
│   ├── Recompensa.java         — implementa Inventariable
│   ├── Arma.java               — implementa Inventariable
│   ├── Inventario.java
│   ├── Utileria.java           — implementa ElementoDinamico
│   └── Nivel.java              — gestiona elementos dinámicos
├── Extensiones
│   ├── Jugador.java            — extends Personaje
│   ├── Enemigo.java            — extends Personaje
│   ├── EnemigoJefe.java        — extends Enemigo
│   └── Tesoro.java             — extends Recompensa
├── Motor / Persistencia
│   └── GestorPersistencia.java
└── MainFX.java                 — Application JavaFX (punto de entrada)
```

## Diagrama de clases (resumen)

```
ElementoDinamico   Destruible   Inventariable
       ↑               ↑              ↑
   Personaje        Obstaculo       Arma
     ↑   ↑                       Recompensa
 Jugador  Enemigo                    ↑
              ↑                   Tesoro
          EnemigoJefe
```
# Pract9
