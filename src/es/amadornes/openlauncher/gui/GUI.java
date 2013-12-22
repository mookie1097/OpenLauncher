package es.amadornes.openlauncher.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
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
	private boolean dragging = false;
	private Point mouseDownCompCoords = null;
	
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
		//Render separator column
		g.setPaint(ColorScheme.active.titlebar);
		g.fillRect(i.left + 170, i.top, 6, height - i.top - i.bottom);
		
		
		//Render image rectangle
		if(OpenLaucher.loggedIn){
			g.setPaint(Color.BLACK);
			g.fillRoundRect(i.left + 5, i.top + 5, 160, 160, 10, 10);
			
			String username = OpenLaucher.username;
			Font f = OpenLaucher.font.deriveFont(Font.BOLD, 36);
			
			try {
				BufferedImage img = ImageIO.read(new URL("https://minotar.net/avatar/" + username + "/150.png"));
				g.drawImage(img, i.left + 10, i.top + 10, null);
			} catch (Exception e) {
				int tx = 30;
				int ty = tx;
				g.translate(i.left + 3, i.top + 21);
				g.translate(tx, ty);
				g.rotate(Math.PI/4);
				
				g.setPaint(Color.WHITE);
				
				if(f.getFontName().equalsIgnoreCase("Pixelade")){
					f = f.deriveFont(55F);
				}
				g.setFont(f);
				g.drawString("ERROR", 0, 0);
				
				g.rotate(-(Math.PI/4));
				g.translate(-tx, -ty);
				g.translate(-(i.left + 10), -(i.top + 10));
			}
			
			g.setFont(f.deriveFont(18F));
			g.setPaint(Color.BLACK);
			drawCenteredString(username, i.left + 10, i.top + 20 + 160, 160, (Graphics)g);
		}else{
			//TODO NOT LOGGED IN
		}
	}
	
	private static void drawCenteredString(String s, int x, int y, int width, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int nx = x + (width - fm.stringWidth(s)) / 2;
		g.drawString(s, nx, y);
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		clickClose(x, y);
		clickMinimize(x, y);
	}
	@Override
	public void onMouseUp(int x, int y, int button) {
		dragging = false;
		if(clickClose(x, y))
			System.exit(0);
		if(clickMinimize(x, y))
			frame.setState(JFrame.ICONIFIED);
	}
	public void onMouseDown(int x, int y, int button){
		if(y >= 0 && y < insets.top){
			if(!clickClose(x, y) && !clickMinimize(x, y)){
				dragging = true;
				mouseDownCompCoords = new Point(x, y);
			}
		}
	}
	public void onMouseMove(int x, int y) {
		if(dragging){
			frame.setLocation((x + frame.getX()) - mouseDownCompCoords.x, (y + frame.getY()) - mouseDownCompCoords.y);
		}
	}
	private boolean clickClose(int x, int y){
		Insets i = insets;
		int width = 25;
		if(x >= this.width - i.right - (width * 2) && x < this.width - i.right){
			if(y >= 0 && y < i.top * 0.75){
				return true;
			}
		}
		return false;
	}
	private boolean clickMinimize(int x, int y){
		Insets i = insets;
		int width = 25;
		if(x >= this.width - i.right - (width * 2) - width - 1 && x < this.width - i.right - (width * 2) - 1){
			if(y >= 0 && y < i.top * 0.75){
				return true;
			}
		}
		return false;
	}
	
}
