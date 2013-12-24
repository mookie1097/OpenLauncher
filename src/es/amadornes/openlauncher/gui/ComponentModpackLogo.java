package es.amadornes.openlauncher.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import es.amadornes.openlauncher.OpenLaucher;
import es.amadornes.openlauncher.api.Component;
import es.amadornes.openlauncher.api.RenderHelper;
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
		hovering = true;
	}
	public void onMouseLeave(int x, int y) {
		hovering = false;
	}
	
	@Override
	public void onMouseUp(int x, int y, int button) {
		owner.selected = modpack;
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
		
		text = RenderHelper.splitStringInLines(modpack.getName(), width - 5 - 5, g2d);
		
		String str = "";
		for(String s : text){
			str += s + "\n";
		}
		
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.PLAIN, width / 8));
		g2d.drawString(str, 5, (width / 8) + 5);
		
		g2d.setComposite(AlphaComposite.Clear);
	}
	
}
