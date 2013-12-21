package es.amadornes.openlauncher.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import es.amadornes.openlauncher.OpenLaucher;
import es.amadornes.openlauncher.api.ColorScheme;
import es.amadornes.openlauncher.api.Frame;

public class GUI extends Frame {
	
	public int screen = 0;
	
	public GUI(int width, int height) {
		super(width, height);
		insets = new Insets(30, 7, 7, 7);
	}
	
	@Override
	protected synchronized void renderBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(ColorScheme.active.background);
		g2d.fillRect(0, 0, width, height);
		renderFrame(g2d);
		renderSidebar(g2d);
	}
	
	private synchronized void renderFrame(Graphics2D g){
		Insets i = insets;
		
		//Render grip
		renderGrip(g);
		
		//Render titlebar
		g.setPaint(ColorScheme.active.titlebar);
		g.fillRect(0, 0, width, i.top);
		
		//Render other bars
		g.fillRect(0, 0, i.left, height);
		g.fillRect(0, height - i.bottom, width, i.bottom);
		g.fillRect(width - i.right, 0, i.right, height);
		
		//Render buttons
		renderButtons(g);
	}
	private synchronized void renderGrip(Graphics2D g){
		Insets i = insets;
		int size = 25;
		g.setPaint(ColorScheme.active.titlebar);
		Polygon p = new Polygon();
		p.addPoint(width - i.right - size, height);
		p.addPoint(width, height - i.bottom - size);
		p.addPoint(width, height);
		g.fillPolygon(p);
	}
	private synchronized void renderButtons(Graphics2D g){
		Insets i = insets;
		int width = 25;
		//Render buttons
		g.setColor(new Color(0xE85F5F));
		g.fillRect(this.width - i.right - (width * 2), 0, width * 2, (int)(i.top * 0.75));
		g.setColor(new Color(0xD5D5D5));
		g.fillRect(this.width - i.right - (width * 2) - width - 1, 0, width, (int)(i.top * 0.75));
		
		//Render text
		Font f = new Font("Arial", Font.PLAIN, 16);
		g.setFont(f);
		FontMetrics fm = new FontMetrics(f){private static final long serialVersionUID = 1L;};
		
		String close = "x";
		String minimize = "_";
		Rectangle2D cb = fm.getStringBounds(close, g);
		Rectangle2D mb = fm.getStringBounds(minimize, g);

		g.setColor(new Color(0xEEEEEE));
		g.drawString(close, this.width - i.right - width - ((float)(cb.getWidth()/2)), ((float)(((i.top * 0.75)/2) + (cb.getHeight()/2))) - 4);
		g.setColor(new Color(0x999999));
		g.drawString(minimize, this.width - i.right - (width * 2) - 3 - (width/2) - ((float)(mb.getWidth()/2)), ((float)(((i.top * 0.75)/2) + (mb.getHeight()/2))) - 8);
	}
	
	private synchronized void renderSidebar(Graphics2D g){
		Insets i = insets;
		//TODO RENDER BAR
		g.setPaint(ColorScheme.active.titlebar);
		g.fillRect(i.left + 170, i.top, 6, height - i.top - i.bottom);
		
		
		//Render image rectangle
		if(OpenLaucher.loggedIn){
			g.setPaint(Color.BLACK);
			g.fillRoundRect(i.left + 5, i.top + 5, 160, 160, 10, 10);
			String username = OpenLaucher.username;
			try {
				BufferedImage img = ImageIO.read(new URL("https://minotar.neta/avatar/" + username + "/150.png"));
				g.drawImage(img, i.left + 10, i.top + 10, null);
			} catch (Exception e) {
				int tx = 30;
				int ty = tx;
				g.translate(i.left + 3, i.top + 21);
				g.translate(tx, ty);
				g.rotate(Math.PI/4);
				
				g.setPaint(Color.WHITE);
				Font f = OpenLaucher.font.deriveFont(Font.BOLD, 36);
				if(f.getFontName().equalsIgnoreCase("Pixelade")){
					f = f.deriveFont(55F);
				}
				g.setFont(f);
				g.drawString("ERROR", 0, 0);
				
				g.rotate(-(Math.PI/4));
				g.translate(-tx, -ty);
				g.translate(-(i.left + 10), -(i.top + 10));
				
				g.setFont(f.deriveFont(18F));
				g.setPaint(Color.BLACK);
				
				FontMetrics fm = new FontMetrics(f){private static final long serialVersionUID = 1L;};
				Rectangle2D b = fm.getStringBounds(username, g);
				g.drawString(username, i.left + 5 + ((float)((b.getWidth()/2))), i.top + 15 + 160);//TODO
			}
		}
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		clickClose(x, y);
		clickMinimize(x, y);
	}
	@Override
	public void onMouseUp(int x, int y, int button) {
		clickClose(x, y);
		clickMinimize(x, y);
	}
	private void clickClose(int x, int y){
		Insets i = insets;
		int width = 25;
		if(x >= this.width - i.right - (width * 2) && x < this.width - i.right){
			if(y >= 0 && y < i.top * 0.75){
				System.exit(0);
			}
		}
	}
	private void clickMinimize(int x, int y){
		Insets i = insets;
		int width = 25;
		if(x >= this.width - i.right - (width * 2) - width - 1 && x < this.width - i.right - (width * 2) - 1){
			if(y >= 0 && y < i.top * 0.75){
				frame.setState(JFrame.ICONIFIED);
			}
		}
	}
	
}
