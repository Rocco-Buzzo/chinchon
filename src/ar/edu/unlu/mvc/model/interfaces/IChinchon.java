package ar.edu.unlu.mvc.model.interfaces;

import ar.edu.unlu.mvc.model.clases.*;
import ar.edu.unlu.mvc.model.enumerates.EstadoPartida;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.utilities.Cola;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IChinchon extends Serializable, IObservableRemoto {
    void iniciarPartida() throws RemoteException;

    void establecerValores(boolean contiene, int cantidad, int puntos) throws RemoteException;

    boolean cargarPartida(String nombreArchivo) throws RemoteException;

    void guardarPartida(String nombreArchivo, boolean guardar) throws RemoteException;

    void cerrarRonda() throws RemoteException;

    void continuarPartida() throws RemoteException;

    ArrayList<String> nombreJugadores() throws RemoteException;

    void intercambiarCartas(int n, int m, String jugador) throws RemoteException;

    void agregarJugador(String name) throws RemoteException;

    void cambiarJugador() throws RemoteException;

    void tirarCarta(int numero) throws RemoteException;

    void robarCartaMazo() throws RemoteException;

    void robarCartaDescarte() throws RemoteException;

    int getCantidadRondas() throws RemoteException;

    EstadoPartida getEstadoDePartida() throws RemoteException;

    Top getTop() throws RemoteException;

    Cola<Jugador> getJugadores() throws RemoteException;

    Jugador getGanador() throws RemoteException;

    Jugador getJugadorActual() throws RemoteException;

    Mazo getMazo() throws RemoteException;

    Descarte getDescarte() throws RemoteException;

    Carta getTopeDescarte() throws RemoteException;

    int getPuntosMaximos() throws RemoteException;

    void ordenarCartasPalo(String jugador) throws RemoteException;

    void ordenarCartasValor(String jugador) throws RemoteException;

    int getCartaPosition(Carta carta) throws RemoteException;

    Jugador getJugador(String nombreJugador) throws RemoteException;

    int getPuntajeJugador(String nombre) throws RemoteException;

    ArrayList<Carta> getManoGanadora(String nombre) throws RemoteException;

    ArrayList<Carta> getManoPerdedora(String nombre) throws RemoteException;

    boolean seleccionarJugador(String nombreJugador) throws RemoteException;

    int getJugadoresSeleccionados() throws RemoteException;

    ArrayList<Integer> getTopVictorias() throws RemoteException;
}
