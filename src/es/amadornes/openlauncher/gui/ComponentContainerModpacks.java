package es.amadornes.openlauncher.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import es.amadornes.openlauncher.OpenLaucher;
import es.amadornes.openlauncher.api.ComponentContainer;
import es.amadornes.openlauncher.api.Frame;
import es.amadornes.openlauncher.modpack.Modpack;

public class ComponentContainerModpacks extends ComponentContainer {
	
	private Insets i = new Insets(10, 10, 10, 10);
	private int border = 2;
	private int packsPerRow = 5;//FIXME 7
	private int packSeparation = 10;
	private float descriptionArea = 0.5F;
	private Insets descriptionInsets = new Insets(10, 10, 10, 10);
	
	private int logoWidth = 20;
	
	public Modpack selected = null;
	
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
		
		int pack = 0;
		for(Modpack m : OpenLaucher.modpacks){
			if(m.isPublic() || m.isUnlocked()){
				addComponent(new ComponentModpackLogo(i.left + border + ((packSeparation + this.logoWidth) * ((pack%packsPerRow))), i.top + border + ((packSeparation + this.logoWidth) * ((int)(Math.floor(pack/packsPerRow)))), this.logoWidth, m, this));
				pack++;
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		renderBackground(g);
		renderComponents(g.create(0, 0, width - i.left, height - i.top));
	}
	
	@Override
	protected void renderBackground(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(0, 0, width, height);
	}

}
