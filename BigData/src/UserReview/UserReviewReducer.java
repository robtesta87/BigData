package UserReview;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class UserReviewReducer  extends Reducer<Text,Text,Text,Text> {

	private DoubleWritable result = new DoubleWritable();

    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
      int numberReview = 0;
      int numberCharText =0;
      for (Text val : values) {
         numberReview +=Integer.parseInt(val.toString().split(" ")[0]);
         numberCharText +=Integer.parseInt(val.toString().split(" ")[1]);
      }
      
      String a = ("number Review: "+ new Integer(numberReview).toString())+" number Char Text"+(new Integer(numberCharText).toString());
      
      
      context.write(key, new Text(a));
    }

}
