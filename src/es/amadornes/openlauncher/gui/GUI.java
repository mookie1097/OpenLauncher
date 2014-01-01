package es.amadornes.openlauncher.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import es.amadornes.openlauncher.OpenLauncher;
import es.amadornes.openlauncher.api.gui.ColorScheme;
import es.amadornes.openlauncher.api.gui.Component;
import es.amadornes.openlauncher.api.gui.ComponentButton;
import es.amadornes.openlauncher.api.gui.Frame;
import es.amadornes.openlauncher.api.gui.RenderHelper;

public class GUI extends Frame {
	
	public int screen = 0;
	private boolean dragging = false;
	private Point mouseDownCompCoords = null;
	private List<Tab> tabs = new ArrayList<Tab>();
	
	public JLabel labelUser = new JLabel("Username:");
	public JTextField user = new JTextField(50);
	public JLabel labelPass = new JLabel("Password:");
	public JPasswordField pass = new JPasswordField(50);

	public Component loginButton;
	public Component loginButtonText;
	
	public GUI(int width, int height) {
		super(width, height);
		insets = new Insets(30, 7, 7, 7);
		
		user.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		pass.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		
		frame.add(user);
		frame.add(pass);
		frame.add(labelUser);
		frame.add(labelPass);

		labelUser.setLocation(insets.left + 5 + 10, insets.top + 10 + 10);
		labelUser.setSize(160 - 10 - 10, 10);
		
		user.setLocation(insets.left + 5 + 10, insets.top + 10 + 10 + labelUser.getHeight() + 5);
		user.setSize(160 - 10 - 10, user.getMinimumSize().height + 10);
		
		labelPass.setLocation(insets.left + 5 + 10, insets.top + 10 + 10 + user.getHeight() + 5 + labelUser.getHeight() + 5);
		labelPass.setSize(160 - 10 - 10, 10);
		
		pass.setLocation(insets.left + 5 + 10, insets.top + 10 + 10 + user.getHeight() + 5 + labelUser.getHeight() + 5 + labelPass.getHeight() + 5);
		pass.setSize(160 - 10 - 10, pass.getMinimumSize().height + 10);

		user.setVisible(true);
		pass.setVisible(true);
		labelUser.setVisible(true);
		labelPass.setVisible(true);
		
		loginButton = new ComponentButton(insets.left + 10 + 10 - 2, insets.top + 5 + 87, 160 - 10 - 10 - 10 - 10, 30, "Login"){
			@Override
			public void onMouseUp(int x, int y, int button) {
				OpenLauncher.login();
			}
		};
		addComponent(loginButton);
	}
	
	@Override
	public void addComponent(Component c) {
		if(c instanceof Tab){
			addTab((Tab) c);
		}else{
			super.addComponent(c);
		}
	}
	
	@Override
	public void addComponent(Component c, int zIndex) {
		if(c instanceof Tab){
			addTab((Tab) c);
		}else{
			super.addComponent(c, zIndex);
		}
	}
	
	public void removeComponent(Component c){
		components.remove(c);
	}
	
	public void addTab(Tab tab){
		tabs.add(tab);
	}
	
	public void addTab(Tab tab, int id){
		while(tabs.size() <= id)
			tabs.add(null);
		tabs.set(id, tab);
	}
	
	public List<Tab> getTabs(){
		return tabs;
	}
	
	public Tab getTab(){
		if(tabs.size() > tab)
			return tabs.get(tab);
		return null;
	}
	
	@Override
	protected synchronized void render(Graphics g) {
		super.render(g);
		
		if(!OpenLauncher.loggedIn){
			labelUser.paint(g.create(labelUser.getX(), labelUser.getY(), labelUser.getWidth(), labelUser.getHeight()));
			labelPass.paint(g.create(labelPass.getX(), labelPass.getY(), labelPass.getWidth(), labelPass.getHeight()));
			
			user.paint(g.create(user.getX(), user.getY(), user.getWidth(), user.getHeight()));
			pass.paint(g.create(pass.getX(), pass.getY(), pass.getWidth(), pass.getHeight()));
		}
	}
	
	@Override
	protected synchronized void renderBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(ColorScheme.active.background);
		g2d.fillRect(0, 0, width, height);
		renderFrame(g2d);
		renderSidebar(g2d);
		
		//Render title
		g2d.setFont(new Font("Arial", Font.PLAIN, 14));
		g.setColor(Color.BLACK);
		RenderHelper.drawCenteredString("Open Launcher", 0, 0, width, insets.top, g2d);
		
		if(getTab() != null){
			getTab().render((Graphics2D) g2d.create(insets.left + 170 + 6, insets.top, width - insets.left - 170 - 6 - insets.right, height - insets.top - insets.bottom));
		}
	}
	
	private synchronized void renderFrame(Graphics2D g){
		Insets i = insets;
		
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
		if(OpenLauncher.loggedIn){
			g.setPaint(Color.BLACK);
			g.fillRoundRect(i.left + 5, i.top + 5, 160, 160, 10, 10);
			
			String username = OpenLauncher.username;
			Font f = OpenLauncher.font.deriveFont(Font.BOLD, 36);
			
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
			RenderHelper.drawCenteredString(username, i.left + 10, i.top + 20 + 160, 160, (Graphics)g);
		}else{
			g.setPaint(ColorScheme.active.titlebar);
			g.fillRoundRect(i.left + 5, i.top + 5, 160, 160, 10, 10);
			//TODO: If user is not logged in show Login form
		}
	}
	
	@Override
	public void onClick(int x, int y, int button) {
		clickClose(x, y);
		clickMinimize(x, y);
		if(x >= insets.left + 170 + 6 && x < width - insets.right){
			if(y >= insets.top && y < height - insets.bottom){
				if(getTab() != null){
					getTab().onClick(x - insets.left - 170 - 6, y - insets.top, button);
				}
			}
		}
	}
	@Override
	public void onMouseUp(int x, int y, int button) {
		dragging = false;
		if(clickClose(x, y))
			System.exit(0);
		if(clickMinimize(x, y))
			frame.setState(JFrame.ICONIFIED);
		if(x >= insets.left + 170 + 6 && x < width - insets.right){
			if(y >= insets.top && y < height - insets.bottom){
				if(getTab() != null){
					getTab().onMouseUp(x - insets.left - 170 - 6, y - insets.top, button);
				}
			}
		}
	}
	public void onMouseDown(int x, int y, int button){
		if(y >= 0 && y < insets.top){
			if(!clickClose(x, y) && !clickMinimize(x, y)){
				dragging = true;
				mouseDownCompCoords = new Point(x, y);
			}
		}
		
		if(x >= user.getX() && x < (user.getX() + user.getWidth())){
			if(y >= user.getY() && y < (user.getY() + user.getHeight())){
				user.requestFocus();
			}
		}
		
		if(x >= pass.getX() && x < (pass.getX() + pass.getWidth())){
			if(y >= pass.getY() && y < (pass.getY() + pass.getHeight())){
				pass.requestFocus();
			}
		}
		
		if(x >= insets.left + 170 + 6 && x < width - insets.right){
			if(y >= insets.top && y < height - insets.bottom){
				if(getTab() != null){
					getTab().onMouseDown(x - insets.left - 170 - 6, y - insets.top, button);
				}
			}
		}
	}
	public void onMouseMove(int x, int y) {
		if(dragging){
			frame.setLocation((x + frame.getX()) - mouseDownCompCoords.x, (y + frame.getY()) - mouseDownCompCoords.y);
		}
		if(x >= insets.left + 170 + 6 && x < width - insets.right){
			if(y >= insets.top && y < height - insets.bottom){
				if(getTab() != null){
					getTab().onMouseMove(x - insets.left - 170 - 6, y - insets.top);
				}
			}
		}
	}
	@Override
	public void onMouseEnter(int x, int y) {
		if(x >= insets.left + 170 + 6 && x < width - insets.right){
			if(y >= insets.top && y < height - insets.bottom){
				if(getTab() != null){
					getTab().onMouseEnter(x - insets.left - 170 - 6, y - insets.top);
				}
			}
		}
	}
	@Override
	public void onMouseLeave(int x, int y) {
		if(x >= insets.left + 170 + 6 && x < width - insets.right){
			if(y >= insets.top && y < height - insets.bottom){
				if(getTab() != null){
					getTab().onMouseLeave(x - insets.left - 170 - 6, y - insets.top);
				}
			}
		}
	}
	@Override
	public void onMouseWheelMove(int x, int y, int amount) {
		if(x >= insets.left + 170 + 6 && x < width - insets.right){
			if(y >= insets.top && y < height - insets.bottom){
				if(getTab() != null){
					getTab().onMouseWheelMove(x - insets.left - 170 - 6, y - insets.top, amount);
				}
			}
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
