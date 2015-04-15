import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Postprocessing_Main {

	static Frequent_Itemset [] FI;
	static double confidence_threshold;
	
	public static void main(String[] args) throws IOException{
		FileReader FR = new FileReader(new File(args[0]));
		Scanner file_scanner = new Scanner(FR);
		confidence_threshold = Double.parseDouble(args[1]);
		FileWriter FW = new FileWriter(new File(args[2]));
		FW.write("");
		ArrayList<Association_Rule> association_rules = new ArrayList<Association_Rule>();
		int frequent_item_count = 0;
		while(file_scanner.hasNextLine()){
			file_scanner.nextLine();
			frequent_item_count++;
		}
		file_scanner.close();
		FR.close();
		FR = new FileReader(new File(args[0]));
		file_scanner = new Scanner(FR);
		FI = new Frequent_Itemset[frequent_item_count];
		int i =0;
		while(file_scanner.hasNextLine()){
			String line = file_scanner.nextLine();
			String [] part = line.split(": ");
			int support_count = Integer.parseInt(part[1]);
			System.out.println("Count is "+support_count);
			part[0] = part[0].substring(1,part[0].length()-1);
			String [] basket = part[0].split(", ");
			ArrayList<String> transaction = new ArrayList<String>();
			for(String S : basket){
				transaction.add(S);
			}
			System.out.println("Reading: "+transaction.toString());
			Frequent_Itemset I = new Frequent_Itemset(transaction,support_count);
			System.out.println("Interpreting: "+I.toString());
			FI[i++] = I;
		}
		file_scanner.close();
		for(Frequent_Itemset l : FI){
			association_rules.addAll(Generate_Association_Rule(l));
		}
		for(Association_Rule R : association_rules){
			System.out.println(R);
			FW.append(R.toString());
		}
		FW.close();
		
	}	
		public static ArrayList<Association_Rule> Generate_Association_Rule(Frequent_Itemset l){
			//int i =0;
			ArrayList<Association_Rule> rules = new ArrayList<Association_Rule>();
			for(int start_sub_list =0; start_sub_list<l.itemset.size()-1 ; start_sub_list++){
				for(int end_sub_list = start_sub_list ; end_sub_list<l.itemset.size(); end_sub_list++){
					List<String> sub =  l.itemset.subList(start_sub_list,end_sub_list);
					ArrayList<String> sublist = new ArrayList<String>();
					for(String s : sub){
						sublist.add(s);
					}
					Frequent_Itemset left_side = new Frequent_Itemset();
					left_side.itemset = sublist;
					for(int j =0; j<FI.length ; j++){
						if(left_side.matches(FI[j]))
							left_side.support_count = FI[j].support_count;
							break;
					}
					Association_Rule rule = new Association_Rule(left_side,l,FI);
					if(rule.confidence>confidence_threshold)
						rules.add(rule);
					
				}
			}
			return rules;
		}
}
