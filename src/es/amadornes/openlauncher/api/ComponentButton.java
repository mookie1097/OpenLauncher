package es.amadornes.openlauncher.api;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public class ComponentButton extends Component {
	
	protected boolean hovering = false;
	protected boolean clicking = false;
	
	public ComponentButton(int x, int y, int width, int height) {
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
		Paint c;
		if(clicking){
			c = ColorScheme.active.buttonClick;
		}else{
			if(hovering){
				c = ColorScheme.active.buttonHover;
			}else{
				c = ColorScheme.active.buttonOff;
			}
		}
		g.setPaint(c);
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

}
