package ar.edu.unlu.mvc.view;

import ar.edu.unlu.mvc.controller.Controlador;
import ar.edu.unlu.mvc.model.clases.Carta;
import ar.edu.unlu.mvc.model.clases.Serializacion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VistaGrafica implements IVista {
    private String nombreJugador;
    private Controlador controlador;

    private JPanel cardPane;
    private CardLayout cardLayout;
    private Map<String, JPanel> jPanelMap;

    private final JFrame chinchonFrame = new JFrame("Chinchon - Main Menu");
    private static final Color BACKGROUND_COLOR = new Color(55, 101, 73);
    private static final Font BTN_FONT = new Font("Montserrat", Font.BOLD, 16);
    private static final String IMAGES_PATH = "./ar/edu/unlu/assets/";

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(chinchonFrame.getGraphicsConfiguration());

    // Ajustar el tamaño para que no cubra la barra de tareas
    private final int FRAME_WIDTH = screenSize.width - screenInsets.left - screenInsets.right;
    private final int FRAME_HEIGHT = screenSize.height - screenInsets.top - screenInsets.bottom;


    private String jugadorActual;

    @Override
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void openGame() {
        playerLogin();
    }

    private void initMenu() {
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        chinchonFrame.setResizable(false);
        chinchonFrame.setLocationRelativeTo(null);
        chinchonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chinchonFrame.setBackground(BACKGROUND_COLOR);
        chinchonFrame.setIconImage(new ImageIcon("./ar/edu/unlu/assets/ICONO.png").getImage());

        cardLayout = new CardLayout();
        chinchonFrame.setLayout(cardLayout);
        cardPane = new JPanel(cardLayout);
        chinchonFrame.add(cardPane);

        jPanelMap = new HashMap<>();

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

        renderMenu();
        chinchonFrame.setVisible(true);
    }

    private void renderMenu() {
        chinchonFrame.setTitle("Chinchón");
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        chinchonFrame.setLocationRelativeTo(null);
        JPanel menu = jPanelMap.get("menu-pane");
        menu.removeAll();
        menu.repaint();
        menu.revalidate();
        menu.setLayout(new BorderLayout());
        menu.setBackground(BACKGROUND_COLOR);

        // Panel del titulo del juego
        JPanel imagePane = new ImagePanel(getClass().getClassLoader().getResource(IMAGES_PATH + "titulo.png"));
        imagePane.setPreferredSize(new Dimension(156, 192));
        imagePane.setBackground(BACKGROUND_COLOR);

        // Panel para los botones
        JPanel btnPane = new JPanel(new GridBagLayout());
        btnPane.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        Dimension buttonSize = new Dimension(350, 65);

        // Boton para iniciar la partida
        JButton btnStart = new JButton();
        btnStart.setFont(new Font("Montserrat", Font.BOLD, 24));
        btnStart.setText("Iniciar Partida");
        btnStart.setPreferredSize(buttonSize);
        btnStart.setBackground(new Color(26, 57, 44, 255));
        btnStart.setForeground(Color.WHITE);
        btnStart.addActionListener(_ -> {
            renderSettings();
            chinchonFrame.setTitle("Configuración de Partida");
        });

        JButton btnJoin = new JButton();
        btnJoin.setFont(new Font("Montserrat", Font.BOLD, 24));
        btnJoin.setText("Unirse a Partida");
        btnJoin.setPreferredSize(buttonSize);
        btnJoin.setBackground(new Color(26, 57, 44, 255));
        btnJoin.setForeground(Color.WHITE);
        btnJoin.addActionListener(_ -> {
            controlador.agregarJugador(nombreJugador);
            if (controlador.getJugadoresSize() == 1) {
                esperandoJugadores();
            }
        });

        // Boton para cargar una partida
        JButton btnLoad = new JButton();
        btnLoad.setFont(new Font("Montserrat", Font.BOLD, 24));
        btnLoad.setText("Cargar Partida");
        btnLoad.setPreferredSize(buttonSize);
        btnLoad.setBackground(new Color(26, 57, 44, 255));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.addActionListener(_ -> renderLoadGame());

        // Boton para ver las reglas
        JButton btnRules = new JButton();
        btnRules.setFont(new Font("Montserrat", Font.BOLD, 24));
        btnRules.setText("Reglas");
        btnRules.setPreferredSize(buttonSize);
        btnRules.setBackground(new Color(26, 57, 44, 255));
        btnRules.setForeground(Color.WHITE);
        btnRules.addActionListener(e ->
                renderRules());

        // Boton para salir del juego
        JButton btnQuit = new JButton();
        btnQuit.setFont(new Font("Montserrat", Font.BOLD, 24));
        btnQuit.setText("Salir");
        btnQuit.setPreferredSize(buttonSize);
        btnQuit.setBackground(new Color(26, 57, 44, 255));
        btnQuit.setForeground(Color.WHITE);
        btnQuit.addActionListener(e -> System.exit(0));

        gbc.gridy = 0;
        btnPane.add(btnStart, gbc);
        gbc.gridy = 1;
        btnPane.add(btnLoad, gbc); // Agregar botón de cargar partida
        gbc.gridy = 2;
        btnPane.add(btnJoin, gbc); // Agregar botón de ver ranking
        gbc.gridy = 3;
        btnPane.add(btnRules, gbc); // Agregar botón de salir
        gbc.gridy = 4;
        btnPane.add(btnQuit, gbc); // Agregar botón de salir

        JLabel player = new JLabel();
        player.setText("Estas jugando como: " + this.nombreJugador);
        player.setBackground(BACKGROUND_COLOR);
        player.setForeground(Color.WHITE);
        player.setFont(new Font("Montserrat", Font.BOLD, 16));
        player.setHorizontalAlignment(JLabel.CENTER);

        menu.add(imagePane, BorderLayout.NORTH);
        menu.add(btnPane, BorderLayout.CENTER);
        menu.add(player, BorderLayout.SOUTH);

        cardLayout.show(cardPane, "menu-pane");
    }

    private void renderLoadGame() {
        chinchonFrame.setTitle("Cargar una Partida");
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        chinchonFrame.setLocationRelativeTo(null);
        JPanel load = jPanelMap.get("load-pane");
        load.removeAll();
        load.repaint();
        load.revalidate();
        load.setLayout(new BorderLayout());
        load.setBackground(BACKGROUND_COLOR);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Introduzca el nombre de la partida a cargar");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titleLabel.setForeground(Color.yellow);
        load.add(titleLabel, BorderLayout.NORTH);

        ArrayList<String> partidasGuardadas = Serializacion.listarPartidas();
        if (partidasGuardadas.isEmpty()) {
            renderMenu();
            notificar("No hay partidas guardadas.");
            return;
        }

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (String partida : partidasGuardadas) {
            comboBoxModel.addElement(partida);
        }
        JComboBox<String> partidasComboBox = new JComboBox<>(comboBoxModel);
        partidasComboBox.setBackground(Color.WHITE);
        partidasComboBox.setFont(new Font("Montserrat", Font.PLAIN, 14));
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
        nombrePartidaTextField.setFont(new Font("Montserrat", Font.PLAIN, 14));
        nombrePartidaTextField.setPreferredSize(new Dimension(480, 30));
        nombrePartidaTextField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 1;
        centerPanel.add(nombrePartidaTextField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton cargarButton = new JButton("Cargar");
        cargarButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        cargarButton.setBackground(new Color(0x61C462));
        cargarButton.setPreferredSize(new Dimension(175, 30));
        cargarButton.addActionListener(e -> {
            String partidaSeleccionada = nombrePartidaTextField.getText().trim();
            if (partidaSeleccionada.isEmpty()) {
                notificar("Debe introducir o seleccionar un nombre de partida válido.");
            } else {
                if (controlador.cargarPartida(partidaSeleccionada)) {
                    notificar("Partida cargada correctamente.");
                    // Actualizar la interfaz con la información de la partida cargada
                    continuarPartida();
                    renderInGame();
                } else {
                    notificar("La partida seleccionada no existe.");
                }
            }
        });

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        cancelarButton.setBackground(new Color(0xFF3C3C));
        cancelarButton.setPreferredSize(new Dimension(175, 30));
        cancelarButton.addActionListener(e ->
                renderMenu());

        buttonPanel.add(cargarButton);
        buttonPanel.add(cancelarButton);

        load.add(buttonPanel, BorderLayout.SOUTH);
        cardLayout.show(cardPane, "load-pane");
    }

    private void renderSettings() {
        chinchonFrame.setTitle("Reglas del Chinchón");
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        chinchonFrame.setLocationRelativeTo(null);

        JPanel settings = jPanelMap.get("settings-pane");
        settings.removeAll();
        settings.repaint();
        settings.revalidate();
        settings.setLayout(new BorderLayout());
        settings.setBackground(BACKGROUND_COLOR);

        JPanel imagePane = new ImagePanel(getClass().getClassLoader().getResource(IMAGES_PATH + "titulo.png"));
        imagePane.setPreferredSize(new Dimension(156, 192));
        imagePane.setBackground(BACKGROUND_COLOR);

        JPanel settingsPane = new JPanel(new GridBagLayout());
        settingsPane.setBackground(BACKGROUND_COLOR);

        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        chinchonFrame.setLocationRelativeTo(null);
        chinchonFrame.setTitle("Chinchon - Settings");

        JLabel cartasOption = new JLabel();
        cartasOption.setBackground(BACKGROUND_COLOR);
        cartasOption.setForeground(Color.WHITE);
        cartasOption.setFont(new Font("Montserrat", Font.BOLD, 24));
        cartasOption.setHorizontalAlignment(JLabel.CENTER);
        cartasOption.setText("CANTIDAD DE CARTAS");

        String[] cartasBoxItems = {"40", "48"};
        JComboBox<String> cartasBox = new JComboBox<>(cartasBoxItems);
        cartasBox.setFont(new Font("Montserrat", Font.BOLD, 16));
        cartasBox.setPreferredSize(new Dimension(100, 25));
        cartasBox.setSelectedItem("40");

        // Contiene comodin
        JLabel comodinOption = new JLabel();
        comodinOption.setBackground(BACKGROUND_COLOR);
        comodinOption.setForeground(Color.WHITE);
        comodinOption.setFont(new Font("Montserrat", Font.BOLD, 24));
        comodinOption.setHorizontalAlignment(JLabel.CENTER);
        comodinOption.setText("COMODINES");

        String[] comodinBoxItems = {"No", "Si"};
        JComboBox<String> comodinBox = new JComboBox<>(comodinBoxItems);
        comodinBox.setFont(new Font("Montserrat", Font.BOLD, 16));
        comodinBox.setPreferredSize(new Dimension(100, 25));
        comodinBox.setSelectedItem("No");

        // Puntos maximos
        JLabel puntosOption = new JLabel();
        puntosOption.setBackground(BACKGROUND_COLOR);
        puntosOption.setForeground(Color.WHITE);
        puntosOption.setFont(new Font("Montserrat", Font.BOLD, 24));
        puntosOption.setHorizontalAlignment(JLabel.CENTER);
        puntosOption.setText("PUNTOS MÁXIMOS");

        String[] puntosBoxItems = {"50", "100"};
        JComboBox<String> puntosBox = new JComboBox<>(puntosBoxItems);
        puntosBox.setFont(new Font("Montserrat", Font.BOLD, 16));
        puntosBox.setPreferredSize(new Dimension(100, 25));
        puntosBox.setSelectedItem("50");

        JButton btnAccept = new JButton();
        btnAccept.setFont(new Font("Montserrat", Font.BOLD, 18));
        btnAccept.setText("Aceptar");
        btnAccept.setPreferredSize(new Dimension(150, 50));
        btnAccept.setBackground(new Color(36, 184, 97, 255));
        btnAccept.setForeground(Color.WHITE);
        btnAccept.addActionListener(e -> {
            int cantidadCartas;
            int puntosMaximos;
            boolean contieneComodin = true;
            if (Objects.equals(cartasBox.getSelectedItem(), "40")) {
                cantidadCartas = 40;
            } else {
                cantidadCartas = 48;
            }
            if (Objects.equals(comodinBox.getSelectedItem(), "No")) {
                contieneComodin = false;
            }
            if (Objects.equals(puntosBox.getSelectedItem(), "50")) {
                puntosMaximos = 50;
            } else {
                puntosMaximos = 100;
            }
            controlador.establecerValores(contieneComodin, cantidadCartas, puntosMaximos);
            controlador.agregarJugador(nombreJugador);
            if (controlador.getJugadoresSize() == 1) {
                esperandoJugadores();
            }
        });

        JButton btnBack = new JButton();
        btnBack.setFont(new Font("Montserrat", Font.BOLD, 18));
        btnBack.setText("Volver");
        btnBack.setPreferredSize(new Dimension(150, 50));
        btnBack.setBackground(new Color(255, 60, 60, 255));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> {
            cardLayout.show(cardPane, "menu-pane");
            chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
            chinchonFrame.setLocationRelativeTo(null);
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Columna 0
        gbc.gridy = 0; // Fila 0
        gbc.insets = new Insets(10, 10, 10, 10);

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

        settings.add(imagePane, BorderLayout.NORTH);
        settings.add(settingsPane, BorderLayout.CENTER);

        cardLayout.show(cardPane, "settings-pane");
    }

    // Funciona correctamente.
    private void renderRules() {
        chinchonFrame.setTitle("Reglas del Chinchón");
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        chinchonFrame.setLocationRelativeTo(null);
        JPanel rules = jPanelMap.get("rules-pane");
        rules.removeAll();
        rules.repaint();
        rules.revalidate();
        rules.setLayout(new BorderLayout());
        rules.setBackground(BACKGROUND_COLOR);

        JPanel ruleDescription = new JPanel(new GridBagLayout());
        ruleDescription.setBackground(BACKGROUND_COLOR);

        JLabel ruleTitle = new JLabel("Reglas del CHINCHÓN");
        ruleTitle.setBackground(BACKGROUND_COLOR);
        ruleTitle.setForeground(Color.yellow);
        ruleTitle.setFont(new Font("Montserrat", Font.BOLD, 38));
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
        jugarCartasTitle.setFont(new Font("Montserrat", Font.BOLD, 24));
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
        ligarCartasTitle.setFont(new Font("Montserrat", Font.BOLD, 24));
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
        cerrarManoTitle.setFont(new Font("Montserrat", Font.BOLD, 24));
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
        colocarCartasTitle.setFont(new Font("Montserrat", Font.BOLD, 24));
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
        recuentoManoTitle.setFont(new Font("Montserrat", Font.BOLD, 24));
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
        btnBack.setFont(new Font("Montserrat", Font.BOLD, 18));
        btnBack.setText("Volver");
        btnBack.setPreferredSize(new Dimension(150, 50));
        btnBack.setBackground(new Color(255, 60, 60, 255));
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> {
            cardLayout.show(cardPane, "menu-pane");
            chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
            chinchonFrame.setLocationRelativeTo(null);
        });

        rules.add(ruleTitle, BorderLayout.NORTH);
        rules.add(scrollPane, BorderLayout.CENTER);
        rules.add(btnBack, BorderLayout.SOUTH);

        cardLayout.show(cardPane, "rules-pane");
    }

    private void renderInGame() {
        chinchonFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        chinchonFrame.setTitle("Chinchon - In Game - " + nombreJugador);
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        JPanel inGame = jPanelMap.get("ingame-pane");
        inGame.removeAll();
        inGame.repaint();
        inGame.revalidate();
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

        JLabel puntajeLabel = new JLabel("Puntaje: " + controlador.getPuntaje(nombreJugador));
        puntajeLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        puntajeLabel.setForeground(Color.WHITE);
        infoPanel.add(puntajeLabel, gbc);

        gbc.gridy = 1;
        JLabel turnoLabel = new JLabel("Turno de " + controlador.getJugadorActual());
        turnoLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        turnoLabel.setForeground(Color.WHITE);
        infoPanel.add(turnoLabel, gbc);

        // Panel de Botones
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0x254633));

        // Insertamos boton de Cerrar Ronda
        gbc.gridy = 0;
        JButton btnCerraRonda = new JButton("Cerrar Ronda");
        configurarBoton(btnCerraRonda);
        buttonPanel.add(btnCerraRonda, gbc);

        // Insertamos boton de Robar del Mazo
        gbc.gridy = 1;
        JButton btnRobarMazo = new JButton("Robar Carta Mazo");
        configurarBoton(btnRobarMazo);
        btnRobarMazo.addActionListener(e -> {
            controlador.robarCartaMazo();
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnRobarMazo, gbc);

        // Insertamos boton de Robar del Descarte
        gbc.gridy = 2;
        JButton btnRobarDescarte = new JButton("Robar Carta Descarte");
        configurarBoton(btnRobarDescarte);
        btnRobarDescarte.addActionListener(e -> {
            controlador.robarCartaDescarte();
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnRobarDescarte, gbc);

        // Insertamos boton de Intercambiar Cartas
        gbc.gridy = 3;
        JButton btnIntercambiar = new JButton("Intercambiar Cartas");
        configurarBoton(btnIntercambiar);
        btnIntercambiar.addActionListener(e -> {
            intercambiarCarta();
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnIntercambiar, gbc);

        // Insertamos boton de Robar de Descarte
        gbc.gridy = 4;
        JButton btnOrdenarPalo = new JButton("Ordenar por Palo");
        configurarBoton(btnOrdenarPalo);
        btnOrdenarPalo.addActionListener(e -> {
            controlador.ordenarPalo(nombreJugador);
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnOrdenarPalo, gbc);

        gbc.gridy = 5;
        JButton btnOrdenarValor = new JButton("Ordenar por Valor");
        configurarBoton(btnOrdenarValor);
        btnOrdenarValor.addActionListener(e -> {
            controlador.ordenarValor(nombreJugador);
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnOrdenarValor, gbc);

        gbc.gridy = 6;
        JButton btnExit = new JButton("Salir");
        configurarBoton(btnExit);
        buttonPanel.add(btnExit, gbc);

        playerOptionsPanel.add(infoPanel, BorderLayout.NORTH);
        playerOptionsPanel.add(buttonPanel, BorderLayout.CENTER);

        inGame.add(playerOptionsPanel, BorderLayout.WEST);
        inGame.add(cardsPanel, BorderLayout.CENTER);

        cardLayout.show(cardPane, "ingame-pane");
    }

    private void renderCardsPane(JPanel cardsPanel) {
        cardsPanel.removeAll();
        cardsPanel.setBackground(BACKGROUND_COLOR);
        cardsPanel.setLayout(new BorderLayout());

        JPanel cartasEnMano = new JPanel(new FlowLayout());
        cartasEnMano.setBackground(BACKGROUND_COLOR);
        Dimension buttonSize = new Dimension(90, 150);

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
                button.addActionListener(e -> controlador.tirarCarta(controlador.getCartaPosition(carta)));
            }

            cartasEnMano.add(button);
        }

        cardsPanel.add(cartasEnMano, BorderLayout.SOUTH);
        cardsPanel.repaint();
        cardsPanel.revalidate();
    }

    private void renderWaitingTurn() {
        chinchonFrame.setTitle("Chinchon - Waiting - " + nombreJugador);
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        JPanel wait = jPanelMap.get("waitingturn-pane");
        wait.removeAll();
        wait.repaint();
        wait.revalidate();
        wait.setBackground(Color.GREEN);
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
        puntajeLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        puntajeLabel.setForeground(Color.WHITE);
        infoPanel.add(puntajeLabel, gbc);

        gbc.gridy = 1;
        JLabel turnoLabel = new JLabel("Turno de " + controlador.getJugadorActual());
        turnoLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        turnoLabel.setForeground(Color.WHITE);
        infoPanel.add(turnoLabel, gbc);

        // Panel de Botones
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0x254633));

        // Insertamos boton de Intercambiar Cartas
        gbc.gridy = 0;
        JButton btnIntercambiar = new JButton("Intercambiar Cartas");
        configurarBoton(btnIntercambiar);
        btnIntercambiar.addActionListener(e -> {
            intercambiarCarta();
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnIntercambiar, gbc);

        // Insertamos boton de Robar de Descarte
        gbc.gridy = 1;
        JButton btnOrdenarPalo = new JButton("Ordenar por Palo");
        configurarBoton(btnOrdenarPalo);
        btnOrdenarPalo.addActionListener(e -> {
            controlador.ordenarPalo(nombreJugador);
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnOrdenarPalo, gbc);

        gbc.gridy = 2;
        JButton btnOrdenarValor = new JButton("Ordenar por Valor");
        configurarBoton(btnOrdenarValor);
        btnOrdenarValor.addActionListener(e -> {
            controlador.ordenarValor(nombreJugador);
            renderCardsPane(cardsPanel);
        });
        buttonPanel.add(btnOrdenarValor, gbc);

        gbc.gridy = 3;
        JButton btnExit = new JButton("Salir");
        configurarBoton(btnExit);
        buttonPanel.add(btnExit, gbc);


        buttonPanel.add(btnExit, gbc);

        playerOptionsPanel.add(infoPanel, BorderLayout.NORTH);
        playerOptionsPanel.add(buttonPanel, BorderLayout.CENTER);

        wait.add(playerOptionsPanel, BorderLayout.WEST);
        wait.add(cardsPanel, BorderLayout.CENTER);
        cardLayout.show(cardPane, "waitingturn-pane");
    }

    private void esperandoJugadores() {
        chinchonFrame.setTitle("Esperando Jugadores");
        chinchonFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        chinchonFrame.setLocation(screenInsets.left, screenInsets.top);
        JPanel esperando = jPanelMap.get("esperando-pane");
        esperando.removeAll();

        esperando.setLayout(new BorderLayout());
        esperando.setBackground(BACKGROUND_COLOR);

        JLabel jLabel = new JLabel("Esperando Jugadores ..");
        jLabel.setFont(BTN_FONT);
        jLabel.setForeground(Color.WHITE);
        jLabel.setHorizontalAlignment(JLabel.CENTER);

        esperando.add(jLabel, BorderLayout.CENTER);
        esperando.repaint();
        esperando.revalidate();
        cardLayout.show(cardPane, "esperando-pane");
    }

    private void intercambiarCarta() {

    }

    private void configurarBoton(JButton boton) {
        Dimension buttonSize = new Dimension(250, 35); // Ancho: 275, Alto: 75
        boton.setFont(BTN_FONT);
        boton.setBackground(new Color(26, 57, 44));
        boton.setForeground(Color.WHITE);
        boton.setPreferredSize(buttonSize);
    }

    private JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setBackground(BACKGROUND_COLOR);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Montserrat", Font.BOLD, 18));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBorder(BorderFactory.createEmptyBorder());
        return textArea;
    }

    private void playerLogin() {
        Login login = new Login(this);
        login.setVisible(true);

        if (nombreJugador != null) {
            initMenu();
        }
    }

    @Override
    public void startGame() {
        controlador.iniciarPartida();
        actualizarVista();
    }

    @Override
    public void setJugador(String nombre) {
        this.nombreJugador = nombre;
    }

    @Override
    public void cambiarTurno() {
        actualizarVista();
    }

    private void actualizarVista() {
        jugadorActual = controlador.getJugadorActual();
        if (jugadorActual.equals(nombreJugador)) {
            renderInGame();
        } else {
            renderWaitingTurn();
        }
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
