package es.amadornes.openlauncher.gui;

import es.amadornes.openlauncher.api.gui.ComponentContainer;
import es.amadornes.openlauncher.api.gui.Frame;

public abstract class Tab extends ComponentContainer {

	public Tab(int x, int y, int width, int height, Frame owner) {
		super(x, y, width, height, owner);
	}
	
}
