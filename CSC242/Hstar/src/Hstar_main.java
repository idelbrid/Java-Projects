import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class Hstar_main {

	
//	public static void main(String [] args) throws FileNotFoundException{
//		File goalFile = new File(args[0]);
//		File initFile = new File(args[1]);
//		State.loadGoal(goalFile);
//		State s = State.init(initFile);
//		s.printPretty();
//	}
	public static void main(String [] args) throws FileNotFoundException{
		File goalFile = new File(args[0]);
		File initFile = new File(args[1]);
		int numberExplored = 0;
		State.loadGoal(goalFile);
		State s = State.init(initFile);
		PriorityQueue<State> open = new PriorityQueue<State>();	
		LinkedList<State> closed = new LinkedList<State>();
		Hashtable<int[][],State> visited = new Hashtable<int[][],State>();
		open.add(s); 
		visited.put(s.getBoard(),s);
		State goal= null;
		while(true){
			if(open.isEmpty())
				break;
			State n = open.remove();
			numberExplored++;
			closed.add(n);
			System.out.println("Opening:");
			n.printPretty();
			System.out.println("f: "+n.getF()+ " h: "+n.getH());
			if(n.isGoal())
			{goal = n; break;}
			ArrayList<State> successors = n.successor();
			for(State t : successors){
				if(visited.containsKey(t.getBoard())){
					State old_t = visited.get(t.getBoard());
					if(old_t.getG()>t.getG()){
						old_t.parent = t.parent;
						old_t.setG(t.getG());
						old_t.setF(t.getF());
					}
				}
				else{
					t.setValues();
					open.add(t); visited.put(t.getBoard(), t);
				}
			}
		}
		if(goal!=null){
			System.out.println("Success:) ");
			goal.printPath();
			System.out.println("Steps: "+goal.getG());
			System.out.println("Explored: "+numberExplored);
		}
		else System.out.println("Error :(");
	}	
}
