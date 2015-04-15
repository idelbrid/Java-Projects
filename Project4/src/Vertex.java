
public class Vertex implements Comparable<Vertex>{
	int position;
	double xpos,ypos;
	boolean known;
	double dist;
	Vertex path=this;
	Edge list;
	long id;
	
	public Vertex(long id , double xpos, double ypos)
	{
		this.id = id;
		this.xpos = xpos;
		this.ypos = ypos;
	}
	
	public int compareTo(Vertex v)
	{
		if(this.dist<v.dist)
			return -1;
		else if(this.dist>v.dist)
			return 1;
		else return 0;
	}

//	@Override
//	public int compareTo(Object o) {
//		// TODO Auto-generated method stub
//		return 0;
//	}


	

	
	
}
