package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.controller.Controlador;

public interface IVista {
    void setControlador(Controlador controlador);

    void openGame();

    void startGame();

    void setJugador(String nombre);

    void cambiarTurno();

    void cargarPartida();

    void continuarPartida();

    void finishGame();
}
