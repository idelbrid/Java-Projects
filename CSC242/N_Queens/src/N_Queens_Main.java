import java.util.ArrayList;
public class N_Queens_Main {

	static int N;
	public static void main(String[] args){
		N = Integer.parseInt(args[0]);
		State.size = N;
		int n = Integer.parseInt(args[1]);
		State s = null;
		double time0 = System.currentTimeMillis();
		for(int i=0; i<n; i++){  //tries to find global min
			System.out.println("try " + i);
			s = State.initial();
			boolean goal = false;
			while(!goal){
				ArrayList<State> nbhood = s.neighbors();
				State t = BestNode(nbhood);
				if(s.getF()<=t.getF())
					goal = true;
				else{ s=t; System.out.println("Picked " + s);}
			}
			if(s.getF()==0)
				break;
		}
		double time1 = System.currentTimeMillis();
		System.out.println("Result: "+s.toString() +" in "+(time1-time0)+"ms");
		s.printPretty();
	}
//	public static void main(String[] args){
//		N = 10;
//		State teststate = new State(10);
//		int[] ary = {1,5,2,4,2,3,7,4,2,2};
//		teststate.board = ary;
//		System.out.println(teststate.toString());
//		teststate.printPretty();
//		for(State t: teststate.neighbors()){
//			System.out.println(t.toString());
//			t.printPretty();
//		}
//	}
	

	public static State BestNode(ArrayList<State> nbhood){
		int index=-1;
		int value = Integer.MAX_VALUE;
		for(int i=0; i<nbhood.size(); i++){
			int f = nbhood.get(i).getF();
			if(f< value){ value = f; index = i;}
		}
		return nbhood.get(index);
		
	}


}
