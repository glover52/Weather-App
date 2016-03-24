package org.chocvanilla.weatherapp.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * The Interface that the user interacts with, control
 * navigation and will be the first point of contact
 * 
 * Also acts as a superclass to other GUI elements
 */
public abstract class Menu {
	
	private List<Button> buttons = new ArrayList<Button>();
	protected UIHandler uiHandler;
	
	protected Menu(UIHandler uiHandler) {
		this.uiHandler = uiHandler;
	}
	
	/**
	 * Add a button to the menu.
	 * Assumes buttons are added from top - bottom, left - right
	 * for keyboard navigation purposes.
	 */
	public void addButton(Button button) {
		this.buttons.add(button);
	}

}
