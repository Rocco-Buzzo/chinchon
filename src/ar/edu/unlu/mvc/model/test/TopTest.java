package ar.edu.unlu.mvc.model.test;

import ar.edu.unlu.mvc.model.clases.Chinchon;
import ar.edu.unlu.mvc.model.clases.Jugador;
import ar.edu.unlu.mvc.model.clases.Serializacion;
import ar.edu.unlu.mvc.model.clases.Top;
import ar.edu.unlu.mvc.model.interfaces.IChinchon;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TopTest {
    @Test
    public void testTopFive() {
        Top top = new Top();
        Jugador jugador1 = new Jugador("Maria");
        Jugador jugador2 = new Jugador("Juan");
        Jugador jugador3 = new Jugador("Mario");
        Jugador jugador4 = new Jugador("Paula");
        Jugador jugador5 = new Jugador("Carla");
        Jugador jugador6 = new Jugador("David");
        for (int i = 0; i < 6; i++) {
            top.agregarAlTop(jugador1);
            top.agregarAlTop(jugador2);
            top.agregarAlTop(jugador4);
            top.agregarAlTop(jugador4);
            top.agregarAlTop(jugador5);
            top.agregarAlTop(jugador6);
        }

        top.agregarAlTop(jugador3);
        top.agregarAlTop(jugador3);
        top.agregarAlTop(jugador3);
        assertEquals(6, top.getTop().size());

        Top topCargado = Serializacion.cargarTop();
        if (topCargado.getTop().isEmpty()) {
            System.out.println(" El top 5 de jugadores está vacío.");
            return;
        }

        System.out.println("Top 5 de jugadores:");
        System.out.printf("%-10s %-20s %s\n","Puesto", "Nombre", "Victorias");

        for (int i = 0; i < Math.min(5, topCargado.getTop().size()); i++) {
            String jugador = topCargado.getTop().get(i);
            System.out.printf("%-10d %-20s\n", (i+1), jugador);
        }
    }

    @Test
    public void testVerTopFive() throws RemoteException {
        IChinchon iChinchon = new Chinchon();
        Top top = iChinchon.getTop();
        ArrayList<String> jugadors = top.getTop();
        for (String j : jugadors) {
            System.out.println(j);
            System.out.println(iChinchon.getPuntajeJugador(j));
        }
    }
}
