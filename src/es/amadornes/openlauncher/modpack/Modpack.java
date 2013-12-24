package es.amadornes.openlauncher.modpack;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class Modpack {
	
	private String name;
	private BufferedImage logo;
	private boolean isPublic;
	private boolean unlocked;
	
	public Modpack(String name, URL logo, boolean isPublic) {
		this.name = name;
		try {
			this.logo = ImageIO.read(logo);
		} catch (Exception e) {}
		this.isPublic = isPublic;
	}
	
	public String getName() {
		return name;
	}
	
	public BufferedImage getLogo() {
		return logo;
	}
	
	public boolean isPublic() {
		return isPublic;
	}
	
	public void unlock() {
		this.unlocked = true;
	}
	
	public boolean isUnlocked() {
		return unlocked;
	}
	
}
