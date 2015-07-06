package TrimestreReview;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TrimestreReviewReducer extends Reducer<Text,IntWritable,Text,Text> {
	//private IntWritable result = new IntWritable();
	private Text result = new Text();
    public void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
      int sum1 = 0;
      int sum2 = 0;
      int sum3 = 0;
      int sum4 = 0;
      for (IntWritable val : values) {
    	  if (val.get()==1)
    		  sum1 += val.get();
    	  if (val.get()==2)
    		  sum2 += 1;
    	  if (val.get()==3)
    		  sum3 += 1;
    	  if (val.get()==4)
    		  sum4 += 1;
      }
      //Text totPrimoSemetre = (Text)sum1;
      String s= "Trimestre1:"+sum1+" Trimestre2:"+sum2+" Trimestre2:"+sum3+" Trimestre4:"+sum4;
      result.set(s);
      context.write(key, result);
    }
}
