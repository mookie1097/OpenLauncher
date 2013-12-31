package es.amadornes.openlauncher.api;

import java.util.Comparator;
import java.util.Map;

public class ZIndexSorter implements Comparator<Component> {
	
	private Map<Component, Integer> components;
	
	public ZIndexSorter(Map<Component, Integer> components) {
		this.components = components;
	}
	
	@Override
	public int compare(Component c1, Component c2) {
		return components.get(c1).intValue() - components.get(c2).intValue();
	}

}
