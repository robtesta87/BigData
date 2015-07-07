package WordReview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class WordReview2Reducer extends Reducer<Text,Text,Text,Text> {
	
	public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
		String words="";
		ArrayList<Word> collectionWord = new ArrayList<Word>();
		
		for (Text val : values) {
			Word w = new Word();
			//words=words+" "+val.toString();
			String []ss=val.toString().split(": ");
			if (ss.length>1){
				String a= ss[0];

				w.setWord(a);
				Integer c = Integer.parseInt(val.toString().split(": ")[1]);
				w.setCont(c);
				collectionWord.add(w);
			}
		}
		Comparator<Word> comp;
		Collections.sort(collectionWord,comp= new Comparator<Word>() {

			@Override
			public int compare(Word o1, Word o2) {
				// TODO Auto-generated method stub
				return o2.getCont().compareTo(o1.getCont());
			}

		});
		collectionWord.sort(comp);
		for (int i = 0; ((i < collectionWord.size())&&(i<=10)); i++) {
			words = words+" "+collectionWord.get(i).getWord()+":"+collectionWord.get(i).getCont();
			//words = words+" "+"a:"+collectionWord.get(i).getCont();

		}
		context.write(key, new Text(words));
	}
	/*
    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
      String words="";
      for (Text val : values) {
        words=words+" "+val.toString();
      }
 
      context.write(key, new Text(words));
    }
	*/
}
