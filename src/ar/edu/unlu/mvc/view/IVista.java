package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.controller.Controlador;

public interface IVista {
    void setControlador(Controlador controlador);

    void openGame();

    void iniciarTurnos();

    void startGame();

    void loadGame();

    void cerrarRonda();

    void setJugador(String nombre);

    void finishGame(boolean guardado);

    void actualizarMesa();

}
