package SocialBeer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
public class queries {
	private static final String DB_PATH = "neo4j-community-2.2.3/data/graph.db";

	public static void main(String[] args) throws IOException {
		System.out.println( "Starting database ..." );
		FileWriter f = new FileWriter("tempo di esecuzione query.txt");	
		Date start = new Date();
		String username = "hopdog";
		String beer = "BarleyIslandSinisterMinisterBelgianBlackAle";
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		queries q = new queries();
		//q.getAll(graphDb);
		q.getReviews(graphDb, username);
		//q.getSuggestionBeers(graphDb, username);
		//q.getSuggestionBeers(graphDb, username,beer);
		//q.getTextReviews(graphDb, beer);
		 Date end = new Date();
			f.write("Tempo di esecuzione query in ms: "+(end.getTime()-start.getTime()));
			FileWriter f2 = new FileWriter("Tempo di esecuzione in ms: "+(end.getTime()-start.getTime())+".txt");
			f.close();
		graphDb.shutdown();
	}
	
	
	public void getAll(GraphDatabaseService graphDb) throws IOException {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine.execute("MATCH (u1:User)-[r:review]->(b:Beer) RETURN u1,b,r");
		//String results = execResult.dumpToString();
		//System.out.println(results);
		PrintWriter out=null;
		out = new PrintWriter(new BufferedWriter(new FileWriter("util/All.txt", true)));
		//out.println(results);
		execResult.writeAsStringTo(out);
		out.close();
		System.out.println("GET ALL OK");
	}
	public void getReviews(GraphDatabaseService graphDb,String username) throws IOException {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine.execute("MATCH (u1:User)-[r:review]->(b:Beer) WHERE u1.username='"+username+"' RETURN r");
		//String results = execResult.dumpToString();
		//System.out.println(results);
		PrintWriter out=null;
		out = new PrintWriter(new BufferedWriter(new FileWriter("util/All.txt", true)));
		//out.println(results);
		execResult.writeAsStringTo(out);
		out.close();
		System.out.println("GET Review OK");
	}
	
	public void getSuggestionBeers(GraphDatabaseService graphDb,String username) throws IOException {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine.execute("MATCH (u1:User)-[r:review]->(b:Beer)<-[r2:review]-(u2:User)-[r3:review]->(b2:Beer) WHERE (u1.username='"+username+"')AND(r.overall>16)AND(r2.overall>16)AND(r3.overall>16)AND(u2.qualityReview>0.2) RETURN b2");
		//String results = execResult.dumpToString();
		//System.out.println(results);
		PrintWriter out=null;
		out = new PrintWriter(new BufferedWriter(new FileWriter("util/SuggestionBeer.txt", true)));
		//out.println(results);
		execResult.writeAsStringTo(out);
		out.close();
		System.out.println("GET SuggestionBeer OK");
	}
	public void getSuggestionBeers(GraphDatabaseService graphDb,String username, String beer) throws IOException {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine.execute("MATCH (u1:User)-[r:review]->(b:Beer)<-[r2:review]-(u2:User)-[r3:review]->(b2:Beer) WHERE (u1.username='"+username+"')AND(b.Name='"+beer+"')AND(r2.overall>16)AND(r3.overall>16)AND(u2.qualityReview>0.1) RETURN b2");
		//String results = execResult.dumpToString();
		//System.out.println(results);
		PrintWriter out=null;
		out = new PrintWriter(new BufferedWriter(new FileWriter("util/SuggestionBeer.txt", true)));
		//out.println(results);
		execResult.writeAsStringTo(out);
		out.close();
		System.out.println("GET Suggestion Beer OK");
	}
	
	public void getTextReviews(GraphDatabaseService graphDb,String beerName) throws IOException {
		ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		ExecutionResult execResult = execEngine.execute("MATCH (User)-[r:review]->(b:Beer) WHERE b.Name='"+beerName+"' RETURN r");
		//String results = execResult.dumpToString();
		//System.out.println(results);
		PrintWriter out=null;
		out = new PrintWriter(new BufferedWriter(new FileWriter("util/TextReview.txt", true)));
		//out.println(results);
		execResult.writeAsStringTo(out);
		out.close();
		System.out.println("GET Text review OK");
	}
	
}
