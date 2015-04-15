import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;


public class FP_Tree {
	ArrayList<String> suffix = new ArrayList<String>();
	FP_Node root;
	Hashtable<String,FP_Node> header_table = new Hashtable<String,FP_Node>();
	
	public FP_Tree(){
		
	}
	public FP_Tree(FileReader FR, int threshold, 
			Hashtable<String,Integer> item_hash) throws IOException{

		Scanner file_scanner = new Scanner(FR);
	//a bunch of initialization
		Basket_Sorter BS = new Basket_Sorter(item_hash);
		FP_Node null_head = new FP_Node();
		root = null_head;
		FP_Node current_node;
		
		while(file_scanner.hasNext()){ //for each item
			String line = file_scanner.nextLine();
			Scanner line_scanner = new Scanner(line);	
			ArrayList<String> Basket = new ArrayList<String>();
			current_node = null_head;
			while(line_scanner.hasNext()){				//creating itemlist
				String raw_word = line_scanner.next();
				if(raw_word.endsWith(","))
					raw_word = raw_word.substring(0,raw_word.indexOf(","));
				String item = raw_word;
				if(item_hash.containsKey(item)){
					Basket.add(item);
				}
			}
			line_scanner.close();
			Collections.sort(Basket,BS);				//sorting the basket according to items' positions
														//in the header_table
			Iterator<String> it = Basket.iterator();
			while(it.hasNext()){						//iterating through the basket/itemset
				String item = it.next();
				if(current_node.has_child(item)){ 		//previous prefix found
					FP_Node next = current_node.get_child(item);
					next.count ++;
					current_node = next;
				}
				else{									//previous prefix NOT found
					FP_Node added_node = new FP_Node();
					added_node.value = item;
					added_node.count = 1;
					if( header_table.containsKey(item))
					{
						FP_Node previous_head = header_table.get(item);
						added_node.sibling = previous_head;
						header_table.put(item,added_node);
						
					}
					else{
						header_table.put(item, added_node);
					}
					added_node.parent = current_node;
					current_node.children.add(added_node);
					current_node =added_node;
				}
				
			}//ends itemset
				
		}//ends data 

		file_scanner.close();
		
	}
	
	public FP_Tree Conditional_Tree(String selected_item, int threshold){

		FP_Tree cond_tree = new FP_Tree();
		cond_tree.root = new FP_Node();
		cond_tree.suffix.addAll(this.suffix);
		cond_tree.suffix.add(selected_item);
		System.out.println("Constructing Conditional Tree for "+ cond_tree.suffix.toString());
		FP_Node current = header_table.get(selected_item);
		Hashtable<String,Integer> item_hash = new Hashtable<String,Integer>();
		ArrayList<path> paths = new ArrayList<path>();
		System.out.println("Finding paths for "+selected_item);
		while(current != null){ 							//goes through each node with "item"

			paths.add(new path(this,current));
			current = current.sibling;
		}
		System.out.print("The paths are: ");
		for(path p : paths){
			System.out.print(p + ", ");
		}
		System.out.println("");
		System.out.println("Counting the paths of "+ selected_item);
		for(path p : paths){
			for(FP_Node n : p.list){
				if(item_hash.containsKey(n.value))
					item_hash.put(n.value, item_hash.get(n.value)+p.weight);
				else item_hash.put(n.value, p.weight);
			}
		}
		ArrayList<String> removal_list = new ArrayList<String>();
		Iterator<Entry<String, Integer>> it = item_hash.entrySet().iterator();	
		System.out.println("Removing infrequent items");
		while(it.hasNext()){
			Entry<String,Integer> e = it.next();
			if(e.getValue()<threshold){
				removal_list.add(e.getKey()); 
				it.remove(); 
			}
		}	
		Basket_Sorter BS = new Basket_Sorter(item_hash);
		
		for(path p : paths){
			for(int i=0; i<p.list.size(); i++){
				if(removal_list.contains(p.list.get(i).value))
				{System.out.println("Removing "+p.list.get(i).value+" from list");
					p.list.remove(p.list.get(i)); i--;}
			}
			for(int i=0; i<p.basket.size(); i++){
				String s = p.basket.get(i);
				if(removal_list.contains(s))
				{System.out.println("Removing "+s+" from basket");
					p.basket.remove(s); i--; }
			}
			Collections.sort(p.basket,BS);
			System.out.println("\nSorted: "+p.toString());
			FP_Node current_node =  cond_tree.root;

			Iterator<String> iter = p.basket.iterator();
			while(iter.hasNext()){						//iterating through the basket/itemset
				String item = iter.next();
				if(current_node.has_child(item)){ 		//previous prefix found
					FP_Node next = current_node.get_child(item);
					next.count += p.weight;
					System.out.println("Adding to existing node for "+item+": ");
					System.out.println("\t"+next);
					current_node = next;
				}
				else{									//previous prefix NOT found
					System.out.println("Adding new node for "+item + ": ");
					FP_Node added_node = new FP_Node();
					added_node.value = item;
					added_node.count = p.weight;
					if( cond_tree.header_table.containsKey(item))
					{
						FP_Node previous_head = cond_tree.header_table.get(item);
						added_node.sibling = previous_head;
						cond_tree.header_table.put(item,added_node);
						
					}
					else{
						cond_tree.header_table.put(item, added_node);
					}
					added_node.parent = current_node;
					current_node.children.add(added_node);
					current_node =added_node;
					System.out.println("\t"+added_node.toString());
				}
				
			}//ends itemset
		}	

	
	return cond_tree;
	
	}
}
