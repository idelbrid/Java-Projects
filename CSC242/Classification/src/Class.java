import java.util.ArrayList;
//import java.util.Hashtable;
 

public class Class {
	static int num_classes = 0;
	int serial = -1;
	int size = 0;
	ArrayList<FeatureVec> members;
	FeatureVec mean;
//	double dist_table [];
//	Hashtable<Cluster,Double> dist_hash;
	
	public Class(){
		members = new ArrayList<FeatureVec>();
		this.mean = new FeatureVec();
		serial = num_classes;
		num_classes++;
//		dist_hash = new Hashtable<Cluster,Double>();
	}
	
	public Class(FeatureVec mean){
		members = new ArrayList<FeatureVec>();
		this.mean = new FeatureVec(mean);
		serial = num_classes;
		num_classes++;
//		dist_hash = new Hashtable<Cluster,Double>();
	}
	
	public void addVec(FeatureVec v){
		members.add(v);
		if(v.cl !=null)
			v.cl.remVec(v);
		v.cl = this;
		v.label = this.serial;
		size++;
	}
	
	public void remVec(FeatureVec v){
		members.remove(v);
		size--;
	}
	
	public double distortion(){
		double distortion = 0;
		for(FeatureVec member : members){
			distortion += FeatureVec.d(member, this.mean);
		}
		return Math.sqrt(distortion);
	}
	
	public double d(Class other){
//		if(this.dist_table[other.serial]!=0){
//			System.out.println("Using table"); return dist_table[other.serial];
//		}
//		if(dist_hash.contains(other))
//		{System.out.println("Using hash");	return dist_hash.get(other); }
		double min = Double.MAX_VALUE;
		for(FeatureVec membera : this.members)
			for(FeatureVec memberb : other.members){
				double d = FeatureVec.d(membera, memberb);
				if(d<min)
					min = d;
			}
//		dist_hash.put(other,min);
		return min;
	}

	public void mergeClusters(Class other) {
		
		System.out.println("Merging Clusters");
//		for(Cluster c : other.dist_hash.keySet()){ //not sure if this works
//			if(this.dist_hash.contains(c))
//			{
//				if(other.dist_hash.get(c)<this.dist_hash.get(c))
//					this.dist_hash.put(c, other.dist_hash.get(c));
//			}
//			else this.dist_hash.put(c,  other.dist_hash.get(c));
//		}

//		for(int i=0; i<dist_table.length ; i++){
//			if(other.dist_table[i]!=0){
//				if(dist_table[i]!=0){
//					if(other.dist_table[i]<dist_table[i])
//						dist_table[i] = other.dist_table[i];
//				}
//				else dist_table[i] = other.dist_table[i];
//			}
//		}
		for(FeatureVec member : other.members){
			
			this.members.add(member);
			member.label = this.serial;
			member.cl = this;
			this.size++;
		}
		other.size = 0;
		other.members = null;
		num_classes --;
		
	}

	public void relableSerial(int i) {
		for(FeatureVec member: members){
			member.label = i;
		}
		this.serial = i;
		num_classes = i;
		
	}
	
	public String toString(){
		String toReturn = "C"+this.serial;
		return toReturn;
	}
}
