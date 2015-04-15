import java.util.ArrayList;


public class Frequent_Itemset {

	int support_count=0;
	ArrayList<String> itemset = new ArrayList<String>();
	
	public Frequent_Itemset(){
		
	}
	public Frequent_Itemset(Candidate c) {
		this.itemset = c.itemset;
		this.support_count = c.count;
	}
	
	public Frequent_Itemset(ArrayList<String> itemset, int support_count){
		this.itemset = itemset;
		this.support_count = support_count;
	}

	public String toString(){
		String outstring = "";
		for(String s : this.itemset){
			outstring += s + ", ";
		}
		if(outstring.equals(""))
			outstring = "EMPTY";
		else outstring = outstring.substring(0,outstring.length()-2);
		
		return outstring;
	}
	
	public boolean matches(Frequent_Itemset other){
		for(String s : other.itemset){
			boolean eq = false;
			for(String o : this.itemset){
				if(o.equals(s))
					eq = true;
			}
			if(!eq){
				return false;
			}			
		}
		return true;
	}
}