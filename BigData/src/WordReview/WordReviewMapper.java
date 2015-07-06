package WordReview;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.tartarus.snowball.ext.PorterStemmer;

public class WordReviewMapper extends Mapper<Object, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
    	String textReview = extractTextReview(line);
    	//String cleanText = cleanText(textReview);
    	String beerName = extractBeerName(line);
    	
    	String[] wordsReview = textReview.split(" ");
    	//String[] wordsReview = textReview.split(" ");
    	for (int i = 0; i < wordsReview.length; i++) {
    		word.set(beerName+"#"+wordsReview[i]);
    		context.write(word, one);
		}
    	
    	
    }
    
    private static String extractTextReview(String line)throws IOException{
		String text = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("text")){
				text = splittedLine[j+2];
				return text;
			}
			//System.out.println(j+":"+split[j]);
		}

		return text;
	}
    
    private static String extractBeerName(String line) throws IOException{

		int i = 0;
		String beerName = "";
		String[] splittedLine = line.split("\"\"");
		for (int j = 0; j < splittedLine.length; j++) {
			if (splittedLine[j].equals("Name")){
				beerName = splittedLine[j+2];
				return beerName;
			}
			//System.out.println(j+":"+split[j]);
		}

		return beerName;
	}
    /*
    private static String cleanText(String text) throws IOException{
    	FileReader f=new FileReader("util/stop-word-list.txt");
		BufferedReader b=new BufferedReader(f);
		String line = "";
		String cleanText = "";
		ArrayList<String> stopList = new ArrayList<String>();
		while ((line = b.readLine())!=null){
			stopList.add(line);
		}
		
		StringTokenizer itr = new StringTokenizer(text);
		while (itr.hasMoreTokens()) {
			PorterStemmer stemmer = new PorterStemmer();
			String token =itr.nextToken().split("[ \t\n,\\.\"!?$~()\\[\\]\\{\\}:;/\\\\<>+=%*]")[0];
			boolean stop =false;
			if (stopList.contains(token.toLowerCase()))
				stop=true;
			
			if (stop==false){
				stemmer.setCurrent(token); //set string you need to stem
				stemmer.stem();  //stem the word
				//System.out.println(stemmer.getCurrent());//get the stemmed word
				cleanText=cleanText+" "+stemmer.getCurrent();
			}
			
		}
		
		//System.out.println(cleanText);
		b.close();
		f.close();
		return cleanText;
    }*/
	
}
