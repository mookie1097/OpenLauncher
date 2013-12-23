package es.amadornes.openlauncher.gui;

import java.awt.Graphics;
import java.util.ArrayList;

import es.amadornes.openlauncher.OpenLaucher;
import es.amadornes.openlauncher.api.RenderHelper;
import es.amadornes.openlauncher.modpack.Modpack;

public class TabModpacks extends Tab {

	public TabModpacks(GUI owner) {
		super(owner);
		addComponent(new ComponentModpackLogo(200, 200, 128, new ArrayList<Modpack>(OpenLaucher.modpacks.keySet()).get(0)));
	}

	@Override
	protected void renderBackground(Graphics g) {
		g.setFont(OpenLaucher.font.deriveFont(64F));
		RenderHelper.drawCenteredString("Modpacks", 0, 30, getWidth(), g);
	}
	
	protected void renderPackPanel(Graphics g){
		
	}

}
