package SocialBeer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.io.fs.FileUtils;

import test.prova3.RelationType;

public class ParseBeer {

	private static final String PATH_review10 = "/home/roberto/workspace/git/BigData/BigData/util/ratebeer10.txt";
	private static final String DB_PATH = "/home/roberto/neo4j-community-2.3.0-M02/data/graph.db";
	
	public enum RelationType implements RelationshipType{
		review;
	}

	public static void main(String[] args) throws IOException{
		
		System.out.println( "Starting database ..." );
		FileUtils.deleteRecursively( new File( DB_PATH ) );
		
		// START SNIPPET: startDb
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		// END SNIPPET: startDb
		
		FileReader f;
		f=new FileReader(PATH_review10);

		BufferedReader b;
		b=new BufferedReader(f);

		String s;

		int i = 0;
		int j =0;
		String[] splittedLine;
		 
		Beer beer = new Beer();
		ReviewBeer reviewBeer = new ReviewBeer();
		User user = new User();
		
		
		IndexDefinition indexDefinitionUser;
		IndexDefinition indexDefinitionBeer;
		try ( Transaction tx = graphDb.beginTx() )
		{
			Schema schema = graphDb.schema();
			indexDefinitionUser = schema.indexFor( DynamicLabel.label( "User" ) )
					.on( "username" )
					.create();
			indexDefinitionBeer = schema.indexFor( DynamicLabel.label( "Beer" ) )
					.on( "Name" )
					.create();
			tx.success();
		}
		// END SNIPPET: createIndex
		// START SNIPPET: wait
		try ( Transaction tx = graphDb.beginTx() )
		{
			Schema schema = graphDb.schema();
			schema.awaitIndexOnline( indexDefinitionUser, 10, TimeUnit.SECONDS );
			schema.awaitIndexOnline( indexDefinitionBeer, 10, TimeUnit.SECONDS );

		}
		// END SNIPPET: wait
		Node beerNode = null;
		Node userNode = null;
		
		ResourceIterator<Node> resultIterator = null;
		Map<String, Object> parameters = new HashMap<>();
		String queryString = "";
		
		while(j<10) {
			s=b.readLine();
			splittedLine = s.split(": ");
			if (splittedLine[0].equals("beer/name"))
				beer.setBeerName(splittedLine[1]);
			if (splittedLine[0].equals("beer/beerId"))
				beer.setBrewerId(splittedLine[1]);
			if (splittedLine[0].equals("beer/brewerId"))
				beer.setBrewerId(splittedLine[1]);
			if (splittedLine[0].equals("beer/ABV"))
				beer.setABV(splittedLine[1]);
			if (splittedLine[0].equals("beer/style"))
				beer.setStyle(splittedLine[1]);
			if (splittedLine[0].equals("review/profileName"))
				user.setUserName(splittedLine[1]);
			if (splittedLine[0].equals("review/appearance"))
				reviewBeer.setAppearance(Double.parseDouble(splittedLine[1].split("/")[0])*2);
			if (splittedLine[0].equals("review/aroma"))
				reviewBeer.setAroma(Double.parseDouble(splittedLine[1].split("/")[0]));
			if (splittedLine[0].equals("review/palate"))
				reviewBeer.setPalate(Double.parseDouble(splittedLine[1].split("/")[0])*2);
			if (splittedLine[0].equals("review/taste"))
				reviewBeer.setTaste(Double.parseDouble(splittedLine[1].split("/")[0]));
			if (splittedLine[0].equals("review/overall"))
				reviewBeer.setOverall(Double.parseDouble(splittedLine[1].split("/")[0])/2);
			if (splittedLine[0].equals("review/time")){
				long input = Long.parseLong(splittedLine[1]);
				Timestamp ts = new Timestamp(input*1000);
				reviewBeer.setTime(timestampToDate(ts).toString());
			}
				
			if (splittedLine[0].equals("review/text"))
				reviewBeer.setText(splittedLine[1]);
				
			System.out.println(s);
			i++;
			if (i==14){
				
				// START SNIPPET: createIndex
				
				
				resultIterator = null;
				try ( Transaction tx = graphDb.beginTx() )
				{
					Label labelUser = DynamicLabel.label( "User" );
					Label labelBeer = DynamicLabel.label( "Beer" );
					
					queryString = "MERGE (n:Beer {Name: {Name},ABV: {ABV},numberReview:{numberReview}}) RETURN n";
				    parameters.put( "Name", beer.getBeerName() );
				    parameters.put("ABV", beer.getABV());
				    parameters.put("numberReview", 0 );
				    resultIterator = graphDb.execute( queryString, parameters ).columnAs( "n" );
				    beerNode = resultIterator.next();

				    queryString = "MERGE (m:User {username: {username},numberReview:{numberReview}}) RETURN m";
					parameters.put( "username", user.getUserName() );
					parameters.put("numberReview", 0 );
					resultIterator = graphDb.execute( queryString, parameters ).columnAs( "m" );
				    userNode = resultIterator.next();

					//userNodes.get(0).setProperty("numberReview", (int)userNodes.get(0).getProperty("numberReview")+1);
					//beerNodes.get(0).setProperty("numberReview", (int)beerNodes.get(0).getProperty("numberReview")+1);
				
					Relationship relationship = userNode.createRelationshipTo(beerNode, RelationType.review);
					
					relationship.setProperty("appearance", reviewBeer.getAppearance());
					relationship.setProperty("aroma", reviewBeer.getAroma());
					relationship.setProperty("palate", reviewBeer.getPalate());
					relationship.setProperty("taste", reviewBeer.getTaste());
					relationship.setProperty("overall", reviewBeer.getOverall());
					relationship.setProperty("time", reviewBeer.getTime());
					relationship.setProperty("text", reviewBeer.getText());
					
				    tx.success();
				}
				
				j++;
				i=0;
			}
		}
		try ( Transaction tx = graphDb.beginTx() )
		{
		queryString = "MATCH (u1:User)-[r1:review]->(b:Beer)<-[r2:review]-(u2:User) WHERE (r1.overall>=8 AND r2.overall>=8)  CREATE (u1)-[:affinity]->(u2)";
		graphDb.execute( queryString, parameters );
		tx.success();
		}
		System.out.println( "Shutting down database ..." );
		// START SNIPPET: shutdownDb
		graphDb.shutdown();
		// END SNIPPET: shutdownDb
		
	}
	
	public static Date timestampToDate(Timestamp ts) {
		  try {
		        return new Date(ts.getTime());
		  } catch (Exception e) { return null; }
		 }

	
}