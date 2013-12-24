package es.amadornes.openlauncher.api;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class RenderHelper {
	
	public static void drawCenteredString(String s, int x, int y, int width, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int nx = x + (width - fm.stringWidth(s)) / 2;
		int ny = y + (fm.getAscent() - fm.getDescent());
		g.drawString(s, nx, ny);
	}
	
	public static void drawCenteredString(String s, int x, int y, int width, int height, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int nx = x + (width - fm.stringWidth(s)) / 2;
		int ny = y + ((fm.getAscent() - fm.getDescent() + height)/2);
		g.drawString(s, nx, ny);
	}
	
	public static String[] splitStringInLines(String s, int width, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		char[] str = s.toCharArray();
		int ch = 0;

		List<String> list = new ArrayList<String>();
		
		do{
			String l = "";
			do{
				l = l + str[ch];
				ch++;
			}while((fm.stringWidth(l) + (fm.getLeading()*l.length())) < width && ch < s.length());
			if(str[ch - 1] == ' '){
				l = l.substring(0, l.length() - 1);
				ch--;
			}
			if(ch < s.length()){
				if(str[ch] == ' '){
					ch++;
				}else{
					l += "-";
				}
			}
			list.add(l);
		}while(ch < s.length());
		return list.toArray(new String[list.size()]);
	}
	
}
