import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;


public class Apriori_Main {
	static boolean debug_message;
	
	public static void main(String[] args) throws IOException{
		long startTime = System.nanoTime();
		ArrayList<Frequent_Itemset> L = apriori(args);
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		
		System.out.println("\nDone");
		File outfile = null;
		if(!args[2].isEmpty())
			outfile = new File(args[2]);
		FileWriter FW = new FileWriter(outfile);
		FW.write("");
		for(Frequent_Itemset I : L){
			if(I.itemset.size() > 1){
				System.out.println("["+I +"]"+ ": "+I.support_count);
				FW.append("["+I.toString()+"]" + ": " + I.support_count+"\n"); 
			}	
		}
		System.out.println("Algorithm time: "+ duration);
		FW.close();
	}
	
	

	public static ArrayList<Frequent_Itemset> apriori(String[] args) throws FileNotFoundException{
		File DB_file = new File(args[0]);
		debug_message = false;
		final int threshold = Integer.parseInt(args[1]);
		FileReader FR = new FileReader(DB_file);
		System.out.println("Reader initialized");
		ArrayList<ArrayList<Frequent_Itemset>> L = new ArrayList<ArrayList<Frequent_Itemset>>();
		//ArrayList<ArrayList<Candidate>> C = new ArrayList<ArrayList<Candidate>>();
		ArrayList<Frequent_Itemset> L1 = find_frequent_1_itemsets(DB_file, threshold);
		L.add(L1);
		for(int k=1; L.get(k-1).size()!=0 ; k++){
			int temp = k+1;
			System.out.println("Calculating Frequent"+temp+"-itemsets.\n");
			ArrayList<Candidate> Ck = apriori_gen(L.get(k-1));
			FR = new FileReader(DB_file);
			Scanner file_scanner = new Scanner(FR);
			while(file_scanner.hasNextLine()){
				String basket_str = file_scanner.nextLine();
				ArrayList<Candidate> Ct = subset(Ck,basket_str);
				for(Candidate c : Ct )
					c.count++;
				
			}
			file_scanner.close();
			ArrayList<Frequent_Itemset> Lk = new ArrayList<Frequent_Itemset>();
			for(Candidate c : Ck)
				if(c.count>=threshold)
					Lk.add(new Frequent_Itemset(c));
			L.add(Lk);			
		}
		ArrayList<Frequent_Itemset> all = new ArrayList<Frequent_Itemset>();
		for(ArrayList<Frequent_Itemset> l : L)
			all.addAll(l);
		return all;
		
	}


	private static ArrayList<Frequent_Itemset> find_frequent_1_itemsets(
			File DB_file, int threshold) throws FileNotFoundException {
		System.out.println("Finding frequent 1-itemsets...");
		Hashtable<String,Integer> hash = new Hashtable<String,Integer>();
		ArrayList<Frequent_Itemset> L1 = new ArrayList<Frequent_Itemset>();
		FileReader FR = new FileReader(DB_file);
		Scanner file_scanner = new Scanner(FR);
		int i =0;
		while(file_scanner.hasNext())
		{	
			String item = file_scanner.next();
			if(item.endsWith(","))
				item = item.substring(0,item.indexOf(","));
			if(debug_message) {System.out.print("Scanning in "+item+": "); }
			if(!hash.containsKey(item))
			{	hash.put(item, 1); if(debug_message){System.out.println(1);} }
			else {hash.put(item, hash.get(item)+1); if(debug_message){System.out.println(hash.get(item)); }}
		}
		System.out.println("Finished first scan.");
		Iterator<Entry<String, Integer>> it = hash.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,Integer> e = it.next();
			if(e.getValue()>=threshold){
				ArrayList<String> itemset = new ArrayList<String>();
				itemset.add(e.getKey());
				
				L1.add(new Frequent_Itemset( itemset,e.getValue()));
			}
		}
		if(debug_message){
			System.out.println("Frequent 1-itemsets are: ");
			for(Frequent_Itemset I : L1){
				System.out.println(I);
			}
		}
		file_scanner.close();
		return L1;
	}

	
	private static ArrayList<Candidate> subset(ArrayList<Candidate> ck,
			String basket_str) {
		if(debug_message)
			System.out.println("\nCalculating subset");
		Hashtable<String, Integer> basket = new Hashtable<String, Integer>();
		ArrayList<Candidate> outlist = new ArrayList<Candidate>();
		String[] basket_array = basket_str.split(" ");
		for(String item: basket_array){
			if(item.endsWith(","))
				item = item.substring(0,item.indexOf(","));
			if(!basket.containsKey(item))
				basket.put(item, 1);
		}
		for(Candidate c : ck){
			boolean any_item_is_missing = false;
			for(String s : c.itemset)
				if(!basket.containsKey(s))
					any_item_is_missing = true;
			if(!any_item_is_missing)
				outlist.add(c);
		}
		if(debug_message){	
			System.out.print("The candidates found in this basket are: ");
			for(Candidate c : outlist){
				System.out.println(c);
			}
		}
		return outlist;
	}
	
	public static ArrayList<Candidate> apriori_gen(ArrayList<Frequent_Itemset> Lkm1){
		System.out.println("Entering apriori_gen: starting candidate generation");
		ArrayList<Candidate> Ck = new ArrayList<Candidate>();
		for(Frequent_Itemset l1 : Lkm1){
			for(Frequent_Itemset l2 : Lkm1){
				ArrayList<String> c = Check_and_Join(l1,l2);
				if(c==null)
					continue;
				if( has_infrequent_subset(c,Lkm1) )
					continue;
				else{
					boolean already_exists_in_set = false;
					for(Candidate cl : Ck)
						if(cl.itemset.containsAll(c))
						{	already_exists_in_set = true; break; } 
					if(!already_exists_in_set)
						Ck.add(new Candidate(c));
				}
			}
		}
		return Ck;
	}
	
	private static ArrayList<String> Check_and_Join(Frequent_Itemset l1,
			Frequent_Itemset l2) {
		if (debug_message)
			System.out.println("Checking "+l1+" and "+l2);
		ArrayList<String> in_common = new ArrayList<String>();
		String different_from_l1 = "";
		for(String item : l1.itemset){
			if(l2.itemset.contains(item))
				in_common.add(item);
			else different_from_l1= item;
		}
		if(in_common.size()!=l1.itemset.size()-1)
		{ if(debug_message){System.out.println("No viable way to join");}	return null;}
		in_common.add(different_from_l1);
		for(String item : l2.itemset){
			if(!in_common.contains(item))
				in_common.add(item);
		}
		String printstring = "";
		for(String s : in_common)
		{
			printstring += s+", ";
		}
		printstring = printstring.substring(0,printstring.length() - 2);
		if(debug_message)
			System.out.println("I joined them into "+ printstring);
		return in_common;
	}


	public static boolean has_infrequent_subset(ArrayList<String> c, ArrayList<Frequent_Itemset> Lkm1){
		String printstring = "";
		for(String s : c)
		{
			printstring += s+", ";
		}
		printstring = printstring.substring(0,printstring.length() - 2);
		if(debug_message)
			System.out.println("Determining if "+printstring+ " has an infrequent subset");
		for(String s :  c){

			Frequent_Itemset km1_sub = new Frequent_Itemset();
			for(String k : c){
				if(!k.equals(s))
					km1_sub.itemset.add(k);
			}
			boolean infrequent = true;
			for(Frequent_Itemset I : Lkm1)
			{
				if(I.matches(km1_sub))
				{	infrequent = false; break; }
					
			}
			if(infrequent)
			{ if(debug_message){System.out.println("PRUNED:: "+printstring+ " has infrequent substring: "+km1_sub);}	return true; }
		}
		if(debug_message)
			System.out.println(printstring + " has no infrequent substrings");
		return false;
	}
	
	
}
