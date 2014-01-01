package es.amadornes.openlauncher.api.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ComponentLabel extends Component {
	
	private String label = "";
	
	public ComponentLabel(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		label = text;
	}
	
	@Override
	public boolean shouldOverlap() {
		return false;
	}

	@Override
	public void render(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		RenderHelper.drawCenteredString(label, x, y, width, height, g);
	}

}
