package CoupleReview;

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



public class CoupleReview {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		FileWriter f = new FileWriter("tempo di esecuzione MapReduce es1_1.txt");	
		Date start = new Date();
		
		Configuration conf = new Configuration();
				
	    Job job1 = Job.getInstance(conf, "Couple Review");
	    job1.setJarByClass(CoupleReview.class);
	    
	    job1.setMapperClass(CoupleReviewMapper.class);
	    //job.setCombinerClass(NumberPieciesSoldcer.class);
	    job1.setReducerClass(CoupleReviewReducer.class);
	    
	    job1.setOutputKeyClass(Text.class);
	    job1.setOutputValueClass(Text.class);
	    FileInputFormat.addInputPath(job1, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job1, new Path(args[1]));
	    job1.waitForCompletion(true);
	    
	    
		Job job2 = new Job(new Configuration(), "couple review");
        job2.setJarByClass(CoupleReview.class);
        job2.setMapperClass(CoupleReview2Mapper.class);
    	job2.setReducerClass(Reduce1Reducer.class);
        
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);
        
    	FileInputFormat.addInputPath(job2, new Path(args[1]));
		FileOutputFormat.setOutputPath(job2, new Path(args[2]));
		if(	job1.waitForCompletion(true))
			job2.waitForCompletion(true);
		
		Date end = new Date();
		f.write("Tempo di esecuzione es 1.1 in ms: "+(end.getTime()-start.getTime()));
		f.close();
	}
}
