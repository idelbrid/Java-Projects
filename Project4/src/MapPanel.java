import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class MapPanel extends JPanel {

	Graph graph;
	int xsize=0;
	int ysize=0;
	BufferedImage img = null;
	boolean picture = false;
	
	public MapPanel(Graph G,boolean picture)
	{
		this.graph = G;
		
		BufferedImage img = null;
		setBackground(Color.BLACK);
		this.picture = picture;
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		xsize=getWidth();
		ysize=getHeight();
		
		//*****************THE BACKGROUND**********************
		if(picture){
			try {
			    img = ImageIO.read(new File("county-big-map-clean.png")); //a "blank" map of monroe county with the lake contour
			    } catch (IOException e) {
			}
		}
//		System.out.println("Painting");
		if(picture) 
			g.drawImage(img, 0, 0, xsize, ysize, null);
		
		//*****************LEGEND******************************
		g.setColor(Color.BLUE);
		g.drawString("Regular Streets", 0, ysize-40);
		
		g.setColor(Color.green);
		g.drawString("Minimum Spanning Tree", 0, ysize-25);	
		
		g.setColor(Color.RED);								
		g.drawString("Shortest path", 0, ysize-10);			
		
		//*****************ACTUAL GRAPH / MAP******************
		for(int i = 0 ; i<graph.vHash.length ; i++)  //goes through each vertex
		{
			if(graph.vHash[i]!=null)  //making sure we have a defined vertex, skipping nulls
			{
				if(graph.vHash[i].list!=null){   //if it has a first edge,
					Edge temp = graph.vHash[i].list;
					
					while(temp!=null)
					{
						g.setColor(Color.BLUE);		//blue is regular color.
						if(temp.inMST)
							g.setColor(Color.green); //Minimim spanning tree is in green
						if(temp.highlighted)
							g.setColor(Color.RED);  // the shortest path is in red,
						g.drawLine((int)temp.V1.xpos*xsize/635,	//drawing a line
								   (int)temp.V1.ypos*ysize/600,//resizable because of xsize/635, etc.
								   (int)temp.V2.xpos*xsize/635,
								   (int)temp.V2.ypos*ysize/600);
						
						temp = temp.next;

					}
				}
			}	
		}
	}
}
