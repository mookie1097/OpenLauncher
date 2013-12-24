package es.amadornes.openlauncher.gui;

import java.awt.Graphics;

import es.amadornes.openlauncher.OpenLaucher;
import es.amadornes.openlauncher.api.RenderHelper;

public class TabModpacks extends Tab {

	public TabModpacks(GUI owner) {
		super(0, 0, 0, 0, owner);
		addComponent(new ComponentContainerModpacks(20, 80, getWidth() - 20 - 20, getHeight() - 80 - 20, owner));
	}

	@Override
	protected void renderBackground(Graphics g) {
		g.setFont(OpenLaucher.font.deriveFont(64F));
		RenderHelper.drawCenteredString("Modpacks", 0, 30, getWidth(), g);
	}

}
