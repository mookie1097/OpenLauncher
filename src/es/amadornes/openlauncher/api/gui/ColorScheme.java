package es.amadornes.openlauncher.api.gui;

import java.awt.Color;
import java.awt.Paint;

public enum ColorScheme {
	
	WHITE_ORANGE(new Color(0xF7F7F7), new Color(0xDDDDDD), new Color(0xF7F7F7), new Color(0xEDEDED), new Color(0xE8DFC1)),
	WHITE_GREEN(new Color(0xF7F7F7), new Color(0xDDDDDD), new Color(0xF7F7F7), new Color(0xEDEDED), new Color(0xC1E8D3));
	
	public static ColorScheme active = WHITE_ORANGE;
	
	public Paint background, titlebar, buttonOff, buttonHover, buttonClick;
	
	private ColorScheme(Paint background, Paint titlebar, Paint buttonOff, Paint buttonHover, Paint buttonClick) {
		this.background = background;
		this.titlebar = titlebar;
		this.buttonOff = buttonOff;
		this.buttonHover = buttonHover;
		this.buttonClick = buttonClick;
	}
	
}
