package es.amadornes.openlauncher.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.URL;

import javax.swing.JEditorPane;

import es.amadornes.openlauncher.api.gui.ComponentContainer;
import es.amadornes.openlauncher.api.gui.Frame;

public class ComponentContainerWebsite extends ComponentContainer {

	public static final int border = 2;
	
	public ComponentContainerWebsite(int x, int y, int width, int height, Frame owner, String url) {
		super(x, y, width, height, owner);
		
		JEditorPane jep = null;
		try {
			jep = new JEditorPane(new URL(url));
		} catch (Exception e) {
			jep = new JEditorPane();
			jep.setContentType("text/html");
		    jep.setText("<html>Could not load the news.</html>");
		}
		jep.setEditable(false);
		jep.setLocation(border, border);
		jep.setSize(width - border - border, height - border - border);
		
		addJComponent(jep);
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	protected void renderBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(0, 0, width, border);
		g2d.fillRect(0, 0, border, height);
		g2d.fillRect(width - border, 0, border, height);
		g2d.fillRect(0, height - border, width, border);
	}

}
