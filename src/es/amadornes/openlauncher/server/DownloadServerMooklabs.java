package es.amadornes.openlauncher.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import es.amadornes.openlauncher.OpenLauncher;
import es.amadornes.openlauncher.api.server.DownloadServer;
import es.amadornes.openlauncher.modpack.Modpack;
import es.amadornes.openlauncher.util.Downloader;
import es.amadornes.openlauncher.util.JSONUtils;
import es.amadornes.openlauncher.util.Util;
import es.amadornes.openlauncher.util.ZipUtils;

public class DownloadServerMooklabs extends DownloadServer {

	private static final String serverURL = "http://pc.amadornes.es/openlauncher/";
	public String url = "http://www.technicpack.net/modpack/details/";
	//public Icon = new ImageIcon(pack.getLogo().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH));


	public DownloadServerMooklabs() {
		super("amadornes");
	}

	@Override
	public String[] getAvailablePacks() {
		try{
			URL packList = new URL(serverURL + "modpacks/modpacks.json");
			URLConnection con = packList.openConnection();
			con.connect();
			JSONArray list = JSONUtils.getJSONArrayFromInputStream(con.getInputStream());

			List<String> l = new ArrayList<String>();
			for(int i = 0; i < list.length(); i++)
				l.add(list.getString(i));

			return l.toArray(new String[l.size()]);
		}catch(Exception exception){}
		return null;
	}







	@Override
	public Modpack getPack(String pack) {
		try{
			URL packLogo = new URL(serverURL + "modpacks/" + pack + "/pack.png");
			URL packData = new URL(serverURL + "modpacks/" + pack + "/pack.json");
			URL packDescription = new URL(serverURL + "modpacks/" + pack + "/description.txt");
			URLConnection con = packData.openConnection();
			con.connect();
			JSONObject data = JSONUtils.getJSONObjectFromInputStream(con.getInputStream());

			String id = pack;
			String name = data.getString("name");
			String author = data.getString("author");
			int versionid = data.getInt("versionid");
			String version = data.getString("version");
			String mcversion = data.getString("mcversion");
			boolean isPrivate = data.getBoolean("private");

			String description = "";

			try{
				URLConnection desc = packDescription.openConnection();
				desc.connect();
				description = JSONUtils.joinStringFromInputStream(desc.getInputStream(), "\n");
			}catch(Exception ex){}

			Modpack modpack = new Modpack(id, name, packLogo, author, isPrivate, versionid, version, mcversion, this.id);
			if(!description.equals("") && description != null){
				modpack.setDescription(description);
			}

			return modpack;
		}catch(Exception exception){}
		return null;
	}

	@Override
	public boolean canUnlockPackWithKey(String pack, String key) {
		return false;
	}

	@Override
	public boolean isRecursiveUpdate() {
		return true;
	}

	@Override
	public void updatePack(String pack) {
		Modpack server = OpenLauncher.getPack(this.id, pack);
		Modpack client = server.getClientVersion();
		File folder = new File(Util.getInstancesFolder(), this.id + "_" + pack + "/");
		File dlFolder = new File(Util.getDownloadsFolder(), this.id + "_" + pack + "/");

		File pdf = new File(folder, "pack.json");

		folder.mkdirs();
		dlFolder.mkdirs();

		int localVersion = 0;
		int serverVersion = server.getVersion();
		if(client != null){
			localVersion = client.getVersion();
		}

		for(int ver = (localVersion + 1); ver <= serverVersion; ver++){
			try{
				URL zipURL = new URL(serverURL + "modpacks/" + pack + "/versions/" + ver + ".zip");
				File zip = new File(dlFolder, ver + ".zip");

				Downloader.download(zipURL, zip);
				ZipUtils.unzip(zip.getAbsolutePath(), folder.getAbsolutePath());

				File delFile = new File(folder, "deleted.json");

				if(delFile.exists()){
					JSONArray files = JSONUtils.getJSONArrayFromInputStream(new FileInputStream(delFile));
					for(int i2 = 0; i2 < files.length(); i2++){
						File f = new File(folder, files.getString(i2));
						if(f.exists())
							f.delete();
					}
				}

				zip.delete();

				JSONObject packdata = null;
				try{
					packdata = JSONUtils.getJSONObjectFromInputStream(new FileInputStream(pdf));
				}catch(Exception e){}
				if(packdata == null){
					URL packdataurl = new URL(serverURL + "modpacks/" + pack + "/pack.json");
					Downloader.download(packdataurl, pdf);
					try{
						packdata = JSONUtils.getJSONObjectFromInputStream(new FileInputStream(pdf));
					}catch(Exception e){}
				}
				System.out.println("Has: " + packdata.has("versionid"));
				packdata.remove("versionid");
				packdata.put("versionid", ver);

				if(pdf.exists())
					pdf.delete();
				pdf.createNewFile();

				FileWriter w = new FileWriter(pdf);
				packdata.write(w);
				w.close();
			}catch(Exception e){
				System.err.println("There was an error updating \"" + pack + "\" from the server \"" + this.id + "\". Contact a server admin to help you.");
				break;
			}
		}

		dlFolder.delete();
	}
}
