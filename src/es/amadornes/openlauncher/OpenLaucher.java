package es.amadornes.openlauncher;

import java.awt.Font;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import es.amadornes.openlauncher.api.ComponentFancyButton1;
import es.amadornes.openlauncher.gui.GUI;
import es.amadornes.openlauncher.gui.TabModpacks;
import es.amadornes.openlauncher.modpack.Modpack;

public class OpenLaucher {
	
	public static GUI gui;
	
	public static boolean loggedIn = true;
	public static String username = "amadornes";
	public static Font font;
	
	public static Map<Modpack, Boolean> modpacks = new HashMap<Modpack, Boolean>();
	
	public static void main(String[] args){
		try {
			modpacks.put(new Modpack("Test Modpack", new URL("http://localhost/testpack.png")), true);
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
