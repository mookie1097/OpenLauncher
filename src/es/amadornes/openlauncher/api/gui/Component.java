package es.amadornes.openlauncher.api.gui;

import java.awt.Graphics;

public abstract class Component {

	protected int x, y, width, height;
	private boolean overlap = true;
	protected boolean canHaveFocus = false;
	public Frame owner = null;

	public Component(int x, int y, int width, int height) {
		this.width = Math.max(1, width);
		this.height = Math.max(1, height);
		this.x = Math.max(x, 0);
		this.y = Math.max(y, 0);
	}

	public void shouldOverlap(boolean should){
		overlap = should;
	}

	public boolean shouldOverlap(){
		return overlap;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void onClick(int x, int y, int button){}

	public void onMouseDown(int x, int y, int button){}
	public void onMouseUp(int x, int y, int button){
		if(owner != null){
			if(canHaveFocus){
				owner.focus = this;
			}else{
				owner.focus = null;
			}
		}
	}

	public void onMouseMove(int x, int y){}
	public void onMouseEnter(int x, int y){}
	public void onMouseLeave(int x, int y){}

	public void onMouseWheelMove(int x, int y, int amount){}

	public void onKeyDown(int key){}
	public void onKeyUp(int key){}
	public void onKeyType(int key){}

	public abstract void render(Graphics g);

}
