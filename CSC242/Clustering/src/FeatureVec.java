import java.text.DecimalFormat;


public class FeatureVec {

	static int D = 1;
	int label=-1;
	double features[];
	int serial = -1;
	static int num_vecs = 0;
	Cluster cluster;
	
	public FeatureVec(){
		serial = num_vecs;
		num_vecs ++;
		features = new double [D];
	}
	public FeatureVec( double[] feat ){
		serial = num_vecs;
		num_vecs ++;
		features = new double[D];
		try{
			for(int i =0; i<D; i++){
				features[i] = feat[i];
			}
		}catch(IndexOutOfBoundsException e){
			System.err.println("Error "+ e+ ";"
					+ "\nFeature Vector dimension mismatch: D>size(input array)");
			for(int i=0; i<D ; i++)
				features[i]=-1;
		}finally{

		}
		
		
	}
	public FeatureVec(FeatureVec original){
		features = new double[D];
		try{
			for(int i =0; i<D; i++){
				features[i] = original.features[i];
			}
		}catch(IndexOutOfBoundsException e){
			System.err.println("Error "+ e+ ";"
					+ "\nFeature Vector dimension mismatch: D>size(input array)");
			for(int i=0; i<D-1 ; i++)
				features[i]=-1;
		}finally{

		}
	}
	
	public static double d(FeatureVec o1, FeatureVec o2){
		double sum = 0.0;
		for(int i=0; i<o1.features.length; i++){
			sum += Math.pow(o1.features[i]-o2.features[i], 2);
		}
		return Math.sqrt(sum);
	}
	
	public String toString(){
		
		DecimalFormat myFormatter = new DecimalFormat("##.####");
		String output = "";

		for(int i = 0; i<D; i++)
		{
			String temp = myFormatter.format(this.features[i]);
			output += temp + " ";
		}
		if(this.label!=-1)
			output += " -> "+this.label;
		return output;
	}
	
	public double d(Cluster cluster){ //using the distance between it and the smallest member 
		double min = Double.MAX_VALUE;
		for(FeatureVec member : cluster.members){
			double d = FeatureVec.d(member,this);
			if(d<min)
				min = d;
		}
		return min;
	}
}
