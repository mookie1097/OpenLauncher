package es.amadornes.openlauncher.api.gui;

import java.awt.Color;
import java.awt.Paint;

public enum ColorScheme {
	
	WHITE_ORANGE(new Color(0xF7F7F7), new Color(0xDDDDDD), new Color(0xE3D58A), new Color(0xF0DD13), new Color(0xE6D415)),
	WHITE_GREEN(new Color(0xF7F7F7), new Color(0xDDDDDD), new Color(0xADF2A0), new Color(0x65ED58), new Color(0x21DB0F));
	
	public static ColorScheme active = WHITE_GREEN;
	
	public Paint background, titlebar, buttonOff, buttonHover, buttonClick;
	
	private ColorScheme(Paint background, Paint titlebar, Paint buttonOff, Paint buttonHover, Paint buttonClick) {
		this.background = background;
		this.titlebar = titlebar;
		this.buttonOff = buttonOff;
		this.buttonHover = buttonHover;
		this.buttonClick = buttonClick;
	}
	
}
