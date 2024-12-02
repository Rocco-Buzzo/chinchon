package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.controller.Controlador;

import javax.swing.*;

public class Consola implements IVista {
    private String nombreJugador;
    private Controlador controlador;

    private JPanel panel1;
    private JTextArea textArea1;
    private JTextField textField1;
    private JButton button1;

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void openGame() {

    }

    @Override
    public void iniciarTurnos() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void setJugador(String nombre) {
        this.nombreJugador = nombre;
    }

    @Override
    public void cargarPartida() {

    }

    @Override
    public void continuarPartida() {

    }

    @Override
    public void finishGame() {

    }

    @Override
    public void actualizarMesa() {

    }
}
