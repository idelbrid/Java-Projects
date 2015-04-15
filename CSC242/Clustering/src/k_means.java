import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

public class k_means {
	
	static DecimalFormat df = new DecimalFormat("####.#####");
	
	public static void main(String [] args) throws IOException{
		//Argument 0 is k
		//Argument 1 is input file
		//Argument 2 is output file (optional)
			//default output == k_means_output.txt
		int k = Integer.parseInt(args[0]);
		File infile = new File(args[1]);
		File outfile = null;
		if(!args[2].isEmpty())
			 outfile = new File(args[2]);
		else outfile = new File("k_means_output.txt");
		Scanner fileScan = new Scanner(infile);
		Scanner testScan = new Scanner(infile);
		int D =0;
		String line = testScan.nextLine();
		Scanner lineScan = new Scanner(line);
		while(lineScan.hasNext()){
			lineScan.next();
			D++;
		}
		testScan.close();
		lineScan.close();
		FeatureVec.D = D;
		ArrayList<FeatureVec> dataList = new ArrayList<FeatureVec>();
		while(fileScan.hasNextLine()){
			if(fileScan.hasNext()){
				double[] vec = new double[D];
				for(int i=0;i<D; i++)
					vec[i] = Double.parseDouble(fileScan.next());
				dataList.add(new FeatureVec(vec));
			}
			else fileScan.nextLine();
		}
		fileScan.close();
		FeatureVec[] data = new FeatureVec[dataList.size()];
		for(int i=0; i<dataList.size();i++)
			data[i]=dataList.get(i);
		Cluster clusters[] = K_MEANS(data,k);
		FileWriter FW = new FileWriter(outfile);
		FW.write("");
		for(FeatureVec FV : data){
			FW.append(FV.toString() + "\n");
		}
		double sum = 0;
		for(int i =0; i<k; i++){
			double dist = clusters[i].distortion();
			System.out.println("Distortion("+i+") = "+dist);
			FW.append("\nDistortion("+i+") = "+dist);
			sum +=dist;
		}
		System.out.println("Total: "+sum);
		FW.append("\nDistortion in total = "+ sum);
		FW.close();

		draw_data(data);
		
	}
	
	
	private static void draw_data(FeatureVec[] data) {
		double minx=0,miny=0,maxx=0,maxy=0;
		for(int i=0; i<data.length; i++){
			double [] feats = data[i].features;
			if(feats[0]>maxx)
				maxx=feats[0];
			if(feats[0]<minx)
				minx=feats[0];
			if(feats[1]>maxy)
				maxy=feats[1];
			if(feats[1]<miny)
				miny=feats[1];
		}
		for(int i =0; i<data.length; i++)
		{
			data[i].features[0]-=minx;
			data[i].features[0]/=maxx;
			data[i].features[1]-=miny;		//normalizing data for display
			data[i].features[1]/=maxy;
		}
		JFrame frame = new JFrame("Visualization of Clustered Data");
		frame.setSize(1300,1300);  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new DisplayPanel(data));
		frame.setVisible(true);
		
		
	}


	public static Cluster[] K_MEANS(FeatureVec[] data,int k){
		FeatureVec[] means = initialize(data,k);
		Cluster[] clusters = new Cluster[k];
		for(int i=0; i<k ; i++){
			clusters[i] = new Cluster(means[i]);
		}
		
		do {
			for(int i=0; i<means.length; i++)
				means[i] = new FeatureVec(clusters[i].mean);//overwrite old means (shallow)
			Assign_Clusters(data,clusters); //
			Calculate_Means(data,clusters);	//adjusts means in the clusters
		}while(mean_changes(means, clusters));
		return clusters;
	}
	
	public static FeatureVec[] initialize(FeatureVec[] data, int k){
		System.out.println("Initializing...");
		FeatureVec[] means = new FeatureVec [k];
		for(int i =0; i<k ; i++)
			means[i]= new FeatureVec(data[i]); //shallow copy first k
		return means;	
	}
	
	public static boolean mean_changes(FeatureVec[] means, Cluster[] clusters){
		System.out.println("Does the mean change?");
		for(int i =0 ; i<means.length ; i++){
			double[] oldmean = means[i].features;
			double[] newmean = clusters[i].mean.features;
			for(int j=0; j<FeatureVec.D; j++)
				if(!df.format(oldmean[j]).equals(df.format(newmean[j])))
					{System.out.println("Yes");return true; } //TAKE THIS OUT
		}
		return false;
	}
	
	public static void Assign_Clusters(FeatureVec[] data, Cluster[] clusters){
		System.out.println("Assigning Clusters");
		//naive algorithm first
		for(int i=0; i<data.length;i++){
			int min_label=0;
			double min_dist = Double.MAX_VALUE;
			for(int j=0; j<clusters.length; j++){
				double d = FeatureVec.d(data[i], clusters[j].mean);
				if(d<min_dist)
				{min_dist = d; min_label = j;}	
			}
			clusters[min_label].addVec(data[i]);
		}
	}
	
	public static void Calculate_Means(FeatureVec[] data, Cluster[] clusters){
		System.out.println("Calculating Means");
		for(Cluster c : clusters ){		  //
			double [] mean = c.mean.features;
			for(int i =0 ; i<FeatureVec.D; i++)
				mean[i] = 0;			     	  //
		}
		for(int i=0; i<data.length; i++){
			FeatureVec mean = data[i].cluster.mean;
			for(int j=0;j<FeatureVec.D;j++)
				mean.features[j]+=data[i].features[j];
		}
		for(int i=0; i<clusters.length; i++){
			for(int j=0;j<FeatureVec.D;j++)
				if(clusters[i].size!=0)
					clusters[i].mean.features[j] /= clusters[i].size;
		}
	}
	
	

}
