package es.amadornes.openlauncher.util;

import java.io.File;
import java.io.FileInputStream;

import org.json.JSONObject;

public class Util {
	
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
	
	public static File getDownloadsFolder(){
		return new File(getWorkingDirectory(), "downloads/");
	}
	
	public static boolean isPackInstalled(String server, String packid){
		return new File(getInstancesFolder(), server + "_" + packid + "/").exists();
	}
	
	public static JSONObject getPackData(String server, String packid){
		try{
			File f = new File(getInstancesFolder(), server + "_" + packid + "/pack.json");
			FileInputStream fis = new FileInputStream(f);
			return JSONUtils.getJSONObjectFromInputStream(fis);
		}catch(Exception e){}
		return null;
	}
	
	public static File getPackDataFile(String server, String packid){
		try{
			return new File(getInstancesFolder(), server + "_" + packid + "/pack.json");
		}catch(Exception e){}
		return null;
	}
	
}
