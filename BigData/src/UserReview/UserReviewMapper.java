package UserReview;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class UserReviewMapper extends Mapper<Object, Text, Text, Text> {
	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String username = extractUsername(line);
		int length = lengthReview(line);
		word.set(username);
		context.write(word, new Text("1 "+length));

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
	
	private static int lengthReview(String line)throws IOException{
		int length=0;
		String text = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("text")){
				text = splittedLine[j+2];
				length= text.length();
				return length;
			}
			//System.out.println(j+":"+split[j]);
		}

		return length;
	}

}
