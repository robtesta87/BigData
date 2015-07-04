package BestBeerMR;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class BestBeer {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		//FileWriter f = new FileWriter("tempo di esecuzione MapReduce es 1f.txt");	
		//Date start = new Date();
		
		/*ath input = new Path(args[0]);
	    Path temp1 = new Path("temp");
	    Path output = new Path(args[1]);*/
	    
	    Configuration conf = new Configuration();
	    
	    Job job1 = Job.getInstance(conf, "BestBeer");
	    job1.setJarByClass(BestBeer.class);
	    
	    job1.setMapperClass(BestBeerMapper.class);
	    //job.setCombinerClass(NumberPieciesSoldReducer.class);
	    job1.setReducerClass(BestBeerReducer.class);
	    //job1.setNumReduceTasks(1);
	    job1.setOutputKeyClass(Text.class);
	    job1.setOutputValueClass(IntWritable.class);
	    FileInputFormat.addInputPath(job1, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job1, new Path(args[1]));
	    job1.waitForCompletion(true);
	    
	    
/*
		Job job2 = new Job(new Configuration(), "pickorderbyvalue");
        job2.setJarByClass(Coppie.class);
        job2.setMapperClass(OrderByValueMapper.class);
    	job2.setSortComparatorClass(SortIntComparator.class);
    	job2.setReducerClass(OrderByValueReducer.class);
        
        job2.setOutputKeyClass(IntWritable.class);
        job2.setOutputValueClass(Text.class);
        
    	FileInputFormat.addInputPath(job2, new Path(args[1]));
		FileOutputFormat.setOutputPath(job2, new Path(args[2]));
		if(	job1.waitForCompletion(true))
			job2.waitForCompletion(true);
		*/
		//Date end = new Date();
		//f.write("Tempo di esecuzione in ms: "+(end.getTime()-start.getTime()));
		//f.close();
	}
}
