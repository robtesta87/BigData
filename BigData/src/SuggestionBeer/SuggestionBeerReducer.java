package SuggestionBeer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;



public class SuggestionBeerReducer extends Reducer<Text,IntWritable,Text,DoubleWritable>{
	private static final int TOP_K = 10;
	 private class Pair {
	      public String str;
	      public Double count;
	      
	      public Pair(String str, Double count) {
	        this.str = str;
	        this.count = count;
	      }
	 };
	 
	    private PriorityQueue<Pair> queue;
	    private PriorityQueue<Pair> queue2;
	    @Override
	    protected void setup(Context ctx) {
	      queue = new PriorityQueue<Pair>(TOP_K, new Comparator<Pair>() {

			@Override
			 public int compare(Pair p1, Pair p2) {
	          return p1.count.compareTo(p2.count);
	        }
	      });
	      queue2 = new PriorityQueue<Pair>(TOP_K, new Comparator<Pair>() {

			@Override
			public int compare(Pair p1, Pair p2) {
		     return p1.count.compareTo(p2.count);
		    }
		      });
	    }
	    
	    @Override
	    protected void reduce(Text key, Iterable<IntWritable> values, 
	        Context ctx) throws IOException, InterruptedException {
	      double count = 0;
	      for (IntWritable value : values) {
	        count = count + value.get();
	      }
	      Boolean contains= key.toString().contains(",");
	      if (contains)
	    	  queue.add(new Pair(key.toString(), count));
	      else 
	    	  queue2.add(new Pair(key.toString(), count));
	      if (queue.size() > TOP_K) {
	        queue.remove();
	      }
	    }
	    
	    @Override
	    protected void cleanup(Context ctx) throws IOException, InterruptedException {
	      List<Pair> topKPairs = new ArrayList<Pair>();
	      while (!queue.isEmpty()) {
	        topKPairs.add(queue.remove());
	      }
	      List<Pair> topKPairs2 = new ArrayList<Pair>();
	      while (!queue2.isEmpty()) {
	        topKPairs2.add(queue2.remove());
	      }
	      double avg=0;
	      for (int i = topKPairs2.size() - 1; i >= 0; i--) {
			String el1 = topKPairs2.get(i).str;
	    	  for (int j =topKPairs.size() - 1; j >=0; j--) {
	    		  if (el1.equals(topKPairs.get(j).str.split(",")[0])){
	    			  //ctx.write(new Text(topKPairs.get(j).str.split(",")[0]), new DoubleWritable(topKPairs.get(j).count));
	    			  //avg=topKPairs.get(j).count/topKPairs2.get(i).count;
	    			  avg=topKPairs.get(j).count/topKPairs2.get(i).count;
	    		  }
	    				  
	    	  }
	    	  ctx.write(new Text(el1), new DoubleWritable(avg));
	      }
	      
	      /*int tot=0;
	      for (int i = topKPairs.size() - 1; i >= 0; i--) {
	    	  
	    	  
	        Pair topKPair = topKPairs.get(i);
	        String el1 = topKPair.str.split(",")[1];
	        
	        for (int j =topKPairs2.size() - 1; j >=0; j--) {
	        	Pair topKPair2 = topKPairs2.get(j);
	        	if (el1.equals(topKPair2.str))
	        		tot=topKPair2.count;
			}
	        Double a = new Double(tot).doubleValue();
	        Double b = new Double(topKPair.count).doubleValue();
	        
	        Float result = (float) (b/a);
	        int c =Math.round(result);
	        //ctx.write(new Text(topKPair.str), new Text(""+topKPair.count+"/"+tot));
	        //ctx.write(new Text(topKPair.str), new FloatWritable(result));
	        ctx.write(new Text(topKPair.str), new IntWritable(c));
	      }*/
	    }
}
