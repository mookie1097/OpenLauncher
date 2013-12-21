package es.amadornes.openlauncher;

import es.amadornes.openlauncher.api.ComponentButton;
import es.amadornes.openlauncher.gui.GUI;

public class OpenLaucher {
	
	public static GUI gui;
	
	public static void main(String[] args){
		gui = new GUI(1280, 720);
		gui.center();
		gui.show();
	}
	
}
