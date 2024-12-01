package ar.edu.unlu.mvc.model.test;

import ar.edu.unlu.mvc.model.clases.*;
import ar.edu.unlu.mvc.model.enumerates.EstadoPartida;
import ar.edu.unlu.mvc.model.enumerates.Palo;
import ar.edu.unlu.mvc.model.interfaces.IChinchon;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class JuegoTest {
    @Test
    public void testCantidadCartasMazo() {
        Chinchon juego = new Chinchon();
        try {
            juego.agregarJugador("Player 1");
            juego.agregarJugador("Player 2");
            juego.establecerValores(true, 48, 100);
            juego.iniciarPartida();

            // Caso uno: verificar el tamaño inicial del mazo.
            Assert.assertEquals(48, juego.getMazo().getSizeInicial());

            // Caso dos: verificar el tamano actual del mazo.
            Assert.assertEquals(35, juego.getMazo().getSize());

            // Caso tres: verificar si contiene comodin o no.
            Assert.assertTrue(juego.getMazo().isContieneComodin());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEstadoDePartida() {
        Chinchon juego = new Chinchon();
        try {
            juego.agregarJugador("Rocco");
            juego.agregarJugador("Mariano");
            juego.establecerValores(false, 40, 50);
            juego.iniciarPartida();
            Assert.assertEquals(EstadoPartida.JUGANDO, juego.getEstadoDePartida());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCambioDeJugador() {
        Chinchon juego = new Chinchon();
        try {
            juego.agregarJugador("Rocco");
            juego.agregarJugador("Mariano");
            juego.establecerValores(false, 40, 50);
            juego.iniciarPartida();
            for (int i = 0; i < 10; i++) {
                juego.cambiarJugador();
            }

            Assert.assertTrue(juego.getJugadorActual());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTirarCartaJugador() {
        Chinchon juego = new Chinchon();
        try {
            juego.agregarJugador("Rocco");
            juego.agregarJugador("Mariano");
            juego.establecerValores(false, 40, 50);
            juego.iniciarPartida();
            juego.tirarCarta(1);
            Assert.assertEquals(7, juego.getJugadorActual().getMano().getCartas().size());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTirarCartaJugadorDos() {
        Chinchon juego = new Chinchon();
        try {
            juego.agregarJugador("Rocco");
            juego.agregarJugador("Mariano");
            juego.establecerValores(false, 40, 50);
            juego.iniciarPartida();
            juego.robarCartaDescarte();
            juego.tirarCarta(1);
            Assert.assertEquals(7, juego.getJugadorActual().getMano().getCartas().size());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTirarCartaJugador1() {
        Chinchon juego = new Chinchon();
        try {
            juego.agregarJugador("Rocco");
            juego.agregarJugador("Mariano");
            juego.establecerValores(false, 40, 50);
            juego.iniciarPartida();
            juego.robarCartaMazo();
            Carta cartaTirada = juego.getJugadorActual().getMano().getCarta(4);
            juego.tirarCarta(4);
            Assert.assertEquals(cartaTirada, juego.getTopeDescarte());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetCartaPos() {
        Chinchon juego = new Chinchon();
        try {
            juego.agregarJugador("Rocco");
            juego.agregarJugador("Jusepe");
            juego.establecerValores(false, 40, 50);
            juego.iniciarPartida();
            juego.getJugadorActual().getMano().getCartas().clear();
            juego.cambiarJugador();
            juego.getJugadorActual().getMano().getCartas().clear();
            juego.cambiarJugador();

            juego.getJugadorActual().agregarCarta(new Carta(1, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(2, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(3, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(4, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(5, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(6, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(7, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(8, Palo.ESPADA));

            juego.getCartaPosition(new Carta(2, Palo.ESPADA));

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTerminarRonda() {
        Chinchon juego = new Chinchon();
        try {
            juego.agregarJugador("Rocco");
            juego.agregarJugador("Jusepe");
            juego.establecerValores(false, 40, 50);
            juego.iniciarPartida();
            juego.getJugadorActual().getMano().getCartas().clear();
            juego.cambiarJugador();
            juego.getJugadorActual().getMano().getCartas().clear();

            juego.getJugadorActual().agregarCarta(new Carta(1, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(2, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(3, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(4, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(11, Palo.COPA));
            juego.getJugadorActual().agregarCarta(new Carta(11, Palo.BASTO));
            juego.getJugadorActual().agregarCarta(new Carta(12, Palo.COPA));

            juego.cambiarJugador();

            juego.getJugadorActual().agregarCarta(new Carta(9, Palo.ORO));
            juego.getJugadorActual().agregarCarta(new Carta(9, Palo.COPA));
            juego.getJugadorActual().agregarCarta(new Carta(9, Palo.BASTO));
            juego.getJugadorActual().agregarCarta(new Carta(11, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(12, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Carta(10, Palo.ESPADA));
            juego.getJugadorActual().agregarCarta(new Comodin());

            juego.cambiarJugador();
            juego.cerrarRonda();

            juego.cambiarJugador();
            juego.cerrarRonda();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testCargarTopFive() {
        Top top = Serializacion.cargarTop();

        System.out.println("Top 5 de jugadores:");
        System.out.printf("%-10s %-20s %s\n", "Puesto", "Nombre", "Victorias");

        for (int i = 0; i < Math.min(5, top.getTop().size()); i++) {
            Jugador jugador = top.getTop().get(i);
            System.out.printf("%-10d %-20s %d\n", (i + 1), jugador.getNombre(), jugador.getVictorias());
        }
    }

    @Test
    public void testCargarPartida() throws RemoteException {
        IChinchon iChinchon = new Chinchon();
        iChinchon.agregarJugador("Rocco");
        iChinchon.agregarJugador("Enzo");
        iChinchon.establecerValores(false, 40, 50);
        iChinchon.iniciarPartida();
        iChinchon.guardarPartida("wor king");
        IChinchon chinchon = Serializacion.cargarPartida("wor king");
        ArrayList<String> games = Serializacion.listarPartidas();
        for (String partida : games) {
            System.out.println(partida);
        }
        Assert.assertNotNull(chinchon);
    }
}
