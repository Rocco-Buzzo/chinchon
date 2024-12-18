package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.enumerates.Palo;

import java.util.ArrayList;

public class CartaImagen extends Carta {
    // Constructor que recibe un String
    public CartaImagen(int valor, Palo palo ) {
        super(valor, palo);
    }

    public CartaImagen(String cartaStr) {
        super(parseValor(cartaStr), parsePalo(cartaStr));
    }

    // Metodo para convertir el String a un valor entero
    private static int parseValor(String cartaStr) {
        String[] partes = cartaStr.split("-");
        return Integer.parseInt(partes[0]);
    }

    private static Palo parsePalo(String cartaStr) {
        String[] partes = cartaStr.split("-");
        return Palo.valueOf(partes[1].toUpperCase());
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

    public static StringBuilder[] getCartasASCII(ArrayList<String> cartas) {
        // Inicializar las líneas de las cartas
        StringBuilder[] cartaLineas = new StringBuilder[5];
        for (int i = 0; i < cartaLineas.length; i++) {
            cartaLineas[i] = new StringBuilder();
        }

        // Iterar sobre cada carta y generar su representación ASCII
        for (String carta : cartas) {
            // Divide el string para obtener el valor y el palo
            String[] partes = carta.split("-");
            int valor = Integer.parseInt(partes[0]);
            Palo palo = Palo.valueOf(partes[1].toUpperCase());

            // Generar la representación ASCII de la carta
            String[] asciiCarta = new CartaImagen(valor, palo).toASCII();

            // Agregar cada línea de la carta a las líneas correspondientes
            for (int i = 0; i < asciiCarta.length; i++) {
                cartaLineas[i].append(asciiCarta[i]).append(" "); // Añade espacio entre cartas
            }
        }

        return cartaLineas;
    }


    public static StringBuilder[] getCartaASCII(String carta) {
        // Divide el string para obtener el valor y el palo
        String[] partes = carta.split("-");
        int valor = Integer.parseInt(partes[0]);
        Palo palo = Palo.valueOf(partes[1].toUpperCase());

        // Crea la representación gráfica de la carta
        String[] asciiCarta = new CartaImagen(valor, palo).toASCII();
        StringBuilder[] cartaLineas = new StringBuilder[asciiCarta.length];

        // Agrega cada línea a los StringBuilders
        for (int i = 0; i < asciiCarta.length; i++) {
            cartaLineas[i] = new StringBuilder(asciiCarta[i]);
        }

        return cartaLineas;
    }
}
