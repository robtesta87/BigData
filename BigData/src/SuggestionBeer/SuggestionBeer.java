package SuggestionBeer;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class SuggestionBeer {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		//FileWriter f = new FileWriter("tempo di esecuzione MapReduce es 1f.txt");	
		//Date start = new Date();
		
		/*ath input = new Path(args[0]);
	    Path temp1 = new Path("temp");
	    Path output = new Path(args[1]);*/
	    
	    Configuration conf = new Configuration();
	    
	    Job job1 = Job.getInstance(conf, "BestBeer");
	    job1.setJarByClass(SuggestionBeer.class);
	    
	    job1.setMapperClass(SuggestionBeerMapper.class);
	    //job.setCombinerClass(NumberPieciesSoldReducer.class);
	    job1.setReducerClass(SuggestionBeerReducer.class);
	    //job1.setNumReduceTasks(1);
	    job1.setOutputKeyClass(Text.class);
	    job1.setOutputValueClass(IntWritable.class);
	    FileInputFormat.addInputPath(job1, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job1, new Path(args[1]));
	    job1.waitForCompletion(true);
	    
	    

		//Date end = new Date();
		//f.write("Tempo di esecuzione in ms: "+(end.getTime()-start.getTime()));
		//f.close();
	}
}
