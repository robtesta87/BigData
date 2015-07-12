package TopTenReviewMR;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class BestBeerMapper extends Mapper<Object, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		if (line.contains("Node[")){
			int overall = extractOverall(line);
			if (overall>14){
				word.set(extractBeerName(line));
				context.write(word, one);
			}
		}
	}

	private static String extractBeerName(String line) throws IOException{

		int i = 0;
		String beerName = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("Name")){
				beerName = splittedLine[j+2];
				return beerName;
			}
			//System.out.println(j+":"+split[j]);
		}

		return beerName;
	}

	private static int extractOverall(String line) throws IOException{
		int overall = Integer.parseInt(line.split("overall")[1].split(":")[1].split("\\.")[0]);

		/*int i = 0;
		String overallName = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("overall")){
				overallName = splittedLine[j+1].split(":")[1].split(",")[0];
				return overallName;
			}
			//System.out.println(j+":"+split[j]);
		}*/

		return overall;
	}
	/*
	private static String extractBeerName(String line) throws IOException{
		Reader in = new StringReader(line);
		int i = 0;
		String beerName = "";
		for (CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
			for (String field : record) {
				if (i==1){
					beerName = field.split("\"")[9];
					System.out.println(beerName);
				}
				i++;	

			}

		}
		return beerName;
	}*/
}