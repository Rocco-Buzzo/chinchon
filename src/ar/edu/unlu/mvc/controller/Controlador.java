package ar.edu.unlu.mvc.controller;

import ar.edu.unlu.mvc.model.clases.Carta;
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

    public String getJugadorActual() {
        try {
            return iChinchon.getJugadorActual().getNombre();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int sizeCartasJugadorNoActual() {
        try {
            return iChinchon.getJugadores().getFondo().getMano().getCartas().size();
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

    public int getCartaPosition(Carta carta) {
        try {
            return iChinchon.getCartaPosition(carta);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String topeDescarte() {
        try {
            return iChinchon.getDescarte().toString();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void intercambiarCartas(int x, int y, String jugador) {
        try {
            iChinchon.intercambiarCartas(x, y, jugador);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void continuarPartida() {
        try {
            iChinchon.continuarPartida();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void cerrarRonda() {
        try {
            iChinchon.cerrarRonda();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> nombreJugadores() {
        try {
            return iChinchon.nombreJugadores();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarPartida(String nombrePartida, boolean guardar) {
        try {
            iChinchon.guardarPartida(nombrePartida, guardar);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public String getGanador() {
        try {
            return iChinchon.getGanador().getNombre();
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
                case PARTIDA_INICIADA, CAMBIO_TURNO -> iVista.iniciarTurnos();
                case PARTIDA_CARGADA -> iVista.loadGame();
                case PARTIDA_TERMINADA, PARTIDA_CANCELADA -> iVista.finishGame(false);
                case RONDA_TERMINADA -> iVista.cerrarRonda();
                case CARTA_ROBADA -> iVista.actualizarMesa();
                case PARTIDA_GUARDADA -> iVista.finishGame(true);
            }
        }
    }


}
