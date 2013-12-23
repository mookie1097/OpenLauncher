package es.amadornes.openlauncher.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import es.amadornes.openlauncher.api.Component;
import es.amadornes.openlauncher.modpack.Modpack;

public class ComponentModpackLogo extends Component {
	
	private Modpack modpack;
	
	private int progress = 0;
	
	private boolean hovering = false;
	
	public ComponentModpackLogo(int x, int y, int size, Modpack modpack) {
		super(x, y, size, size);
		this.modpack = modpack;
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
						Thread.sleep(10);
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
	
	@Override
	public void onMouseEnter(int x, int y) {
		hovering = true;
	}
	
	@Override
	public void onMouseLeave(int x, int y) {
		hovering = false;
	}
	
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(modpack.getLogo(), 0, 0, width, height, null);
		
		float alpha = progress;
		alpha /= 100;
		alpha *= 0.6F;
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
		g2d.setPaint(Color.GRAY);
		g2d.drawRect(0, 0, width, height);
		
		g2d.setComposite(AlphaComposite.Clear);
	}
	
}
