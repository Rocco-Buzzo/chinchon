package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.controller.Controlador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class VistaConsola implements IVista {
    private Controlador controlador;
    private String nombreJugador;

    private JTextField input;
    private JTextArea consola;
    private JButton ok;

    private final JFrame chinchonFrame = new JFrame("Chinchon - Main Menu");

    private static final Font GAME_FONT = new Font("Courier New", Font.PLAIN, 12);

    private final WindowAdapter exit = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            renderExitGame();
        }
    };

    private final WindowAdapter login = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            renderPlayerLogin();
        }
    };

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void openGame() {
        initMenu();
        if (controlador.getJugadoresSize() == 2) {
            renderMenu();
        } else {
            renderPlayerLogin();
        }
    }

    @Override
    public void iniciarTurnos() {
        if (nombreJugador.equals(controlador.getJugadorActual())) {
            renderInGame();
        } else {
            renderWaitingTurn();
        }
    }

    @Override
    public void startGame() {
        controlador.iniciarPartida();
        iniciarTurnos();
    }

    @Override
    public void loadGame() {
        limpiarConsola();
        limpiarActionListeners(input, ok);
        deleteJugador();
        // Obtener nombres de jugadores disponibles desde el controlador
        ArrayList<String> nombresJugadores = controlador.nombreJugadores();

        // Verificar que hay jugadores disponibles
        if (nombresJugadores.size() == 2) {
            consola.append("Seleccione qué jugador desea ser:\n");
            consola.append("1. " + nombresJugadores.get(0) + "\n");
            consola.append("2. " + nombresJugadores.get(1) + "\n");

            // ActionListener para elegir jugador
            ActionListener loadGameAction = _ -> {
                String opcion = input.getText().trim();
                input.setText(""); // Limpiar el inputField

                if (opcion.equals("1") || opcion.equals("2")) {
                    int seleccion = Integer.parseInt(opcion) - 1; // Convertir a índice
                    String nombreSeleccionado = nombresJugadores.get(seleccion);

                    // Bloquear el nombre seleccionado en el controlador
                    if (controlador.seleccionarJugador(nombreSeleccionado)) {
                        consola.append("Has seleccionado el jugador: " + nombreSeleccionado + "\n");
                        setJugador(nombreSeleccionado); // Asignar el nombre a la vista
                        if (controlador.jugadoresSeleccionadosSize() == 2) {
                            controlador.iniciarPartida();
                        } else {
                            renderWaitingPlayers();
                        }
                    } else {
                        consola.append("El jugador '" + nombreSeleccionado + "' ya fue seleccionado. Intenta nuevamente.\n");
                    }
                } else {
                    consola.append("\nOpción no válida. Por favor, ingresa 1 o 2.\n");
                }
            };

            // Asignar el listener al inputField y botón OK
            input.addActionListener(loadGameAction);
            ok.addActionListener(loadGameAction);
        }
    }

    private void deleteJugador() {
        this.nombreJugador = null;
    }

    @Override
    public void cerrarRonda() {
        limpiarActionListeners(input, ok);
        limpiarConsola();
        ArrayList<String> jugadores = controlador.nombreJugadores();

        consola.append("Cerrando ronda.\n\n");
        consola.append("¡Ronda terminada!\n");
        consola.append("Ganador: " + controlador.getJugadorActual() + "\n\n");
        renderManosFinales();
        consola.append("Jugadores en partida:\n");
        for (String jugador : jugadores) {
            consola.append("- " + jugador + " puntaje: " + controlador.getPuntaje(jugador) + "\n");
        }

        ActionListener continueGame = _ -> {
            input.setText("");
            iniciarTurnos();
        };

        input.addActionListener(continueGame);
        ok.addActionListener(continueGame);
    }

    @Override
    public void setJugador(String nombre) {
        this.nombreJugador = nombre;
    }

    @Override
    public void finishGame(boolean guardado) {
        limpiarConsola();
        limpiarActionListeners(input, ok);
        ArrayList<String> jugadores = controlador.nombreJugadores();

        // Mensaje principal
        String mensaje = (controlador.getGanador() != null && !controlador.getGanador().isEmpty())
                ? "¡La partida ha finalizado! Ganador: " + controlador.getGanador()
                : "Partida cancelada.";

        if (guardado) {
            mensaje = "Partida guardada correctamente.";
        }

        consola.append(mensaje + "\n\n");

        // Mostrar mano ganadora solo si no se guardó la partida y no fue cancelada
        if (controlador.getGanador() != null && !controlador.getGanador().isEmpty()) {
            renderManosFinales();
        }

        // Mostrar puntajes finales de todos los jugadores
        consola.append("\nPuntajes finales:\n");
        for (String jugador : jugadores) {
            consola.append("- " + jugador + " puntaje: " + controlador.getPuntaje(jugador) + "\n");
        }

        // Mensaje de cierre
        consola.append("\nPresione ENTER para salir.\n");

        // Acción para cerrar el juego
        ActionListener exitGame = _ -> {
            input.setText("");
            renderMenu();
        };

        input.addActionListener(exitGame);
        ok.addActionListener(exitGame);
    }

    @Override
    public void actualizarMesa() {
        iniciarTurnos();
    }

    // Inicializar Menu.
    private void initMenu() {
        chinchonFrame.setTitle("Consola");
        chinchonFrame.setResizable(false);
        chinchonFrame.setSize(1024, 684);
        chinchonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chinchonFrame.setIconImage(new ImageIcon("src/ar/edu/unlu/assets/ICONO.png").getImage());
        chinchonFrame.setLayout(new BorderLayout());

        // Panel principal (zona negra)
        consola = new JTextArea();
        consola.setBackground(Color.BLACK);
        consola.setForeground(Color.WHITE);
        consola.setFont(GAME_FONT);
        consola.setEditable(false); // Solo lectura
        JScrollPane scrollPane = new JScrollPane(consola);
        chinchonFrame.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior (zona gris con JTextField y JButton)
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.DARK_GRAY);

        // Campo de texto
        input = new JTextField();
        input.setBackground(Color.BLACK);
        input.setForeground(Color.WHITE);
        input.setCaretColor(Color.WHITE);
        input.setFont(GAME_FONT);
        panelInferior.add(input, BorderLayout.CENTER);

        // Botón
        ok = new JButton("OK");
        ok.setBackground(Color.DARK_GRAY);
        ok.setForeground(Color.WHITE);
        ok.setFont(GAME_FONT);
        panelInferior.add(ok, BorderLayout.EAST);

        chinchonFrame.add(panelInferior, BorderLayout.SOUTH);

        chinchonFrame.setVisible(true);
    }

    // Metodo para renderizar el login del jugador.
    private void renderPlayerLogin() {
        chinchonFrame.removeWindowListener(exit);
        chinchonFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        title();
        consola.append("Bienvenido al juego. Por favor, ingresa tu nombre para continuar.\n");

        // Listener para el campo de texto (Enter) y el botón
        ActionListener loginAction = _ -> {
            String nombre = input.getText().trim();
            if (!nombre.isEmpty()) {
                setJugador(nombre);
                renderMenu(); // Llama al metodo para abrir el menú del juego
            } else {
                consola.append("El nombre no puede estar vacío. Por favor, intenta de nuevo.\n");
            }
        };

        input.addActionListener(loginAction); // Enter en el campo de texto
        ok.addActionListener(loginAction);   // Click en el botón OK
    }

    // Metodo para cargar el Menu Principal.
    private void renderMenu() {
        chinchonFrame.addWindowListener(login);
        chinchonFrame.removeWindowListener(exit);
        limpiarConsola(); // Limpiar la consola antes de mostrar el menú
        title();
        chinchonFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chinchonFrame.removeWindowListener(exit);
        consola.append("Bienvenido, " + nombreJugador + "!\n\n");
        consola.append("Menú principal:\n");
        consola.append("1. Nueva partida\n");
        consola.append("2. Cargar partida\n");
        consola.append("3. Unirse a partida\n");
        consola.append("4. Ranking de Mejores Jugadores\n");
        consola.append("5. Reglas\n");
        consola.append("6. Salir\n");

        limpiarActionListeners(input, ok);

        // Listener para el campo de texto (Enter) y el botón
        ActionListener menuAction = _ -> {
            String input = this.input.getText().trim();
            limpiarConsola(); // Limpia la consola después de la acción

            switch (input) {
                case "1":
                    renderSettings();
                    break;
                case "2":
                    renderLoadGame();
                    break;
                case "3":
                    renderJoinGame();
                    break;
                case "4":
                    renderTopJugadores();
                    break;
                case "5":
                    renderRules();
                    break;
                case "6":
                    renderPlayerLogin();
                    break;
                default:
                    consola.append("Opción no válida. Por favor, selecciona una opción del menú.\n");
                    renderMenu(); // Vuelve a mostrar el menú
                    break;
            }
            this.input.setText(""); // Limpia el campo de texto
        };

        input.addActionListener(menuAction); // Enter en el campo de texto
        ok.addActionListener(menuAction);  // Click en el botón OK
    }

    private void renderTopJugadores() {
        limpiarConsola();
        limpiarActionListeners(input, ok);
        ArrayList<String> topCargado = controlador.getTop();
        ArrayList<Integer> victorias = controlador.getVictorias();

        if (topCargado.isEmpty() || victorias.isEmpty()) {
            consola.append("El top 5 de jugadores está vacío.\n");
        } else {
            consola.append("Top 5 de jugadores:\n");
            String encabezados = String.format("%-10s %-20s %s\n", "Puesto", "Nombre", "Victorias");
            consola.append(encabezados);

            // Verifica que ambas listas tengan el mismo tamaño antes de iterar
            int size = Math.min(topCargado.size(), victorias.size());
            for (int i = 0; i < size; i++) {
                String fila = String.format("%-10d %-20s %d\n", (i + 1), topCargado.get(i), victorias.get(i));
                consola.append(fila);
            }
        }
        consola.append("\n\nPresiona ENTER para volver al menú.");
        ActionListener menuAction = _ -> {
            input.setText("");
            renderMenu();
        };
        input.addActionListener(menuAction);
        ok.addActionListener(menuAction);
    }

    // Métodos correspondientes al menú
    // 1. Renderiza el menu para seleccionar las configuraciones de la partida
    private void renderSettings() {
        limpiarConsola(); // Limpia la consola antes de mostrar la configuración
        consola.append("- Configuración de la partida:\n\n");
        consola.append("Selecciona el número de cartas del mazo (40 o 48):\n");

        // Limpia los listeners previos del inputField
        limpiarActionListeners(input, ok);

        // Paso 1: Seleccionar número de cartas
        ActionListener cartasListener = _ -> {
            String input = this.input.getText().trim();
            if (input.equals("40") || input.equals("48")) {
                int numeroCartas = Integer.parseInt(input);
                consola.append("Número de cartas iniciales seleccionado: " + numeroCartas + "\n");
                consola.append("¿El mazo contiene comodines? (sí/no):\n");
                this.input.setText("");

                // Paso 2: Seleccionar si el mazo contiene moddin
                limpiarActionListeners(this.input, ok);
                ActionListener comodinListener = _ -> {
                    String comodinInput = this.input.getText().trim().toLowerCase();
                    boolean comodin = comodinInput.equals("sí") || comodinInput.equals("si");
                    if (comodin || comodinInput.equals("no")) {
                        consola.append("Mazo con comodin: " + (comodin ? "Sí" : "No") + "\n");
                        consola.append("Selecciona los puntos máximos para terminar la partida (50 o 100):\n");
                        this.input.setText("");

                        // Paso 3: Seleccionar puntos máximos
                        limpiarActionListeners(this.input, ok);
                        ActionListener puntosListener = _ -> {
                            String puntosInput = this.input.getText().trim();
                            if (puntosInput.equals("50") || puntosInput.equals("100")) {
                                int puntosMaximos = Integer.parseInt(puntosInput);
                                consola.append("Puntos máximos seleccionados: " + puntosMaximos + "\n");
                                consola.append("¡Configuración completada! Volviendo al menú...\n");
                                this.input.setText("");
                                controlador.establecerValores(comodin, numeroCartas, puntosMaximos);
                                controlador.agregarJugador(nombreJugador);
                                if (controlador.getJugadoresSize() == 1) {
                                    renderWaitingPlayers();
                                } else {
                                    startGame();
                                }
                            } else {
                                consola.append("Por favor, ingresa 50 o 100.\n");
                            }
                        };
                        this.input.addActionListener(puntosListener);
                    } else {
                        consola.append("Por favor, responde 'sí' o 'no'.\n");
                    }
                };
                this.input.addActionListener(comodinListener);
            } else {
                consola.append("Por favor, ingresa 5 o 7.\n");
            }
        };

        input.addActionListener(cartasListener);
    }

    // 2. Renderiza el cargar partida.
    private void renderLoadGame() {
        limpiarActionListeners(input, ok);
        // Aquí puedes incluir el código para mostrar la lógica de cargar una partida
        ArrayList<String> partidasGuardadas = controlador.listarPartidas(nombreJugador);
        if (partidasGuardadas.isEmpty()) {
            consola.append("No hay partidas guardadas.");
            consola.append("Presion ENTER para continuar.\n");
            ActionListener backAction = _ -> {
                input.setText("");
                renderMenu();
            };
            input.addActionListener(backAction);
            ok.addActionListener(backAction);
        } else {
            consola.append("Partidas guardadas:\n");
            for (int i = 0; i < partidasGuardadas.size(); i++) {
                consola.append(i + 1 + ". " + partidasGuardadas.get(i) + "\n");
            }
            consola.append("Selecciona la partida que deseas cargar ingresando solo el nombre:\n");
            ActionListener loadGameListener = _ -> {
                String partidaSeleccionada = input.getText().trim();
                if (partidaSeleccionada.isEmpty()) {
                    consola.append("Debe introducir o seleccionar un nombre de partida válido.");
                } else {
                    if (controlador.cargarPartida(partidaSeleccionada)) {
                        consola.append("Partida cargada correctamente.");
                    } else {
                        consola.append("La partida seleccionada no existe.");
                    }
                }
            };
            input.addActionListener(loadGameListener);
            ok.addActionListener(loadGameListener);
        }
    }

    // 3. Renderiza el unirse a la partida.
    private void renderJoinGame() {
        limpiarConsola();
        limpiarActionListeners(input, ok);

        controlador.agregarJugador(nombreJugador);
        if (controlador.getJugadoresSize() == 1) {
            consola.append("Uniendose a partida.\n");
            consola.append("Esperando jugadores ...\n");
        } else {
            startGame();
        }
    }

    // 4. Renderizar las reglas del juego.
    private void renderRules() {
        limpiarActionListeners(input, ok);
        limpiarConsola();

        consola.append("- Reglas del CHINCHÓN\n");
        consola.append("Jugar las cartas:\n");
        consola.append("Cada jugador en su turno debe agarrar la carta superior del mazo, o bien, si considera que le conviene, la carta descubierta en el descarte. Luego de agarrar esa carta, y con 8 cartas en la mano, el jugador debe lanzar una de ellas al descarte, sobre las que hay boca arriba.\n\n");
        consola.append("Ligar las cartas:\n");
        consola.append("En cada mano, los jugadores deben intentar ligar sus cartas. Ligar las cartas es hacer grupos de al menos tres cartas unidas por uno de los criterios siguientes: Cartas con el mismo índice. Cartas de un mismo palo que forman una escalera.\n\n");
        consola.append("Cerrar la mano:\n");
        consola.append("Un jugador en su turno, al deshacerse de una carta, puede cerrar la mano si sus cartas cumplen una de estas condiciones: Forman una escalera de siete cartas. Están ligadas en dos grupos, uno de tres cartas y otro de cuatro. Están ligadas en dos grupos de tres cartas (u ocasionalmente una escalera de seis cartas), y la séptima carta tiene un valor inferior a cinco.\n\n");
        consola.append("Colocar las cartas:\n");
        consola.append("En el Chinchón, una vez que un jugador ha cerrado una mano, los demás deben intentar sumar la menor cantidad posible de puntos. Para ello, los jugadores van mostrando los grupos de cartas ligadas.\n\n");
        consola.append("Recuento de la mano:\n");
        consola.append("Una vez colocadas todas las cartas ligadas, cada jugador suma los puntos de las cartas restantes. Si el jugador que cierra la mano lo hace ligando sus 7 cartas, obtiene el premio de restar puntos: \n - Menos 10 (-10) con dos grupos de cartas (uno de tres y otro de cuatro).\n - Menos 25 (-25) con una escalera de siete cartas con dos comodines.\n - Menos 50 (-50) con una escalera de siete cartas con un comodín.\n - Chinchón: con una escalera de siete cartas sin comodines gana la partida directamente.\n\n");

        ActionListener backAction = _ -> {
            input.setText("");
            renderMenu();
        };

        input.addActionListener(backAction);
        ok.addActionListener(backAction);
    }

    // Metodo que permite esperar la partida.
    private void renderWaitingPlayers() {
        limpiarConsola();
        limpiarActionListeners(input, ok);
        consola.append("Esperando jugadores ...\n");
    }

    // Metodo para renderizar la partida del jugador en turno.
    private void renderInGame() {
        chinchonFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        chinchonFrame.removeWindowListener(login);
        chinchonFrame.addWindowListener(exit);
        limpiarConsola();
        limpiarActionListeners(input, ok);
        // Opciones disponibles
        consola.append("Bienvenido, " + nombreJugador + "!\n\n");
        consola.append("Turno de: " + controlador.getJugadorActual() + "\n");
        consola.append("Tu puntaje: " + controlador.getPuntaje(nombreJugador) + "\n\n");

        consola.append("- Opciones:\n");
        consola.append(" [M] Robar una carta del Mazo.\n [D] Robar una carta del Descarte.\n [T] Tirar una carta.\n [C] Cerrar ronda.\n");
        consola.append(" [P] Ordenar mano por palo.\n [V] Ordenar mano por valor.\n [I] Intercambiar cartas.\n [ESC] Salir de la partida.\n\n");
        consola.append("(Nota: Recuerda armar los juegos a la izquierda de la mano, y poner la carta que desea bajar a la derecha del todo.)\n\n");

        // Renderizar Carta Descarte
        consola.append("- Carta Descarte:\n");
        if (controlador.topeDescarte() != null) {
            for (StringBuilder linea : CartaImagen.getCartaASCII(controlador.topeDescarte().toString())) {
                consola.append(linea.toString());
                consola.append("\n");
            }
        }
        // Renderizar cartas del jugador
        consola.append("Tu mano:\n");
        // Crear las líneas para renderizar las cartas
        ArrayList<String> cartasJugador = controlador.getJugadorCartas(nombreJugador);

        StringBuilder[] cartaLineas = CartaImagen.getCartasASCII(cartasJugador);

        // Agregar las líneas de las cartas a la consola
        for (StringBuilder linea : cartaLineas) {
            consola.append(linea.toString());
            consola.append("\n");
        }

        consola.append("\nSelecciona una opción:\n");

        // Limpia los listeners previos del inputField
        limpiarActionListeners(input, ok);

        // Listener para manejar las opciones del jugador
        ActionListener inGameListener = _ -> {
            String in = input.getText().trim().toUpperCase();
            input.setText("");

            switch (in) {
                case "M":
                    robarCarta(true);
                    break;
                case "D":
                    robarCarta(false);
                    break;
                case "T":
                    if (controlador.getJugadorCartas(nombreJugador).size() == 7) {
                        consola.append("Necesitas tener 8 cartas para bajar una");
                        break;
                    }
                    tirarCarta();
                    break;
                case "C":
                    controlador.cerrarRonda();
                    break;
                case "P":
                    ordenarCartas("p");
                    break;
                case "V":
                    ordenarCartas("v");
                    break;
                case "I":
                    intercambiarCartas();
                    break;
                case "ESC":
                    renderExitGame();
                    break;
                default:
                    consola.append("Opción no válida. Por favor, selecciona una opción válida.\n");
                    break;
            }
        };
        input.addActionListener(inGameListener);
        ok.addActionListener(inGameListener);
    }

    // Metodo para renderizar la partida del jugador que no esta en turno.
    private void renderWaitingTurn() {
        chinchonFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        chinchonFrame.addWindowListener(exit);
        chinchonFrame.removeWindowListener(login);

        limpiarConsola();
        limpiarActionListeners(input, ok);
        // Opciones disponibles
        consola.append("Bienvenido, " + nombreJugador + "!\n\n");
        consola.append("Turno de: " + controlador.getJugadorActual() + "\n\n");
        consola.append("- Opciones:\n");
        consola.append(" [P] Ordenar mano por palo.\n [V] Ordenar mano por valor.\n [I] Intercambiar cartas.\n [ESC] Salir de la partida.\n\n");
        consola.append("(Nota: Recuerda armar los juegos a la izquierda de la mano, y poner la carta que desea bajar a la derecha del todo.)\n");

        // Renderizar cartas del jugador
        consola.append("Tu mano:\n");
        // Crear las líneas para renderizar las cartas
        ArrayList<String> cartasJugador = controlador.getJugadorCartas(nombreJugador);

        StringBuilder[] cartaLineas = new StringBuilder[5];
        for (int i = 0; i < cartaLineas.length; i++) {
            cartaLineas[i] = new StringBuilder();
        }

        for (String carta : cartasJugador) {
            // Convierte cada carta a una representación gráfica
            String[] asciiCarta = new CartaImagen(carta).toASCII();
            for (int i = 0; i < asciiCarta.length; i++) {
                cartaLineas[i].append(asciiCarta[i]); // Sin espacios entre cartas
            }
        }

        // Agregar las líneas de las cartas a la consola
        for (StringBuilder linea : cartaLineas) {
            consola.append(linea.toString());
            consola.append("\n");
        }

        consola.append("\nSelecciona una opción:\n");

        // Limpia los listeners previos del inputField
        limpiarActionListeners(input, ok);

        // Listener para manejar las opciones del jugador
        ActionListener inGameListener = _ -> {
            String in = input.getText().trim().toUpperCase();
            input.setText("");

            switch (in) {
                case "P":
                    ordenarCartas("p");
                    break;
                case "V":
                    ordenarCartas("v");
                    break;
                case "I":
                    intercambiarCartas();
                    break;
                case "ESC":
                    renderExitGame();
                    break;
                default:
                    consola.append("Opción no válida. Por favor, selecciona una opción válida.\n");
                    break;
            }
        };

        input.addActionListener(inGameListener);
        ok.addActionListener(inGameListener);
    }

    // Metodo para renderizar la salida de la partida
    private void renderExitGame() {
        limpiarConsola();
        limpiarActionListeners(input, ok);

        // Mensaje inicial
        consola.append("¿Quieres guardar la partida antes de salir?\n");
        consola.append("1. Guardar y salir.\n");
        consola.append("2. Salir sin guardar.\n");
        consola.append("3. Cancelar y volver al juego.\n");

        // Listener para manejar la opción seleccionada
        ActionListener exitGameAction = _ -> {
            String opcion = input.getText().trim();
            input.setText(""); // Limpiar el campo de texto

            switch (opcion) {
                case "1": // Guardar y salir
                    saveGame(true);
                    break;
                case "2": // Salir sin guardar
                    saveGame(false);
                    break;
                case "3": // Cancelar y volver al juego
                    consola.append("Regresando al juego...\n");
                    iniciarTurnos(); // Volver al juego
                    break;
                default: // Opción inválida
                    consola.append("Opción no válida. Por favor, elige una opción entre 1 y 3.\n");
            }
        };
        input.addActionListener(exitGameAction); // Asociar el listener al campo de texto
        ok.addActionListener(exitGameAction);   // Asociar el listener al botón OK
    }

    private void renderManosFinales() {
        ArrayList<String> jugadores = controlador.nombreJugadores();
        if (jugadores == null || jugadores.size() < 2) {
            consola.append("Error: No hay suficientes jugadores para mostrar las manos.\n");
            return;
        }

        for (String nombre : jugadores) {
            if (controlador.getJugadorActual().equals(nombre)) {
                renderMano(("\nLigaciones de " + nombre + ": "), controlador.getManoGanadora(nombre));
            } else {
                renderMano(("\nLigaciones de " + nombre + ": "), controlador.getManoPerdedora(nombre));
            }
        }

        consola.append("\nPresione ENTER para continuar.\n");
    }

    private void renderMano(String titulo, ArrayList<String> mano) {
        consola.append(titulo + "\n");
        if (mano.isEmpty()) {
            consola.append("El jugador no tiene ligaciones.");
        } else {
            // Crear las líneas para renderizar las cartas
            StringBuilder[] cartaLineas = new StringBuilder[5];
            for (int i = 0; i < cartaLineas.length; i++) {
                cartaLineas[i] = new StringBuilder();
            }

            for (String carta : mano) {
                // Convertir cada carta a su representación gráfica
                String[] asciiCarta = new CartaImagen(carta).toASCII();
                for (int i = 0; i < asciiCarta.length; i++) {
                    cartaLineas[i].append(asciiCarta[i]);
                }
            }

            // Agregar las líneas de las cartas a la consola
            for (StringBuilder linea : cartaLineas) {
                consola.append(linea.toString());
                consola.append("\n");
            }
        }
    }

    // Opciones del Jugador
    // [M/D] robar carta del mazo o del descarte.
    private void robarCarta(boolean b) {
        if (b) {
            controlador.robarCartaMazo();
        } else {
            controlador.robarCartaDescarte();
        }
        renderInGame();
    }

    // [T] Tirar carta seleccionada por el jugador.
    private void tirarCarta() {
        consola.append("\n\nIngresa el índice de la carta que deseas tirar (1 a " + controlador.getJugadorCartas(nombreJugador).size() + "):\n");

        limpiarActionListeners(input, ok);
        ActionListener tirarCarta = _ -> {
            int indice = Integer.parseInt(input.getText().trim());
            input.setText("");
            if (indice < 0 || indice >= controlador.getJugadorCartas(nombreJugador).size()) {
                consola.append("Índice inválido. Por favor, ingresa un número válido.\n");
            }
            consola.append("Has seleccionado la carta en la posición " + (indice) + ".\n");
            controlador.tirarCarta(indice);
        };
        input.addActionListener(tirarCarta);
        ok.addActionListener(tirarCarta);
    }

    // [I] Intercambiar las cartas entre el jugador.
    private void intercambiarCartas() {
        consola.append("Ingresa el índice de la primera carta que deseas intercambiar (1 a " + controlador.getJugadorCartas(nombreJugador).size() + "):\n");

        // Limpia los listeners previos del inputField
        limpiarActionListeners(input, ok);

        ActionListener solicitarPrimeraCarta = _ -> {
            try {
                int indice1 = Integer.parseInt(input.getText().trim());
                input.setText("");

                if (indice1 < 0 || indice1 > controlador.getJugadorCartas(nombreJugador).size()) {
                    consola.append("Índice inválido. Por favor, ingresa un número válido.\n");
                    return;
                }

                consola.append("Has seleccionado la carta en la posición " + (indice1) + ".\n");
                consola.append("Ingresa el índice de la segunda carta que deseas intercambiar (1 a " + controlador.getJugadorCartas(nombreJugador).size() + "):\n");

                // Cambiar el listener para solicitar la segunda carta
                limpiarActionListeners(input, ok);

                input.addActionListener(_ -> {
                    try {
                        int indice2 = Integer.parseInt(input.getText().trim());
                        input.setText("");

                        if (indice2 < 0 || indice2 > controlador.getJugadorCartas(nombreJugador).size()) {
                            consola.append("Índice inválido. Por favor, ingresa un número válido.\n");
                            return;
                        }

                        if (indice1 == indice2) {
                            consola.append("No puedes intercambiar una carta consigo misma. Por favor, intenta nuevamente.\n");
                            intercambiarCartas(); // Reiniciar proceso
                            return;
                        }

                        consola.append("Has seleccionado la carta en la posición " + (indice2) + ".\n");
                        consola.append("Intercambiando cartas...\n");

                        // Llama al controlador para realizar el intercambio
                        controlador.intercambiarCartas(indice1, indice2, nombreJugador);

                        consola.append("Cartas intercambiadas exitosamente.\n");
                        iniciarTurnos();
                    } catch (NumberFormatException ex) {
                        consola.append("Por favor, ingresa un número válido.\n");
                    }
                });
            } catch (NumberFormatException ex) {
                consola.append("Por favor, ingresa un número válido.\n");
            }
        };

        input.addActionListener(solicitarPrimeraCarta);
        ok.addActionListener(solicitarPrimeraCarta);
    }

    // [O/P] Metodo que permite ordenar cartas por valor o por palo.
    private void ordenarCartas(String s) {
        if (s.equals("p")) {
            controlador.ordenarPalo(nombreJugador);
        } else {
            controlador.ordenarValor(nombreJugador);
        }
        iniciarTurnos();
    }

    // Metodo para guardar o no la partida.
    private void saveGame(boolean b) {
        if (b) {
            consola.append("Ingresa el nombre con el que deseas guardar la partida:\n");

            // Listener para ingresar el nombre de la partida
            limpiarActionListeners(input, ok);
            input.addActionListener(_ -> {
                String nombrePartida = input.getText().trim();
                if (!nombrePartida.isEmpty()) {
                    controlador.guardarPartida(nombrePartida, true);
                    consola.append("Partida guardada exitosamente como " + nombrePartida + ". Saliendo del juego...\n");
                } else {
                    consola.append("El nombre de la partida no puede estar vacío. Intenta nuevamente.\n");
                }
            });
        } else {
            controlador.guardarPartida("", false);
            consola.append("Saliendo del juego sin guardar...\n");
        }
    }

    // Metodo para limpiar la consola
    private void limpiarConsola() {
        consola.setText("");
        input.setText("");
    }

    // Metodo para limpiar ActionListeners
    private void limpiarActionListeners(JTextField textField, JButton botonOk) {
        for (ActionListener al : textField.getActionListeners()) {
            textField.removeActionListener(al);
        }
        for (ActionListener al : botonOk.getActionListeners()) {
            botonOk.removeActionListener(al);
        }
    }

    // Metodo para cargar el titulo
    private void title() {
        consola.append("╔═╗╦ ╦╦╔╗╔╔═╗╦ ╦╔═╗╔╗╔\n");
        consola.append("║  ╠═╣║║║║║  ╠═╣║ ║║║║\n");
        consola.append("╚═╝╩ ╩╩╝╚╝╚═╝╩ ╩╚═╝╝╚╝\n\n");
        input.setText("");
    }
}
