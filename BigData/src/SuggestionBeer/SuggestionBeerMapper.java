package SuggestionBeer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class SuggestionBeerMapper extends Mapper<Object, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		

		if (line.contains("Node[")){
			String beerName = extractBeerName(line);
			word.set(beerName);
			context.write(word, one);

		}
	}

	private static String extractBeerName(String line) throws IOException{
		String beerName = line.split("Name")[1].split("\"")[1];
		/*int i = 0;
		String beerName = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("Name")){
				beerName = splittedLine[j+2];
				return beerName;
			}
			//System.out.println(j+":"+split[j]);
		}*/

		return beerName;
	}

}