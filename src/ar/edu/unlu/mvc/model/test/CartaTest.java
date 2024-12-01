package ar.edu.unlu.mvc.model.test;

import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.enumerates.Palo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CartaTest {
    @Test
    public void testCartaGetValor() {
        Carta carta = new Carta(3, Palo.ESPADA);
        assertEquals(3, carta.getValor());
    }

    @Test
    public void testCartaGetPalo() {
        Carta carta = new Carta(3, Palo.ESPADA);
        assertEquals(Palo.ESPADA, carta.getPalo());
    }
}
