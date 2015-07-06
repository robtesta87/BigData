package TrimestreReview;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class TrimestreReview {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
	FileWriter f = new FileWriter("tempo di esecuzione MapReduce es 2.txt");	
	Date start = new Date();
	
	Configuration conf = new Configuration();
			
    Job job = Job.getInstance(conf, "Trimestre");
    job.setJarByClass(TrimestreReview.class);
    
    job.setMapperClass(TrimestreReviewMapper.class);
    //job.setCombinerClass(TrimestreReducer.class);
    job.setReducerClass(TrimestreReviewReducer.class);
    
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    
    job.waitForCompletion(true);
    Date end = new Date();
	f.write("Tempo di esecuzione es.2(Trimestre) in ms: "+(end.getTime()-start.getTime()));
	f.close();
	
}
}
