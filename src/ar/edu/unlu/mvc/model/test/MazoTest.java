package ar.edu.unlu.mvc.model.test;

import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.clases.Mazo;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class MazoTest {
    @Test
    public void testMazoCuarentaCartasSinComodin() {
        Mazo mazo = new Mazo(false, 40);
        Mazo mazoAux = new Mazo(false, 0);
        System.out.println("Mazo con 40 cartas: ");
        while (!mazo.isEmpty()) {
            Carta desapilada = mazo.sacar();
            System.out.println(desapilada);
            mazoAux.apilar(desapilada);
        }
        assertEquals(40, mazoAux.getSize());
    }

    @Test
    public void testMazoCuarentaCartasConComodin() {
        Mazo mazo = new Mazo(true, 40);
        Mazo mazoAux = new Mazo(false, 0);
        System.out.println("Mazo con 40 cartas con comodines: ");
        while (!mazo.isEmpty()) {
            Carta desapilada = mazo.sacar();
            System.out.println(desapilada);
            mazoAux.apilar(desapilada);
        }
        assertEquals(42, mazoAux.getSize());
    }

    @Test
    public void testMazoCuarentaYOchoCartasSinComodin() {
        Mazo mazo = new Mazo(false, 48);
        Mazo mazoAux = new Mazo(false, 0);
        System.out.println("Mazo con 48 cartas: ");
        while (!mazo.isEmpty()) {
            Carta desapilada = mazo.sacar();
            System.out.println(desapilada);
            mazoAux.apilar(desapilada);
        }
        assertEquals(48, mazoAux.getSize());
    }

    @Test
    public void testMazoCuarentaYOchoCartasConComodin() {
        Mazo mazo = new Mazo(true, 48);
        Mazo mazoAux = new Mazo(false, 0);
        System.out.println("Mazo con 48 cartas con comodines: ");
        while (!mazo.isEmpty()) {
            Carta desapilada = mazo.sacar();
            System.out.println(desapilada);
            mazoAux.apilar(desapilada);
        }
        assertEquals(50, mazoAux.getSize());
    }

    @Test
    public void testMazoVacio() {
        Mazo mazo = new Mazo(true, 48);
        while (!mazo.isEmpty()) {
            mazo.sacar();
        }
        System.out.println("Mazo Vacio: " + mazo.getSize());
        assertEquals(0, mazo.getSize());
    }

    @Test
    public void testMazoVacioSacarCarta() {
        Mazo mazo = new Mazo(false, 0);
        try {
            mazo.desapilar();
            fail("No se lanzó la excepción NoSuchElementException");
        } catch (NoSuchElementException e) {
            System.out.println("Excepción lanzada: " + e.getMessage());
        }
    }

    @Test
    public void testBarajarMazo() {
        Mazo mazo = new Mazo(true, 48);
        Mazo mazoAux = new Mazo(false, 0);
        mazo.barajar();
        while (!mazo.isEmpty()) {
            Carta desapilada = mazo.sacar();
            System.out.println(desapilada);
            mazoAux.apilar(desapilada);
        }
    }

    @Test
    public void testGetSizeInicial() {
        Mazo mazo1 = new Mazo(false, 40);
        Mazo mazo3 = new Mazo(true, 48);
        assertEquals(48, mazo3.getSizeInicial());
        assertEquals(40, mazo1.getSizeInicial());
    }

    @Test
    public void testIsContieneComodin() {
        Mazo mazo1 = new Mazo(false, 40);
        Mazo mazo3 = new Mazo(true, 48);
        assertTrue(mazo3.isContieneComodin());
        assertFalse(mazo1.isContieneComodin());
    }
}
