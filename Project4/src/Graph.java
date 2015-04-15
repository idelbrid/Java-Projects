import java.awt.Color;
import java.io.File;
import java.lang.reflect.Array;
import java.math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Graph {

	Vertex [] vHash; //built in hashtable: load factor 3/5, linear probing
	int V=0; 		 //number of vertices
	int E=0; 		 //number of edges
	boolean isdigraph;
	double lengthMST = 0;
	int edgesinMST=-1;		//-1 because we will +1 for every node touched, including source node;
	

	public Graph(int Vnum, boolean isdigraph)
	{
		this.isdigraph = isdigraph;
		
		vHash = new Vertex[Vnum*5/3];
	}
	
	public void dijkstra(long id , long id2)
	{
		
		Vertex start = lookup(id);   			//the starting vertex
		Vertex stop = lookup(id2);				//the target vertex
		PriorityQueue<Vertex> heap = new PriorityQueue<Vertex>();
		
		
		//***PREPARING THE QUEUE AND RESETTING THE VERTICES' DIST AND KNOWN****
		for(int i=0; i<vHash.length ; i++)
		{
			if(vHash[i]!=null)
			{
				vHash[i].dist = Double.MAX_VALUE;		//dist is infinity for all
				vHash[i].known = false;					//everything unknown
			}
		}
		
		start.path = start;			//Start has itself as path
		start.dist = 0;				//Start has dist zero
		heap.add(start);			//Add it to the heap
		
		//****************THE MAIN WHILE LOOP***************************
		while(!heap.isEmpty())		//until all vertices are visited
		{
			Vertex selected = heap.poll();	//take the closest vertex
			if(selected.known ==false)		//if it isn't already known
			{
				selected.known =true;		//it BECOMES known
				Edge traverse = selected.list;		
				while(traverse!=null)	//and for all adjacent vertices
				{
					
					Vertex Vadj = traverse.V2;	//this is the selected vertex
					double wght = traverse.weight;	

					if(selected.dist + wght<Vadj.dist)		//if we can lower the distance
					{
						Vadj.dist = selected.dist + wght;   //lower it
						traverse.V2.path = selected;		//change the path
						heap.add(Vadj);						//and read it.
					}
					
					traverse = traverse.next;
				}
				if(selected==stop)	//if we had found the target node, break out of loop
					break;
			}
		}//ENDS THE MAIN WHILE LOOP
		
		if(stop.path==stop)	//If we get through the while loop, 
		{					//and somehow the path of the stop hasn't changed...
			System.err.println("Start and Stopping intersections are not connected or are one and the same.");
			return;			//Make sure not to attempt to find the path if there isn't one.
		}
		
		highlightPath(stop);
	
	}
	
	public void prim()
	{
		long id = 134122623;			//any intersection in the main connected subgraph
										//THIS WOULD CHANGE IF monroe-county.tab WERE DIFFERENT
		Vertex start = lookup(id);
		PriorityQueue<Vertex> heap = new PriorityQueue<Vertex>();
		
		for(int i=0; i<vHash.length ; i++)
		{
			if(vHash[i]!=null)
			{
				vHash[i].dist = Double.MAX_VALUE;
				vHash[i].known = false;
			}
		}
		
		start.path = start;
		start.dist = 0;
		heap.add(start);
		
		while(!heap.isEmpty())			//until all vertices are added to the MST
		{
			Vertex selected = heap.poll(); //find the closest unknown vertex
			if(selected.known ==false)
			{
				lengthMST += selected.dist;	//increase the total weight of the MST by this edge's weight
				edgesinMST ++;
				selected.known =true;		//make it known
				Edge traverse = selected.list;
				while(traverse!=null)		//examine each of its adjacent vertices
				{
					Vertex Vadj = traverse.V2;
					double wght = traverse.weight;
					if(!Vadj.known)			//don't continue if it's a known vertex already
					{
						if(wght<Vadj.dist)		//if this edge makes the vertex closer...
						{
							Vadj.dist = wght;  //it's distance becomes the weight of the edge
							Vadj.path = selected; //and path becomes the first vertex
							heap.add(Vadj);
						}
					}
					traverse = traverse.next;
				}
			}
		}
		
		highlightMST();			//Set highlighting flags to true;
		System.out.println("We have a Minimum Spanning Tree with total length " + lengthMST
							+ " and using " + edgesinMST + " edges");
						

	}
	
	public String[] getRoadPath(long id) //gets a sorted array of roads depicting the route of the path of an intersection
	{
		ArrayList<String> roadlist = new ArrayList<String>();
		Vertex traverse = lookup(id);
		while(traverse.path != traverse)
		{
			Edge road = findEdge(traverse,traverse.path);
			roadlist.add(road.rid);
			traverse = traverse.path;
		}
		String[] roadarray = new String[roadlist.size()];
		int c =0;
		for(String road : roadlist)
		{
			roadarray[c] = road;
			c++;
		}
		Arrays.sort(roadarray);
		return roadarray;
	}
	
	public Edge findEdge(Vertex v1, Vertex v2) //finds edge from v1 to v2
	{
		Edge temp = v1.list;
		while(temp.V2!=v2)
		{
			temp = temp.next;
		}
		return temp;
	}
	
	public void highlightPath(Vertex V)
	{
		while(V.path!= V)
		{
			findEdge(V.path,V).highlighted = true;
			findEdge(V,V.path).highlighted = true;
			V=V.path;
		}
	}
	
	public void highlightMST()
	{
		for(int i =0 ; i<vHash.length ; i++){
			if(vHash[i]!=null)
			{
				if(vHash[i].path!=vHash[i])
				{
					findEdge(vHash[i].path,vHash[i]).inMST = true;
					findEdge(vHash[i],vHash[i].path).inMST = true;
				}
				
			}
		}

	}
	
	public void printPath(Vertex V) //defunct
	{
		if(V.path != V)
		{
			printPath(V.path);
			Edge E = findEdge(V.path,V);
			E.highlighted=true;
			if(!isdigraph)
				findEdge(V,V.path).highlighted=true;
			System.out.print(" to ");
		}
		System.out.println("i"+V.id);
	}
	
	public void insert(Edge E)
	{
		this.E++;
		Vertex V1 = lookup(E.v1id);
		Vertex V2 = lookup(E.v2id);
		E.V1 = V1;
		E.V2 = V2;
		double chgex = V1.xpos - V2.xpos;
		double chgey = V1.ypos - V2.ypos;
		E.weight = Math.sqrt( chgex*chgex + chgey*chgey);
//		if(E.V1.list!=null)		//inserting at head
			E.next = E.V1.list;	//points E's next to the head of the list
		E.V1.list = E;			//makes E the new head of list
		if(!isdigraph)
		{
			Edge Eopp = new Edge(E.rid,E.v2id,E.v1id);  //do the same in the opposite
			Eopp.weight=E.weight;				  //direction if not digraph
			Eopp.V1=V2;
			Eopp.V2=V1;
			Eopp.next = V2.list;
			V2.list = Eopp;
		}
	
	}
	
	private int Hash(long id) //hashing the id of each vertex
	{
		long temp = id%vHash.length;
		return (int)temp;
	}
	
	public void insert(Vertex V)
	{
		this.V++;
		int spot = Hash(V.id);				//hash the vertex's key
		while(vHash[spot]!=null)			//moving to next
			spot= (spot+1)%vHash.length;
		V.position = spot;
		vHash[spot] = V;					//insert
		
	}
	
	public void delete(Vertex V)			//might not have to use it, but it's here in case
	{
		int spot = Hash(V.id);
		while(V!=vHash[spot])
		{
			spot= (spot+1)%vHash.length;
			if(vHash[spot]==null)
			{
				System.err.println("Vertex not found");
				return;
			}
		}
		vHash[spot] = null;
	}
	
	public Vertex lookup(long id){
		int spot = Hash(id);
		while(vHash[spot].id!=id)
		{
			spot = (spot+1)%vHash.length;
			if(vHash[spot]==null)
			{
				
				System.err.println("Vertex not found.");
				return null;
			}
		}
		return vHash[spot];
	}
	
	public boolean exists(Vertex V){
		int spot = Hash(V.id);
		while(vHash[spot].id!=V.id)
		{
			spot = (spot+1)%vHash.length;
			if(vHash[spot]==null)
			{
				return false;
			}
		}
		return true;
	}
}
