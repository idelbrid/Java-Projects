import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;


public class Project4 {
	
	public static void main(String[] args) throws FileNotFoundException
	{
		File infile = new File(args[0]);	
		boolean picture = false;
		if(args.length==2)
			picture = true;
		
		
		File streets = new File("monroe-county.tab");
		Scanner countscan = new Scanner(streets);
		Scanner srtscan = new Scanner(streets);
		
		
		int intersectioncount = 0;
		String ln = "";
		while(countscan.hasNextLine()){		//This counts the number of vertices before 
			ln = countscan.nextLine();		//I create the graph.
			Scanner lnscan = new Scanner(ln);
			if(lnscan.next().equals("i"))
				intersectioncount++;
		}
//		System.out.println(intersectioncount);
		
		
		Graph graph = new Graph(intersectioncount,false);		//Graph(int numberofvertices, boolean isdigraph)

		while(srtscan.hasNextLine())
		{
			
			String line = srtscan.nextLine(); 
			Scanner lnscan = new Scanner(line);
			if(lnscan.next().equals("i"))						//for entries of intersections
			{
				String code = lnscan.next().substring(1);			//id of the vertex, but I remove the "i"
				long id = Long.parseLong(code);
				double xpos = Double.parseDouble(lnscan.next());	//x position
				double ypos = Double.parseDouble(lnscan.next());	//y position			
				Vertex V = new Vertex(id,xpos,ypos);
				graph.insert(V);
			}
			else 
			{
				String rid = lnscan.next();
				String code1 = lnscan.next().substring(1);		//I assume it's an edge if not an intersection
				String code2 = lnscan.next().substring(1);		//again, I remove the "i"
				long id1 = Long.parseLong(code1);
				long id2 = Long.parseLong(code2);
				Edge E = new Edge(rid, id1, id2);
				graph.insert(E);
			}
		}
		
		Scanner scanin = new Scanner(infile);
		long start=0, stop =0;
		if(scanin.next().equals("i"))				//scanning in the "infile"
		{
			String startstr = scanin.next().substring(1);		//I don't use a loop just
			start = Long.parseLong(startstr);					//because we read in 2 lines, 4 "words" each
			scanin.next();										//
			scanin.next();
		}
		if(scanin.next().equals("i"))
		{
			String stopstr = scanin.next().substring(1);
			stop = Long.parseLong(stopstr);
			scanin.next();
			scanin.next();
		}
		
		//***************CALLING TEH ALGORITHMS****************************
		graph.prim();				//prim's algorithm for the MST
		graph.dijkstra(start, stop);//dijkstra's algorithm for the shortest path between start and stop
		
		String [] roads = graph.getRoadPath(stop);				//*
		System.out.println("Our shortest path will take: ");	//*
		for(String road : roads)								//These lines are all devoted to 
		{														//retrieving the shortest path's roads
			System.out.println(road);							//*	
		}														//*
		
		
		//****************PAINTING/GRAPHICS BOLOGNA************************
		JFrame frame = new JFrame("Streets of Monroe County");
		frame.setSize(650,650);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new MapPanel(graph , picture));
		frame.setVisible(true);
		
		
		
	//load the graph data
	//build graph
	//draw map
	//compute map
	
	}
}
