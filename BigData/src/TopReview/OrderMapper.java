package TopReview;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class OrderMapper extends
Mapper<LongWritable, Text, Text, DoubleWritable> {

	private static final DoubleWritable frequency = new DoubleWritable();
	private Text word = new Text();

	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();

		StringTokenizer tokenizer = new StringTokenizer(line);
		String name = tokenizer.nextToken();
		double a=Double.parseDouble(tokenizer.nextToken());
		frequency.set(a);
		word.set(name);
		context.write(word,frequency);


	}
}