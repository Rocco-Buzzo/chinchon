package ar.edu.unlu.mvc.model.test;

import ar.edu.unlu.mvc.model.clases.Descarte;
import ar.edu.unlu.mvc.model.clases.Mazo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DescarteTest {
    @Test
    public void testAgregarCarta() {
        Mazo mazo = new Mazo(true, 40);
        Descarte descarte = new Descarte();
        descarte.apilar(mazo.sacar());
        System.out.println(descarte.getTope());
    }

    @Test
    public void testSacarCarta() {
        Mazo mazo = new Mazo(true, 40);
        Descarte descarte = new Descarte();
        descarte.apilar(mazo.sacar());
        System.out.println(descarte.getTope());
        descarte.desapilar();
        System.out.println(descarte.getTope());
    }

    @Test
    public void testVaciarDescarte() {
        Mazo mazo = new Mazo(true, 48);
        Descarte descarte = new Descarte();
        for (int i = 0; i < 7; i++) {
            descarte.apilar(mazo.sacar());
        }
        assertEquals(7, descarte.getSize());
        descarte.vaciarDescarte();
        assertEquals(0, descarte.getSize());
    }

    @Test
    public void testDescarte() {
        Mazo mazo = new Mazo(true, 48);
        Descarte descarte = new Descarte();
        System.out.println(descarte);
        descarte.apilar(mazo.sacar());
        System.out.println(descarte);
    }
}
