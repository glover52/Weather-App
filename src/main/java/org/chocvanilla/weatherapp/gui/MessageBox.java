package org.chocvanilla.weatherapp.gui;

import javax.swing.*;

public class MessageBox {
    public static void show(String message, String title) {
        SwingUtilities.invokeLater(() -> showMessage(message, title));
    }

    public static void showNow(String message, String title) {
        try {
            SwingUtilities.invokeAndWait(() -> showMessage(message, title));
        } catch (Exception ignored) {
            // if we fail to show the message, tough luck
        }
    }

    private static void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
