package es.amadornes.openlauncher.gui;

import java.awt.Color;
import java.awt.Graphics;

import es.amadornes.openlauncher.OpenLauncher;
import es.amadornes.openlauncher.api.gui.RenderHelper;

public class TabNews extends Tab {

	public TabNews(GUI owner) {
		super(0, 0, 0, 0, owner);
		addComponent(new ComponentContainerWebsite(20, 80, getWidth() - 20 - 20, getHeight() - 80 - 20, owner, "http://m.simplepie.org/?feed=http%3A%2F%2Fapocalypsead.enjin.com%2Fhome%2Fm%2F23148107%2Frss%2Ftrue"));
	}

	@Override
	protected void renderBackground(Graphics g) {
		g.setFont(OpenLauncher.font.deriveFont(64F));
		g.setColor(Color.BLACK);
		RenderHelper.drawCenteredString("News", 0, 30, getWidth(), g);
	}

}
