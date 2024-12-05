package ar.edu.unlu.mvc.model.clases;

import ar.edu.unlu.mvc.model.enumerates.Palo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Mano implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;
    private final ArrayList<Carta> cartas;

    /**
     * Constructor de la clase Mano.
     */
    public Mano() {
        cartas = new ArrayList<>(7);
    }

    /**
     * Agrega una carta a la mano. Si es que la mano tiene menos de 7 cartas
     * (durante el reparto inicial) entonces la agrega directamente. Si tiene ya
     * las 7 cartas, entonces se agrega como una carta "extra".
     *
     * @param c La carta a agregar a la mano.
     */
    public void agregarCarta(Carta c) {
        if (c != null && cartas.size() <= 7) {
            cartas.add(c);
        }
    }

    /**
     * Verifica si la carta es indice dentro de la mano.
     *
     * @param n Posicion de la carta.
     * @return true o false.
     */
    private boolean esIndice(int n) {
        return n >= 0 && n <= 7;
    }

    /**
     * Devuelve la carta dada su posición en la mano.
     *
     * @param n Índice de la carta.
     * @return La carta seleccionada.
     */
    public Carta getCarta(int n) {
        n--;
        if (esIndice(n)) {
            return cartas.get(n);
        } else {
            return null;
        }
    }

    /**
     * Getter de carta.
     *
     * @return Cartas.
     */
    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    /**
     * Intercambia de lugar dos cartas, dadas sus posiciones.
     *
     * @param i Posición de la primera carta. (1-8)
     * @param j Posición de la segunda carta. (1-8)
     */
    public void intercambiarCartas(int i, int j) {
        if (i != j) {
            int max = Math.max(i, j);
            int min = (max == i) ? j : i;
            max--;
            min--;
            if (esIndice(max) && (esIndice(min))) {
                Carta tmp = cartas.get(min);
                cartas.set(min, cartas.get(max));
                cartas.set(max, tmp);
            }
        }
    }

    /**
     * Indica si las cartas dadas (sus posiciones en la mano) tienen el mismo
     * valor numérico, con lo cual forman un juego. Son necesarias 3 cartas
     * como mínimo para el juego, y 4 como máximo.
     *
     * @param indices Las posiciones de las cartas en la mano.
     * @return Si forman el juego de cartas con el mismo valor numérico.
     */
    private boolean iguales(int[] indices) {
        if (indices.length == 3 || indices.length == 4) {
            boolean hayComodin = false;
            int valor = 0; // Valor en común entre todas las cartas para formar

            Carta c;
            for (int indice : indices) {
                c = cartas.get(indice);

                // La carta es un comodín?:
                if (c.getPalo() == Palo.JOKER) {
                    if (hayComodin) {
                        // Solo se permite un (1) comodín por juego.
                        return false;
                    }
                    hayComodin = true;
                } else {
                    if (valor == 0) {
                        // Pongo el valor de la primer carta (no comodín) que encuentre.
                        valor = c.getValor();
                    } else {
                        if (c.getValor() != valor) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Indica si las cartas dadas (sus posiciones en la mano) tienen el mismo
     * palo, con lo cual forman un juego. Son necesarias 3 cartas como mínimo
     * para el juego.
     *
     * @param indices Las posiciones de las cartas en la mano.
     * @return Si forman el juego de cartas con el mismo palo.
     */
    private boolean escalera(int[] indices) {
        if (indices.length > 2 && indices.length < 8) {
            // Agrego las cartas seleccionadas en un arreglo y los ordeno
            ArrayList<Carta> tmp = new ArrayList<>(indices.length);
            Carta c;
            boolean hayComodin = false;

            for (int indice : indices) {

                c = cartas.get(indice);

                // Filtro los comodines, si hay más de uno devuelvo falso:
                if (c.getPalo() == Palo.JOKER) {
                    if (hayComodin) {
                        return false;
                    }
                    hayComodin = true;
                } else {
                    tmp.add(c);
                }
            }

            c = tmp.getFirst();
            // Palo en común que deben tener todas las cartas del juego:
            Palo paloJuego = c.getPalo();
            int valorAnt = c.getValor(),    // Valor de la carta anterior
                    tmpSize = tmp.size();
            boolean aplicaComodin = hayComodin;

            for (int i = 1; i < tmpSize; i++) {
                c = tmp.get(i);
                if (c.getPalo() != paloJuego) {
                    // Si el palo es distinto no hay juego
                    return false;
                }
                if (c.getValor() != valorAnt + 1) {
                    if (aplicaComodin && (c.getValor() == valorAnt + 2)) {
                        aplicaComodin = false;
                    } else {
                        return false;
                    }
                }
                valorAnt = c.getValor();
            }
            return true;
        }
        return false;
    }

    /**
     * Indica si las cartas dadas forman, chinchon, dos grupos de tres o un grupo de tres cartas y otro de cuatro.
     *
     * @return true si es posible, false si no.
     */
    public boolean puedeCerrar() {
        if (esChinchon()) {
            return true;
        } else {
            if (verificarGrupos(3,4, false)) {
                return true;
            } else if (verificarGrupos(4,3, false)) {
                return true;
            } else return verificarGrupos(3,3, true);
        }
    }

    private boolean verificarGrupos(int tamGrupo1, int tamGrupo2, boolean verificarCartaExtra) {
        // Obtener las posiciones de las cartas en la mano del jugador
        ArrayList<Integer> posiciones = new ArrayList<>();
        for (int i = 0; i < cartas.size(); i++) {
            posiciones.add(i);
        }

        // Verificar los grupos
        for (int i = 0; i <= posiciones.size() - tamGrupo1; i++) {
            for (int j = i + tamGrupo1; j <= posiciones.size() - tamGrupo2; j++) {
                int[] grupo1 = new int[tamGrupo1];
                int[] grupo2 = new int[tamGrupo2];

                for (int k = 0; k < tamGrupo1; k++) {
                    grupo1[k] = posiciones.get(i + k);
                }

                for (int k = 0; k < tamGrupo2; k++) {
                    grupo2[k] = posiciones.get(j + k);
                }

                if ((iguales(grupo1) && iguales(grupo2)) ||
                        (escalera(grupo1) && iguales(grupo2)) ||
                        (iguales(grupo1) && escalera(grupo2)) ||
                        (escalera(grupo1) && escalera(grupo2))) {

                    if (verificarCartaExtra) {
                        if (posiciones.size() > j + tamGrupo2 && cartas.get(posiciones.get(j + tamGrupo2)).getValor() <= 5) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Si el jugador que cierra la mano lo hace ligando sus 7 cartas,
     * obtiene el premio de restar puntos:
     *
     * @return Premio del Jugador
     */
    public int calcularPuntajeGanador() {
        if (esChinchon()) {
            return switch (cantidadComodines()) {
                case 2 -> -25;
                case 1 -> -50;
                default -> -100;
            };
        }

        if (verificarGrupos(3, 4, false) || verificarGrupos(4, 3, false)) {
            return -10;
        }

        // Verificar dos grupos de tres cartas y una séptima carta con valor <= 5
        if (verificarGrupos(3, 3, true)) {
            if (cartas.getLast().getValor() <= 5) {
                return cartas.getLast().getValor();
            }
        }

        return 0;
    }

    /**
     * Bajar combinaciones válidas de la mano del jugador y sumar puntos de las cartas restantes.
     *
     * @return Los puntos de las cartas restantes que no forman combinación.
     */
    public int calcularPuntajePerdedor() {
        ArrayList<Carta> combinacionesBajadas = new ArrayList<>();
        int puntosRestantes = 0;

        // Bajar combinaciones de 3 cartas
        bajarCombinacionesDeN(3, combinacionesBajadas);

        // Bajar combinaciones de 4 cartas
        bajarCombinacionesDeN(4, combinacionesBajadas);

        // Bajar combinaciones de 4 cartas
        bajarCombinacionesDeN(5, combinacionesBajadas);

        // Sumar puntos de las cartas restantes
        for (Carta carta : cartas) {
            if (!combinacionesBajadas.contains(carta)) {
                puntosRestantes += carta.getValor();
            }
        }

        return puntosRestantes;
    }

    /**
     * Bajar combinaciones válidas de N cartas.
     *
     * @param n                    Número de cartas en la combinación.
     * @param combinacionesBajadas Lista de cartas que forman combinaciones.
     */
    private void bajarCombinacionesDeN(int n, ArrayList<Carta> combinacionesBajadas) {
        for (int i = 0; i <= cartas.size() - n; i++) {
            int[] grupo = new int[n];
            for (int j = 0; j < n; j++) {
                grupo[j] = i + j;
            }

            if (iguales(grupo) || escalera(grupo)) {
                for (int j = 0; j < n; j++) {
                    combinacionesBajadas.add(cartas.get(i + j));
                }
                i += n - 1; // Saltar al siguiente conjunto de cartas
            }
        }
    }

    /**
     * Calcula la cantidad de comodines
     *
     * @return Cantidad de comodines
     */
    private int cantidadComodines() {
        int acum = 0;
        for (Carta c : cartas) {
            if (c.isComodin()) {
                acum++;
            }
        }
        return acum;
    }

    /**
     * Indica si las cartas de la mano formán chinchón. Se forma chinchón cuando
     * las 7 cartas son del mismo palo y además tienen valores numéricos
     * consecutivos. Ejemplo: 1, 2, 3, 4, 5, 6, 7 de Oro.
     * Se permite un máximo de 2 comodines, pero no pueden estar consecutivos.
     *
     * @return Si hay chinchón en la mano,
     */
    private boolean esChinchon() {
        ArrayList<Carta> tmp = new ArrayList<>(cartas);
        Palo p = null;
        int comodines = 0;
        // Contamos comodines y determinamos el palo de las cartas no comodines
        for (Carta carta : tmp) {
            if (carta.getPalo() == Palo.JOKER) {
                comodines++;
            } else {
                if (p == null) {
                    p = carta.getPalo();
                } else if (carta.getPalo() != p) {
                    return false;
                }
            }
        }
        if (p == null) {
            // Si todas las cartas son comodines
            return true;
        }
        // Verificamos la secuencia con los comodines, asegurando que no haya comodines consecutivos
        int comodinesNecesitados = 0;
        boolean ultimaComodin = false;
        for (int i = 1; i < tmp.size(); i++) {
            Carta current = tmp.get(i);
            Carta previous = tmp.get(i - 1);
            if (current.getPalo() == Palo.JOKER) {
                if (ultimaComodin) {
                    return false; // No se permiten dos comodines consecutivos
                }
                ultimaComodin = true;
                continue;
            }
            ultimaComodin = false;
            if (current.getValor() != previous.getValor() + 1) {
                comodinesNecesitados += (current.getValor() - previous.getValor() - 1);
            }
            if (comodinesNecesitados > comodines) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ordena carta por Palo.
     */
    public void ordenarCartaPalo() {
        ArrayList<Carta> todasLasCartas = new ArrayList<>(cartas);
        todasLasCartas.sort(Comparator.comparing(Carta::getPalo).thenComparing(Carta::getValor));

        cartas.clear();
        cartas.addAll(todasLasCartas);
    }

    /**
     * Ordena carta por Valor.
     */
    public void ordenarCartaValor() {
        ArrayList<Carta> todasLasCartas = new ArrayList<>(cartas);
        todasLasCartas.sort(Comparator.comparing(Carta::getValor));

        cartas.clear();
        cartas.addAll(todasLasCartas);
    }

    public void vaciar() {
        cartas.clear();
    }
}
