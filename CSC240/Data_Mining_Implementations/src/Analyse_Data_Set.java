import java.io.File;
import java.io.IOException;


public class Analyse_Data_Set {

	public static void main(String []args) throws IOException{
		String  DB_file = args[1];
		String output = args[2];
		String support_threshold = args[3];
		String confidence_threshold = args[4];
		String [] toPreprocessing = new String[]{args[1],"out"+args[1]};
		Preprocessing_Main.main(toPreprocessing);
		int size = Preprocessing_Main.COUNT(new File(args[1]));
		Integer count_threshold = (int) Double.parseDouble(args[3])*size;
		if(args[0].equalsIgnoreCase("apriori"))
		{	String [] toApriori = new String[]{"out"+args[1],count_threshold.toString(), "apriori_"+args[1]};
			Apriori_Main.main(toApriori);
			String[] toPost = new String[]{ "apriori_"+args[1],confidence_threshold , "rules_"+args[1]};
			Postprocessing_Main.main(toPost);
					
		}
		else if(args[0].equalsIgnoreCase("FP-growth")){
			String [] tofpgrowth = new String[]{ "out"+args[1],count_threshold.toString(), "fp_growth_"+args[1]};
			FP_Growth_Main.main(tofpgrowth);
			String[] toPost = new String[]{ "fp_growth_"+args[1],confidence_threshold , "rules_"+args[1]};
			Postprocessing_Main.main(toPost);
		}
		else if(args[0].equalsIgnoreCase("Improved_apriori")){
			String [] toApriori = new String[]{"out"+args[1],count_threshold.toString(), "improved_apriori_"+args[1]};
			Improved_Apriori_Main.main(toApriori);
			String[] toPost = new String[]{ "improved_apriori_"+args[1],confidence_threshold , "rules_"+args[1]};
			Postprocessing_Main.main(toPost);
		}

		System.out.println("Composite complete");
		
	}
	
}
