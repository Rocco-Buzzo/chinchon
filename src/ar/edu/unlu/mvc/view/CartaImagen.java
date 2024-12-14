package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.enumerates.Palo;

import java.util.ArrayList;

public class CartaImagen extends Carta {
    public CartaImagen(int valor, Palo palo) {
        super(valor, palo);
    }

    // Generar la representación gráfica de la carta
    public String[] toASCII() {
        String[] ascii = new String[5];
        String valorStr = (super.getValor() < 10) ? super.getValor() + " " : String.valueOf(super.getValor()); // Asegura que el valor tenga al menos 2 caracteres
        String paloLetra = primerLetraPalo(super.getPalo().toString());

        ascii[0] = " .---------. ";
        ascii[1] = " |" + valorStr + ".---.  | ";
        ascii[2] = " |  : " + paloLetra + " :  | ";
        ascii[3] = " |  '---'" + valorStr + "| ";
        ascii[4] = " '---------' ";

        return ascii;
    }

    // Obtiene la primera letra del palo
    private String primerLetraPalo(String palo) {
        return palo.substring(0, 1).toUpperCase();
    }

    // NUEVA FUNCIÓN: Mostrar múltiples cartas en ASCII
    public static StringBuilder[] getCartasASCII(ArrayList<Carta> cartas) {
        // Arreglo para almacenar las líneas de cada carta
        StringBuilder[] cartaLineas = new StringBuilder[5];
        for (int i = 0; i < cartaLineas.length; i++) {
            cartaLineas[i] = new StringBuilder();
        }

        // Generar la representación gráfica de cada carta
        for (Carta carta : cartas) {
            String[] asciiCarta = new CartaImagen(carta.getValor(), carta.getPalo()).toASCII();
            for (int i = 0; i < asciiCarta.length; i++) {
                cartaLineas[i].append(asciiCarta[i]); // Agregar la representación a cada línea
            }
        }

        return cartaLineas;
    }

    public static StringBuilder[] getCartaASCII(Carta carta) {
        // Arreglo para almacenar las líneas de cada carta
        StringBuilder[] cartaLineas = new StringBuilder[5];
        for (int i = 0; i < cartaLineas.length; i++) {
            cartaLineas[i] = new StringBuilder();
        }

        // Generar la representación gráfica de cada carta
        String[] asciiCarta = new CartaImagen(carta.getValor(), carta.getPalo()).toASCII();
        for (int i = 0; i < asciiCarta.length; i++) {
            cartaLineas[i].append(asciiCarta[i]); // Agregar la representación a cada línea
        }

        return cartaLineas;
    }
}
