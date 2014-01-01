package es.amadornes.openlauncher.api.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

public class Frame {
	
	public Map<Component, Integer> components = new HashMap<Component, Integer>();
	protected int x, y, width, height;
	protected JFrame frame = new JFrame(){
		private static final long serialVersionUID = 1L;
		public void addNotify() {
	        super.addNotify();
	        createBufferStrategy(2);
	    }
		public void update(Graphics g) {};
	};
	protected Canvas screen = new Canvas(){
		private static final long serialVersionUID = 1L;
		public void addNotify() {
	        super.addNotify();
	        createBufferStrategy(2);
	    }
		public void update(Graphics g) {
	        createBufferStrategy(2);
	    };
	};
	private BufferStrategy bs = null;
	public Insets insets = new Insets(0, 0, 0, 0);
	protected int tab = 0;
	
	public Frame(int width, int height) {
		frame.setUndecorated(true);
		setWidth(Math.max(1, width));
		setHeight(Math.max(1, height));
		this.x = 0;
		this.y = 0;

		screen.setIgnoreRepaint(true);
		
		frame.setIgnoreRepaint(true);
		frame.setMinimumSize(new Dimension(100, 100));
		frame.setVisible(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");
		
		addComponentListeners();
		
		screen.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent ev) {
				onMouseUp(ev.getX(), ev.getY(), ev.getButton());
			}
			public void mousePressed(MouseEvent ev) {
				onMouseDown(ev.getX(), ev.getY(), ev.getButton());
			}
			public void mouseExited(MouseEvent ev) {
				onMouseLeave(ev.getX(), ev.getY());
			}
			public void mouseEntered(MouseEvent ev) {
				onMouseEnter(ev.getX(), ev.getY());
			}
			public void mouseClicked(MouseEvent ev) {
				onClick(ev.getX(), ev.getY(), ev.getButton());
			}
		});
		screen.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent ev) {
				onMouseMove(ev.getX(), ev.getY());
			}
			public void mouseDragged(MouseEvent ev) {
				onMouseMove(ev.getX(), ev.getY());
			}
		});
		screen.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent ev) {
				onMouseWheelMove(ev.getPoint().x, ev.getPoint().y, ev.getScrollAmount());
			}
		});
		/*frame.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent ev) {
				onKeyType(ev.getKeyCode());
			}
			public void keyReleased(KeyEvent ev) {
				onKeyUp(ev.getKeyCode());
			}
			public void keyPressed(KeyEvent ev) {
				onKeyDown(ev.getKeyCode());
			}
		});*/
		final Frame me = this;
		frame.addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent ev) {}
			public void componentResized(ComponentEvent ev) {
				me.width = frame.getWidth();
				me.height = frame.getHeight();
			}
			public void componentMoved(ComponentEvent ev) {}
			public void componentHidden(ComponentEvent ev) {}
		});
		
		frame.setLayout(null);
        frame.add(screen);
        screen.setLocation(0, 0);
        screen.setSize(width, height);
		
		startTickingRenderer();
	}
	
	private int lastMouseX = -1;
	private int lastMouseY = -1;
	public Component focus;
	
	private void addComponentListeners(){
		screen.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent ev) {
				Component c = getInFrontAt(ev.getX(), ev.getY());
				if(c != null){
					Point p = transport(c, ev.getX(), ev.getY());
					c.onMouseUp(p.x, p.y, ev.getButton());
				}
			}
			public void mousePressed(MouseEvent ev) {
				Component c = getInFrontAt(ev.getX(), ev.getY());
				if(c != null){
					Point p = transport(c, ev.getX(), ev.getY());
					c.onMouseDown(p.x, p.y, ev.getButton());
				}
			}
			public void mouseExited(MouseEvent ev) {}
			public void mouseEntered(MouseEvent ev) {}
			public void mouseClicked(MouseEvent ev) {
				Component c = getInFrontAt(ev.getX(), ev.getY());
				if(c != null){
					Point p = transport(c, ev.getX(), ev.getY());
					c.onClick(p.x, p.y, ev.getButton());
				}
			}
		});
		
		screen.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent ev) {
				if(lastMouseX >= 0 && lastMouseX < width && lastMouseY >= 0 && lastMouseY < width){
					Component c = getInFrontAt(ev.getX(), ev.getY());
					Component last = getInFrontAt(lastMouseX, lastMouseY);
					if(c != null){
						Point p = transport(c, ev.getX(), ev.getY());
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
				lastMouseX = ev.getX();
				lastMouseY = ev.getY();
			}
			public void mouseDragged(MouseEvent ev) {}
		});
		screen.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent ev) {
				Component c = getInFrontAt(ev.getX(), ev.getY());
				if(c != null){
					Point p = transport(c, ev.getX(), ev.getY());
					c.onMouseWheelMove(p.x, p.y, ev.getScrollAmount());
				}
			}
		});
		/*screen.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				if(focus != null){
					focus.onKeyType(e.getKeyCode());
				}
			}
			public void keyReleased(KeyEvent e) {
				if(focus != null){
					focus.onKeyUp(e.getKeyCode());
				}
			}
			public void keyPressed(KeyEvent e) {
				if(focus != null){
					focus.onKeyDown(e.getKeyCode());
				}
			}
		});*/
	}
	
	public void setX(int x){
		this.x = x;
		frame.setLocation(x, y);
	}
	
	public void setY(int y){
		this.y = y;
		frame.setLocation(x, y);
	}
	
	public void setWidth(int width){
		this.width = width;
		frame.setSize(width, height);
        screen.setSize(width, height);
	}
	
	public void setHeight(int height){
		this.height = height;
		frame.setSize(width, height);
        screen.setSize(width, height);
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
	
	private boolean isWithinBoundingBox(Component c, int x, int y){
		return c != null && (x >= (c.getX() + insets.left) && x < (c.getX() + insets.left + c.getWidth()) && y >= (c.getY() + insets.top) && y < (c.getY() + insets.top + c.getHeight()));
		//return c != null && ((c.getX() + insets.left) <= x && ((c.getX() + c.getWidth()) + insets.left) > x && (c.getY() + insets.top) <= y && ((c.getY() + c.getHeight()) + insets.top) > y);
	}
	
	private Point transport(Component c, int x, int y){
		Point p = null;
		if(c != null){
			p = new Point(x - (c.getX() + insets.left), y - (c.getY() + insets.top));
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
	
	public void addComponent(final Component c, int zIndex){
		if(!components.keySet().contains(c)){
			c.owner = this;
			components.put(c, new Integer(zIndex));
		}
	}
	
	public void addComponent(Component c){
		addComponent(c, components.size());
	}
	
	public void onClick(int x, int y, int button){}
	
	public void onMouseDown(int x, int y, int button){}
	public void onMouseUp(int x, int y, int button){}
	
	public void onMouseMove(int x, int y){}
	public void onMouseEnter(int x, int y){}
	public void onMouseLeave(int x, int y){}
	
	public void onMouseWheelMove(int x, int y, int amount){}
	
	public void onKeyDown(int key){}
	public void onKeyUp(int key){}
	public void onKeyType(int key){}
	
	public void center(){
		frame.setLocationRelativeTo(null);
	}
	
	public void show(){
		frame.setVisible(true);
		screen.createBufferStrategy(2);
		bs = screen.getBufferStrategy();
	}
	
	public void hide(){
		frame.setVisible(false);
		bs = null;
	}
	
	public JFrame getFrame(){
		return frame;
	}
	
	public Canvas getCanvas(){
		return screen;
	}
	
	private void startTickingRenderer(){
		new Thread(new Runnable() {
			public void run() {
				boolean firstTime = true;
				while(true){
					if(bs != null){
						try{
							do{
								do{
									Image img = frame.createVolatileImage(width, height);
									render(img.getGraphics());
									bs.getDrawGraphics().drawImage(img, 0, 0, null);
									img.flush();
									bs.getDrawGraphics().dispose();
								}while(bs.contentsRestored());
								bs.show();
							}while(bs.contentsLost());
							bs.getDrawGraphics().dispose();
						    Thread.yield();
						    firstTime = false;
						}catch(Exception e){}
					}
					if(firstTime){
						frame.repaint();
					}
				}
			}
		}).start();
	}
	
	protected synchronized void render(Graphics g){
		renderBackground(g);
		renderComponents(g.create(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom));
	}
	
	protected synchronized void renderBackground(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
	}
	
	protected synchronized void renderComponents(Graphics g){
		List<Component> components = sortComponentsByZIndex();
		for(Component c : components){
			c.render(g.create(c.x, c.y, c.width, c.height));
		}
	}
	
	private List<Component> sortComponentsByZIndex(){
		List<Component> list = new ArrayList<Component>();
		
		for(Component c : components.keySet()){
			list.add(c);
		}
		
		Collections.sort(list, new ZIndexSorter(components));
		
		return list;
	}
	
}
