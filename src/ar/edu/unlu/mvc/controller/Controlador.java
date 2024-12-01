package ar.edu.unlu.mvc.controller;

import ar.edu.unlu.mvc.model.clases.Carta;
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

    public String getJugadorActual() {
        try {
            return iChinchon.getJugadorActual().getNombre();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPuntaje(String nombre) {
        try {
            return iChinchon.getPuntajeJugador(nombre);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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

    public int getJugadoresSize() {
        try {
            return iChinchon.getJugadores().size();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Carta> getJugadorCartas(String nombreJugador) {
        try {
            return iChinchon.getJugador(nombreJugador).getMano().getCartas();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getJugador(String nombreJugador) {
        try {
            return iChinchon.getJugador(nombreJugador).getNombre();
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
