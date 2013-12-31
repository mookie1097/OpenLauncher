package es.amadornes.openlauncher.modpack;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class Modpack {
	
	private String id;
	private String name;
	private BufferedImage logo;
	private boolean isPrivate;
	private boolean unlocked;
	private String description = "No description available.";
	private String author = "Unknown";
	
	private int version = 0;
	private String versionString = "0.0.0";
	private String mcVersion = "1.6.4";
	
	public Modpack(String id, String name, URL logo, String author, boolean isPrivate, int version, String versionString, String mcVersion, String serverID) {
		this.id = id;
		this.name = name;
		try {
			this.logo = ImageIO.read(logo);
		} catch (Exception e) {}
		this.author = author;
		this.isPrivate = isPrivate;
		this.version = version;
		this.versionString = versionString;
		this.mcVersion = mcVersion;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public BufferedImage getLogo() {
		return logo;
	}
	
	public boolean isPublic() {
		return !isPrivate;
	}
	
	public boolean isPrivate(){
		return isPrivate;
	}
	
	public void unlock() {
		this.unlocked = true;
	}
	
	public boolean isUnlocked() {
		return unlocked;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public void setCreator(String creator) {
		this.author = creator;
	}
	
	public int getVersion() {
		return version;
	}
	
	public String getMcVersion() {
		return mcVersion;
	}
	
	public String getVersionString() {
		return versionString;
	}
	
	public String getAuthor() {
		return author;
	}
	
	@Override
	public String toString() {
		return "{id=" + id + ";name=" + name + ";hasLogo=" + (logo != null) + ";private=" + isPrivate + (isPrivate ? (";unlocked=" + unlocked) : "") + ";author=" + author + ";version=" + version + ";versionString=" + versionString + ";mcversion=" + mcVersion + "}";
	}
	
}
