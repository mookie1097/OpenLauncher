package es.amadornes.openlauncher;

import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mooklabs.FeedMessage;
import mooklabs.ReadXMLFromURl;
import net.minecraft.login.MojangAuth;
import net.technicpack.launchercore.install.InstalledPack;
import es.amadornes.openlauncher.api.gui.ComponentFancyButton;
import es.amadornes.openlauncher.api.server.DownloadServer;
import es.amadornes.openlauncher.gui.GUI;
import es.amadornes.openlauncher.gui.TabConsole;
import es.amadornes.openlauncher.gui.TabModpacks;
import es.amadornes.openlauncher.gui.TabNews;
import es.amadornes.openlauncher.gui.TabSettings;
import es.amadornes.openlauncher.modpack.Modpack;
import es.amadornes.openlauncher.server.DownloadServerAmadornes;
import es.amadornes.openlauncher.util.Downloader;
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

	public static void main(String[] args)  {
		/* Add shutdown event */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {@Override
			public void run(){ exit(); }}));
		System.out.println("preinit");


		File dlFolder = new File(Util.getDownloadsFolder(), "asdf" + "/");
		dlFolder.mkdirs();



		//{{ add modpacks
		String urlstring = "http://textuploader.com/03aa/raw";//dont think ill ever have to change this(for git i did)
		boolean isprivate = false;
		String mcVersion = "mcv71.7.2";
		int version =1;
		for (FeedMessage m: ReadXMLFromURl.getModpackData(urlstring)) {
			try {
				modpacks.add(new Modpack(m.title,m.title,new URL(m.link),m.author,isprivate,version,m.version,mcVersion,"serverid",m.link));
				modpacks.get(modpacks.size()-1).setDescription(m.description);//set desc


			} catch (MalformedURLException e) {

				e.printStackTrace();
			}
		}
		for (Modpack m: modpacks) {
			File zip = new File(dlFolder,m.getName()+m.getVersion()+".jar");
			System.out.println(m.link);
			try {
				Downloader.download(new URL(m.link), zip);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		//}}

		preInit();

		/* Try to set Pixel font, if fails, set Arial */
		try {
			InputStream fontstream = OpenLauncher.class.getResourceAsStream("/font/pixel.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fontstream).deriveFont(Font.PLAIN, 14);
		} catch (Exception e) {
			font = new Font("Arial", Font.PLAIN, 14);
		}
		System.out.println("gui");

		/* Set GUI with dimensions 1280x720 */
		gui = new GUI(1280, 720);
		System.out.println("initcomp");
		System.out.println("1");
		/* Add sidebar Tabs */
		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, gui.getHeight() - 75 - gui.insets.top - 45 - 45 - 45, 150, 40, "News").setSelected(true));
		System.out.println("1");
		gui.addTab(new TabNews(gui), 0);
		System.out.println("2");

		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, gui.getHeight() - 75 - gui.insets.top - 45 - 45, 150, 40, "Modpacks").setTab(1));
		gui.addTab(new TabModpacks(gui), 1);		System.out.println("1");

		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, gui.getHeight() - 75 - gui.insets.top - 45, 150, 40, "Console").setTab(2));
		gui.addTab(new TabConsole(gui), 2);		System.out.println("1");

		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, gui.getHeight() - 75 - gui.insets.top, 150, 40,	"Settings").setTab(3));
		gui.addTab(new TabSettings(gui), 3);		System.out.println("1");

		System.out.println("settab");

		gui.setTab(0);
		System.out.println("init");

		init();

		/* Center and show the GUI */
		gui.center();
		gui.show();
		System.out.println("postinit");

		InstalledPack pack = new InstalledPack();

		postInit();
	}

	public static void loadModpacks() { new Thread(new Runnable() { @Override
		public void run() {

		for(DownloadServer sv : servers) {
			String[] packs = sv.getAvailablePacks();
			if(packs != null){
				for(String id : packs) {
					Modpack p = sv.getPack(id);
					if(p != null) {
						modpacks.add(p);
					} else {
						System.err.println("An error occoured while download information about the pack \"" + id + "\" from the server \"" + sv.getServerID() + "\"");
					}
				}
			}else{
				System.err.println("Could not connect to to the server \"" + sv.getServerID() + "\"");
			}
		}

	}}).start();}

	private static void preInit(){
		/* Create "openlauncher" data directory */
		Util.getWorkingDirectory().mkdirs();
		Util.getInstancesFolder().mkdirs();
		Util.getDownloadsFolder().mkdirs();
	}

	private static void init(){
		/* Try to login using the LastLogin function */
		LastLogin.tryLoading();
	}

	private static void postInit(){
		/* Start loading modpacks from the various servers */
		loadModpacks();
	}

	private static void exit(){}

	public static Modpack getPack(String server, String id){
		for(Modpack m : modpacks)
			if(m.getId().equals(id) && m.getServerID().equals(server))
				return m;
		return null;
	}

	public static DownloadServer getServer(String server){
		for(DownloadServer s : servers)
			if(s.getServerID().equals(server))
				return s;
		return null;
	}

	public static void setLoggedIn(boolean logged){
		gui.labelUser.setVisible(!logged);
		gui.labelPass.setVisible(!logged);
		gui.user.setVisible(!logged);
		gui.pass.setVisible(!logged);
		loggedIn = logged;
		if(logged){
			gui.removeComponent(gui.loginButton);
			gui.removeComponent(gui.loginButtonText);
		}else{
			gui.addComponent(gui.loginButton);
			gui.addComponent(gui.loginButtonText);
		}
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
		}
	}

}
