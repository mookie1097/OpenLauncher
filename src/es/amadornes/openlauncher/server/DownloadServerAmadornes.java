package es.amadornes.openlauncher.server;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import es.amadornes.openlauncher.api.gui.server.DownloadServer;
import es.amadornes.openlauncher.modpack.Modpack;
import es.amadornes.openlauncher.util.JSONUtils;

public class DownloadServerAmadornes extends DownloadServer {
	
	private static final String serverURL = "http://pc.amadornes.es/openlauncher/";

	public DownloadServerAmadornes() {
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
			if(!description.equals("")){
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
	public void updatePack(String pack, int version) {
		
	}

}
