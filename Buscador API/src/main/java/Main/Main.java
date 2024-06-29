package Main;

import view.Window;

import javax.swing.*;

/**
 * @author Rensso
 * @Title: Buscador de API
 */

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window viewer = new Window();
            viewer.setVisible(true);
        });
    }
}
