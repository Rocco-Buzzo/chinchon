package ar.edu.unlu.mvc.view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private Image image;

    // Constructor para cargar la imagen desde un recurso URL
    public ImagePanel(URL imagePath) {
        try {
            // Cargar la imagen desde el URL directamente
            image = ImageIO.read(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int width = getWidth();
            int height = getHeight();
            g.drawImage(image, 0, 0, width, height, this); // Redimensiona la imagen al tamaño del panel
        }
    }
}
