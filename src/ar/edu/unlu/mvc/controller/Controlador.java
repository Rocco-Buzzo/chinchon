package ar.edu.unlu.mvc.controller;

import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.clases.Descarte;
import ar.edu.unlu.mvc.model.clases.Jugador;
import ar.edu.unlu.mvc.model.enumerates.Eventos;
import ar.edu.unlu.mvc.model.interfaces.IChinchon;
import ar.edu.unlu.mvc.view.IVista;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Controlador implements IControladorRemoto {
    private IVista iVista;
    private IChinchon iChinchon;

    public Controlador(IVista vista) {
        setVista(vista);
    }

    public void setVista(IVista vista) {
        this.iVista = vista;
        this.iVista.setControlador(this);
    }

    public void iniciarPartida() {
        try {
            iChinchon.iniciarPartida();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void establecerValores(boolean contiene, int cartas, int puntos) {
        try {
            iChinchon.establecerValores(contiene, cartas, puntos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Descarte getDescarte() {
        try {
            return iChinchon.getDescarte();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void agregarJugador(String nombre) {
        try {
            iChinchon.agregarJugador(nombre);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cambiarJugador() {
        try {
            iChinchon.cambiarJugador();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Jugador getJugadorActual() {
        try {
            return iChinchon.getJugadorActual();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPuntaje(String nombre) {
        try {
            for (Jugador jugador : iChinchon.getJugadores()) {
                if (jugador.getNombre().equals(nombre)) {
                    return jugador.getPuntaje();
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public boolean cargarPartida(String partidaSeleccionada) {
        try {
            return iChinchon.cargarPartida(partidaSeleccionada);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void robarCartaMazo() {
        try {
            iChinchon.robarCartaMazo();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void ordenarValor(String nombre) {
        try {
            iChinchon.ordenarCartasValor(nombre);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void ordenarPalo(String nombre) {
        try {
            iChinchon.ordenarCartasPalo(nombre);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void tirarCarta(int numero) {
        try {
            iChinchon.tirarCarta(numero);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void robarCartaDescarte() {
        try {
            iChinchon.robarCartaDescarte();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Jugador> getJugadores() {
        try {
            return iChinchon.getJugadores();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Jugador getJugador(String nombreJugador) {
        try {
            return iChinchon.getJugador(nombreJugador);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getCartaPosition(Carta carta) {
        try {
            return iChinchon.getCartaPosition(carta);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
        this.iChinchon = (IChinchon) t;
    }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof Eventos index) {
            switch (index) {
                case JUGADORES_MAXIMOS, PARTIDA_CARGADA -> iVista.startGame();
                case PARTIDA_TERMINADA -> iVista.finishGame();
                case CAMBIO_TURNO -> iVista.cambiarTurno();
                case RONDA_TERMINADA -> iVista.continuarPartida();
            }
        }
    }
}
