
public class Edge implements Comparable<Edge> {
	double weight;
	long v1id,v2id; //intersection IDs
	String rid;		//road ID
	Vertex V1,V2;
	Edge next;
	boolean highlighted = false;
	boolean inMST = false;
	
	
	public Edge(String rid , long v1id, long v2id)
	{
		this.rid = rid;
		this.v1id = v1id;
		this.v2id = v2id;
	}

	public int compareTo(Edge E)
	{
		if(this.weight<E.weight)
			return -1;
		else if(this.weight>E.weight)
			return 1;
		else return 0;
	}

}
