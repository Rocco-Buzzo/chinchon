package ar.edu.unlu.mvc.model.clases;

import ar.edu.unlu.mvc.model.interfaces.IChinchon;

import java.io.*;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Serializacion {
    // Ruta del archivo donde se guardará el top 5 de jugadores
    private static final String TOP_FILE_PATH = System.getProperty("user.dir") + "/chinchon-game/src/ar/edu/unlu/top/top.dat";
    // Ruta del archivo donde se guardarán las partidas
    private static final String GAMES_FILE_PATH = System.getProperty("user.dir") + "/src/ar/edu/unlu/partidas/";

    /**
     * Funcion que permite guardar las partidas.
     * @param partida Partida a guardar.
     * @param nombreArchivo Archivo de la partida.
     */
    public static void guardarPartida(IChinchon partida, String nombreArchivo, String playerOneName, String playerTwoName) {
        String rutaArchivo = GAMES_FILE_PATH + nombreArchivo + ".dat";
        File archivo = new File(rutaArchivo);

//        if (archivo.exists()) {
//            System.out.println(" Ya existe una partida con ese nombre.");
//            System.out.println(" ¿Desea sobrescribir el archivo? (s/n)");
//
//            Scanner scanner = new Scanner(System.in);
//            String respuesta = scanner.nextLine().trim().toLowerCase();
//
//            if (!respuesta.equals("s")) {
//                System.out.println(" Operación cancelada.");
//                return;
//            }
//        }

        try {
            FileOutputStream archivoSalida = new FileOutputStream(rutaArchivo);
            ObjectOutputStream salida = new ObjectOutputStream(archivoSalida);

            LocalDateTime horaGuardado = LocalDateTime.now();

            salida.writeObject(partida);
            salida.writeObject(horaGuardado);
            salida.writeObject(playerOneName);
            salida.writeObject(playerTwoName);

            salida.close();
            archivoSalida.close();
        } catch (IOException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    /**
     * Funcion que permite cargar las partidas.
     * @param nombreArchivo Nombre del archivo donde se encuentran las partidas.
     */
    public static IChinchon cargarPartida(String nombreArchivo) {
        IChinchon partida = null;
        try {
            FileInputStream archivoEntrada = new FileInputStream(GAMES_FILE_PATH + nombreArchivo + ".dat");
            ObjectInputStream entrada = new ObjectInputStream(archivoEntrada);
            partida = (IChinchon) entrada.readObject();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
        }
        return partida;
    }

    /**
     * Funcion que permite listar las partidas guardadas.
     **/
    public static ArrayList<String> listarPartidas() {
        ArrayList<String> archivos = new ArrayList<>();
        HashMap<LocalDateTime, ArrayList<String>> archivosPorDiaYHora = new HashMap<>();

        File carpeta = new File(GAMES_FILE_PATH);

        // Verifica si la carpeta existe y es un directorio
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] archivosEnCarpeta = carpeta.listFiles((dir, name) -> name.endsWith(".dat"));

            if (archivosEnCarpeta != null) {
                for (File archivo : archivosEnCarpeta) {
                    LocalDateTime horaGuardado = obtenerHoraGuardado(archivo);
                    if (horaGuardado != null) {
                        String nombreArchivo = archivo.getName();
                        String fileNameLoad = archivo.getName();
                        if (fileNameLoad.endsWith(".dat")) {
                            fileNameLoad = fileNameLoad.substring(0, fileNameLoad.length() - 4);
                        }
                        IChinchon partida = cargarPartida(fileNameLoad);

                        // Obtiene los nombres de los jugadores de la partida
                        try {
                            String playerOneName = partida.getJugadores().getFirst().getNombre();
                            String playerTwoName = partida.getJugadores().getLast().getNombre();

                            String partidaInfo = String.format("%s : %s (Jugadores: %s vs %s)",
                                    horaGuardado.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                                    nombreArchivo, playerOneName, playerTwoName);

                            archivos.add(partidaInfo);

                        } catch (RemoteException e) {
                            throw new RuntimeException("Error al obtener los nombres de los jugadores: " + e.getMessage(), e);
                        }
                    }
                }
            }
        }

        // Ordenar la lista de archivos por fecha
        archivos.sort((a, b) -> {
            LocalDateTime fechaA = LocalDateTime.parse(a.split(" : ")[0], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            LocalDateTime fechaB = LocalDateTime.parse(b.split(" : ")[0], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            return fechaB.compareTo(fechaA);
        });

        return archivos;
    }


    /**
     * Funcion auxiliar que permite obtener el dia y las horas donde se guardo la partida
     *
     * @param archivo El archivo donde se encuentran las partidas.
     */
    private static LocalDateTime obtenerHoraGuardado(File archivo) {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivo))) {
            entrada.readObject();
            Object horaGuardadoObj = entrada.readObject();
            if (horaGuardadoObj instanceof LocalDateTime) {
                return (LocalDateTime) horaGuardadoObj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * Guarda el top 5 de jugadores en un archivo.
     * @param top El objeto Top que contiene el top 5 de jugadores.
     */
    public static void guardarTop(Top top) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(TOP_FILE_PATH))) {
            outputStream.writeObject(top);
        } catch (IOException e) {
            System.out.println(" Error al guardar el top 5 de jugadores: " + e.getMessage());
        }
    }

    /**
     * Carga el top 5 de jugadores desde un archivo.
     * @return El objeto Top que contiene el top 5 de jugadores.
     */
    public static Top cargarTop() {
        Top top = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(TOP_FILE_PATH))) {
            top = (Top) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(" El top 5 de jugadores está vacío.");
        }
        // Devuelve un nuevo objeto Top si no se pudo cargar el archivo
        return top != null ? top : new Top();
    }
}
