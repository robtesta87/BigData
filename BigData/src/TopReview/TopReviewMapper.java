package TopReview;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class TopReviewMapper extends Mapper<Object, Text, Text, Text> {
	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		if (line.contains("Node[")){

			String beerName = extractBeerName(line);
			String overall = extractOverall(line);
			word.set(beerName);
			context.write(word, new Text("1 "+overall));
		}
		/*StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);

		}*/
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
	private static String extractOverall(String line) throws IOException{
		String overall = (line.split("overall")[1].split(":")[1].split("\\.")[0]);

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
}
