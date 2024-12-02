package ar.edu.unlu.mvc.model.test;

import ar.edu.unlu.mvc.model.clases.*;
import ar.edu.unlu.mvc.model.enumerates.Palo;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManoTest {
    @Test
    public void testAgregarCarta() {
        Mano mano = new Mano();
        Mazo mazo = new Mazo(true, 48);
        for (int i = 0; i < 7; i++) {
            mano.agregarCarta(mazo.sacar());
        }
        System.out.println(mano.getCartas());
        assertEquals(7, mano.getCartas().size());
    }

    @Test
    public void testTirarCarta() {
        Mano mano = new Mano();
        Mazo mazo = new Mazo(true, 48);
        for (int i = 0; i < 8; i++) {
            mano.agregarCarta(mazo.sacar());
        }
        System.out.println(mano.getCartas());
        mano.getCarta(1);
        System.out.println(mano.getCartas());
    }

    @Test
    public void testGetCarta() {
        Mano mano = new Mano();
        Mazo mazo = new Mazo(true, 48);
        for (int i = 0; i < 7; i++) {
            mano.agregarCarta(mazo.sacar());
        }
        System.out.println(mano.getCartas());
        System.out.println(mano.getCarta(2));
    }

    @Test
    public void testIntercambbiarCartas() {
        Mano mano = new Mano();
        Mazo mazo = new Mazo(true, 48);
        for (int i = 0; i < 9; i++) {
            mano.agregarCarta(mazo.sacar());
        }
        System.out.println(mano.getCartas());
        mano.intercambiarCartas(1, 2);
        System.out.println(mano.getCartas());

        System.out.println(mano.getCartas());
        mano.intercambiarCartas(8, 2);
        System.out.println(mano.getCartas());
    }

    @Test
    public void testIguales() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(9, Palo.ORO)); // 0
        mano.agregarCarta(new Comodin()); // 1
        mano.agregarCarta(new Carta(9, Palo.BASTO)); // 2
        mano.agregarCarta(new Carta(8, Palo.COPA)); // 3
        mano.agregarCarta(new Comodin()); // 4
        mano.agregarCarta(new Carta(10, Palo.COPA)); // 5
        mano.agregarCarta(new Carta(4, Palo.ORO)); // 6

        // Tres cartas del mismo valor
        assertTrue(mano.puedeCerrar());
    }

    @Test
    public void testEscalera() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.ORO)); // 0
        mano.agregarCarta(new Carta(2, Palo.ORO)); // 1
        mano.agregarCarta(new Carta(3, Palo.ORO)); // 2
        mano.agregarCarta(new Comodin()); // 5
        mano.agregarCarta(new Carta(4, Palo.ORO)); // 3
        mano.agregarCarta(new Carta(5, Palo.ORO)); // 4
        mano.agregarCarta(new Comodin()); // 6

        // Tres cartas del mismo palo en secuencia
        assertTrue(mano.puedeCerrar());
    }

    @Test
    public void testOrdenarCartaPalo() {
        Mano mano = new Mano();
        Mazo mazo = new Mazo(true, 48);
        for (int i = 0; i < 8; i++) {
            mano.agregarCarta(mazo.sacar());
        }
        System.out.println(mano.getCartas());
        mano.ordenarCartaPalo();
        System.out.println(mano.getCartas());
    }

    @Test
    public void testOrdenarCartaValor() {
        Mano mano = new Mano();
        Mazo mazo = new Mazo(true, 48);
        for (int i = 0; i < 8; i++) {
            mano.agregarCarta(mazo.sacar());
        }
        System.out.println(mano.getCartas());
        mano.ordenarCartaValor();
        System.out.println(mano.getCartas());
    }

    @Test
    public void testGetCartaExtra() {
        Mano mano = new Mano();
        Mazo mazo = new Mazo(true, 48);
        for (int i = 0; i < 9; i++) {
            mano.agregarCarta(mazo.sacar());
        }
        System.out.println(mano.getCartas());
    }

    @Test
    public void testCartasReales() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(12, Palo.BASTO));
        mano.agregarCarta(new Carta(4, Palo.COPA));
        mano.agregarCarta(new Carta(12, Palo.ESPADA));
        mano.agregarCarta(new Carta(5, Palo.COPA));
        mano.agregarCarta(new Carta(1, Palo.BASTO));
        mano.agregarCarta(new Carta(4, Palo.ORO));
        mano.agregarCarta(new Carta(6, Palo.ESPADA));
        mano.ordenarCartaPalo();
        System.out.println(mano.getCartas());
        mano.ordenarCartaValor();
        System.out.println(mano.getCartas());
    }

    @Test
    public void testEsChinchon() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.ORO));
        mano.agregarCarta(new Carta(2, Palo.ORO));
        mano.agregarCarta(new Carta(3, Palo.ORO));
        mano.agregarCarta(new Carta(4, Palo.ORO));
        mano.agregarCarta(new Carta(5, Palo.ORO));
        mano.agregarCarta(new Carta(6, Palo.ORO));
        mano.agregarCarta(new Carta(7, Palo.ORO));

        // Caso positivo: todas las cartas son del mismo palo y consecutivas
        assertTrue(mano.puedeCerrar());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.ORO));
        mano.agregarCarta(new Carta(2, Palo.ORO));
        mano.agregarCarta(new Carta(3, Palo.ORO));
        mano.agregarCarta(new Carta(4, Palo.ORO));
        mano.agregarCarta(new Carta(5, Palo.ORO));
        mano.agregarCarta(new Comodin());
        mano.agregarCarta(new Carta(6, Palo.ORO));

        // Caso positivo: todas las cartas son del mismo palo y consecutivas con un comodín
        assertTrue(mano.puedeCerrar());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.ORO));
        mano.agregarCarta(new Carta(2, Palo.ORO));
        mano.agregarCarta(new Comodin());
        mano.agregarCarta(new Carta(4, Palo.ORO));
        mano.agregarCarta(new Carta(6, Palo.ORO));
        mano.agregarCarta(new Comodin());
        mano.agregarCarta(new Carta(7, Palo.ORO));

        // Caso positivo: todas las cartas son del mismo palo y consecutivas con dos comodines no consecutivos
        assertTrue(mano.puedeCerrar());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.ORO));
        mano.agregarCarta(new Carta(2, Palo.ORO));
        mano.agregarCarta(new Carta(3, Palo.ORO));
        mano.agregarCarta(new Carta(5, Palo.ORO));
        mano.agregarCarta(new Carta(6, Palo.ORO));
        mano.agregarCarta(new Carta(7, Palo.ORO));
        mano.agregarCarta(new Carta(9, Palo.ORO));

        // Caso negativo: las cartas no son consecutivas ni con comodines
        assertFalse(mano.puedeCerrar());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.ORO));
        mano.agregarCarta(new Carta(2, Palo.ESPADA));
        mano.agregarCarta(new Carta(3, Palo.ORO));
        mano.agregarCarta(new Carta(4, Palo.ORO));
        mano.agregarCarta(new Carta(5, Palo.ORO));
        mano.agregarCarta(new Carta(6, Palo.ORO));
        mano.agregarCarta(new Carta(7, Palo.ORO));

        // Caso negativo: las cartas no son del mismo palo
        assertFalse(mano.puedeCerrar());
    }

    @Test
    public void testGrupoDeTresYGrupoDeCuatro() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(2, Palo.COPA));
        mano.agregarCarta(new Carta(3, Palo.COPA));
        mano.agregarCarta(new Carta(4, Palo.ORO));
        mano.agregarCarta(new Carta(5, Palo.ORO));
        mano.agregarCarta(new Carta(6, Palo.ORO));
        mano.agregarCarta(new Carta(7, Palo.ORO));
        // Caso verdadero: Escalera de 3 y escalera de 4.
        assertTrue(mano.puedeCerrar());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(1, Palo.ESPADA));
        mano.agregarCarta(new Carta(1, Palo.BASTO));
        mano.agregarCarta(new Carta(1, Palo.ORO));
        mano.agregarCarta(new Carta(12, Palo.BASTO));
        mano.agregarCarta(new Carta(12, Palo.COPA));
        mano.agregarCarta(new Carta(12, Palo.ESPADA));
        assertTrue(mano.puedeCerrar());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(1, Palo.ESPADA));
        mano.agregarCarta(new Carta(1, Palo.BASTO));
        mano.agregarCarta(new Carta(12, Palo.ORO));
        mano.agregarCarta(new Carta(12, Palo.BASTO));
        mano.agregarCarta(new Carta(12, Palo.COPA));
        mano.agregarCarta(new Carta(9, Palo.ESPADA));
        assertFalse(mano.puedeCerrar());

    }

    @Test
    public void testCalularPuntaje() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(2, Palo.COPA));
        mano.agregarCarta(new Carta(3, Palo.COPA));
        mano.agregarCarta(new Carta(4, Palo.ORO));
        mano.agregarCarta(new Carta(5, Palo.ORO));
        mano.agregarCarta(new Carta(6, Palo.ORO));
        mano.agregarCarta(new Carta(7, Palo.ORO));

        // Caso positivo: grupo de tres y grupo de cuatro.
        assertEquals(-10, mano.calcularPuntajeGanador());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(2, Palo.COPA));
        mano.agregarCarta(new Carta(3, Palo.COPA));
        mano.agregarCarta(new Carta(4, Palo.COPA));
        mano.agregarCarta(new Carta(5, Palo.ORO));
        mano.agregarCarta(new Carta(6, Palo.ORO));
        mano.agregarCarta(new Carta(7, Palo.ORO));

        // Caso positivo: grupo de cuatro y grupo de tres.
        assertEquals(-10, mano.calcularPuntajeGanador());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(2, Palo.COPA));
        mano.agregarCarta(new Carta(3, Palo.COPA));
        mano.agregarCarta(new Carta(4, Palo.COPA));
        mano.agregarCarta(new Carta(5, Palo.COPA));
        mano.agregarCarta(new Carta(6, Palo.COPA));
        mano.agregarCarta(new Carta(7, Palo.COPA));

        // Caso positivo: grupo de tres y grupo de cuatro
        assertEquals(-100, mano.calcularPuntajeGanador());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(2, Palo.COPA));
        mano.agregarCarta(new Carta(3, Palo.COPA));
        mano.agregarCarta(new Comodin());
        mano.agregarCarta(new Carta(5, Palo.COPA));
        mano.agregarCarta(new Carta(6, Palo.COPA));
        mano.agregarCarta(new Carta(7, Palo.COPA));

        // Caso positivo: grupo de tres y grupo de cuatro
        assertEquals(-50, mano.calcularPuntajeGanador());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Comodin());
        mano.agregarCarta(new Carta(3, Palo.COPA));
        mano.agregarCarta(new Carta(4, Palo.COPA));
        mano.agregarCarta(new Comodin());
        mano.agregarCarta(new Carta(6, Palo.COPA));
        mano.agregarCarta(new Carta(7, Palo.COPA));

        // Caso positivo: grupo de tres y grupo de cuatro
        assertEquals(-25, mano.calcularPuntajeGanador());

    }

    @Test
    public void testCalcularPuntajeGanadorRestante() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(1, Palo.ESPADA));
        mano.agregarCarta(new Carta(1, Palo.BASTO));
        mano.agregarCarta(new Carta(12, Palo.ORO));
        mano.agregarCarta(new Carta(12, Palo.BASTO));
        mano.agregarCarta(new Carta(12, Palo.COPA));
        mano.agregarCarta(new Carta(5, Palo.ESPADA));
        // Caso positivo
        assertEquals(5, mano.calcularPuntajeGanador());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(1, Palo.ESPADA));
        mano.agregarCarta(new Carta(1, Palo.BASTO));
        mano.agregarCarta(new Carta(12, Palo.ORO));
        mano.agregarCarta(new Carta(12, Palo.BASTO));
        mano.agregarCarta(new Carta(12, Palo.COPA));
        mano.agregarCarta(new Carta(9, Palo.ESPADA));
        // Caso negativo
        assertEquals(0, mano.calcularPuntajeGanador());
    }

    @Test
    public void testCalcularPuntajePderdedor() {
        Mano mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.COPA));
        mano.agregarCarta(new Carta(1, Palo.ESPADA));
        mano.agregarCarta(new Carta(1, Palo.BASTO));
        mano.agregarCarta(new Carta(1, Palo.ORO));
        mano.agregarCarta(new Carta(5, Palo.BASTO));
        mano.agregarCarta(new Carta(2, Palo.COPA));
        mano.agregarCarta(new Carta(5, Palo.ESPADA));
        assertEquals(12, mano.calcularPuntajePerdedor());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.BASTO));
        mano.agregarCarta(new Carta(2, Palo.BASTO));
        mano.agregarCarta(new Carta(3, Palo.BASTO));
        mano.agregarCarta(new Carta(4, Palo.BASTO));
        mano.agregarCarta(new Carta(5, Palo.BASTO));
        mano.agregarCarta(new Carta(2, Palo.COPA));
        mano.agregarCarta(new Carta(5, Palo.ESPADA));
        assertEquals(7, mano.calcularPuntajePerdedor());

        mano = new Mano();
        mano.agregarCarta(new Carta(1, Palo.BASTO));
        mano.agregarCarta(new Carta(2, Palo.BASTO));
        mano.agregarCarta(new Carta(3, Palo.BASTO));
        mano.agregarCarta(new Carta(4, Palo.ESPADA));
        mano.agregarCarta(new Carta(4, Palo.BASTO));
        mano.agregarCarta(new Carta(4, Palo.COPA));
        mano.agregarCarta(new Carta(5, Palo.ESPADA));
        assertEquals(5, mano.calcularPuntajePerdedor());

    }
}
