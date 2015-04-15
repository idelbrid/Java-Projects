import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;
public class DisplayPanel extends JPanel{

	FeatureVec [] data;
	int xsize=0;
	int ysize=0;
	Color[] color = {Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.MAGENTA};
	public DisplayPanel(FeatureVec[] data){
		this.data = data;

	}

	
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;
		setBackground(Color.WHITE);
		xsize = getWidth();
		ysize = getHeight();
		super.paintComponent(g2);
		for(int i =0; i<data.length; i++){
			g2.setColor(color[data[i].label]);
			g2.fill(new Ellipse2D.Double(xsize*.9*data[i].features[0],ysize*.9*data[i].features[1],10,10));
		}
	}
	
}
