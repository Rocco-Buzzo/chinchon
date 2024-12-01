package ar.edu.unlu.mvc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Login extends JDialog {
    private static final Color BACKGROUND_COLOR = new Color(55, 101, 73);
    private static final Font BTN_FONT = new Font("Montserrat", Font.BOLD, 16);

    private JPanel contentPane;
    private JPanel btnPane;
    private JButton btnAccept;
    private JButton btnCancel;
    private JTextField nameTextField;
    private JLabel labelName;

    private IVista vista;

    public Login(IVista vista) {
        this.vista = vista;

        setSize(320, 160);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon("chinchon-game/src/ar/edu/unlu/assets/ICONO.png").getImage());
        setTitle("Chinchon - Login");
        setLayout(new BorderLayout());
        getRootPane().setDefaultButton(btnAccept);
        setModal(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        renderPanel();
        setContentPane(contentPane);

        add(btnPane, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

    }

    private void renderPanel() {
        contentPane = new JPanel();
        contentPane.setBackground(BACKGROUND_COLOR);
        contentPane.setLayout(new FlowLayout());

        btnPane = new JPanel();
        btnPane.setBackground(BACKGROUND_COLOR);
        btnPane.setLayout(new FlowLayout());

        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(256, 24));
        labelName = new JLabel();
        labelName.setText("Nombre: ");
        labelName.setFont(BTN_FONT);
        labelName.setForeground(Color.WHITE);

        renderButtons();

        btnPane.add(btnAccept);
        btnPane.add(btnCancel);
        contentPane.add(labelName);
        contentPane.add(nameTextField);
    }

    private void renderButtons() {
        Dimension buttonSize = new Dimension(128, 36);

        btnAccept = new JButton();
        btnAccept.setBackground(new Color(135, 255, 75));
        btnAccept.setPreferredSize(buttonSize);
        btnAccept.setText("Aceptar");
        btnAccept.setFont(BTN_FONT);
        btnAccept.addActionListener(e -> onAccept());

        btnCancel = new JButton();
        btnCancel.setBackground(new Color(255, 60, 60));
        btnCancel.setPreferredSize(buttonSize);
        btnCancel.setText("Cancelar");
        btnCancel.setFont(BTN_FONT);
        btnCancel.addActionListener(e -> onCancel());
    }

    private void onAccept() {
        String playerName = nameTextField.getText();
        if (playerName.isBlank() || playerName.isEmpty()) {
            notificar("Ingrese el nombre del jugador antes de continuar.");
        } else {
            vista.setJugador(playerName);
            dispose();
        }
    }

    private void onCancel() {
        System.exit(0);
    }

    private void notificar(String msg) {
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", BTN_FONT);
        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", BTN_FONT);

        JOptionPane.showMessageDialog(null, msg, "Notificación", JOptionPane.PLAIN_MESSAGE);
    }
}
