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
			Modpack elfco = new Modpack("Elfco SMP Modpack v14.0", new URL("http://wallpaperswa.com/thumbnails/detail/20120401/minecraft%201024x1024%20wallpaper_www.wallpaperhi.com_13.jpg"), true);
			elfco.setDescription("This is a teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee\neeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeest. :D");
			modpacks.add(elfco);
			modpacks.add(new Modpack("Test Modpack 2", new URL("http://www.wallsave.com/wallpapers/1024x1024/minecraft-hd/54338/minecraft-hd-ipod-iphone-ipad-and-54338.jpg"), true));
			modpacks.add(new Modpack("Test Modpack 3", new URL("http://www.wallsave.com/wallpapers/1024x1024/minecraft-hd/54338/minecraft-hd-ipod-iphone-ipad-and-54338.jpg"), true));
			modpacks.add(new Modpack("Test Modpack 4", new URL("http://www.wallsave.com/wallpapers/1024x1024/minecraft-hd/54338/minecraft-hd-ipod-iphone-ipad-and-54338.jpg"), true));
			modpacks.add(new Modpack("Test Modpack 5", new URL("http://www.wallsave.com/wallpapers/1024x1024/minecraft-hd/54338/minecraft-hd-ipod-iphone-ipad-and-54338.jpg"), true));
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
