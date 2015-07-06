package WordReview;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class WordReview2Reducer extends Reducer<Text,Text,Text,Text> {

	
    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
      String words="";
      for (Text val : values) {
        words=words+" "+val.toString();
      }
 
      context.write(key, new Text(words));
    }
	
}
