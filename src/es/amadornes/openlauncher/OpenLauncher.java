package es.amadornes.openlauncher;

import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.amadornes.openlauncher.api.ComponentFancyButton1;
import es.amadornes.openlauncher.gui.GUI;
import es.amadornes.openlauncher.gui.TabModpacks;
import es.amadornes.openlauncher.modpack.Modpack;

public class OpenLauncher {
	
	public static GUI gui;
	
	public static boolean loggedIn = true;
	public static String username = "amadornes";
	public static Font font;
	
	public static List<Modpack> modpacks = new ArrayList<Modpack>();
	
	public static void main(String[] args){
		try {
			InputStream fontstream = OpenLauncher.class.getResourceAsStream("/font/pixel.ttf");
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
		
		loadModpacks();
	}
	
	public static void loadModpacks(){
		new Thread(new Runnable() {
			public void run() {
				try {
					Modpack elfco = new Modpack("Elfco SMP Modpack v14.0", new URL("http://wallpaperswa.com/thumbnails/detail/20120401/minecraft%201024x1024%20wallpaper_www.wallpaperhi.com_13.jpg"), true);
					elfco.setDescription("This is a teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeest. :D");
					elfco.setCreator("Elfco");
					modpacks.add(elfco);
					Modpack prueba = new Modpack("Soy una prueba v1.0", new URL("http://www.nerditos.com/wp-content/uploads/2013/05/meme-lol-que-significa-LOL.jpg"), true);
					prueba.setDescription("Pues eso, que soy una prueba");
					prueba.setCreator("Pruebador");
					modpacks.add(prueba);
					Modpack r290x = new Modpack("Radeon 290x", new URL("http://hwt.dk/images/literature/2013/23159/264o_290x.jpg"), true);
					r290x.setDescription("Elfco, compratelaa!");
					r290x.setCreator("AMD");
					modpacks.add(r290x);
					modpacks.add(new Modpack("Test Modpack 4", new URL("http://www.wallsave.com/wallpapers/1024x1024/minecraft-hd/54338/minecraft-hd-ipod-iphone-ipad-and-54338.jpg"), true));
					modpacks.add(new Modpack("Test Modpack 5", new URL("http://www.wallsave.com/wallpapers/1024x1024/minecraft-hd/54338/minecraft-hd-ipod-iphone-ipad-and-54338.jpg"), true));
				} catch (Exception e) {}
			}
		}).start();
	}
	
	public static String APP_NAME = "openlauncher";
	
	public static OS getPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win"))
			return OS.WINDOWS;
		if (osName.contains("mac"))
			return OS.MACOS;
		if (osName.contains("linux"))
			return OS.LINUX;
		if (osName.contains("unix"))
			return OS.LINUX;
		return OS.UNKNOWN;
	}
    
	public static File getWorkingDirectory() {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (getPlatform()) {
		case WINDOWS:
		case MACOS:
			workingDirectory = new File(userHome, APP_NAME + "/");
			break;
		case SOLARIS:
			String applicationData = System.getenv("APPDATA");
			String folder = applicationData != null ? applicationData
					: userHome;
			workingDirectory = new File(folder, APP_NAME + "/");
			break;
		case LINUX:
			workingDirectory = new File(userHome,
					"Library/Application Support/" + APP_NAME + "/");
			break;
		default:
			workingDirectory = new File(userHome, APP_NAME + "/");
		}
		return workingDirectory;
	}

	private static enum OS {
		WINDOWS, MACOS, SOLARIS, LINUX, UNKNOWN;
	}
	
	public static File getInstancesFolder(){
		return new File(getWorkingDirectory(), "instances/");
	}
	
	public static File getVersionsFolder(){
		return new File(getWorkingDirectory(), "versions/");
	}
	
}
