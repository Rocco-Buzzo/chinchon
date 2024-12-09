package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.enumerates.Palo;

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
}
