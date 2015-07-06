package WordReview;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class WordReview2Mapper extends
Mapper<LongWritable, Text, Text, Text> {

	private Text word = new Text();

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();

		StringTokenizer tokenizer = new StringTokenizer(line);
		String[] splittedLine = tokenizer.nextToken().split("#");
		String name = splittedLine[0];
		if (splittedLine.length>1){
			String wordText = splittedLine[1];
			String a=tokenizer.nextToken();

			word.set(name);

			context.write(word,new Text(wordText+": "+a));
		}

	}
}