package ar.edu.unlu.mvc.model.test;

import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.clases.Comodin;
import ar.edu.unlu.mvc.model.clases.Jugador;
import ar.edu.unlu.mvc.model.enumerates.Palo;
import org.junit.Test;

import static org.junit.Assert.*;

public class JugadorTest {
    @Test
    public void testNewPlayer() {
        Jugador player = new Jugador("Jugador 1");
        assertNotNull(player);
    }

    @Test
    public void testGetNombre() {
        Jugador player = new Jugador("Jugador 1");
        assertEquals("Jugador 1", player.getNombre());
    }

    @Test
    public void testGetInformacion() {
        Jugador player = new Jugador("Jugador 1");
        assertEquals("Jugador 1", player.getNombre());
        assertEquals(0, player.getPuntaje());
        assertNotNull(player.getMano());
        assertEquals(0, player.getVictorias());
    }

    @Test
    public void testAgregarCarta() {
        Jugador player = new Jugador("Jugador 1");
        player.agregarCarta(new Comodin());
        player.agregarCarta(new Carta(1, Palo.ORO));
        player.agregarCarta(new Carta(2, Palo.ORO));
        player.agregarCarta(new Carta(3, Palo.ORO));
        player.agregarCarta(new Carta(4, Palo.ORO));
        player.agregarCarta(new Carta(5, Palo.ORO));
        player.agregarCarta(new Carta(6, Palo.ORO));
        assertEquals(7, player.getMano().getCartas().size());
    }

    @Test
    public void testTirarCarta() {
        Jugador player = new Jugador("Jugador 1");
        player.agregarCarta(new Comodin());
        player.agregarCarta(new Carta(1, Palo.ORO));
        player.agregarCarta(new Carta(2, Palo.ORO));
        player.agregarCarta(new Carta(3, Palo.ORO));
        player.agregarCarta(new Carta(4, Palo.ORO));
        player.agregarCarta(new Carta(5, Palo.ORO));
        player.agregarCarta(new Carta(6, Palo.ORO));
        Carta c = player.getMano().getCartas().getFirst();
        assertEquals(c, player.tirarCarta(1));
    }

    @Test
    public void testAumentarPuntos() {
        Jugador player = new Jugador("Jugador 1");
        player.agregarCarta(new Comodin());
        player.agregarCarta(new Carta(1, Palo.ORO));
        player.agregarCarta(new Carta(2, Palo.ORO));
        player.agregarCarta(new Carta(3, Palo.ORO));
        player.agregarCarta(new Carta(4, Palo.ORO));
        player.agregarCarta(new Carta(5, Palo.ORO));
        player.agregarCarta(new Carta(6, Palo.ORO));
        int suma = player.getMano().getCartas().getFirst().getValor() + player.getMano().getCartas().getLast().getValor();
        player.aumentarPuntuacion(suma);
        assertEquals(31, player.getPuntaje());
    }

    @Test
    public void testAumentarVictorias() {
        Jugador player = new Jugador("Jugador 1");
        player.agregarCarta(new Comodin());
        player.agregarCarta(new Carta(1, Palo.ORO));
        player.agregarCarta(new Carta(2, Palo.ORO));
        player.agregarCarta(new Carta(3, Palo.ORO));
        player.agregarCarta(new Carta(4, Palo.ORO));
        player.agregarCarta(new Carta(5, Palo.ORO));
        player.agregarCarta(new Carta(6, Palo.ORO));
        for (int i = 0; i < 3; i++) {
            player.aumentarVictorias();
        }
        assertEquals(3, player.getVictorias());
    }

    @Test
    public void testCantidadJokers() {
        Jugador player = new Jugador("Jugador 1");
        player.agregarCarta(new Comodin());
        player.agregarCarta(new Carta(1, Palo.ORO));
        player.agregarCarta(new Carta(2, Palo.ORO));
        player.agregarCarta(new Carta(3, Palo.ORO));
        player.agregarCarta(new Carta(4, Palo.ORO));
        player.agregarCarta(new Carta(5, Palo.ORO));
        player.agregarCarta(new Carta(6, Palo.ORO));
        assertEquals(1, player.cantidadJokers());
    }

}
