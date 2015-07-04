package TopReview;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class TopReviewReducer extends Reducer<Text,Text,Text,DoubleWritable> {

	private DoubleWritable result = new DoubleWritable();

    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
      int sum = 0;
      double rev =0;
      for (Text val : values) {
         sum +=Integer.parseInt(val.toString().split(" ")[0]);
         rev +=Double.parseDouble(val.toString().split(" ")[1]);
      }
      double avg = rev/sum;
      String a = (new Integer(sum).toString())+(new Double(avg).toString());
      double b = Double.parseDouble(a);
      result.set(b);
      context.write(key, result);
    }
}
