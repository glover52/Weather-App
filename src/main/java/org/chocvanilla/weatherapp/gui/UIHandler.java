package org.chocvanilla.weatherapp.gui;

import javax.swing.JFrame;

/**
 * Passes inputs through to certain GUIs
 */
public class UIHandler {
	private JFrame frame;
	
	public UIHandler(JFrame frame) {
		this.frame = frame;
		this.frame.setVisible(true);
	}
}

