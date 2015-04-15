import java.util.ArrayList;


public class path {

	ArrayList<FP_Node> list;
	ArrayList<String> basket;
	int weight;
	
	public path(FP_Tree tree,FP_Node node){
		FP_Node traversal_node =  node;
		list = new ArrayList<FP_Node>();
		basket = new ArrayList<String>();
		weight = node.count;
		traversal_node = traversal_node.parent;
		while(traversal_node!=tree.root){
			list.add(traversal_node);
			basket.add(traversal_node.value);
			traversal_node = traversal_node.parent;
		}
	}
	
	public String toString(){
		String outstring = "{";
		for(String s : basket){
			outstring += s;
		}
		outstring += "}";
		return outstring;
	}
	
}
