package es.amadornes.openlauncher.api;

import java.awt.FontMetrics;
import java.awt.Graphics;

public class RenderHelper {
	
	public static void drawCenteredString(String s, int x, int y, int width, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int nx = x + (width - fm.stringWidth(s)) / 2;
		int ny = y + (fm.getAscent() - fm.getDescent());
		g.drawString(s, nx, ny);
	}
	
	public static void drawCenteredString(String s, int x, int y, int width, int height, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int nx = x + (width - fm.stringWidth(s)) / 2;
		int ny = y + ((fm.getAscent() - fm.getDescent() + height)/2);
		g.drawString(s, nx, ny);
	}
	
}
