import java.util.Comparator;
import java.util.Hashtable;


public class Basket_Sorter implements Comparator<String>{

	Hashtable<String,Integer> hash;
	
	public Basket_Sorter(Hashtable<String,Integer> hash){
		this.hash = hash;
		
	}
	
	public int compare(String s1, String s2) {
		if(hash.get(s1)>hash.get(s2))
			return -1;
		if(hash.get(s1)<hash.get(s2))
			return 1;
		return s1.compareTo(s2);
	}

}
