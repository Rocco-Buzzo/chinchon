package ar.edu.unlu.servidor;

import ar.edu.unlu.mvc.model.clases.Chinchon;
import ar.edu.unlu.mvc.model.interfaces.IChinchon;
import ar.edu.unlu.rmimvc.RMIMVCException;

import java.rmi.RemoteException;

public class Servidor {
    public static void main(String[] args) {
        try {
            IChinchon chinchon = new Chinchon();
            ar.edu.unlu.rmimvc.servidor.Servidor servidor = new ar.edu.unlu.rmimvc.servidor.Servidor("127.0.0.1", 50000);
            servidor.iniciar(chinchon);
        } catch (RemoteException | RMIMVCException e) {
            e.printStackTrace();
        }
    }
}

