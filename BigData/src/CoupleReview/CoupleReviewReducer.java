package CoupleReview;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class CoupleReviewReducer extends Reducer<Text,Text,Text,Text> {

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
		String beers="";		
		for (Text val : values) {
			beers+=val.toString()+",";
		}
		word.set(beers);
		context.write(new Text(""),word);


	}


}
