package MaxText;

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


public class MaxText {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		FileWriter f = new FileWriter("tempo di esecuzione MapReduce es 1_2.txt");	
		Date start = new Date();
		
		Configuration conf = new Configuration();
		
		
	    Job job1 = Job.getInstance(conf, "Max Text");
	    job1.setJarByClass(MaxText.class);
	    
	    job1.setMapperClass(MaxTextMapper.class);
	    //job1.setCombinerClass(NumberPieciesSoldReducer.class);
	    job1.setReducerClass(MaxTextReducer.class);
	    //job1.setNumReduceTasks(1);
	    job1.setOutputKeyClass(Text.class);
	    job1.setOutputValueClass(Text.class);
	    FileInputFormat.addInputPath(job1, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job1, new Path(args[1]));
	    job1.waitForCompletion(true);
	    
	    
	    Date end = new Date();
		f.write("Tempo di esecuzione es.1.2 in ms: "+(end.getTime()-start.getTime()));
		FileWriter f2 = new FileWriter("Tempo di esecuzione in ms: "+(end.getTime()-start.getTime())+".txt");
		
		f.close();

		
	}
}

