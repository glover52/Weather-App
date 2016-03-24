package org.chocvanilla.weatherapp.gui;

public class Button {
	private int x, y;
	private int w, h;
	private String value;
	@SuppressWarnings("unused")
	private String text;
	
	public Button(int x, int y, int w, int h, String text, String value) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
		this.value = value;
	}
}
