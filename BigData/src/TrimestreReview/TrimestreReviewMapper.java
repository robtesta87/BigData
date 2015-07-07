package TrimestreReview;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class TrimestreReviewMapper extends Mapper<Object, Text, Text, IntWritable> {
	private final static IntWritable one = new IntWritable(1);
	private final static IntWritable two = new IntWritable(2);
	private final static IntWritable three = new IntWritable(3);
	private final static IntWritable four = new IntWritable(4);

	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		if (line.contains("Node[")){
			int month = extractMonth(line);
			String nameBeer = extractBeerName(line);

			int trimestre = 0;
			if ((month==1)||(month==2)||(month==3))
				trimestre=1;
			if ((month==4)||(month==5)||(month==6))
				trimestre=2;
			if ((month==7)||(month==8)||(month==9))
				trimestre=3;
			if ((month==10)||(month==11)||(month==12))
				trimestre=4;
			word.set(nameBeer);
			if (trimestre==1)
				context.write(word, one);
			if (trimestre==2)
				context.write(word, two);
			if (trimestre==3)
				context.write(word, three);
			if (trimestre==4)
				context.write(word, four);
		}

	}

	private static int extractMonth(String line) throws IOException{
		int month = Integer.parseInt(line.split("time")[1].split("\"")[1].substring(5, 7));

		/*int i = 0;
		String month = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("time")){
				month = splittedLine[j+2].substring(5, 7);
				return Integer.parseInt(month);
			}
			//System.out.println(j+":"+split[j]);
		}*/

		return (month);
	}

	private static String extractBeerName(String line) throws IOException{
		String beerName =line.split("Name")[1].split("\"")[1];

		/*int i = 0;
		String beerName = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("Name")){
				beerName = splittedLine[j+2];
				return beerName;
			}
			//System.out.println(j+":"+split[j]);
		}*/

		return beerName;
	}

}
