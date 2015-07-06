package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class parseCVS {
	public static void main(String[] args) throws IOException {
		FileReader f;
		f=new FileReader("/home/roberto/Scaricati/export4.csv");
		String s;

		BufferedReader b;
		b=new BufferedReader(f);
		s=b.readLine();
		Reader in = new StringReader(s);
		int i=0;
		String userName = "";
		String beerName = "";
		String vote ="";
		String[] split = s.split("\"\"");
		//beerName = s.split("\"\"")[15];
		//System.out.println(beerName);
		for (int j = 0; j < split.length; j++) {
			if (split[j].equals("time")){
				beerName = split[j+2].substring(5, 7);
				int n = Integer.parseInt(beerName);
				System.out.println("n"+n);
				//System.out.println(j+":"+split[j+1].split(":")[1].split(",")[0]);
				System.out.println(j+":"+beerName);
			}
		}
		//List<CSVRecord> list = CSVFormat.DEFAULT.parse(in).getRecords();
		/*for (CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
			for (String field : record) {
				if (i==0){
					userName = field.split("\"")[3];
					System.out.println(userName);
				}
				if (i==1){
					beerName = field.split("\"")[9];
					System.out.println(beerName);
				}
				if (i==2){
					vote = field.split("\"")[14];
					System.out.println(vote);
				}
				i++;	
				//System.out.print("\"" + field + "\", ");
			}
			System.out.println();
		}*/

	}
}
