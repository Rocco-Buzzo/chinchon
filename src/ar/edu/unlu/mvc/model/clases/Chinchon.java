package ar.edu.unlu.mvc.model.clases;

import ar.edu.unlu.mvc.controller.Controlador;
import ar.edu.unlu.mvc.model.enumerates.EstadoPartida;
import ar.edu.unlu.mvc.model.enumerates.Eventos;
import ar.edu.unlu.mvc.model.interfaces.IChinchon;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import ar.edu.unlu.utilities.Cola;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class Chinchon extends ObservableRemoto implements IChinchon {
    @Serial
    private static final long serialVersionUID = 2L;
    private Mazo mazo;
    private Descarte descarte = new Descarte();
    private Cola<Jugador> jugadores = new Cola<>();
    private Jugador jugadorActual;
    private Jugador ganador = new Jugador("");
    private Top top = new Top();
    private int puntosMaximos = 0;
    private int cantidadRondas = 1;
    private boolean contieneComodin = false;
    private int cantCartas = 0;
    private EstadoPartida estadoPartida = EstadoPartida.ESTABLECIENDO;

    /**
     * Funcion para Iniciar el juego.
     */
    @Override
    public void iniciarPartida() throws RemoteException {
        if (jugadores.size() == 2) {
            mazo = new Mazo(contieneComodin, cantCartas);
            sortearTurno();
            repartir();
            descarte.apilar(mazo.sacar());
            estadoPartida = EstadoPartida.JUGANDO;
            notificarObservadores(Eventos.PARTIDA_INICIADA);
        }
    }

    @Override
    public void establecerValores(boolean contiene, int cantidad, int puntos) {
        contieneComodin = contiene;
        cantCartas = cantidad;
        puntosMaximos = puntos;
    }

    /**
     * Funcion que permite cargar una partida guardada.
     *
     * @param nombreArchivo Partida guardada.
     * @return true si se puede cargar, en otro caso false.
     */
    @Override
    public boolean cargarPartida(String nombreArchivo) throws RemoteException {
        IChinchon iJuego = Serializacion.cargarPartida(nombreArchivo);
        if (iJuego != null) {
            descarte = iJuego.getDescarte();
            mazo = iJuego.getMazo();
            jugadores = iJuego.getJugadores();
            ganador = iJuego.getGanador();
            cantidadRondas = iJuego.getCantidadRondas();
            jugadorActual = iJuego.getJugadorActual();
            estadoPartida = iJuego.getEstadoDePartida();
            puntosMaximos = iJuego.getPuntosMaximos();
            contieneComodin = iJuego.getMazo().isContieneComodin();
            top = getTop();
            estadoPartida = EstadoPartida.JUGANDO;
            notificarObservadores(Eventos.PARTIDA_CARGADA);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Funcion que permite guardar una partida. Se ingresa el nombre del archivo y se guarda la Instancia del juego actual.
     *
     * @param nombreArchivo - Nombre de la partida.
     */
    @Override
    public void guardarPartida(String nombreArchivo, boolean guardar) throws RemoteException {
        if (guardar) {
            Serializacion.guardarPartida(this, nombreArchivo);
            notificarObservadores(Eventos.PARTIDA_GUARDADA);
        } else {
            notificarObservadores(Eventos.PARTIDA_CANCELADA);
        }
    }

    /**
     * Funcion que le permite al cerrar la ronda actual.
     */
    @Override
    public void cerrarRonda() throws RemoteException {
        if (jugadorActual.getMano().puedeCerrar()) {
            cantidadRondas++;
            calcularPuntaje(jugadorActual);     // Ganador
            calcularPuntajePerdedor(jugadores.getFondo()); // Perdedor
            if (puedeTerminar()) {
                terminarPartida();
                notificarObservadores(Eventos.PARTIDA_TERMINADA);
            } else {
                estadoPartida = EstadoPartida.JUGANDO;
                notificarObservadores(Eventos.RONDA_TERMINADA);
                continuarPartida();
            }
        }
    }

    /**
     * Funcion que permite continuar la partida.
     */
    @Override
    public void continuarPartida() throws RemoteException {
        // Limpiar cartas de los jugadores
        vaciarCartas();

        // Limpiar mazo y descarte
        mazo = new Mazo(getMazo().isContieneComodin(), getMazo().getSizeInicial());
        descarte = new Descarte();
        // Reiniciar turno
        sortearTurno();
        // Repartir cartas nuevamente
        repartir();
        // Poner carta en el descarte
        descarte.apilar(mazo.sacar());
    }

    @Override
    public ArrayList<String> nombreJugadores() throws RemoteException {
        ArrayList<String> players = new ArrayList<>();
        Jugador actual = jugadores.getFrente();
        while (actual != null) {
            if (players.size() == 2) {
                break;
            }
            players.add(actual.getNombre());
            actual = jugadores.getFondo();
        }
        return players;
    }

    private void vaciarCartas() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jAux;
            jugadores.getFrente().vaciarCartas();
            jAux = jugadores.desencolar();
            jugadores.encolar(jAux);
        }
    }

    @Override
    public void intercambiarCartas(int n, int m, String jugador) throws RemoteException {
        getJugador(jugador).getMano().intercambiarCartas(n, m);
    }

    /**
     * Funcion para girar el mazo, cuando no haya mas cartas en el mazo apilamos todas las cartas del descarte y la ponemos en el mazo.
     */
    private void girarMazo() {
        if (mazo.getSize() == 0) {
            while (descarte.getTope() != null) {
                mazo.apilar(descarte.desapilar());
            }
            descarte.apilar(mazo.sacar());
        }
    }

    /**
     * Funcion para agregar un jugador, a partir de un nombre, crea un nuevo jugador con ese nombre y lo agrega a la partida.
     *
     * @param name Nombre del nuevo jugador.
     */
    @Override
    public void agregarJugador(String name) throws RemoteException {
        if (estadoPartida == EstadoPartida.ESTABLECIENDO) {
            if (this.jugadores.estaVacia()) {
                Jugador jugador = new Jugador(name);
                jugadores.encolar(jugador);
            } else if (this.jugadores.size() == 1 && !jugadores.getFrente().getNombre().equals(name)) {
                Jugador jugador = new Jugador(name);
                jugadores.encolar(jugador);
            } else {
                notificarObservadores(Eventos.MISMO_NOMBRE);
            }
        }
    }

    /**
     * Funcion que cambia el turno del jugador.
     */
    @Override
    public void cambiarJugador() throws RemoteException {
        if (this.estadoPartida == EstadoPartida.JUGANDO) {
            Jugador jAux;
            jAux = jugadores.desencolar();
            jugadores.encolar(jAux);

            // Update jugadorActual to the player with turno = true
            jugadorActual = getJugadorActual();

            // Notify observers of the turn change
            notificarObservadores(Eventos.CAMBIO_TURNO);
        }
    }

    /**
     * Funcion que permite tirar una carta, recibe un numero como parametro y tira la carta que corresponde.
     *
     * @param numero Posicion de la carta en la mano.
     */
    @Override
    public void tirarCarta(int numero) throws RemoteException {
        if (jugadorActual.getMano().getCartas().size() == 8) {
            Carta cartaAtirar = jugadorActual.tirarCarta(numero);
            for (Carta c : jugadorActual.getMano().getCartas()) {
                if (c.equals(cartaAtirar)) {
                    jugadorActual.getMano().getCartas().remove(cartaAtirar);
                    break;
                }
            }
            descarte.apilar(cartaAtirar);
            cambiarJugador();
        }
    }

    /**
     * Funcion para robar una carta del mazo.
     */
    @Override
    public void robarCartaMazo() throws RemoteException {
        girarMazo();
        if (jugadorActual.getMano().getCartas().size() == 7) {
            jugadorActual.agregarCarta(mazo.sacar());
            notificarObservadores(Eventos.CARTA_ROBADA);
        }
    }

    @Override
    public int getCartaPosition(Carta carta) {
        ArrayList<Carta> cartas = jugadorActual.getMano().getCartas();
        int index = 1;
        for (Carta c : cartas) {
            if (carta.getPalo() == c.getPalo() && carta.getValor() == c.getValor()) {
                return index;
            } else {
                index++;
            }
        }
        return 0;
    }

    @Override
    public Jugador getJugador(String nombreJugador) throws RemoteException {
        if (jugadorActual.getNombre().equals(nombreJugador)) {
            return jugadorActual;
        } else {
            return jugadores.getFondo();
        }
    }

    @Override
    public int getPuntajeJugador(String nombre) throws RemoteException {
        Jugador actual = jugadores.getFrente();
        while (actual != null) {
            // Aplica tu función aquí
            if (actual.getNombre().equals(nombre)) {
                return actual.getPuntaje();
            }
            actual = jugadores.getFondo();
        }
        return 0;
    }

    /**
     * Funcion para robar una carta del Descarte.
     */
    @Override
    public void robarCartaDescarte() throws RemoteException {
        if (jugadorActual.getMano().getCartas().size() == 7) {
            if (descarte.getTope() != null) {
                jugadorActual.agregarCarta(descarte.desapilar());
                notificarObservadores(Eventos.CARTA_ROBADA);
            }
        }
    }

    /**
     * Devuelve la cantidad de rondas.
     *
     * @return Cantidad de rondas.
     */
    @Override
    public int getCantidadRondas() {
        return cantidadRondas;
    }

    @Override
    public EstadoPartida getEstadoDePartida() {
        return estadoPartida;
    }

    @Override
    public Top getTop() {
        return Serializacion.cargarTop();
    }

    @Override
    public Cola<Jugador> getJugadores() {
        return jugadores;
    }

    @Override
    public Jugador getGanador() {
        return ganador;
    }

    @Override
    public Jugador getJugadorActual() {
        return jugadores.getFrente();
    }

    @Override
    public Mazo getMazo() {
        return mazo;
    }

    @Override
    public Descarte getDescarte() {
        return descarte;
    }

    @Override
    public Carta getTopeDescarte() {
        return descarte.getTope();
    }

    @Override
    public void agregarObs(Controlador controlador) throws RemoteException {
        agregarObservador(controlador);
    }

    @Override
    public int getPuntosMaximos() {
        return puntosMaximos;
    }

    @Override
    public void ordenarCartasValor(String jugadorName) {
        Jugador actual = jugadores.getFrente();
        while (actual != null) {
            // Aplica tu función aquí
            if (actual.getNombre().equals(jugadorName)) {
                actual.getMano().ordenarCartaValor();
                return;
            }
            actual = jugadores.getFondo();
        }
    }

    @Override
    public void ordenarCartasPalo(String jugadorName) {
        Jugador actual = jugadores.getFrente();
        while (actual != null) {
            // Aplica tu función aquí
            if (actual.getNombre().equals(jugadorName)) {
                actual.getMano().ordenarCartaPalo();
                return;
            }
            actual = jugadores.getFondo();
        }
    }

    /**
     * Funcion auxiliar para repartir cartas.
     */
    private void repartir() {
        for (int i = 0; i < 7; i++) { // Repartir 7 cartas a cada jugador
            Jugador actual; // Obtener el primer jugador

            for (int j = 0; j < jugadores.size(); j++) {
                actual = jugadores.getFrente(); // Iterar sobre todos los jugadores
                actual.getMano().agregarCarta(mazo.sacar()); // Dar carta al jugador actual
                jugadores.moverAlFondo(); // Pasar al siguiente jugador en la cola
            }
        }
    }

    /**
     * Funcion auxiliar para sortear el turno.
     */
    private void sortearTurno() {
        Random random = new Random();
        int rotaciones = random.nextInt(jugadores.size());
        for (int i = 0; i < rotaciones; i++) {
            jugadores.encolar(jugadores.desencolar());
        }
        jugadorActual = jugadores.getFrente();
    }

    /**
     * Funcion que permite calcular puntaje de un jugador en especifico.
     */
    private void calcularPuntaje(Jugador jugador) {
        int suma = jugador.getMano().calcularPuntajeGanador();
        jugador.aumentarPuntuacion(suma);
    }

    /**
     * Calcula los puntos que perdio el jugador que perdio la ronda.
     *
     * @param jugador Jugador que perdio
     */
    private void calcularPuntajePerdedor(Jugador jugador) {
        int suma = jugador.getMano().calcularPuntajePerdedor();
        jugador.aumentarPuntuacion(suma);
    }

    /**
     * Verifica si alguno de los jugadores supera la puntuación máxima.
     *
     * @return true si alguno de los jugadores tiene un puntaje mayor o igual a la puntuación máxima, false en caso contrario.
     */
    private boolean puedeTerminar() {
        Jugador actual = jugadores.getFrente(); // Itera desde el frente de la cola
        for (int i = 0; i < jugadores.size(); i++) {
            if (actual.getPuntaje() >= puntosMaximos) {
                return true; // Termina si encuentra un jugador que supera el límite
            }
            actual = jugadores.getFondo(); // Avanza al siguiente jugador
        }
        return false; // Ningún jugador supera el límite
    }


    /**
     * Funcion para finalizar la partida. En el caso de que cualquiera de los jugadores tenga
     */
    private void terminarPartida() throws RemoteException {
        Jugador actual = jugadores.getFrente();
        while (actual != null) {
            if (actual.getPuntaje() >= puntosMaximos) {
                ganador = actual;
                top.agregarAlTop(ganador);
                break;
            }
            actual = jugadores.getFondo();
        }
        estadoPartida = EstadoPartida.TERMINADA;
        notificarObservadores(Eventos.PARTIDA_TERMINADA);
    }
}
