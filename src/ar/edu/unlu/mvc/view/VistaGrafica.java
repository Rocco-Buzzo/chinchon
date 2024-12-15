package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.controller.Controlador;
import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.clases.Serializacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class VistaGrafica implements IVista {
    private String nombreJugador;
    private Controlador controlador;

    private JPanel cardPane;
    private CardLayout cardLayout;
    private Map<String, JPanel> jPanelMap;

    private final JFrame chinchonFrame = new JFrame("Chinchon - Main Menu");
    private static final Color BACKGROUND_COLOR = new Color(55, 101, 73);
    private static final Font BTN_FONT = new Font("Arial", Font.BOLD, 24);
    private static final String IMAGES_PATH = "./ar/edu/unlu/assets/";

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(chinchonFrame.getGraphicsConfiguration());

    // Ajustar el tamaño para que no cubra la barra de tareas
    private final int FRAME_WIDTH = screenSize.width - screenInsets.left - screenInsets.right;
    private final int FRAME_HEIGHT = screenSize.height - screenInsets.top - screenInsets.bottom;

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void cerrarRonda() {
        JPanel cerrarPane = jPanelMap.get("cerrar-pane");
        cerrarPane.removeAll();
        cerrarPane.setLayout(new BoxLayout(cerrarPane, BoxLayout.Y_AXIS)); // Layout en columna
        cerrarPane.setBackground(BACKGROUND_COLOR);

        ArrayList<String> jugadores = controlador.nombreJugadores();

        // Mensaje de fin de ronda
        JLabel rondaTerminada = new JLabel("¡Ronda terminada!");
        rondaTerminada.setFont(new Font("Arial", Font.BOLD, 32));
        rondaTerminada.setForeground(Color.YELLOW);
        rondaTerminada.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        cerrarPane.add(rondaTerminada);

        cerrarPane.add(Box.createVerticalStrut(20)); // Espacio entre elementos
        JLabel ganador = new JLabel("Ganador: " + controlador.getJugadorActual());
        ganador.setFont(new Font("Arial", Font.BOLD, 24));
        ganador.setForeground(Color.WHITE);
        ganador.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        cerrarPane.add(ganador);
        JLabel manoGanadora = new JLabel("Mano ganadora: ");
        manoGanadora.setFont(new Font("Arial", Font.BOLD, 24));
        manoGanadora.setForeground(Color.WHITE);
        manoGanadora.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        cerrarPane.add(manoGanadora);

        Dimension buttonSize = new Dimension(113, 200);

        JPanel manoGanadoraPane = new JPanel(new FlowLayout());
        manoGanadoraPane.setBackground(BACKGROUND_COLOR);
        for (Carta c : controlador.getManoGanadora()) {
            JButton button = new JButton();
            ImageIcon iconoOriginal = new ImageIcon(c.getImagen().getImage());
            Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(imagenRedimensionada));
            button.setBackground(BACKGROUND_COLOR);
            button.setPreferredSize(buttonSize);
            manoGanadoraPane.add(button);
        }
        cerrarPane.add(manoGanadoraPane);

        // Mostrar los puntajes
        for (String jugador : jugadores) {
            int puntos = controlador.getPuntaje(jugador);
            String mensaje = String.format("El jugador %s tiene %d puntos.", jugador, puntos);
            JLabel puntajes = new JLabel(mensaje);
            puntajes.setFont(new Font("Arial", Font.BOLD, 24));
            puntajes.setForeground(Color.WHITE);
            puntajes.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            cerrarPane.add(puntajes);
        }

        cerrarPane.add(Box.createVerticalStrut(30)); // Espacio entre los puntajes y el botón

        // Panel para el botón y el contador de jugadores listos
        JPanel readyPanel = new JPanel(new FlowLayout());
        readyPanel.setBackground(BACKGROUND_COLOR);

        JLabel readyLabel = new JLabel("Jugadores listos: 0/" + jugadores.size());
        readyLabel.setFont(new Font("Arial", Font.BOLD, 18));
        readyLabel.setForeground(Color.WHITE);

        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 18));
        btnContinuar.setPreferredSize(new Dimension(200, 50));
        btnContinuar.setBackground(new Color(135, 255, 75));
        btnContinuar.setForeground(Color.BLACK);

        // Lógica del botón para actualizar el contador y verificar la condición
        final int[] jugadoresListos = {0}; // Contador de jugadores listos
        btnContinuar.addActionListener(_ -> {
            jugadoresListos[0]++;
            readyLabel.setText("Jugadores listos: " + jugadoresListos[0] + "/" + jugadores.size());

            if (jugadoresListos[0] == jugadores.size() / 2) {
                iniciarTurnos(); // Llamar a la función iniciarTurnos cuando haya 2 listos
            }
        });

        readyPanel.add(btnContinuar);
        readyPanel.add(readyLabel);
        cerrarPane.add(readyPanel);
        cerrarPane.add(Box.createVerticalStrut(20)); // Espacio final

        cerrarPane.revalidate();
        cerrarPane.repaint();
        // Actualizar el panel principal
        cardLayout.show(cardPane, "cerrar-pane");
    }

    // Funciona
    @Override
    public void openGame() {
        chinchonFrame.repaint();
        chinchonFrame.revalidate();
        initMenu();
        if (controlador.getJugadoresSize() == 2) {
            renderMenu();
        } else {
            playerLogin();
        }
    }

    // Funciona
    @Override
    public void startGame() {
        controlador.iniciarPartida();
        iniciarTurnos();
    }

    @Override
    public void loadGame() {
        controlador.continuarPartida();
        iniciarTurnos();
    }

    @Override
    public void setJugador(String nombre) {
        this.nombreJugador = nombre;
    }

    // Funciona correctamente
    @Override
    public void iniciarTurnos() {
        if (nombreJugador.equals(controlador.getJugadorActual())) {
            renderInGame();
        } else {
            renderWaitingTurn();
        }
    }

    @Override
    public void finishGame(boolean guardado) {
        JPanel finishPane = jPanelMap.get("finish-pane");
        finishPane.removeAll();
        finishPane.setLayout(new GridBagLayout()); // Usar GridBagLayout para centrar
        finishPane.setBackground(BACKGROUND_COLOR); // Fondo azul oscuro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado entre componentes
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Mensaje del estado de la partida
        String mensaje = (controlador.getGanador() != null && !controlador.getGanador().isEmpty())
                ? "¡La partida ha finalizado! Ganador: " + controlador.getGanador()
                : "Partida cancelada.";

        if (guardado) {
            mensaje = "Partida guardada correctamente.";
        }

        JLabel lblMensaje = new JLabel(mensaje);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 24));
        lblMensaje.setForeground(Color.WHITE);
        finishPane.add(lblMensaje, gbc);

        // Mostrar la mano ganadora solo si no se guardó la partida y no fue cancelada
        if (!guardado && controlador.getGanador() != null && !controlador.getGanador().isEmpty()) {
            gbc.gridy++; // Mover a la siguiente fila
            JLabel lblManoGanadora = new JLabel("Mano ganadora:");
            lblManoGanadora.setFont(new Font("Arial", Font.BOLD, 24));
            lblManoGanadora.setForeground(Color.WHITE);
            finishPane.add(lblManoGanadora, gbc);

            gbc.gridy++; // Mover a la siguiente fila
            JPanel manoGanadoraPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            manoGanadoraPane.setBackground(BACKGROUND_COLOR);

            Dimension buttonSize = new Dimension(113, 200);
            for (Carta c : controlador.getManoGanadora()) {
                JButton button = new JButton();
                ImageIcon iconoOriginal = new ImageIcon(c.getImagen().getImage());
                Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(imagenRedimensionada));
                button.setBackground(BACKGROUND_COLOR);
                button.setPreferredSize(buttonSize);
                manoGanadoraPane.add(button);
            }
            finishPane.add(manoGanadoraPane, gbc);
        }

        // Mostrar puntajes finales de cada jugador
        ArrayList<String> jugadores = controlador.nombreJugadores();
        for (String jugador : jugadores) {
            gbc.gridy++; // Mover a la siguiente fila
            int puntos = controlador.getPuntaje(jugador);
            String puntajeMensaje = String.format("El jugador %s tiene %d puntos.", jugador, puntos);
            JLabel lblPuntaje = new JLabel(puntajeMensaje);
            lblPuntaje.setFont(new Font("Arial", Font.BOLD, 24));
            lblPuntaje.setForeground(Color.WHITE);
            finishPane.add(lblPuntaje, gbc);
        }

        // Botón para cerrar el juego
        gbc.gridy++; // Mover a la siguiente fila
        JButton btnCerrar = new JButton("Cerrar juego");
        btnCerrar.setFont(new Font("Arial", Font.PLAIN, 18));
        btnCerrar.setBackground(new Color(94, 180, 70));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(_ -> System.exit(0));
        finishPane.add(btnCerrar, gbc);

        // Refrescar el panel
        finishPane.revalidate();
        finishPane.repaint();

        // Mostrar el panel final
        cardLayout.show(cardPane, "finish-pane");
    }

    @Override
    public void actualizarMesa() {
        iniciarTurnos();
    }

    private void initMenu() {
        cardLayout = new CardLayout();
        chinchonFrame.setLayout(cardLayout);
        cardPane = new JPanel(cardLayout);
        chinchonFrame.add(cardPane);

        jPanelMap = new HashMap<>();

        // Panel del login donde el jugador ingresa su usuario para jugar.
        JPanel loginPane = new JPanel();
        cardPane.add(loginPane, "login-pane");
        jPanelMap.put("login-pane", loginPane);

        // Panel del menu principal, aquel que estan los botones para iniciar, unirse o cargar una partida
        JPanel menuPane = new JPanel();
        cardPane.add(menuPane, "menu-pane");
        jPanelMap.put("menu-pane", menuPane);

        JPanel settingsPane = new JPanel();
        cardPane.add(settingsPane, "settings-pane");
        jPanelMap.put("settings-pane", settingsPane);

        JPanel rulesPane = new JPanel();
        cardPane.add(rulesPane, "rules-pane");
        jPanelMap.put("rules-pane", rulesPane);

        JPanel inGamePane = new JPanel();
        cardPane.add(inGamePane, "ingame-pane");
        jPanelMap.put("ingame-pane", inGamePane);

        JPanel waitingTurnPane = new JPanel();
        cardPane.add(waitingTurnPane, "waitingturn-pane");
        jPanelMap.put("waitingturn-pane", waitingTurnPane);

        JPanel esperandoPane = new JPanel();
        cardPane.add(esperandoPane, "esperando-pane");
        jPanelMap.put("esperando-pane", esperandoPane);

        JPanel loadPane = new JPanel();
        cardPane.add(loadPane, "load-pane");
        jPanelMap.put("load-pane", loadPane);

        JPanel finishPane = new JPanel();
        cardPane.add(finishPane, "finish-pane");
        jPanelMap.put("finish-pane", finishPane);

        JPanel cerrarPane = new JPanel();
        cardPane.add(cerrarPane, "cerrar-pane");
        jPanelMap.put("cerrar-pane", cerrarPane);

        JPanel guardarPane = new JPanel();
        cardPane.add(guardarPane, "guardar-pane");
        jPanelMap.put("guardar-pane", guardarPane);
    }

    // Funciona correctamente.
    private void renderMenu() {
        chinchonFrame.setTitle("Chinchón");
        JPanel menu = jPanelMap.get("menu-pane");
        menu.removeAll();
        menu.setLayout(new GridBagLayout()); // Cambiamos a GridBagLayout para centrar
        menu.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        // Panel del título del juego
        JPanel imagePane = new ImagePanel(getClass().getClassLoader().getResource(IMAGES_PATH + "titulo.png"));
        imagePane.setPreferredSize(new Dimension(1024, 108));
        imagePane.setBackground(BACKGROUND_COLOR);

        // Panel para los botones
        JPanel btnPane = new JPanel(new GridBagLayout());
        btnPane.setBackground(BACKGROUND_COLOR);

        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.gridx = 0;
        btnGbc.gridy = 0;
        btnGbc.insets = new Insets(10, 10, 10, 10);

        Dimension buttonSize = new Dimension(350, 65);

        // Botón para iniciar la partida
        JButton btnStart = new JButton();
        btnStart.setFont(new Font("Arial", Font.BOLD, 24));
        btnStart.setText("Iniciar Partida");
        btnStart.setPreferredSize(buttonSize);
        btnStart.setBackground(new Color(26, 57, 44, 255));
        btnStart.setForeground(Color.WHITE);
        btnStart.addActionListener(_ -> {
            renderSettings();
            chinchonFrame.setTitle("Configuración de Partida");
        });

        JButton btnJoin = new JButton();
        btnJoin.setFont(new Font("Arial", Font.BOLD, 24));
        btnJoin.setText("Unirse a Partida");
        btnJoin.setPreferredSize(buttonSize);
        btnJoin.setBackground(new Color(26, 57, 44, 255));
        btnJoin.setForeground(Color.WHITE);
        btnJoin.addActionListener(_ -> {
            controlador.agregarJugador(nombreJugador);
            if (controlador.getJugadoresSize() == 1) {
                renderWaitingPlayers();
            } else {
                startGame();
            }
        });

        JButton btnLoad = new JButton();
        btnLoad.setFont(new Font("Arial", Font.BOLD, 24));
        btnLoad.setText("Cargar Partida");
        btnLoad.setPreferredSize(buttonSize);
        btnLoad.setBackground(new Color(26, 57, 44, 255));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.addActionListener(_ -> renderLoadGame());

        JButton btnRules = new JButton();
        btnRules.setFont(new Font("Arial", Font.BOLD, 24));
        btnRules.setText("Reglas");
        btnRules.setPreferredSize(buttonSize);
        btnRules.setBackground(new Color(26, 57, 44, 255));
        btnRules.setForeground(Color.WHITE);
        btnRules.addActionListener(_ -> renderRules());

        JButton btnQuit = new JButton();
        btnQuit.setFont(new Font("Arial", Font.BOLD, 24));
        btnQuit.setText("Salir");
        btnQuit.setPreferredSize(buttonSize);
        btnQuit.setBackground(new Color(26, 57, 44, 255));
        btnQuit.setForeground(Color.WHITE);
        btnQuit.addActionListener(_ -> {
            deleteJugador();
            cardLayout.show(cardPane, "login-pane");
        });

        // Agregar los botones al panel de botones
        btnPane.add(btnStart, btnGbc);
        btnGbc.gridy++;
        btnPane.add(btnLoad, btnGbc);
        btnGbc.gridy++;
        btnPane.add(btnJoin, btnGbc);
        btnGbc.gridy++;
        btnPane.add(btnRules, btnGbc);
        btnGbc.gridy++;
        btnPane.add(btnQuit, btnGbc);

        // Texto del jugador actual
        JLabel player = new JLabel();
        player.setText("Estás jugando como: " + this.nombreJugador);
        player.setBackground(BACKGROUND_COLOR);
        player.setForeground(Color.WHITE);
        player.setFont(new Font("Arial", Font.BOLD, 16));
        player.setHorizontalAlignment(JLabel.CENTER);

        // Agregar componentes al panel principal centrado
        gbc.gridy = 0;
        menu.add(imagePane, gbc);
        gbc.gridy = 1;
        menu.add(btnPane, gbc);
        gbc.gridy = 2;
        menu.add(player, gbc);

        // Actualizar y mostrar el panel
        menu.repaint();
        menu.revalidate();
        cardLayout.show(cardPane, "menu-pane");
    }

    private void renderLoadGame() {
        chinchonFrame.setTitle("Cargar una Partida");
        JPanel load = jPanelMap.get("load-pane");
        load.removeAll();
        load.setLayout(new BorderLayout());
        load.setBackground(BACKGROUND_COLOR);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Introduzca el nombre de la partida a cargar");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titleLabel.setForeground(Color.yellow);
        load.add(titleLabel, BorderLayout.NORTH);

        ArrayList<String> partidasGuardadas = Serializacion.listarPartidas(nombreJugador);
        if (partidasGuardadas.isEmpty()) {
            notificar("No hay partidas guardadas.");
            renderMenu();
            return;
        }

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (String partida : partidasGuardadas) {
            comboBoxModel.addElement(partida);
        }
        JComboBox<String> partidasComboBox = new JComboBox<>(comboBoxModel);
        partidasComboBox.setBackground(Color.WHITE);
        partidasComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        partidasComboBox.setPreferredSize(new Dimension(480, 30));
        partidasComboBox.setSelectedIndex(-1); // No seleccionar ningún ítem por defecto
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        centerPanel.add(partidasComboBox, gbc);

        load.add(centerPanel, BorderLayout.CENTER);

        // Campo de texto editable para introducir el nombre de la partida
        JTextField nombrePartidaTextField = new JTextField();
        nombrePartidaTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        nombrePartidaTextField.setPreferredSize(new Dimension(480, 30));
        nombrePartidaTextField.setHorizontalAlignment(JTextField.CENTER);
        nombrePartidaTextField.setText((String) comboBoxModel.getSelectedItem());
        gbc.gridy = 1;
        centerPanel.add(nombrePartidaTextField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton cargarButton = new JButton("Cargar");
        cargarButton.setFont(new Font("Arial", Font.BOLD, 14));
        cargarButton.setBackground(new Color(0x61C462));
        cargarButton.setPreferredSize(new Dimension(175, 30));
        cargarButton.addActionListener(_ -> {
            String partidaSeleccionada = nombrePartidaTextField.getText().trim();
            if (partidaSeleccionada.isEmpty()) {
                notificar("Debe introducir o seleccionar un nombre de partida válido.");
            } else {
                if (controlador.cargarPartida(partidaSeleccionada)) {
                    notificar("Partida cargada correctamente.");
                } else {
                    notificar("La partida seleccionada no existe.");
                }
            }
        });

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelarButton.setBackground(new Color(0xFF3C3C));
        cancelarButton.setPreferredSize(new Dimension(175, 30));
        cancelarButton.addActionListener(_ -> cardLayout.show(cardPane, "menu-pane"));

        buttonPanel.add(cargarButton);
        buttonPanel.add(cancelarButton);

        load.add(buttonPanel, BorderLayout.SOUTH);

        load.repaint();
        load.revalidate();
        cardLayout.show(cardPane, "load-pane");
    }

    private void renderSettings() {
        chinchonFrame.setTitle("Reglas del Chinchón");
        chinchonFrame.setLocationRelativeTo(null);

        JPanel settings = jPanelMap.get("settings-pane");
        settings.removeAll();
        settings.repaint();
        settings.revalidate();
        settings.setLayout(new GridBagLayout()); // Layout para centrar
        settings.setBackground(BACKGROUND_COLOR);

        // Logo - Panel de imagen
        JPanel imagePane = new ImagePanel(getClass().getClassLoader().getResource(IMAGES_PATH + "titulo.png"));
        imagePane.setPreferredSize(new Dimension(1024, 108)); // Ajusta el tamaño más grande
        imagePane.setBackground(BACKGROUND_COLOR);

        // Panel de configuración
        JPanel settingsPane = new JPanel(new GridBagLayout());
        settingsPane.setBackground(BACKGROUND_COLOR);

        // Opciones
        JLabel cartasOption = new JLabel("CANTIDAD DE CARTAS", JLabel.CENTER);
        cartasOption.setForeground(Color.WHITE);
        cartasOption.setFont(new Font("Arial", Font.BOLD, 24));

        String[] cartasBoxItems = {"40", "48"};
        JComboBox<String> cartasBox = new JComboBox<>(cartasBoxItems);
        cartasBox.setFont(new Font("Arial", Font.BOLD, 16));
        cartasBox.setPreferredSize(new Dimension(100, 25));

        JLabel comodinOption = new JLabel("COMODINES", JLabel.CENTER);
        comodinOption.setForeground(Color.WHITE);
        comodinOption.setFont(new Font("Arial", Font.BOLD, 24));

        String[] comodinBoxItems = {"No", "Si"};
        JComboBox<String> comodinBox = new JComboBox<>(comodinBoxItems);
        comodinBox.setFont(new Font("Arial", Font.BOLD, 16));
        comodinBox.setPreferredSize(new Dimension(100, 25));

        JLabel puntosOption = new JLabel("PUNTOS MÁXIMOS", JLabel.CENTER);
        puntosOption.setForeground(Color.WHITE);
        puntosOption.setFont(new Font("Arial", Font.BOLD, 24));

        String[] puntosBoxItems = {"50", "100"};
        JComboBox<String> puntosBox = new JComboBox<>(puntosBoxItems);
        puntosBox.setFont(new Font("Arial", Font.BOLD, 16));
        puntosBox.setPreferredSize(new Dimension(100, 25));

        JButton btnAccept = new JButton("Aceptar");
        btnAccept.setFont(new Font("Arial", Font.BOLD, 18));
        btnAccept.setPreferredSize(new Dimension(150, 50));
        btnAccept.setBackground(new Color(36, 184, 97));
        btnAccept.setForeground(Color.WHITE);
        btnAccept.addActionListener(_ -> {
            int cantidadCartas = Objects.equals(cartasBox.getSelectedItem(), "40") ? 40 : 48;
            boolean contieneComodin = !Objects.equals(comodinBox.getSelectedItem(), "No");
            int puntosMaximos = Objects.equals(puntosBox.getSelectedItem(), "50") ? 50 : 100;

            controlador.establecerValores(contieneComodin, cantidadCartas, puntosMaximos);
            controlador.agregarJugador(nombreJugador);
            if (controlador.getJugadoresSize() == 1) {
                renderWaitingPlayers();
            } else {
                startGame();
            }
        });

        JButton btnBack = new JButton("Volver");
        btnBack.setFont(new Font("Arial", Font.BOLD, 18));
        btnBack.setPreferredSize(new Dimension(150, 50));
        btnBack.setBackground(new Color(255, 60, 60));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(_ -> cardLayout.show(cardPane, "menu-pane"));

        // Agregar componentes al settingsPane con GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0;
        settingsPane.add(cartasOption, gbc);
        gbc.gridy = 1;
        settingsPane.add(cartasBox, gbc);
        gbc.gridy = 2;
        settingsPane.add(comodinOption, gbc);
        gbc.gridy = 3;
        settingsPane.add(comodinBox, gbc);
        gbc.gridy = 4;
        settingsPane.add(puntosOption, gbc);
        gbc.gridy = 5;
        settingsPane.add(puntosBox, gbc);
        gbc.gridy = 6;
        settingsPane.add(btnAccept, gbc);
        gbc.gridy = 7;
        settingsPane.add(btnBack, gbc);

        // Centrar el logo y el settingsPane en el panel principal
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.insets = new Insets(0, 0, 20, 0); // Espaciado
        settings.add(imagePane, mainGbc);

        mainGbc.gridy = 1;
        settings.add(settingsPane, mainGbc);

        cardLayout.show(cardPane, "settings-pane");
    }

    // Funciona correctamente.
    private void renderRules() {
        chinchonFrame.setTitle("Reglas del Chinchón");
        chinchonFrame.setLocationRelativeTo(null);
        JPanel rules = jPanelMap.get("rules-pane");
        rules.removeAll();
        rules.setLayout(new BorderLayout());
        rules.setBackground(BACKGROUND_COLOR);

        JPanel ruleDescription = new JPanel(new GridBagLayout());
        ruleDescription.setBackground(BACKGROUND_COLOR);

        JLabel ruleTitle = new JLabel("Reglas del CHINCHÓN");
        ruleTitle.setBackground(BACKGROUND_COLOR);
        ruleTitle.setForeground(Color.yellow);
        ruleTitle.setFont(new Font("Arial", Font.BOLD, 38));
        ruleTitle.setHorizontalAlignment(JLabel.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Regla JUGAR CARTAS
        gbc.gridy = 0;
        JLabel jugarCartasTitle = new JLabel("Jugar las cartas");
        jugarCartasTitle.setBackground(BACKGROUND_COLOR);
        jugarCartasTitle.setForeground(Color.yellow);
        jugarCartasTitle.setFont(new Font("Arial", Font.BOLD, 24));
        jugarCartasTitle.setHorizontalAlignment(JLabel.CENTER);
        ruleDescription.add(jugarCartasTitle, gbc);

        gbc.gridy = 1;
        JTextArea jugarCartasDescription = createTextArea("Cada jugador en su turno debe agarrar la carta superior del mazo, o bien, si considera que le conviene, la carta descubierta en el descarte. Luego de agarrar esa carta, y con 8 cartas en la mano, el jugador debe lanzar una de ellas al descarte, sobre las que hay boca arriba.");
        ruleDescription.add(jugarCartasDescription, gbc);

        // REGLA LIGAR CARTAS
        gbc.gridy = 2;
        JLabel ligarCartasTitle = new JLabel("Ligar las cartas");
        ligarCartasTitle.setBackground(BACKGROUND_COLOR);
        ligarCartasTitle.setForeground(Color.yellow);
        ligarCartasTitle.setFont(new Font("Arial", Font.BOLD, 24));
        ligarCartasTitle.setHorizontalAlignment(JLabel.CENTER);
        ruleDescription.add(ligarCartasTitle, gbc);

        gbc.gridy = 3;
        JTextArea ligarCartasDescription = createTextArea("En cada mano, los jugadores deben intentar ligar sus cartas. Ligar las cartas es hacer grupos de al menos tres cartas unidas por uno de los criterios siguientes: Cartas con el mismo índice. Cartas de un mismo palo que forman una escalera.");
        ruleDescription.add(ligarCartasDescription, gbc);

        // REGLA CERRAR MANO
        gbc.gridy = 4;
        JLabel cerrarManoTitle = new JLabel("Cerrar la mano");
        cerrarManoTitle.setBackground(BACKGROUND_COLOR);
        cerrarManoTitle.setForeground(Color.yellow);
        cerrarManoTitle.setFont(new Font("Arial", Font.BOLD, 24));
        cerrarManoTitle.setHorizontalAlignment(JLabel.CENTER);
        ruleDescription.add(cerrarManoTitle, gbc);

        gbc.gridy = 5;
        JTextArea cerrarManoDescription = createTextArea("Un jugador en su turno, al deshacerse de una carta, puede cerrar la mano si sus cartas cumplen una de estas condiciones: Forman una escalera de siete cartas. Están ligadas en dos grupos, uno de tres cartas y otro de cuatro. Están ligadas en dos grupos de tres cartas (u ocasionalmente una escalera de seis cartas), y la séptima carta tiene un valor inferior a cinco.");
        ruleDescription.add(cerrarManoDescription, gbc);

        // REGLA COLOCAR CARTAS
        gbc.gridy = 6;
        JLabel colocarCartasTitle = new JLabel("Colocar las cartas");
        colocarCartasTitle.setBackground(BACKGROUND_COLOR);
        colocarCartasTitle.setForeground(Color.yellow);
        colocarCartasTitle.setFont(new Font("Arial", Font.BOLD, 24));
        colocarCartasTitle.setHorizontalAlignment(JLabel.CENTER);
        ruleDescription.add(colocarCartasTitle, gbc);

        gbc.gridy = 7;
        JTextArea colocarCartasDescription = createTextArea("En el Chinchón, una vez que un jugador ha cerrado una mano, los demás deben intentar sumar la menor cantidad posible de puntos. Para ello, los jugadores van mostrando los grupos de cartas ligadas.");
        ruleDescription.add(colocarCartasDescription, gbc);

        // Regla RECUENTO MANO
        gbc.gridy = 8;
        JLabel recuentoManoTitle = new JLabel("Recuento de la mano");
        recuentoManoTitle.setBackground(BACKGROUND_COLOR);
        recuentoManoTitle.setForeground(Color.yellow);
        recuentoManoTitle.setFont(new Font("Arial", Font.BOLD, 24));
        recuentoManoTitle.setHorizontalAlignment(JLabel.CENTER);
        ruleDescription.add(recuentoManoTitle, gbc);

        gbc.gridy = 9;
        JTextArea recuentoManoDescription = createTextArea("Una vez colocadas todas las cartas ligadas, cada jugador suma los puntos de las cartas restantes. Si el jugador que cierra la mano lo hace ligando sus 7 cartas, obtiene el premio de restar puntos: \nMenos 10 (-10) con dos grupos de cartas (uno de tres y otro de cuatro).\nMenos 25 (-25) con una escalera de siete cartas con dos comodines.\nMenos 50 (-50) con una escalera de siete cartas con un comodín.\nChinchón: con una escalera de siete cartas sin comodines gana la partida directamente.");
        ruleDescription.add(recuentoManoDescription, gbc);

        JScrollPane scrollPane = new JScrollPane(ruleDescription);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        JButton btnBack = new JButton();
        btnBack.setFont(new Font("Arial", Font.BOLD, 18));
        btnBack.setText("Volver");
        btnBack.setPreferredSize(new Dimension(150, 50));
        btnBack.setBackground(new Color(255, 60, 60, 255));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(_ -> {
            cardLayout.show(cardPane, "menu-pane");
            chinchonFrame.setLocationRelativeTo(null);
        });

        rules.add(ruleTitle, BorderLayout.NORTH);
        rules.add(scrollPane, BorderLayout.CENTER);
        rules.add(btnBack, BorderLayout.SOUTH);

        rules.repaint();
        rules.revalidate();

        cardLayout.show(cardPane, "rules-pane");
    }

    // Funcionatodo menos el guardar o salir
    private void renderInGame() {
        chinchonFrame.setTitle("Chinchon - In Game - " + nombreJugador);
        chinchonFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        chinchonFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                renderExitGame();
            }
        });
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        JPanel inGame = jPanelMap.get("ingame-pane");
        inGame.removeAll();
        inGame.setLayout(new BorderLayout());

        JPanel cardsPanel = new JPanel();
        renderCardsPane(cardsPanel);

        // Panel de opciones
        JPanel playerOptionsPanel = new JPanel(new BorderLayout());
        playerOptionsPanel.setBackground(new Color(0x254633));

        // Panel de Información
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(0x254633));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel puntajeLabel = new JLabel("Tu puntaje: " + controlador.getPuntaje(nombreJugador));
        puntajeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        puntajeLabel.setForeground(Color.WHITE);
        infoPanel.add(puntajeLabel, gbc);

        gbc.gridy = 1;
        JLabel turnoLabel = new JLabel("Turno de " + controlador.getJugadorActual());
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        turnoLabel.setForeground(Color.WHITE);
        infoPanel.add(turnoLabel, gbc);

        // Panel de Botones
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0x254633));

        // Insertamos boton de Cerrar Ronda
        gbc.gridy = 0;
        JButton btnCerraRonda = new JButton("Cerrar Ronda");
        configurarBoton(btnCerraRonda);
        btnCerraRonda.addActionListener(_ -> controlador.cerrarRonda());
        buttonPanel.add(btnCerraRonda, gbc);

        // Insertamos boton de Robar del Mazo
        gbc.gridy = 1;
        JButton btnRobarMazo = new JButton("Robar Carta Mazo");
        configurarBoton(btnRobarMazo);
        btnRobarMazo.addActionListener(_ -> {
            controlador.robarCartaMazo();
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnRobarMazo, gbc);

        // Insertamos boton de Robar del Descarte
        gbc.gridy = 2;
        JButton btnRobarDescarte = new JButton("Robar Carta Descarte");
        configurarBoton(btnRobarDescarte);
        btnRobarDescarte.addActionListener(_ -> {
            controlador.robarCartaDescarte();
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnRobarDescarte, gbc);

        // Insertamos boton de Intercambiar Cartas
        gbc.gridy = 3;
        JButton btnIntercambiar = new JButton("Intercambiar Cartas");
        configurarBoton(btnIntercambiar);
        btnIntercambiar.addActionListener(_ -> {
            renderExchangeCards();
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnIntercambiar, gbc);

        // Insertamos boton de Robar de Descarte
        gbc.gridy = 4;
        JButton btnOrdenarPalo = new JButton("Ordenar por Palo");
        configurarBoton(btnOrdenarPalo);
        btnOrdenarPalo.addActionListener(_ -> {
            controlador.ordenarPalo(nombreJugador);
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnOrdenarPalo, gbc);

        gbc.gridy = 5;
        JButton btnOrdenarValor = new JButton("Ordenar por Valor");
        configurarBoton(btnOrdenarValor);
        btnOrdenarValor.addActionListener(_ -> {
            controlador.ordenarValor(nombreJugador);
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnOrdenarValor, gbc);

        playerOptionsPanel.add(infoPanel, BorderLayout.NORTH);
        playerOptionsPanel.add(buttonPanel, BorderLayout.CENTER);

        inGame.add(playerOptionsPanel, BorderLayout.WEST);
        inGame.add(cardsPanel, BorderLayout.CENTER);
        inGame.repaint();
        inGame.revalidate();
        cardLayout.show(cardPane, "ingame-pane");
    }

    // Funciona correctamente.
    private void renderCardsPane(JPanel cardsPanel) {
        cardsPanel.removeAll();
        cardsPanel.setBackground(BACKGROUND_COLOR);
        cardsPanel.setLayout(new BorderLayout());

        JPanel cartasEnMano = new JPanel(new FlowLayout());
        cartasEnMano.setBackground(BACKGROUND_COLOR);
        Dimension buttonSize = new Dimension(113, 200);

        ArrayList<Carta> cartasAMostrar;

        // Si es el turno del jugador actual, mostramos sus cartas; si no, mostramos las del rival.
        cartasAMostrar = controlador.getJugadorCartas(nombreJugador);

        // Renderizar botones de cartas
        for (Carta carta : cartasAMostrar) {
            JButton button = new JButton();
            ImageIcon iconoOriginal = new ImageIcon(carta.getImagen().getImage());
            Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(imagenRedimensionada));
            button.setBackground(BACKGROUND_COLOR);
            button.setPreferredSize(buttonSize);

            // Solo añadimos acción para el jugador en turno
            if (controlador.getJugadorActual().equals(nombreJugador)) {
                button.addActionListener(_ -> controlador.tirarCarta(controlador.getCartaPosition(carta)));
            }

            cartasEnMano.add(button);
        }

        JPanel cartasRival = new JPanel(new FlowLayout());
        int cantidadCartasRival;
        if (nombreJugador.equals(controlador.getJugadorActual())) {
            cantidadCartasRival = controlador.sizeCartasJugadorNoActual();
        } else {
            cantidadCartasRival = controlador.getJugadorCartas(controlador.getJugadorActual()).size();
        }
        cartasRival.setBackground(BACKGROUND_COLOR);

        // Renderizar botones de cartas
        for (int i = 0; i < cantidadCartasRival; i++) {
            JButton button = new JButton();
            ImageIcon iconoOriginal = new ImageIcon("src/ar/edu/unlu/assets/REVERSO.png");
            Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(imagenRedimensionada));
            button.setBackground(BACKGROUND_COLOR);
            button.setBorderPainted(false);
            button.setPreferredSize(buttonSize);
            cartasRival.add(button);
        }

        JPanel mazoYDescarte = new JPanel(new GridBagLayout());
        mazoYDescarte.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Renderizar botones de MAZO
        JButton button = new JButton();
        ImageIcon iconoOriginal = new ImageIcon("src/ar/edu/unlu/assets/REVERSO.png");
        buttonSize = new Dimension(203, 360);
        Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(imagenRedimensionada));
        button.setBackground(BACKGROUND_COLOR);
        button.setPreferredSize(buttonSize);
        if (nombreJugador.equals(controlador.getJugadorActual())) {
            button.addActionListener(_ -> {
                controlador.robarCartaMazo();
                renderCardsPane(cardsPanel);
            });
        }
        mazoYDescarte.add(button, gbc);

        gbc.gridx = 1;
        // Renderizar boton de DESCARTE
        button = new JButton();
        if (controlador.topeDescarte() != null) {
            iconoOriginal = new ImageIcon("src/ar/edu/unlu/assets/" + controlador.topeDescarte().toString() + ".png");
        } else {
            iconoOriginal = new ImageIcon("src/ar/edu/unlu/assets/DESCARTE.png");
        }
        imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(buttonSize.width, buttonSize.height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(imagenRedimensionada));
        button.setBackground(BACKGROUND_COLOR);
        button.setPreferredSize(buttonSize);
        if (nombreJugador.equals(controlador.getJugadorActual())) {
            button.addActionListener(_ -> {
                controlador.robarCartaDescarte();
                renderCardsPane(cardsPanel);
            });
        }
        mazoYDescarte.add(button, gbc);

        cardsPanel.add(cartasRival, BorderLayout.NORTH);
        cardsPanel.add(cartasEnMano, BorderLayout.SOUTH);
        cardsPanel.add(mazoYDescarte, BorderLayout.CENTER);
        cardsPanel.repaint();
        cardsPanel.revalidate();
    }

    // Funciona
    private void renderWaitingTurn() {
        chinchonFrame.setTitle("Chinchon - Waiting - " + nombreJugador);
        chinchonFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        chinchonFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                renderExitGame();
            }
        });
        JPanel wait = jPanelMap.get("waitingturn-pane");
        wait.removeAll();
        wait.setLayout(new BorderLayout());

        JPanel cardsPanel = new JPanel();
        renderCardsPane(cardsPanel);

        // Panel de opciones
        JPanel playerOptionsPanel = new JPanel(new BorderLayout());
        playerOptionsPanel.setBackground(new Color(0x254633));

        // Panel de Información
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(0x254633));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel puntajeLabel = new JLabel("Puntaje: " + controlador.getPuntaje(nombreJugador));
        puntajeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        puntajeLabel.setForeground(Color.WHITE);
        infoPanel.add(puntajeLabel, gbc);

        gbc.gridy = 1;
        JLabel turnoLabel = new JLabel("Turno de " + controlador.getJugadorActual());
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        turnoLabel.setForeground(Color.WHITE);
        infoPanel.add(turnoLabel, gbc);

        // Panel de Botones
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0x254633));

        // Insertamos boton de Intercambiar Cartas
        gbc.gridy = 0;
        JButton btnIntercambiar = new JButton("Intercambiar Cartas");
        configurarBoton(btnIntercambiar);
        btnIntercambiar.addActionListener(_ -> {
            renderExchangeCards();
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnIntercambiar, gbc);

        // Insertamos boton de Robar de Descarte
        gbc.gridy = 1;
        JButton btnOrdenarPalo = new JButton("Ordenar por Palo");
        configurarBoton(btnOrdenarPalo);
        btnOrdenarPalo.addActionListener(_ -> {
            controlador.ordenarPalo(nombreJugador);
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnOrdenarPalo, gbc);

        gbc.gridy = 2;
        JButton btnOrdenarValor = new JButton("Ordenar por Valor");
        configurarBoton(btnOrdenarValor);
        btnOrdenarValor.addActionListener(_ -> {
            controlador.ordenarValor(nombreJugador);
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnOrdenarValor, gbc);

        playerOptionsPanel.add(infoPanel, BorderLayout.NORTH);
        playerOptionsPanel.add(buttonPanel, BorderLayout.CENTER);

        wait.add(playerOptionsPanel, BorderLayout.WEST);
        wait.add(cardsPanel, BorderLayout.CENTER);
        wait.repaint();
        wait.revalidate();
        cardLayout.show(cardPane, "waitingturn-pane");
    }

    // Funcionatodo menos el guardar o salir
    private void renderWaitingPlayers() {
        chinchonFrame.setTitle("Esperando Jugadores");

        JPanel esperando = jPanelMap.get("esperando-pane");
        esperando.removeAll();
        esperando.setLayout(new BorderLayout());
        esperando.setBackground(BACKGROUND_COLOR);

        JLabel jLabel = new JLabel("Esperando Jugadores ...");
        jLabel.setFont(BTN_FONT);
        jLabel.setForeground(Color.WHITE);
        jLabel.setHorizontalAlignment(JLabel.CENTER);

        esperando.add(jLabel, BorderLayout.CENTER);
        esperando.repaint();
        esperando.revalidate();
        cardLayout.show(cardPane, "esperando-pane");
    }

    private void renderExitGame() {
        chinchonFrame.setTitle("Chinchón - Guardar Partida");

        JPanel guardarPane = jPanelMap.get("guardar-pane");
        guardarPane.removeAll();
        guardarPane.setBackground(BACKGROUND_COLOR);
        guardarPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Mensaje
        JLabel mensaje = new JLabel("¿Quieres guardar la partida antes de salir?", JLabel.CENTER);
        mensaje.setFont(new Font("Arial", Font.BOLD, 36));
        mensaje.setForeground(Color.WHITE);

        // Campo de texto
        JTextField nombrePartidaTextField = new JTextField();
        nombrePartidaTextField.setFont(new Font("Arial", Font.PLAIN, 36));
        nombrePartidaTextField.setPreferredSize(new Dimension(480, 50));
        nombrePartidaTextField.setHorizontalAlignment(JTextField.CENTER);

        // Botones
        JButton guardarButton = new JButton("Guardar");
        configurarBoton(guardarButton, Color.GREEN, _ -> {
            String partidaAguardar = nombrePartidaTextField.getText().trim();
            if (partidaAguardar.isEmpty()) {
                notificar("Debe introducir un nombre para la partida.");
            } else {
                controlador.guardarPartida(partidaAguardar, true);
            }
        });

        JButton noGuardarButton = new JButton("No Guardar");
        configurarBoton(noGuardarButton, Color.RED, _ -> controlador.guardarPartida("", false));

        JButton cancelarButton = new JButton("Cancelar");
        configurarBoton(cancelarButton, Color.DARK_GRAY, _ -> iniciarTurnos());

        // Panel de botones
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botonesPanel.setBackground(BACKGROUND_COLOR);
        botonesPanel.add(guardarButton);
        botonesPanel.add(noGuardarButton);
        botonesPanel.add(cancelarButton);

        // Agregar componentes al panel principal
        guardarPane.add(mensaje, gbc);
        guardarPane.add(nombrePartidaTextField, gbc);
        guardarPane.add(botonesPanel, gbc);

        // Refrescar panel
        guardarPane.revalidate();
        guardarPane.repaint();
        cardLayout.show(cardPane, "guardar-pane");
    }

    private void configurarBoton(JButton boton, Color colorFondo, ActionListener action) {
        boton.setFont(new Font("Arial", Font.BOLD, 36));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.addActionListener(action);
    }

    private void renderExchangeCards() {
        JTextField campoX = new JTextField(5);
        JTextField campoY = new JTextField(5);

        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.add(new JLabel("N:"));
        panel.add(campoX);
        panel.add(Box.createHorizontalStrut(15)); // Espacio entre campos
        panel.add(new JLabel("M:"));
        panel.add(campoY);

        int resultado = JOptionPane.showConfirmDialog(null, panel,
                "Intercambiar carta;", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int x = Integer.parseInt(campoX.getText().trim());
                int y = Integer.parseInt(campoY.getText().trim());
                controlador.intercambiarCartas(x, y, nombreJugador);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        iniciarTurnos();
    }

    private void configurarBoton(JButton boton) {
        Dimension buttonSize = new Dimension(350, 45); // Ancho: 275, Alto: 75
        boton.setFont(BTN_FONT);
        boton.setBackground(new Color(26, 57, 44));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(buttonSize);
    }

    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Arial", Font.BOLD, 18));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        return textArea;
    }

    private void playerLogin() {
        // Configuración de la ventana principal
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setTitle("Chinchón - Login");
        chinchonFrame.setLocationRelativeTo(null);
        chinchonFrame.setResizable(false);
        chinchonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chinchonFrame.setBackground(BACKGROUND_COLOR);
        chinchonFrame.setIconImage(new ImageIcon("src/ar/edu/unlu/assets/ICONO.png").getImage());

        // Panel principal
        JPanel loginPane = jPanelMap.get("login-pane");
        loginPane.removeAll();
        loginPane.setLayout(new GridBagLayout());
        loginPane.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titlePane = new JLabel("Ingrese el nombre de usuario", JLabel.CENTER);
        titlePane.setFont(new Font("Arial", Font.BOLD, 24));
        titlePane.setForeground(Color.WHITE);
        gbc.gridy = 0;
        loginPane.add(titlePane, gbc);

        // Panel de entrada de datos
        JPanel contentPane = new JPanel(new FlowLayout());
        contentPane.setBackground(BACKGROUND_COLOR);

        JLabel labelName = new JLabel("Nombre: ");
        labelName.setFont(BTN_FONT);
        labelName.setForeground(Color.WHITE);

        JTextField nameTextField = new JTextField();
        nameTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameTextField.setPreferredSize(new Dimension(480, 50));

        contentPane.add(labelName);
        contentPane.add(nameTextField);
        gbc.gridy = 1;
        loginPane.add(contentPane, gbc);

        // Panel de botones
        JPanel btnPane = new JPanel(new FlowLayout());
        btnPane.setBackground(BACKGROUND_COLOR);

        Dimension buttonSize = new Dimension(192, 36);

        JButton btnAccept = new JButton("Aceptar");
        btnAccept.setBackground(new Color(135, 255, 75));
        btnAccept.setPreferredSize(buttonSize);
        btnAccept.setFont(BTN_FONT);

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(new Color(255, 60, 60));
        btnCancel.setPreferredSize(buttonSize);
        btnCancel.setFont(BTN_FONT);

        btnPane.add(btnAccept);
        btnPane.add(btnCancel);
        gbc.gridy = 2;
        loginPane.add(btnPane, gbc);

        // Listeners compartidos para botón Aceptar y tecla Enter
        Runnable acceptAction = () -> {
            String playerName = nameTextField.getText();
            if (playerName.isBlank()) {
                notificar("Ingrese el nombre del jugador antes de continuar.");
            } else {
                setJugador(playerName);
                renderMenu();
            }
        };

        // Acción para el botón "Aceptar"
        btnAccept.addActionListener(_ -> acceptAction.run());

        // Acción para la tecla "Enter"
        nameTextField.addActionListener(_ -> acceptAction.run());

        // Acción para el botón "Cancelar"
        btnCancel.addActionListener(_ -> onCancel());

        // Mostrar el panel
        cardLayout.show(cardPane, "login-pane");
        chinchonFrame.setVisible(true);
    }

    private void onCancel() {
        System.exit(0);
    }

    private void deleteJugador() {
        this.nombreJugador = null;
    }

    private void notificar(String msg) {
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", BTN_FONT);
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", BTN_FONT);

        JOptionPane.showMessageDialog(cardPane, msg, "Notificación", JOptionPane.PLAIN_MESSAGE);
    }
}
