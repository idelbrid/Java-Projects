import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
public class FP_Growth_Main {

	static boolean debug_message;
	
	public static void main(String [] args) throws IOException{
		debug_message = false;
		ArrayList<Frequent_Itemset> frequent_itemsets = FP_Growth_Init(args);
		FileWriter FW = new FileWriter(new File(args[2]));
		FW.write("");
		for(Frequent_Itemset I: frequent_itemsets){
			FW.append(I.itemset.toString() + ": "+ I.support_count+"\n");
		}
		FW.close();
		
		
	}
	
	
	public static ArrayList<Frequent_Itemset>  FP_Growth_Init(String[] args) throws IOException {
		File file = new File(args[0]);
		
		FileReader FR = new FileReader(file); 
		final int threshold = Integer.parseInt(args[1]);
		//Scan #1: count each of the itemsets and count the items 
		Hashtable<String,Integer> item_hash = Count_Items(FR);
		FR.close();
		
		Iterator<Entry<String, Integer>> it = item_hash.entrySet().iterator();	
		while(it.hasNext()){
			Entry<String,Integer> e = it.next();
			if(debug_message)
				System.out.print("Item = "+ e.getKey() + ": "+e.getValue());
			if(e.getValue()<threshold)
			{	it.remove(); if(debug_message){System.out.print(" (removed)");} }
			if(debug_message)
				System.out.println();
		}

		//Scan #2: based on frequency of an item, sort each basket
		//Add to tree
		FR = new FileReader(file);
		FP_Tree main_tree = new FP_Tree(FR,threshold,item_hash);
		
		
		//Start reading from tree: 
		ArrayList<String> dummyset = new ArrayList<String>();
		ArrayList<Frequent_Itemset> frequent_itemsets =FP_Growth(main_tree, dummyset, threshold);

//		for(Frequent_Itemset i : frequent_itemsets){
//		//	System.out.println("Itemset: " + i.itemset.toString() + " with support "+ i.support_count);
//		}
		return frequent_itemsets;
	}
	
	public static Hashtable<String,Integer> Count_Items(FileReader FR){
		if(debug_message)
			System.out.println("Counting! ... ");
		Hashtable<String,Integer> temptable = new Hashtable<String,Integer>();
		Scanner scan = new Scanner(FR);
		while(scan.hasNext()){
			String raw_word = scan.next();
			if(raw_word.endsWith(","))
				raw_word = raw_word.substring(0,raw_word.indexOf(","));
			String word = raw_word;
			if(temptable.containsKey(word))
				temptable.put(word, temptable.get(word)+1);
			else temptable.put(word, 1);
		}	
		scan.close();
		if(debug_message)
			System.out.println("Done counting.\n");
		return temptable;
	}

	
	public static ArrayList<Frequent_Itemset> FP_Growth(FP_Tree main_tree, ArrayList<String> suffix,int threshold){
		System.out.println("Starting FP_Growth for "+ suffix.toString()+"\n");
		Hashtable<String,FP_Node> header_table = main_tree.header_table;
		Iterator<Entry<String, FP_Node>> it = header_table.entrySet().iterator();	
		ArrayList<Frequent_Itemset> All_Freq_Itemsets = new ArrayList<Frequent_Itemset>();
		
		while(it.hasNext()){
			Entry<String,FP_Node> e = it.next();
			int support = 0;
			FP_Node head = e.getValue();
			if(debug_message)
				System.out.print("Choosing item "+ head.value);

			support += head.count;
			FP_Node current = head;
			while(current.sibling!= null){
				current = current.sibling;
				support += current.count;	
			}
			if(debug_message)
				System.out.println(": "+ support);
			if(support>=threshold && !suffix.contains(e.getKey())){
				if(debug_message)
					System.out.println("New Frequent Pattern detected (" + suffix + head.value);
				Frequent_Itemset added_set = new Frequent_Itemset();
				added_set.itemset.add(e.getKey());
				added_set.itemset.addAll(suffix);
				added_set.support_count = support;
				All_Freq_Itemsets.add(added_set);
				FP_Tree conditional_tree = main_tree.Conditional_Tree(e.getKey(),threshold);
				ArrayList<Frequent_Itemset> toAdd  = FP_Growth(conditional_tree, added_set.itemset, threshold);
				All_Freq_Itemsets.addAll(toAdd);	
			} 
		}
		
		System.out.println("Finishing FP_Growth for "+suffix.toString());
		return All_Freq_Itemsets;	
	}
	

}
