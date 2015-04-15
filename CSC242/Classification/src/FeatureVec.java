import java.text.DecimalFormat;
import java.util.PriorityQueue;


public class FeatureVec implements Comparable<FeatureVec>{
	
	static int D = 1;
	int label=-1;
	double features[];
	int serial = -1;
	static int num_vecs = 0;
	Class cl;
	double dist_to_current = 0;
	
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
			sum += Math.pow(o1.features[i]-o2.features[i], 2);  //Euclidean
		}
		return Math.sqrt(sum);
//		double sum = 0.0;
//		for(int i=0; i<o1.features.length; i++){
//			sum += Math.abs(o1.features[i]-o2.features[i]);  //Manhattan
//		}
//		return sum;
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
	
	public double d(Class cl){ //using the distance between it and the smallest member 
		double min = Double.MAX_VALUE;
		for(FeatureVec member : cl.members){
			double d = FeatureVec.d(member,this);
			if(d<min)
				min = d;
		}
		return min;
	}
	
	public FeatureVec[] inefficientFindNeighbors(FeatureVec[] trainingData, int k){
		PriorityQueue<FeatureVec> orderedData = new PriorityQueue<FeatureVec>();
		FeatureVec[] toReturn = new FeatureVec[k];
		for(int i=0; i<trainingData.length; i++){
			trainingData[i].dist_to_current = FeatureVec.d(trainingData[i],this);
			orderedData.add(trainingData[i]);
		}
		for(int i=0; i<k ; i++)
			toReturn[i] = orderedData.poll();
		return toReturn;
	}
	public int compareTo(FeatureVec other) {
		if(this.dist_to_current>other.dist_to_current)
			return 1;
		else if(this.dist_to_current<other.dist_to_current)
			return -1;
		return 0;
	}
}
