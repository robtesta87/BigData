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
		f=new FileReader("util/prova.txt");
		String s;
		String userName = "";
		String beerName = "";
		String text = "";
		int overall =0;
		int lengthReview = 0;
		int month = 0;
		int year = 0;
		BufferedReader b;
		b=new BufferedReader(f);
		int i =0;
		while ((s=b.readLine())!=null){
			s=b.readLine();
			//if ((!s.substring(0, 1).equals("+"))&&(!s.contains("u1"))&&(!s.contains("rows"))){
			if (s.contains("Node")){
				userName = s.split("username")[1].split("\"")[1];
				beerName = s.split("Name")[1].split("\"")[1];
				text = s.split("text")[1].split("\"")[1];
				overall = Integer.parseInt(s.split("overall")[1].split(":")[1].split("\\.")[0]);
				lengthReview = Integer.parseInt(s.split("lengthText")[1].split(":")[1].split("}")[0]);
				month = Integer.parseInt(s.split("time")[1].split("\"")[1].substring(5, 7));
				year =Integer.parseInt(s.split("time")[1].split("\"")[1].substring(0, 4));
				System.out.println(text);
				
			}
			i++;
				
		}
		
		/*
		FileReader f;
		f=new FileReader("/home/roberto/Scaricati/export2.csv");
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
			if (split[j].equals("lengthText")){
				//beerName = split[j+2].substring(5, 7);
				//int n = Integer.parseInt(beerName);
				//System.out.println("n"+n);
				String a= split[j+1];
				System.out.println(a.substring(1, a.length()-2));
				//System.out.println(j+":"+beerName);
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
		}
		
	}*/
}
}
