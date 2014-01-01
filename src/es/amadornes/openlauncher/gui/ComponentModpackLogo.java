package es.amadornes.openlauncher.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import es.amadornes.openlauncher.OpenLauncher;
import es.amadornes.openlauncher.api.gui.Component;
import es.amadornes.openlauncher.api.gui.RenderHelper;
import es.amadornes.openlauncher.modpack.Modpack;

public class ComponentModpackLogo extends Component {
	
	private Modpack modpack;
	
	private int progress = 0;
	
	private boolean hovering = false;
	
	private ComponentContainerModpacks owner;
	
	public String[] text = new String[]{};
	
	public ComponentModpackLogo(int x, int y, int size, Modpack modpack, ComponentContainerModpacks owner) {
		super(x, y, size, size);
		this.modpack = modpack;
		this.owner = owner;
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						if(hovering){
							if(progress < 100){
								progress++;
							}
						}else{
							if(progress > 0){
								progress--;
							}
						}
						Thread.sleep(5);
						Thread.yield();
					} catch (Exception e) {}
				}
			}
		}).start();
	}
	
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		height = width;
	}
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		width = height;
	}
	
	public void onMouseEnter(int x, int y) {
		if(OpenLauncher.loggedIn)
			hovering = true;
	}
	public void onMouseLeave(int x, int y) {
		hovering = false;
	}
	
	@Override
	public void onMouseUp(int x, int y, int button) {
		if(OpenLauncher.loggedIn){
			if(owner.slide == false) {
				owner.slide = true;
				owner.selected = modpack;
				super.onMouseUp(x, y, button);
			} else if(owner.slide == true && owner.selected != modpack) {
				// TODO: Slide to a side (depending on Modpack position) to change actual Modpack
				owner.slide = true;
				owner.selected = modpack;
				super.onMouseUp(x, y, button);
			} else if(owner.slide == true && owner.selected == modpack) {
				owner.slide = false;
				super.onMouseUp(x, y, button);
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(modpack.getLogo(), 0, 0, width, height, null);
		
		float alpha = progress;
		alpha /= 100;
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.6F));
		
		g2d.setPaint(Color.LIGHT_GRAY);
		g2d.fillRect(0, 0, width, height);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		float fontDivider = 5;
		float size = width / fontDivider;
		
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.PLAIN, (int)size));
		
		text = RenderHelper.splitStringInLines(modpack.getName(), width - 8 - 8, g2d);
		
		int strings = 0;
		for(String s : text){
			g2d.drawString(s, 5, (size*(strings+1)) + 5);
			strings++;
		}
		
		g2d.setComposite(AlphaComposite.Clear);
	}
	
}
