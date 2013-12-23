package es.amadornes.openlauncher.modpack;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class Modpack {
	
	private String name;
	private BufferedImage logo;
	
	public Modpack(String name, URL logo) {
		this.name = name;
		try {
			this.logo = ImageIO.read(logo);
		} catch (Exception e) {}
	}
	
	public String getName() {
		return name;
	}
	
	public BufferedImage getLogo() {
		return logo;
	}
	
}
