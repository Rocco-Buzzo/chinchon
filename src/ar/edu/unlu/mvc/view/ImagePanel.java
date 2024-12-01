package ar.edu.unlu.mvc.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private Image image;

    // Constructor para cargar la imagen desde un archivo
    public ImagePanel(String imagePath) {
        try {
            // Cargar la imagen desde el archivo
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sobrescribir el metodo paintComponent para dibujar la imagen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Dibujar la imagen ajustada al tamaño del panel
            g.drawImage(image, 64, 20, getWidth() - 128, getHeight() - 20, this);
        }
    }
}
