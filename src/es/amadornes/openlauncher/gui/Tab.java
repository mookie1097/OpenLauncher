package es.amadornes.openlauncher.gui;

import es.amadornes.openlauncher.api.ComponentContainer;
import es.amadornes.openlauncher.api.Frame;

public abstract class Tab extends ComponentContainer {

	public Tab(int x, int y, int width, int height, Frame owner) {
		super(x, y, width, height, owner);
	}
	
}
