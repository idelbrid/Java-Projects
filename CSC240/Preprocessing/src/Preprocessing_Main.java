import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Preprocessing_Main {

	public static void main(String [] args) throws IOException{
		File infile = new File(args[0]);
		FileReader FR = new FileReader(infile);
		File outfile = new File(args[1]);
		FileWriter FW = new FileWriter(outfile);
		
		String Field[] = new String[15];
		Field[0] = "age"; //cont
		Field[1] = "workclass";
		Field[2] = "fnlwgt"; //cont
		Field[3] = "education";
		Field[4] = "education-num"; //cont
		Field[5] = "marital-status";
		Field[6] = "occupation";
		Field[7] = "relationship";
		Field[8] = "race";
		Field[9] = "sex";
		Field[10] = "capital-gain"; //cont
		Field[11] = "capital-loss"; //cont
		Field[12] = "hours-per-week"; //cont
		Field[13] = "native-country";
		Field[14] = "income"; //>50k, <=50k
		
		FW.write("");
		Scanner file_scanner = new Scanner(FR);
		while(file_scanner.hasNextLine()){
			String line = file_scanner.nextLine();
			String [] transaction = line.split(" ");
			for(int i=0; i<transaction.length; i++){
//				if(transaction[i].endsWith(","))
//					transaction[i] = transaction[i].substring(0,transaction[i].length()-2);
				transaction[i] = Field[i] + "=" +transaction[i];
				FW.append(transaction[i] + " ");
			}
			if(file_scanner.hasNextLine())
				FW.append("\n");
		}
		file_scanner.close();
		FR.close();
		FW.close();
	}
}
