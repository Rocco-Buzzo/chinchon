package ar.edu.unlu.mvc.model.clases;

import ar.edu.unlu.mvc.model.enumerates.Palo;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Clase Carta, representa a la carta de la baraja Española
 */
public class Carta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int valor;
    private final Palo palo;

    /**
     * Constructor para la clase Carta, pasando valor y palo.
     * @param valor Valor de la carta
     * @param palo Palo de la carta
     */
    public Carta(int valor, Palo palo) {
        this.valor = valor;
        this.palo = palo;
    }

    /**
     * Constructor para la clase Carta, pasando carta.
     * @param c Datos de la carta para crear.
     */
    public Carta(Carta c) {
        this.valor = c.getValor();
        this.palo = c.getPalo();
    }

    /**
     * Devuelve el valor de la carta.
     * @return valor
     */
    public int getValor() {
        return valor;
    }

    /**
     * Devuelve el palo de la carta.
     * @return palo
     */
    public Palo getPalo() {
        return palo;
    }

    /**
     * Permite determinar si una carta es un Comodin.
     * @return boolean
     */
    public boolean isComodin() {
        return false;
    }

    /**
     * Metodo toString sobreescrito.
     * @return String
     */
    @Override
    public String toString() {
        if (this.isComodin()) {
            return "JOKER";
        }
        return getValor() + "-" + getPalo().toString();
    }

    public boolean equals(Carta carta) {
        return this.valor == carta.valor && this.palo == carta.palo;
    }

//  CREAR UNA NUEVA CLASE PARA MOSTRAR LA CARTA EN PANTALLA.

//    public void cartaConsola(StringBuilder[] lines) {
//        String valorStr = (valor > 9 ? String.valueOf(valor) : " " + valor);
//
//        lines[0].append(" .---------.");
//        lines[1].append(" |").append(valorStr).append(".---.  |");
//        lines[2].append(" |  : ").append(primerLetraPalo(palo.toString())).append(" :  |");
//        lines[3].append(" |  '---'").append(valorStr).append("|");
//        lines[4].append(" '---------'");
//    }

//    public String primerLetraPalo(String palo) {
//        return String.valueOf(palo.charAt(0));
//    }

    public ImageIcon getImagen() {
       return new ImageIcon( "src/ar/edu/unlu/assets/" + this + ".png");
    }
}
