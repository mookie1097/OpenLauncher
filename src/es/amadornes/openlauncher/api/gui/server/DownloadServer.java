package es.amadornes.openlauncher.api.gui.server;

import java.util.ArrayList;
import java.util.List;

import es.amadornes.openlauncher.modpack.Modpack;

public abstract class DownloadServer {
	
	public static List<DownloadServer> servers = new ArrayList<DownloadServer>();
	
	public static DownloadServer getServer(String id){
		for(DownloadServer d : servers){
			if(d.id.equals(id)){
				return d;
			}
		}
		return null;
	}
	
	protected String id = "";
	
	public DownloadServer(String serverID) {
		id = serverID;
	}
	
	public abstract String[] getAvailablePacks();
	public abstract Modpack getPack(String pack);
	public abstract boolean canUnlockPackWithKey(String pack, String key);
	public abstract boolean isRecursiveUpdate();
	public abstract void updatePack(String pack, int version);
	
	public String getServerID(){
		return id;
	}
	
}
