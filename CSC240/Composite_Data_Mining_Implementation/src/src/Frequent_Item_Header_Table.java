import java.util.ArrayList;


public class Frequent_Item_Header_Table implements Comparable<Frequent_Item_Header_Table>{

	static int size =0; 
	String value;
	ArrayList<FP_Node> FP_Node_List;
	int count;
	
	public Frequent_Item_Header_Table(){
		FP_Node_List = new ArrayList<FP_Node>();
		value = "";
		count = 0;
	}
	public int compareTo(Frequent_Item_Header_Table obj) {
		if(this.count>obj.count)
				return -1;
		if(this.count<obj.count)
				return 1;
		return 0;
	}
	
}
