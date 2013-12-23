package es.amadornes.openlauncher.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.amadornes.openlauncher.api.Component;
import es.amadornes.openlauncher.api.ZIndexSorter;

public abstract class Tab {
	
	public GUI owner;
	
	public Tab(GUI owner) {
		this.owner = owner;
	}
	
	protected synchronized void render(Graphics g){
		renderBackground(g);
		renderComponents(g);
	}
	
	protected abstract void renderBackground(Graphics g);
	
	protected synchronized void renderComponents(Graphics g){
		List<Component> components = sortComponentsByZIndex();
		for(Component c : components){
			c.render(g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
		}
	}
	
	public Map<Component, Integer> components = new HashMap<Component, Integer>();
	
	public void addComponent(final Component c, int zIndex){
		if(!components.keySet().contains(c)){
			c.owner = owner;
			components.put(c, new Integer(zIndex));
		}
	}
	
	public void addComponent(Component c){
		addComponent(c, components.size());
	}
	
	public int getWidth(){
		return owner.getWidth() - owner.insets.left - 170 - 6 - owner.insets.right;
	}
	
	public int getHeight(){
		return owner.getHeight() - owner.insets.top - owner.insets.bottom;
	}
	
	public void onClick(int x, int y, int button){
		Component c = getInFrontAt(x, y);
		if(c != null){
			Point p = transport(c, x, y);
			c.onClick(p.x, p.y, button);
		}
	}
	
	public void onMouseDown(int x, int y, int button){
		Component c = getInFrontAt(x, y);
		if(c != null){
			Point p = transport(c, x, y);
			c.onMouseDown(p.x, p.y, button);
		}
	}
	public void onMouseUp(int x, int y, int button){
		Component c = getInFrontAt(x, y);
		if(c != null){
			Point p = transport(c, x, y);
			c.onMouseUp(p.x, p.y, button);
		}
	}
	
	private int lastMouseX = 0, lastMouseY = 0;
	public void onMouseMove(int x, int y){
		if(lastMouseX >= 0 && lastMouseX < getWidth() && lastMouseY >= 0 && lastMouseY < getWidth()){
			Component c = getInFrontAt(x, y);
			Component last = getInFrontAt(lastMouseX, lastMouseY);
			if(c != null){
				Point p = transport(c, x, y);
				if(!isWithinBoundingBox(c, lastMouseX, lastMouseY) || c != last){
					c.onMouseEnter(p.x, p.y);
				}else{
					c.onMouseMove(p.x, p.y);
				}
			}
			if(last != null){
				Point p = transport(last, lastMouseX, lastMouseY);
				if(last != c){
					last.onMouseLeave(p.x, p.y);
				}
			}
		}
		lastMouseX = x;
		lastMouseY = y;
	}
	public void onMouseEnter(int x, int y){}
	public void onMouseLeave(int x, int y){}
	
	public void onMouseWheelMove(int x, int y, int amount){
		Component c = getInFrontAt(x, y);
		if(c != null){
			Point p = transport(c, x, y);
			c.onMouseWheelMove(p.x, p.y, amount);
		}
	}
	
	public void onKeyDown(int key){}
	public void onKeyUp(int key){}
	public void onKeyType(int key){}
	
	private List<Component> sortComponentsByZIndex(){
		List<Component> list = new ArrayList<Component>();
		
		for(Component c : components.keySet()){
			list.add(c);
		}
		
		Collections.sort(list, new ZIndexSorter(components));
		
		return list;
	}
	
	private boolean isWithinBoundingBox(Component c, int x, int y){
		return c != null && (x >= (c.getX()) && x < (c.getX() + c.getWidth()) && y >= (c.getY()) && y < (c.getY() + c.getHeight()));
		//return c != null && ((c.getX() + insets.left) <= x && ((c.getX() + c.getWidth()) + insets.left) > x && (c.getY() + insets.top) <= y && ((c.getY() + c.getHeight()) + insets.top) > y);
	}
	
	private Point transport(Component c, int x, int y){
		Point p = null;
		if(c != null){
			p = new Point(x - c.getX(), y - c.getY());
		}
		return p;
	}
	
	private Component getInFrontAt(int x, int y){
		Component last = null;
		for(Component c : sortComponentsByZIndex()){
			if(isWithinBoundingBox(c, x, y)){
				if(c.shouldOverlap()){
					last = c;
				}
			}
		}
		return last;
	}
	
}
