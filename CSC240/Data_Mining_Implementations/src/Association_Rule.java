
public class Association_Rule {
	Frequent_Itemset[] L;
	Frequent_Itemset itemset1;
	Frequent_Itemset itemset2;
	int support_count=0;
	double support;
	double confidence;
	
	public Association_Rule(Frequent_Itemset left, Frequent_Itemset right, Frequent_Itemset[] L){
		this.L = L;
		this.itemset1 = left;
		this.itemset2 = right;
		this.support_count = right.support_count;
		this.support =1.0*support_count/L.length*1.0;
		this.confidence = 1.0*left.support_count /1.0*right.support_count;
		
		
	}
	
	public String toString(){
		String outstring = "";
		outstring += "{"+itemset1+"}=>{"+itemset2+"}";
		return outstring;
	}
	

}
