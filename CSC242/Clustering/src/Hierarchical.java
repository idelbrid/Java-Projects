import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.tree.*;
import javax.swing.JFrame;


public class Hierarchical {
	public static int k = 0 ;
	public static File outfile;

	public static void main(String [] args) throws IOException{
				//Argument 0 is k, the number of clusters.
						//this isn't theoretically required, but it's necessary to know
						//which level of the binary tree to display on the graph
				//Argument 1 is input file
				//Argument 2 is output file (optional)
					//default output == hier_output.txt
				k  = Integer.parseInt(args[0]);
				File infile = new File(args[1]);
				outfile = null;
				if(!args[2].isEmpty())
					 outfile = new File(args[2]);
				else outfile = new File("hier_output.txt");
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
				hierarchical(data, k);
	}

	private static void hierarchical(FeatureVec[] data, int k) throws IOException {
		ArrayList<Cluster> clusters = new ArrayList<Cluster>(data.length);
		System.out.println("Giving each vector its own cluster");
		for(int i = 0 ; i<data.length ; i++){  //giving each vector its own cluster
			Cluster c = new Cluster();
			c.addVec(data[i]);
			clusters.add(c);
		}
//		for(int i =0 ; i<clusters.size(); i++){
//			clusters.get(i).dist_table = new double [clusters.size()];
//		}
		
		while(clusters.size()>1){
			Cluster[] A_B = find_closest(clusters);
			A_B[0].mergeClusters(A_B[1]);
			clusters.remove(A_B[1]);
			if(clusters.size()==k)
			{	draw_data(data,clusters);
				write_to_file(data);
			}
			
		}
		
	}
	
	private static void write_to_file(FeatureVec[] data) throws IOException{
		FileWriter FW = new FileWriter(outfile);
		FW.write("");
		for(FeatureVec FV : data){
			FW.append(FV.toString() + "\n");
		}
		FW.close();
	}

	private static void draw_data(FeatureVec[] data, ArrayList<Cluster> clusters) throws IOException {
		double minx=0,miny=0,maxx=0,maxy=0;
		for(int i=0; i<clusters.size(); i++){
			clusters.get(i).relableSerial(i);
		}
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
		
		System.in.read();
		
	}

	private static Cluster[] find_closest(ArrayList<Cluster> clusters) {
		System.out.println("Finding closest");
		Cluster[] closest = new Cluster[2];
		double min = Double.MAX_VALUE;
		for(int i =0; i<clusters.size() ; i++){  //actually just going through all of the clusters
			for(int j=0; j<i; j++){				 //and finding the minimum
				Cluster c1 = clusters.get(i);
				Cluster c2 = clusters.get(j);
				double d = c1.d(c2);
				if(d<min){
					min = d;
					closest[0]= c1;
					closest[1] = c2;
				}
			}
		}
		return closest;
	}
}
