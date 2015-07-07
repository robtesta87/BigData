package MaxText;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

import com.sun.jersey.server.impl.model.parameter.multivalued.ExtractorContainerException;

public class MaxTextMapper extends Mapper<Object, Text,Text,Text> {

	private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
    	String line = value.toString();
		int lengthText = lengthReview(line);

		word.set("maxText");
		context.write(new Text(""+lengthText),word);
    	
    }
    
    private static int lengthReview(String line)throws IOException{
		int length=0;
		String text = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("lengthText")){
				text = splittedLine[j+1];
				length= Integer.parseInt((text.substring(1, text.length()-2)));
				return length;
			}
			//System.out.println(j+":"+split[j]);
		}

		return length;
	}
	/*private static int lengthReview(String line)throws IOException{
		int length=0;
		String text = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("text")){
				text = splittedLine[j+2];
				length= text.length();
				return length;
			}
			//System.out.println(j+":"+split[j]);
		}

		return length;
	}*/

}
