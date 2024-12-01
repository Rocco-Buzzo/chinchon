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
    private static final long serialVersionUID = -8172177002048510710L;
    private Mazo mazo;
    private Descarte descarte = new Descarte();
    private Cola<Jugador> jugadores = new Cola<>();
    private Jugador jugadorActual;
    private Jugador ganador;
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
            top = new Top();
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
    public void guardarPartida(String nombreArchivo) throws RemoteException {
        Serializacion.guardarPartida(this, nombreArchivo, this.jugadores.frente().getNombre(), this.jugadores.fondo().getNombre());
        notificarObservadores(Eventos.PARTIDA_GUARDADA);
    }

    /**
     * Funcion que le permite al cerrar la ronda actual.
     *
     * @return true si puede terminar, false en caso contrario.
     */
    @Override
    public void cerrarRonda() throws RemoteException {
        if (jugadorActual.getMano().puedeCerrar()) {
            calcularPuntaje(jugadorActual);     // Ganador
            cambiarJugador();
            calcularPuntajePerdedor(jugadorActual); // Perdedor
            cantidadRondas++;
            if (puedeTerminar()) {
                terminarPartida();
                notificarObservadores(Eventos.PARTIDA_TERMINADA);
            } else {
                estadoPartida = EstadoPartida.JUGANDO;
                continuarPartida();
                notificarObservadores(Eventos.RONDA_TERMINADA);
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

    private void vaciarCartas() {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador jAux;
            jugadores.frente().vaciarCartas();
            jAux = jugadores.desencolar();
            jugadores.encolar(jAux);
        }
    }

    @Override
    public void intercambiarCartas(int n, int m) {
        jugadorActual.getMano().intercambiarCartas(n, m);
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
            } else if (this.jugadores.size() == 1 && !jugadores.frente().getNombre().equals(name)) {
                Jugador jugador = new Jugador(name);
                jugadores.encolar(jugador);
                notificarObservadores(Eventos.JUGADORES_MAXIMOS);
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
            for (Jugador jugador : jugadores) {
                jugador.setTurno(!jugador.isTurno()); // Toggle each player's turn status
            }

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
        if (jugadorActual.isTurno() && jugadorActual.getMano().getCartas().size() == 8) {
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
        if (jugadorActual.isTurno() && jugadorActual.getMano().getCartas().size() == 7) {
            jugadorActual.agregarCarta(mazo.sacar());
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
        for (Jugador j : jugadores) {
            if (j.getNombre().equals(nombreJugador)) {
                return j;
            }
        }
        return null;
    }

    /**
     * Funcion para robar una carta del Descarte.
     *
     * @return Carta robada.
     */
    @Override
    public void robarCartaDescarte() throws RemoteException {
        if (jugadorActual.isTurno() && jugadorActual.getMano().getCartas().size() == 7) {
            if (descarte.getTope() != null) {
                jugadorActual.agregarCarta(descarte.desapilar());
            }
        }
    }

    /**
     * Devulve la cantidad de rondas.
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
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    @Override
    public Jugador getGanador() {
        return ganador;
    }

    @Override
    public Jugador getJugadorActual() {
        for (Jugador jugador : jugadores) {
            if (jugador.isTurno()) {
                return jugador;
            }
        }
        return null; // Manejar adecuadamente si ningún jugador tiene el turno
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
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equals(jugadorName)) {
                jugador.getMano().ordenarCartaValor();
            }
        }
    }

    @Override
    public void ordenarCartasPalo(String jugadorName) {
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equals(jugadorName)) {
                jugador.getMano().ordenarCartaPalo();
            }
        }
    }

    /**
     * Funcion auxiliar para repartir cartas.
     */
    private void repartir() {
        for (int i = 0; i < 7; i++) {
            for (Jugador jugador : jugadores) {
                jugador.agregarCarta(mazo.sacar());
            }
        }
    }

    /**
     * Funcion auxiliar para sortear el turno.
     */
    private void sortearTurno() {
        Random random = new Random();
        for (Jugador j : jugadores) {
            j.setTurno(false);
        }
        int indice = random.nextInt(jugadores.size());
        jugadores.get(indice).setTurno(true);
        jugadorActual = jugadores.get(indice);
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
     * Funcion auxiliar para verificar si alguno de los jugadores supera la puntuacion
     *
     * @return true si alguno de los puntajes de los jugadores es mayor que la puntuacion maxima.
     */
    private boolean puedeTerminar() {
        for (Jugador j : jugadores) {
            if (j.getPuntaje() >= puntosMaximos) {
                return true;
            }
        }
        return false;
    }

    /**
     * Funcion para finalizar la partida. En el caso de que cualquiera de los jugadores tenga
     */
    private void terminarPartida() throws RemoteException {
        if (getJugadores().getFirst().getPuntaje() >= puntosMaximos) {
            ganador = getJugadores().getLast();
            top.agregarAlTop(ganador);
        } else if (getJugadores().getLast().getPuntaje() >= puntosMaximos) {
            ganador = getJugadores().getFirst();
            top.agregarAlTop(ganador);
        }
        estadoPartida = EstadoPartida.TERMINADA;
        notificarObservadores(Eventos.PARTIDA_TERMINADA);
    }
}
