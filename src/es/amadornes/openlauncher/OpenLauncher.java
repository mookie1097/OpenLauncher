package es.amadornes.openlauncher;

import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.login.MojangAuth;
import es.amadornes.openlauncher.api.gui.ComponentFancyButton;
import es.amadornes.openlauncher.api.gui.server.DownloadServer;
import es.amadornes.openlauncher.gui.GUI;
import es.amadornes.openlauncher.gui.TabConsole;
import es.amadornes.openlauncher.gui.TabModpacks;
import es.amadornes.openlauncher.gui.TabNews;
import es.amadornes.openlauncher.gui.TabSettings;
import es.amadornes.openlauncher.modpack.Modpack;
import es.amadornes.openlauncher.server.DownloadServerAmadornes;
import es.amadornes.openlauncher.util.Util;

/**
 * OpenLauncher
 * 
 * OpenLauncher.java
 * 
 * @author amadornes (amadornes.es) and DavidJotta (davidjuan.es)
 * @license GNU GPL v2 (http://choosealicense.com/licenses/gpl-v2/)
 */

public class OpenLauncher {
	
	/* Instantiate GUI */
	public static GUI gui;
	
	/* Set some variables */
	public static boolean loggedIn = false;
	public static String username = "Unknown";
	public static Font font;
	
	private static DownloadServer[] servers = new DownloadServer[]{ new DownloadServerAmadornes() };
	public static List<Modpack> modpacks = new ArrayList<Modpack>();
	
	public static void main(String[] args){
		/* Add shutdown event */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {public void run(){ exit(); }}));
		
		preInit();
		
		/* Try to set Pixel font, if fails, set Arial */
		try {
			InputStream fontstream = OpenLauncher.class.getResourceAsStream("/font/pixel.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fontstream).deriveFont(Font.PLAIN, 14);
		} catch (Exception e) {
			font = new Font("Arial", Font.PLAIN, 14);
		}
		
		/* Set GUI with dimensions 1280x720 */
		gui = new GUI(1280, 720);
		
		/* Add sidebar Tabs */
		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, 659 - gui.insets.top - 45 - 45 - 45, 150, 40, "News").setSelected(true));
		gui.addTab(new TabNews(gui), 0);
		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, 659 - gui.insets.top - 45 - 45, 150, 40, "Modpacks").setTab(1));
		gui.addTab(new TabModpacks(gui), 1);
		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, 659 - gui.insets.top - 45, 150, 40, "Console").setTab(2));
		gui.addTab(new TabConsole(gui), 2);
		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, 659 - gui.insets.top, 150, 40,	"Settings").setTab(3));
		gui.addTab(new TabSettings(gui), 3);
		
		init();
		
		/* Center and show the GUI */
		gui.center();
		gui.show();
		
		postInit();
	}
	
	public static void loadModpacks() { new Thread(new Runnable() { public void run() {
		
		for(DownloadServer sv : servers) {
			String[] packs = sv.getAvailablePacks();
			for(String id : packs) {
				Modpack p = sv.getPack(id);
				if(p != null) {
					modpacks.add(p);
				} else {
					System.err.println("An error occoured while download information about the pack \"" + id + "\" from the server \"" + sv.getServerID() + "\"");
				}
			}
		}
		
	}}).start();}
	
	private static void preInit(){
		/* Create "openlauncher" data directory */
		Util.getWorkingDirectory().mkdirs();
		Util.getInstancesFolder().mkdirs();
		Util.getDownloadsFolder().mkdirs();
		
		/* Try to login using the LastLogin function */
		LastLogin.tryLoading();
	}
	
	private static void init(){
		
	}
	
	private static void postInit(){
		/* Start loading modpacks from the various servers */
		loadModpacks();
	}
	
	private static void exit(){}
	
	public static Modpack getPack(String server, String id){
		for(Modpack m : modpacks)
			if(m.getId().equals(server + "_" + id))
				return m;
		return null;
	}
	
	public static void setLoggedIn(boolean logged){
		gui.labelUser.setVisible(!logged);
		gui.labelPass.setVisible(!logged);
		gui.user.setVisible(!logged);
		gui.pass.setVisible(!logged);
		loggedIn = logged;
	}
	
	public static void login(){
		Map<String, String> data = null;
		try {
			data = MojangAuth.authenticate(gui.user.getText(), new String(gui.pass.getPassword()));

			LastLogin.ACCESS_TOKEN = data.get("accessToken");
			LastLogin.CLIENT_TOKEN = data.get("clientToken");
			LastLogin.UUID = data.get("UUID");
			username = data.get("username");
		} catch (Exception e) {}
		
		if(data != null){
			setLoggedIn(true);
			LastLogin.save();
			gui.removeComponent(gui.loginButton);
		}
	}
	
}
