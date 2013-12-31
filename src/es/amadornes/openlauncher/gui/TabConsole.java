package es.amadornes.openlauncher.gui;

import java.awt.Color;
import java.awt.Graphics;

import es.amadornes.openlauncher.OpenLauncher;
import es.amadornes.openlauncher.api.gui.RenderHelper;

public class TabConsole extends Tab {

	public TabConsole(GUI owner) {
		super(0, 0, 0, 0, owner);
	}

	@Override
	protected void renderBackground(Graphics g) {
		g.setFont(OpenLauncher.font.deriveFont(64F));
		g.setColor(Color.BLACK);
		RenderHelper.drawCenteredString("Console", 0, 30, getWidth(), g);
	}

}
