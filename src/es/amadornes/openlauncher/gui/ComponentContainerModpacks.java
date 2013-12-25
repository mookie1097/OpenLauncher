package es.amadornes.openlauncher.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import es.amadornes.openlauncher.OpenLaucher;
import es.amadornes.openlauncher.api.ComponentContainer;
import es.amadornes.openlauncher.api.Frame;
import es.amadornes.openlauncher.api.RenderHelper;
import es.amadornes.openlauncher.modpack.Modpack;

public class ComponentContainerModpacks extends ComponentContainer {
	
	private Insets i = new Insets(10, 10, 10, 10);
	private int border = 2;
	private int packsPerRow = 7;
	private int packSeparation = 10;
	private float descriptionArea = 0.5F;
	private Insets descriptionInsets = new Insets(10, 10, 10, 10);
	
	private float progress = 0;
	
	private int logoWidth = 20;
	
	public Modpack selected = null;
	public boolean slide = false;
	
	private float scroll = 0;
	private boolean needsScroll = false;
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	public ComponentContainerModpacks(int x, int y, int width, int height, Frame owner) {
		super(x, y, width, height, owner);
		
		float logoWidth = width - i.left - i.right - (border*2) - (packSeparation * (packsPerRow - 1));
		logoWidth /= packsPerRow;
		this.logoWidth = (int) logoWidth;
		
		int shownPacks = 0;

		for(Modpack m : OpenLaucher.modpacks){
			if(m.isPublic() || m.isUnlocked()){
				shownPacks++;
			}
		}
		
		needsScroll = (i.top + i.bottom + (border * 2) + ((packSeparation + this.logoWidth) * (((int)(Math.floor(shownPacks/packsPerRow) + 1)) / ((progress/100) * (1F - descriptionArea))))) > height;
		
		int pack = 0;
		for(Modpack m : OpenLaucher.modpacks){
			if(m.isPublic() || m.isUnlocked()){
				addComponent(new ComponentModpackLogo(i.left + border + ((packSeparation + this.logoWidth) * ((pack%packsPerRow))), i.top + border + ((packSeparation + this.logoWidth) * ((int)(Math.floor(pack/packsPerRow)))), this.logoWidth, m, this));
				pack++;
			}
		}
		
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						if(slide){
							if(progress < 100){
								progress += 0.1;
							}
						}else{
							if(progress > 0){
								progress -= 0.1;
							}else{
								if(selected != null)
									selected = null;
							}
						}
						double delay = 20 * descriptionArea;
						delay /= 10;
						Thread.sleep((int)delay);
						Thread.yield();
					} catch (Exception e) {}
				}
			}
		}).start();
	}
	
	@Override
	public void render(Graphics g) {
		renderBackground(g);
		renderModpackInfoTab(g);
		float description = progress;
		description /= 100;
		description *= descriptionArea;
		description *= height;
		renderComponents(g.create(border, border, width - i.left - (border*2), height - i.top - (border*2) - ((int)description)));
	}
	
	@Override
	protected void renderBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		float description = progress;
		description /= 100;
		description *= descriptionArea;
		description *= height;
		
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(0, 0, width, border);
		g2d.fillRect(0, 0, border, height);
		g2d.fillRect(width - border, 0, border, height);
		g2d.fillRect(0, height - ((int)description) - border, width, (int)description + border);
		//g2d.fillRect(0, 0, width, height);
	}
	
	protected void renderModpackInfoTab(Graphics gr){
		float description = progress;
		description /= 100;
		description *= descriptionArea;
		description *= height;
		
		Graphics g = gr.create(descriptionInsets.left, height - ((int)description) - border + descriptionInsets.top, width - descriptionInsets.left - descriptionInsets.right, (int)description + border - descriptionInsets.top - descriptionInsets.bottom);
		Graphics2D g2d = (Graphics2D) g;
		
		int width = g2d.getClipBounds().width;
		int finalHeight = (int) ((height * descriptionArea) + border - descriptionInsets.top - descriptionInsets.bottom);
		
		try{
			g2d.drawImage(selected.getLogo(), 0, 0, finalHeight, finalHeight, null);

			g2d.setColor(Color.BLACK);
			int tSize = 36;
			int dSize = 18;
			g2d.setFont(new Font("Arial", Font.PLAIN, tSize));
			RenderHelper.drawVerticallyCenteredString(selected.getName(), finalHeight + 15, 0, tSize, g2d);
			g2d.setFont(new Font("Arial", Font.PLAIN, dSize));
			String[] desc = RenderHelper.splitStringInLines(selected.getDescription(), width - finalHeight - 15, g2d);
			int line = 0;
			for(String s : desc){
				RenderHelper.drawVerticallyCenteredString(s, finalHeight + 15, tSize + 10 + ((dSize + 7)*line), tSize, g2d);
				line++;
			}
		}catch(Exception e){}

		g2d.setColor(Color.RED);
		g2d.setFont(new Font("Arial", Font.BOLD, 20));
		RenderHelper.drawCenteredString("x", width - 10, 0, 10, 10, g2d);
	}
	
	@Override
	public void onMouseUp(int x, int y, int button) {
		float description = progress;
		description /= 100;
		description *= descriptionArea;
		description *= height;
		int width = this.width - i.left - (border*2);
		
		if(slide){
			if(x >= (width - 15) && x < width){
				if(y >= (height - ((int)description) - border + descriptionInsets.top) && y < (height - ((int)description) - border + descriptionInsets.top + 10)){
					slide = false;
					return;
				}
			}
		}
		super.onMouseUp(x, y, button);
	}

}
