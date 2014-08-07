package es.amadornes.openlauncher;

import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import mooklabs.FeedMessage;
import mooklabs.ReadXMLFromURl;
import mooklabs.SplashScreen;
import net.minecraft.login.MojangAuth;
import sk89.ReleaseManifest;
import es.amadornes.openlauncher.api.gui.ComponentFancyButton;
import es.amadornes.openlauncher.api.server.DownloadServer;
import es.amadornes.openlauncher.gui.GUI;
import es.amadornes.openlauncher.gui.TabConsole;
import es.amadornes.openlauncher.gui.TabModpacks;
import es.amadornes.openlauncher.gui.TabNews;
import es.amadornes.openlauncher.gui.TabSettings;
import es.amadornes.openlauncher.modpack.Modpack;
import es.amadornes.openlauncher.util.Downloader;
import es.amadornes.openlauncher.util.Util;

/**
 * OpenLauncher OpenLauncher.java
 * 
 * @author amadornes (amadornes.es) and DavidJotta (davidjuan.es)
 * @license GNU GPL v2 (http://choosealicense.com/licenses/gpl-v2/)
 */

public class OpenLauncher {

	/** location of them Mojang server that MC itself & the json's are pulled from*/
	public final static String mc_dl = "https://s3.amazonaws.com/Minecraft.Download/";
	/** location of them Mojang server that MC's resources are pulled from*/
	public final static String mc_res = "http://resources.download.minecraft.net/";
	/** location of them Mojang server that hosts the Minecraft Maven host*/
	public final static String mc_libs = "https://libraries.minecraft.net/";
	public static final String VERSION_MANIFEST_URL = "https://s3.amazonaws.com/Minecraft.Download/versions/%s/%s.json";
	public static final String BASE_LIB_URL = "https://s3.amazonaws.com/Minecraft.Download/libraries/";

	/**This is an xml file storing all of the modpack data*/
	static final String urlstring = "http://textuploader.com/03aa/raw";// dont think ill ever have to change this(for git i did)


	/* Instantiate GUI */
	public static GUI gui;

	/* Set some variables */
	public static boolean loggedIn = false;
	public static String username = "Unknown";
	public static Font font;

	private static DownloadServer[] servers = new DownloadServer[] {};//don't need this server new DownloadServerAmadornes() };
	public static List<Modpack> modpacks = new ArrayList<Modpack>();

	public static File instFolder = Util.getInstancesFolder();

	public static void main(String[] args) {
		/* Add shutdown event */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				exit();
			}
		}));

		System.out.println("preinit");
		SplashScreen s = new SplashScreen();

		instFolder.mkdirs();


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

		/* Add sidebar Tabs */
		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, gui.getHeight() - 75 - gui.insets.top - 45 - 45 - 45, 150, 40, "News").setSelected(true));
		gui.addTab(new TabNews(gui), 0);

		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, gui.getHeight() - 75 - gui.insets.top - 45 - 45, 150, 40, "Modpacks").setTab(1));
		gui.addTab(new TabModpacks(gui), 1);

		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, gui.getHeight() - 75 - gui.insets.top - 45, 150, 40, "Console").setTab(2));
		gui.addTab(new TabConsole(gui), 2);

		gui.addComponent(new ComponentFancyButton(29 - gui.insets.left, gui.getHeight() - 75 - gui.insets.top, 150, 40, "Settings").setTab(3));
		gui.addTab(new TabSettings(gui), 3);

		gui.setTab(0);
		System.out.println("init");

		init();//passworld check
		downloadMc();

		/* Center and show the GUI */
		gui.center();
		gui.show();
		System.out.println("postinit");

		postInit();
	}

	/**
	 * creates new thread to download modpacks
	 */
	public static void loadModpacks() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// {{ add my modpacks my way
				boolean isprivate = false;
				String mcVersion = "mcv1.7.2";
				int version = 1;
				for (FeedMessage m : ReadXMLFromURl.getModpackData(urlstring)) {
					try {
						modpacks.add(new Modpack(m.title, m.title, new URL(m.picLink), m.author, isprivate, version, m.version, mcVersion, "serverid", m.link));
						modpacks.get(modpacks.size() - 1).setDescription(m.description);// set desc

					} catch (MalformedURLException e) {
						System.out.println(m.title + " caused a link error!");
					}
				}

				for (Modpack m : modpacks) {
					File zip = new File(instFolder, m.getName() + m.getVersion() + ".jar");
					File imgFile = new File(instFolder, m.getName() + m.getVersion() + ".jpg");

					if (!zip.exists()) try {
						System.out.println("Downloading " + m.getName() + " from " + m.link);
						Downloader.download(new URL(m.link), zip);
						ImageIO.write(m.getLogo(), "jpg", imgFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
					else System.out.println(m.getName() + " Already Downloaded");

				}// }}


				//{{his stuff his way(cant use cause i dont have a server)
				for (DownloadServer sv : servers) {
					String[] packs = sv.getAvailablePacks();
					if (packs != null) {
						for (String id : packs) {
							Modpack p = sv.getPack(id);
							if (p != null) {
								modpacks.add(p);
							} else {
								System.err.println("An error occoured while download information about the pack \"" + id + "\" from the server \"" + sv.getServerID() + "\"");
							}
						}
					} else {
						System.err.println("Could not connect to to the server \"" + sv.getServerID() + "\"");
					}
				}//}}

			}
		}).start();
	}

	private static void preInit() {
		/* Create "openlauncher" data directory */
		Util.getWorkingDirectory().mkdirs();
		Util.getInstancesFolder().mkdirs();
		Util.getDownloadsFolder().mkdirs();
	}

	private static void init() {
		/* Try to login using the LastLogin function */
		LastLogin.tryLoading();
	}

	private static void postInit() {
		/* Start loading modpacks from the various servers */
		loadModpacks();
	}

	private static void exit() {
	}

	public static Modpack getPack(String server, String id) {
		for (Modpack m : modpacks)
			if (m.getId().equals(id) && m.getServerID().equals(server)) return m;
		return null;
	}

	public static DownloadServer getServer(String server) {
		for (DownloadServer s : servers)
			if (s.getServerID().equals(server)) return s;
		return null;
	}

	public static void setLoggedIn(boolean logged) {
		gui.labelUser.setVisible(!logged);
		gui.labelPass.setVisible(!logged);
		gui.user.setVisible(!logged);
		gui.pass.setVisible(!logged);
		loggedIn = logged;
		if (logged) {
			gui.removeComponent(gui.loginButton);
			gui.removeComponent(gui.loginButtonText);
		} else {
			gui.addComponent(gui.loginButton);
			gui.addComponent(gui.loginButtonText);
		}
	}

	public static void login() {
		Map<String, String> data = null;
		try {
			data = MojangAuth.authenticate(gui.user.getText(), new String(gui.pass.getPassword()));

			LastLogin.ACCESS_TOKEN = data.get("accessToken");
			LastLogin.CLIENT_TOKEN = data.get("clientToken");
			LastLogin.UUID = data.get("UUID");
			username = data.get("username");
		} catch (Exception e) {
		}

		if (data != null) {
			setLoggedIn(true);
			LastLogin.save();
		}
	}

	// {{minecraft jar stuff
	public static void downloadMc() {
		if (loggedIn) {
			File jarPath = new File(Util.getMinecraftFolder(), "minecraft.jar");
			File manifestPath = new File(Util.getMinecraftFolder(), "1.7.2.json");
			// Obtain the release manifest, save it, and parse it
			ReleaseManifest manifest;
			try {
				// If the JSON does not exist, update it
				if (!jarPath.exists()) {
					Downloader.download(new URL(VERSION_MANIFEST_URL.replace("%s", "1.7.2")), manifestPath);
					System.out.println("json updated");
				}// If the JAR does not exist, install it
				if (!jarPath.exists()) {
					Downloader.download(new URL("http://s3.amazonaws.com/Minecraft.Download/versions/%s/%s.jar".replace("%s", "1.7.2")), jarPath);
					System.out.println("minecraft.jar updated");
				}
			} catch (Exception e) {
				System.err.println("something went wrong while downloading mc jar or json");
				e.printStackTrace();
			}

			File contentDir = Util.getInstancesFolder();
			File librariesDir = new File(Util.getInstancesFolder(), "1.7.2.json");
			// Assets!
			// JSONObject json = JSONUtils.getJSONObjectFromInputStream(new FileInputStream(manifestPath));//get all needed libs

			// for (String library : json.getJSONArray("libraries").getString("name")) {

			/*
				for (Library library : manifest.getLibraries()) {
					if (true){//library.matches(Util.getPlatform())) {//right os
						URL url = new URL(BASE_LIB_URL); //library.getUrl(Util.getPlatform());
						File file = new File(librariesDir, library.getPath(Util.getPlatform()));

						if (!file.exists()) {
							Downloader.download(url, file);
						}

						//checkInterrupted();
					}
				}*/

		}
	}
	// }}

}
