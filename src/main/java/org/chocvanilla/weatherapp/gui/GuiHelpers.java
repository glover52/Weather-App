package org.chocvanilla.weatherapp.gui;

import org.chocvanilla.weatherapp.data.observations.Field;
import org.chocvanilla.weatherapp.data.observations.WeatherObservation;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

public class GuiHelpers {
       
    /**
     * Installs a listener to receive notification when the text of any
     * {@code JTextComponent} is changed. Internally, it installs a
     * {@link DocumentListener} on the text component's {@link Document},
     * and a {@link ChangeListener} on the text component to detect
     * if the {@code Document} itself is replaced.
     *
     * @param text           any text component, such as a {@link JTextField}
     *                       or {@link JTextArea}
     * @param changeListener a listener to receive {@link ChangeEvent}s
     *                       when the text is changed; the source object for the events
     *                       will be the text component
     * @throws NullPointerException if either parameter is null
     */
    public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
        // From: http://stackoverflow.com/a/27190162/1106367
        // This code has been released into the public domain by the author.
        Objects.requireNonNull(text);
        Objects.requireNonNull(changeListener);
        DocumentListener dl = new DocumentListener() {
            private int lastChange = 0, lastNotifiedChange = 0;

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lastChange++;
                SwingUtilities.invokeLater(() -> {
                    if (lastNotifiedChange != lastChange) {
                        lastNotifiedChange = lastChange;
                        changeListener.stateChanged(new ChangeEvent(text));
                    }
                });
            }
        };
        text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
            Document d1 = (Document) e.getOldValue();
            Document d2 = (Document) e.getNewValue();
            if (d1 != null) d1.removeDocumentListener(dl);
            if (d2 != null) d2.addDocumentListener(dl);
            dl.changedUpdate(null);
        });
        Document d = text.getDocument();
        if (d != null) d.addDocumentListener(dl);
    }

    public static JPanel fieldToLabel(String label, Object data) {
        JPanel entry = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Put label on top
        c.gridx = 0;
        c.gridy = 0;
        JLabel title = new JLabel(label);
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        entry.add(title, c);
        // Put data on bottom
        c.gridy = 1;
        entry.add(new JLabel(data.toString()), c);
        return entry;
    }

    /**
     * Method of taking the most recent observations, and adding them to the returned JPanel
     *
     * @param observation the most recent observation.
     * @return New JPanel.
     */
    static JPanel buildDetails(WeatherObservation observation) {
        JPanel details = new JPanel(new FlowLayout());
        for (Field field : observation.getFields()) {
            details.add(fieldToLabel(field.getLabel(), field.getFormattedValue()));
            details.add(new JSeparator(SwingConstants.VERTICAL));
        }
        return details;
    }
}
