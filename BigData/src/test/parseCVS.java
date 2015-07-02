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
		f=new FileReader("/home/roberto/Scaricati/export.csv");
		String s;

		BufferedReader b;
		b=new BufferedReader(f);
		s=b.readLine();
		Reader in = new StringReader(s);
		
		//List<CSVRecord> list = CSVFormat.DEFAULT.parse(in).getRecords();
		for (CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
			for (String field : record) {
				System.out.print("\"" + field + "\", ");
			}
			System.out.println();
		}

	}
}
