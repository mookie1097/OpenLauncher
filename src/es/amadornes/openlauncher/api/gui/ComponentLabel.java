package es.amadornes.openlauncher.api.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ComponentLabel extends Component {
	
	private boolean hovering = false;
	private boolean clicking = false;
	
	public ComponentLabel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void onMouseEnter(int x, int y) {
		hovering = true;
	}
	public void onMouseLeave(int x, int y) {
		hovering = false;
		clicking = false;
	}
	public void onMouseDown(int x, int y, int button) {
		clicking = true;
	}
	public void onMouseUp(int x, int y, int button) {
		clicking = false;
	}

	@Override
	public void render(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		if(clicking){
			g.setColor(Color.GREEN);
		}else{
			if(hovering){
				g.setColor(Color.RED);
			}else{
				g.setColor(Color.BLUE);
			}
		}
		g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 2, 2);
		g.fillRect(4, 4, getWidth() - 8, getHeight() - 8);
	}

}
