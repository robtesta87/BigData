package CoupleReview;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class CoupleReviewMapper extends Mapper<Object, Text, Text, Text> {


	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String username = extractUsername(line);
		String beerName = extractBeerName(line);

		int overall = Integer.parseInt(extractOverall(line));
		if (overall>=2){
			word.set(username);
			context.write(word, new Text(beerName) );
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

	private static String extractUsername(String line) throws IOException{

		int i = 0;
		String username = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("username")){
				username = splittedLine[j+2];
				return username;
			}
			//System.out.println(j+":"+split[j]);
		}

		return username;
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
