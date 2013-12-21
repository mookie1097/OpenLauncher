package es.amadornes.openlauncher;

import java.awt.Font;
import java.io.InputStream;

import es.amadornes.openlauncher.gui.GUI;

public class OpenLaucher {
	
	public static GUI gui;
	
	public static boolean loggedIn = true;
	public static String username = "amadornes";
	public static Font font;
	
	public static void main(String[] args){
		try {
			InputStream fontstream = OpenLaucher.class.getResourceAsStream("/font/pixel.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fontstream).deriveFont(Font.PLAIN, 14);
		} catch (Exception e) {
			font = new Font("Arial", Font.PLAIN, 14);
		}
		gui = new GUI(1280, 720);
		gui.center();
		gui.show();
	}
	
}
