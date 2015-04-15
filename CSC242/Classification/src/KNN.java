import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class KNN {

	static int k=1;
	
	public static void main(String args[]) throws IOException{
		File training = new File(args[0]);
		File tuning = new File(args[1]);
		File testing = new File(args[2]);
		File outfile = null;
		if(args.length>=4)
			 outfile = new File(args[3]);
		else outfile = new File(args[2]+"+class.txt");
		
		FeatureVec[] trainingData = readTraining(training);
		FeatureVec[] tuningData = readTuning(tuning, trainingData);
		FeatureVec[] testingData = readTesting(testing);
		
		Class[] tuningDataClasses = new Class[tuningData.length];
		double topAccuracy = Double.MIN_VALUE;
		int optimalK = 1;
		
		for(int j=1; j <= trainingData.length ; j++){
			
			k=j;
			double accuracy = 0;
			for(int i=0; i<tuningData.length ; i++){
				tuningDataClasses[i] = knn(tuningData[i],trainingData);
				boolean match = tuningDataClasses[i].equals(tuningData[i].cl);
				//System.out.println(i + ". " + match);
				if(match)
					accuracy++;
			}
			accuracy /= tuningData.length;
			//System.out.println("Total accuracy for k="+k+": "+accuracy);
			if(accuracy>topAccuracy){
				topAccuracy = accuracy;
				optimalK = j;
			}
		}
		System.out.println("The best k-value for tuning data is "+optimalK
				+ " with accuracy of "+topAccuracy);
		
		k = optimalK;
		for(int i=0; i<testingData.length ; i++){
			testingData[i].cl = knn(testingData[i],trainingData);
			System.out.println("Vec"+i+"->"+testingData[i].cl);
		}
		writeClassifiedTesting(outfile, testingData);
		
		
	}
	
	public static Class knn(FeatureVec toClassify,FeatureVec[] trainingData){
		FeatureVec[] nearest_neighbors = toClassify.inefficientFindNeighbors(trainingData, k);
		return majorityClass(nearest_neighbors);
		
	}
	
	public static Class majorityClass(FeatureVec [] nearest_neighbors){
		ArrayList<Class> candidates = new ArrayList<Class>();
		int[] counts = new int[k];
		for(int i=0; i<k; i++){
			Class cl= nearest_neighbors[i].cl;
			if(!candidates.contains(cl)){
				candidates.add(cl);
				counts[candidates.size()-1] = 1;
			}
			else {
				counts[candidates.indexOf(cl)] ++;
			}			
		}
		Class this_ones_class = null;
		int maxcount = 0;
		for(int i=0; i<k; i++){
			if(counts[i]>=maxcount)
			{ maxcount =counts[i] ; this_ones_class = candidates.get(i);}
		}
		return this_ones_class;
	}
	
	public static FeatureVec[] readTraining(File training) throws FileNotFoundException{

		int D = 0;
		int N = 0;
		ArrayList<Integer> classInts = new ArrayList<Integer>();	//First finding D,N
		ArrayList<Class> clsses = new ArrayList<Class>();
		Scanner fileScan = new Scanner(training);
		String firstline = fileScan.nextLine();
		Scanner firstlineScan = new Scanner(firstline);
		while(firstlineScan.hasNext()){
			D++;
			firstlineScan.next();
		}
		firstlineScan.close();
		FeatureVec.D = --D;
		System.out.print("Dimension: "+D); //counted D
		N++; 
		while(fileScan.hasNextLine()){
			N++;
			fileScan.nextLine();
		}
		System.out.println("Samples: "+N); //counted N
		FeatureVec[] data = new FeatureVec[N];
		fileScan.close();
		fileScan = new Scanner(training);
		int index_of_vec=0;
		
		//creating individual vectors
		while(fileScan.hasNext()){
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			Double temp = Double.parseDouble(lineScan.next());
			Integer thisclass = temp.intValue(); //integer representation of class
			boolean oldClass = false; //will be false if we've never seen it before
										//then we'll create it as a class
										//otherwise, we'll just add the vec to that class
			int i;
			for(i=0; i<classInts.size(); i++){
				if(classInts.get(i)==thisclass){
					oldClass = true;
					break;
				}
			}
			if(!oldClass){
				clsses.add(new Class());
				clsses.get(i).serial = thisclass;
				classInts.add(thisclass);
			}
			FeatureVec myvec = new FeatureVec();
			clsses.get(i).addVec(myvec);
			int j=0;
			while(lineScan.hasNext()){
				myvec.features[j] = Double.parseDouble(lineScan.next());
				j++;
			}
			lineScan.close();	
			data[index_of_vec++] = myvec;
		}
		fileScan.close();
		return data;
		
	}
	
	public static FeatureVec[] readTuning(File tuning, FeatureVec[] trainingData) throws FileNotFoundException{
		ArrayList<Class> clsses = new ArrayList<Class>();
		for(int i =0; i<trainingData.length ; i++){
			if(!clsses.contains(trainingData[i].cl)){
				clsses.add(trainingData[i].cl);
			}
		}
		
		int N = 0;
		Scanner fileScan = new Scanner(tuning);
		while(fileScan.hasNextLine()){
			N++;
			fileScan.nextLine();
		}
		FeatureVec[] tuningData = new FeatureVec[N];
		int index_of_vec=0;
		fileScan.close();
		fileScan = new Scanner(tuning);
		while(fileScan.hasNext()){
			FeatureVec myvec = new FeatureVec();
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			Double temp = Double.parseDouble(lineScan.next());
			Integer thisclass = temp.intValue();
			for(Class cl : clsses){
				if(cl.serial == thisclass){
					cl.addVec(myvec);
				}
			}

			int j=0;
			while(lineScan.hasNext()){
				myvec.features[j] = Double.parseDouble(lineScan.next());
				j++;
			}
			lineScan.close();	
			tuningData[index_of_vec++] = myvec;
		}
		fileScan.close();
		return tuningData;
		
	}
	
	public static FeatureVec[] readTesting(File testing) throws FileNotFoundException{

		
		int N = 0;
		Scanner fileScan = new Scanner(testing);
		int i=0;
		while(fileScan.hasNextLine()){
			N++;
			fileScan.nextLine();
		}
		FeatureVec[] tuningData = new FeatureVec[N];
		fileScan.close();
		fileScan = new Scanner(testing);
		while(fileScan.hasNext()){
			FeatureVec myvec = new FeatureVec();
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			int j=0;
			while(lineScan.hasNext()){
				myvec.features[j] = Double.parseDouble(lineScan.next());
				j++;
			}
			lineScan.close();	
			tuningData[i++] = myvec;
		}
		fileScan.close();
		return tuningData;
		
	}
	
	public static void writeClassifiedTesting(File outfile, FeatureVec[] testingData) throws IOException{
		FileWriter FW = new FileWriter(outfile);
		
		for(int i =0; i<testingData.length ;i++){
			String toPrint ="";
			double thisclass = (double) testingData[i].cl.serial;
			toPrint+= thisclass;
			for(int j=0; j<FeatureVec.D; j++){
				toPrint+= " "+testingData[i].features[j];
			}
			//System.out.println(toPrint);
			FW.append(toPrint+"\n");
			
		}
		FW.close();
		
	}
}


