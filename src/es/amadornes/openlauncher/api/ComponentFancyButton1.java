package es.amadornes.openlauncher.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import es.amadornes.openlauncher.OpenLaucher;

public class ComponentFancyButton1 extends Component {

	protected String text = "";
	
	private static int movement = 15;
	
	protected boolean hovering = false;
	
	protected int realX = 0;
	protected int moved = 0;
	
	protected boolean selected = false;
	
	protected int tab = 0;

	public ComponentFancyButton1(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		realX = x;
		this.text = text != null ? text : "";
		final ComponentFancyButton1 me = this;
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						if(hovering){
							if(moved < movement){
								moved++;
								me.x--;
								me.width++;
							}
						}else{
							if(moved > 0){
								moved--;
								me.x++;
								me.width--;
							}
						}
						Thread.sleep(200/movement);
					} catch (Exception e) {}
				}
			}
		}).start();
	}
	
	public ComponentFancyButton1 setSelected(boolean selected) {
		select();
		return this;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setX(int x) {
		this.x = this.x + (realX - x);
		realX = x;
	}
	
	public void onMouseEnter(int x, int y) {
		hovering = true;
	}
	public void onMouseLeave(int x, int y) {
		hovering = false;
	}
	public void onMouseUp(int x, int y, int button) {
		select();
	}
	
	protected void select(){
		if(owner != null){
			for(Component c : owner.components.keySet()){
				if(c instanceof ComponentFancyButton1){
					((ComponentFancyButton1)c).selected = false;
				}
			}
			owner.tab = tab;
		}
		selected = true;
	}

	@Override
	public void render(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		
		Polygon p = new Polygon();
		p.addPoint(width + movement, 0);
		p.addPoint(0, 0);
		p.addPoint(height - 1, height - 1);
		p.addPoint(width + movement, height - 1);
		if(selected){
			if(ColorScheme.active.titlebar instanceof Color){
				g.setPaint(new GradientPaint(new Point(0, 0), ((Color)ColorScheme.active.titlebar).brighter(), new Point(width + moved, height), ((Color)ColorScheme.active.titlebar)));
				//g.setColor(((Color)ColorScheme.active.titlebar).brighter());
			}else{
				g.setColor(Color.GRAY.brighter());
			}
		}else{
			g.setPaint(ColorScheme.active.titlebar);
		}
		g.fillPolygon(p);
		
		if(ColorScheme.active.titlebar instanceof Color){
			g.setColor(((Color)ColorScheme.active.titlebar).darker());
		}else{
			g.setColor(Color.GRAY);
		}
		g.drawLine(0, 0, width - 1 + movement, 0);
		g.drawLine(0, 0, height - 1, height - 1);
		g.drawLine(height - 1, height - 1, width  - 1 + movement, height - 1);
		g.translate(1, 0);
			g.drawLine(0, 0, width - 1 + movement, 0);
			g.drawLine(0, 0, height - 1, height - 1);
			g.drawLine(height - 1, height - 1, width - 1 + movement, height - 1);
		g.translate(-1, 0);
		g.translate(0, 1);
			g.drawLine(0, 0, width - 1 + movement, 0);
			g.drawLine(0, 0, height - 1, height - 1);
			g.drawLine(height - 1, height - 1, width - 1 + movement, height - 1);
		g.translate(0, -1);
		g.translate(0, -1);
			g.drawLine(0, 0, width - 1 + movement, 0);
			g.drawLine(0, 0, height - 1, height - 1);
			g.drawLine(height - 1, height - 1, width - 1 + movement, height - 1);
		g.translate(0, 1);
		
		
		g.setFont(OpenLaucher.font.deriveFont(Font.BOLD, 18F));
		g.setPaint(Color.BLACK);
		RenderHelper.drawCenteredString(text, (int)(width*0.125), (int) (height*0.625), width, (Graphics)g);
	}
	
	public ComponentFancyButton1 setTab(int tab) {
		this.tab = tab;
		return this;
	}
	
	public int getTab() {
		return tab;
	}

}
