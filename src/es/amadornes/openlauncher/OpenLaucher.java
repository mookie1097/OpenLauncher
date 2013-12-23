package es.amadornes.openlauncher;

import java.awt.Font;
import java.io.InputStream;

import es.amadornes.openlauncher.api.ComponentFancyButton1;
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
		gui.addComponent(new ComponentFancyButton1(29 - gui.insets.left, 659 - gui.insets.top, 150, 40,					"").setTab(3));
		gui.addComponent(new ComponentFancyButton1(29 - gui.insets.left, 659 - gui.insets.top - 45, 150, 40,			"").setTab(2));
		gui.addComponent(new ComponentFancyButton1(29 - gui.insets.left, 659 - gui.insets.top - 45 - 45, 150, 40,		"Console").setTab(1));
		gui.addComponent(new ComponentFancyButton1(29 - gui.insets.left, 659 - gui.insets.top - 45 - 45 - 45, 150, 40,	"Modpacks").setSelected(true));
		gui.center();
		gui.show();
	}
	
}
