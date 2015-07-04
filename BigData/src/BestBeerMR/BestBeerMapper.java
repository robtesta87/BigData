package BestBeerMR;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class BestBeerMapper extends Mapper<Object, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String beerName = extractBeerName(line);
		String overall = extractOverall(line);
		
		word.set(beerName);
		context.write(word, one);
		
		word.set(beerName+",review");
		context.write(word ,new IntWritable(Integer.parseInt(overall)));
    	
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
	private static String extractOverall(String line) throws IOException{

		int i = 0;
		String overallName = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("overall")){
				overallName = splittedLine[j+1].split(":")[1].split(",")[0];
				return overallName;
			}
				//System.out.println(j+":"+split[j]);
		}

		return overallName;
	}
}