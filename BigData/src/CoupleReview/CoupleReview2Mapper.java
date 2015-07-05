package CoupleReview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class CoupleReview2Mapper extends Mapper<Object, Text, Text, IntWritable>{
	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] ws = line.split(",");
		String el_coppia="";
		String coppia="";
		if (ws.length>1){
			for (int i = 0; i < ws.length; i++) {
				el_coppia=ws[i];
				for (int j = i+1; j < ws.length; j++) {
					if (!ws[j].equals("")){
						ArrayList<String> prodotti = new ArrayList<String>();
						prodotti.add(ws[i]);
						prodotti.add(ws[j]);
						Collections.sort(prodotti);
						coppia=prodotti.get(0)+","+prodotti.get(1);

						/*if (el_coppia.compareToIgnoreCase(ws[j])<0)
							coppia = el_coppia+","+ws[j];
						else
							coppia = ws[j]+","+el_coppia;*/
					}
					word.set(coppia);
					context.write(word, one);
				}
			}
		}

	}
}
