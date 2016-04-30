package org.chocvanilla.weatherapp.gui;

import javax.swing.*;

public class MessageDialog {
    public static void messageBox(String message, String title) {
        SwingUtilities.invokeLater(() -> showMsg(message, title));
    }

    private static void showMsg(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
