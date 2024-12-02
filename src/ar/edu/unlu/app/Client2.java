package ar.edu.unlu.app;

import ar.edu.unlu.mvc.controller.Controlador;
import ar.edu.unlu.mvc.view.IVista;
import ar.edu.unlu.mvc.view.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;

import java.rmi.RemoteException;

public class Client2 {
    public static void main(String[] args) {
        IVista vista = new VistaGrafica();
        IControladorRemoto controladorRemoto = new Controlador(vista);
        ar.edu.unlu.rmimvc.cliente.Cliente cliente = new ar.edu.unlu.rmimvc.cliente.Cliente("127.0.0.2", 50002, "127.0.0.1", 50000);
        try {
            cliente.iniciar(controladorRemoto);
            vista.openGame();
        } catch (RMIMVCException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
