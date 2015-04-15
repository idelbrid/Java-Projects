import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class State implements Comparable<State>{
	State parent;
	int[][] board;
	int f=-1; //total score
	int g=-1; //weight of path to state
	int h=-1; //heuristic "weight to goal node"
	Point space;
	static Hashtable<Integer, Point> goalMap;
	static int size;
	
	public State(State original, Point movepiece){
		this.board = new int[size][];
		for(int i =0; i<size; i++)
			this.board[i] = original.board[i].clone();
		int swap = board[movepiece.x][movepiece.y];
		board[original.space.x][original.space.y] = swap; //place where space used to be
																   //now has "movepiece"
		board[movepiece.x][movepiece.y] = 0 ; //place where "movepiece" used to be
											  //now has a space
		this.space = new Point(movepiece.x, movepiece.y);
		this.parent = original;
	}
	public State(){
		this.board = new int[size][size];
	}
	public State(int[][] board,Point spacePoint){
		this.board = board;
		this.space = spacePoint;
		this.g = 0;
	}
	public static State init(File initFile) throws FileNotFoundException {
		Scanner fileScan = new Scanner(initFile);

		int[][] toPassBoard = new int [size][size];
		Point space = new Point();
		while(fileScan.hasNextLine()){
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			String pt = lineScan.next();
			String val = lineScan.next();
			String [] sepPt = pt.split(",");
			Point numPt = new Point(Integer.parseInt(sepPt[0]), Integer.parseInt(sepPt[1]));
			int numval = Integer.parseInt(val);
			toPassBoard[numPt.x][numPt.y] = numval;
			if(numval==0){
				space = numPt;
			}
			lineScan.close();
		}
		fileScan.close();
		
		return new State(toPassBoard, space);
	}

	public int compareTo(State t) {
		if(this.getF() > t.getF())
			return 1;
		if(this.getF() < t.getF())
			return -1;
		return 0;
	}

	
	private int manhattan(Point point, Point point2) { 
		int x = Math.abs(point.x - point2.x);
		int y = Math.abs(point.y - point2.y);
		return x+y;
	}

	public boolean isGoal() {
		if(getH()==0)
			return true;
		return false;
	}

	public ArrayList<State> successor() {
		ArrayList<State> toReturn = new ArrayList<State>();
		//up
		if(space.y>0){
			int above = board[space.x][space.y-1];
			if(above>0){
				toReturn.add(new State(this,new Point(space.x,space.y-1)));
			}
		}
		//down
		if(space.y<4){
			int below = board[space.x][space.y+1];
			if(below>0){
				toReturn.add(new State(this,new Point(space.x,space.y+1)));
			}
		}
		//left
		if(space.x>0){
			int left = board[space.x-1][space.y];
			if(left>0){
				toReturn.add(new State(this,new Point(space.x-1,space.y)));
			}
		}
		//right
		if(space.x<4){
			int right = board[space.x+1][space.y];
			if(right>0){
				toReturn.add(new State(this, new Point(space.x+1,space.y)));
			}
		}
		return toReturn;
	}

	public int[][] getBoard() {
		
		return this.board;
	}

	public void setG(int g) {
		this.g = g;		
	}

	public void setF(int f) {
		this.f = f;
	}

	public void setValues() {
		getF();
	}
	public static void loadGoal(File goalFile) throws FileNotFoundException {
		Scanner fileScan = new Scanner(goalFile);
		int size=0;
		goalMap = new Hashtable<Integer,Point>();
		while(fileScan.hasNextLine()){
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			String pt = lineScan.next();
			String val = lineScan.next();
			String [] sepPt = pt.split(",");
			Point numPt = new Point(Integer.parseInt(sepPt[0]), Integer.parseInt(sepPt[1]));
			if(numPt.x>size)
				size= numPt.x;
			int numval = Integer.parseInt(val);
			goalMap.put(numval, numPt);
			lineScan.close();
		}
		State.size = size+1;
		fileScan.close();
	}
	public int getG() {
		if(this.g>= 0)
			return g;
		return parent.getG()+1; //IMPORTANT: CHANGE FOR GENERA APPROACH
	}							//HERE ALL WEIGHTS ARE 1

	public int getH() {
		if(this.h>=0)
			return h;
		else{
			int toReturn=0;
			for(int row=0; row<board.length; row++){
			for(int col=0; col<board[0].length; col++){
				if(board[row][col]>0)
					toReturn += manhattan(new Point(row,col), goalMap.get(board[row][col]));
			}}
			return toReturn;
		}
	}
	public int getF() {
		if(this.f>=0)
			return f;
		return getG() + getH();
	}
	
	public void printPretty(){
		for(int row=0; row<size; row++){
		for(int col=0; col<size; col++){
			System.out.print("[");
			int val = this.board[row][col];
			if(val>0)
				System.out.print(val);
			else{
				if(val==0)
					System.out.print("_");
				if(val<0){
					System.out.print("#");
				}
			}
			if(this.board[row][col]<=9)
				System.out.print(" ");
			System.out.print("]");
		}
		System.out.println();
		}
	}
	
	
	public String toString(){
		
		String toReturn="";
		for(int row=0; row<size; row++){
		for(int col=0; col<size; col++){
			toReturn += ("[");
			int val = this.board[row][col];
			if(val>0)
				toReturn+=(val);
			else{
				if(val==0)
					toReturn+=("_");
				if(val<0){
					toReturn+=("#");
				}
			}
			if(this.board[row][col]<=9)
				toReturn+=(" ");
			toReturn+=("]");
		}
		toReturn += "\n";
		}
		return toReturn;
	}
	public void printPath() {
		if(this.parent!=null){
			this.parent.printPath();
			System.out.println();
		}
		System.out.println("h* = "+this.getH());
		this.printPretty();
		
	}

}
