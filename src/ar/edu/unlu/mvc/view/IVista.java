package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.controller.Controlador;

public interface IVista {
    void setControlador(Controlador controlador);

    void openGame();

    void iniciarTurnos();

    void startGame();

    void setJugador(String nombre);

    void cargarPartida();

    void continuarPartida();

    void finishGame();

    void actualizarMesa();
}
