package es.amadornes.openlauncher;

import java.awt.Font;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.amadornes.openlauncher.api.ComponentFancyButton1;
import es.amadornes.openlauncher.gui.GUI;
import es.amadornes.openlauncher.gui.TabModpacks;
import es.amadornes.openlauncher.modpack.Modpack;

public class OpenLaucher {
	
	public static GUI gui;
	
	public static boolean loggedIn = true;
	public static String username = "amadornes";
	public static Font font;
	
	public static List<Modpack> modpacks = new ArrayList<Modpack>();
	
	public static void main(String[] args){
		try {
			modpacks.add(new Modpack("Test Modpack 1", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 2", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 3", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 4", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 5", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 6", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 7", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 8", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 9", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 10", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 11", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 12", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 13", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 14", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 15", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 16", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 17", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 18", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 19", new URL("http://localhost/testpack.png"), true));
			modpacks.add(new Modpack("Test Modpack 20", new URL("http://localhost/testpack.png"), true));
		} catch (Exception e) {}
		
		try {
			InputStream fontstream = OpenLaucher.class.getResourceAsStream("/font/pixel.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fontstream).deriveFont(Font.PLAIN, 14);
		} catch (Exception e) {
			font = new Font("Arial", Font.PLAIN, 14);
		}
		gui = new GUI(1280, 720);
		
		gui.addComponent(new ComponentFancyButton1(29 - gui.insets.left, 659 - gui.insets.top - 45 - 45 - 45, 150, 40,	"Modpacks").setSelected(true));
		gui.addTab(new TabModpacks(gui), 0);
		
		gui.addComponent(new ComponentFancyButton1(29 - gui.insets.left, 659 - gui.insets.top - 45 - 45, 150, 40,		"Console").setTab(1));
		
		gui.addComponent(new ComponentFancyButton1(29 - gui.insets.left, 659 - gui.insets.top - 45, 150, 40,			"").setTab(2));
		
		gui.addComponent(new ComponentFancyButton1(29 - gui.insets.left, 659 - gui.insets.top, 150, 40,					"").setTab(3));
		
		gui.center();
		gui.show();
	}
	
}
