import java.util.ArrayList;
import java.util.Random;

public class State{
	static int size=8;
	static Random rng = new Random();
	int [] board;
	int f;
	public State(int size){
		board = new int [size];
		f = Integer.MAX_VALUE;
	}
	public State(State original , int col,int row, int confCausedByReplacedQueen){
		board = original.board.clone();
		board[col] = row;
//		f = original.getF() - confCausedByReplacedQueen;
//		for(int i = col+1; i<size; i++){
//			if(board[col]==board[i]) //horizontal
//				f++;
//			if(Math.abs(board[col]-board[i])==Math.abs(col-i)) //diagonal
//				f++;
//		}
		f = Integer.MAX_VALUE;
	}
	public int getF(){
		if(f!=Integer.MAX_VALUE)
			return f;
		else{
			int conflicts = 0;
			for(int i=0; i<size-1; i++){
				for(int j=i+1;j<size; j++){
					if(board[i]==board[j]) //horizontal
						conflicts++;
					if(Math.abs(board[i]-board[j])==Math.abs(i-j)) //diagonal
						conflicts++;
				}
			}
			f = conflicts;
			return f;
		}
	}
	public ArrayList<State> neighbors(){
		int [] conflicts = new int[size];
		for(int i=0; i<size-1; i++){
			for(int j=i+1;j<size; j++){
				if(board[i]==board[j]) //horizontal
				{	conflicts[i]++; conflicts[j]++;}
				if(Math.abs(board[i]-board[j])==Math.abs(i-j)) //diagonal
				{	conflicts[i]++; conflicts[j]++;}
			}
		}
		int index=-1;
		int f = Integer.MIN_VALUE;
		for(int i =0; i<size; i++){
			if(f<=conflicts[i]){
				index = i;
				f = conflicts[i];
			}
		}
		
		int current = board[index];
		ArrayList<State> nbhood = new ArrayList<State>();
		for(int row=0; row<size; row++){
			if(row!=current){
				nbhood.add(new State(this,index,row,f));
			}
		}
		return nbhood;
		
	}
	public static State initial(){
		State s = new State(size);
		for(int i=0; i<size; i++){
			s.board[i] = rng.nextInt(size);
		}
		return s;
	}
	public void printPretty(){
		for(int row=size-1; row>=0; row--){
			if(row>9)
				System.out.print("("+row+") ");
			else System.out.print("("+row+")  ");
			for(int col=0; col<size; col++){
				if(board[col]==row)
					System.out.print("[Q ]");
				else{
					if((row+col)%2==1)
						System.out.print("[  ]");
					if((row+col)%2==0)
						System.out.print("[##]");
					}
			}
			System.out.println();
		}
		System.out.print("     ");
		for(int i=0; i<size; i++){
			if(i>9)
				System.out.print("("+i+")");
			else System.out.print("("+i+") ");

		}
		System.out.println();
		
	}
	public String toString(){
		String toReturn = "("+this.getF()+") ";
		toReturn += "[";
		for(int i =0; i<size;i++){
			toReturn+=this.board[i];
			if(i!=size-1)
				toReturn += ",";
		}
		toReturn += "]";
		return toReturn;
	}
}