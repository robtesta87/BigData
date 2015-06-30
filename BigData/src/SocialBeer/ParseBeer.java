package SocialBeer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.io.fs.FileUtils;

import SocialBeer.prova3.RelationType;

public class ParseBeer {

	private static final String PATH_review10 = "/home/roberto/workspace/git/BigData/BigData/util/ratebeer10.txt";
	private static final String DB_PATH = "/home/roberto/neo4j-community-2.3.0-M02/data/graph.db";

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
		
		ResourceIterator<Node> resultIterator = null;
		Map<String, Object> parameters = new HashMap<>();
		String queryString = "";
		
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
			System.out.println(s);
			i++;
			if (i==14){
				
				// START SNIPPET: createIndex
				
				
				resultIterator = null;
				try ( Transaction tx = graphDb.beginTx() )
				{
					Label labelUser = DynamicLabel.label( "User" );
					Label labelBeer = DynamicLabel.label( "Beer" );
					
					queryString = "MERGE (n:Beer {Name: {Name},Gradi: {Gradi}}) RETURN n";
				    parameters.put( "Name", beer.getBeerName() );
				    parameters.put("Gradi", beer.getABV());
				    resultIterator = graphDb.execute( queryString, parameters ).columnAs( "n" );
				    
				    queryString = "MERGE (m:User {username: {username}}) RETURN m";
					parameters.put( "username", user.getUserName() );
					resultIterator = graphDb.execute( queryString, parameters ).columnAs( "m" );
					
					ArrayList<Node> userNodes = new ArrayList<>();
					try ( ResourceIterator<Node> users = graphDb.findNodes( labelUser, "username", user.getUserName() ) ){
						
						while ( users.hasNext() ){
							userNodes.add( users.next() );
						}
						if (userNodes.size()!=0)
							System.out.println( "The username of user  is " + userNodes.get(0).getProperty("username") );
						else
							System.out.println("utente non trovato");
					}
					
					ArrayList<Node> beerNodes = new ArrayList<>();
					try ( ResourceIterator<Node> beers = graphDb.findNodes( labelBeer, "Name", beer.getBeerName() ) ){
						
						while ( beers.hasNext() ){
							beerNodes.add( beers.next() );
						}
						if (beerNodes.size()!=0)
							System.out.println( "The beer is " + beerNodes.get(0).getProperty("Name") );
						else
							System.out.println("birra non trovata");
					}
					
					Relationship relationship = userNodes.get(0).createRelationshipTo(beerNodes.get(0), RelationType.drink);
					relationship.setProperty("review", "ciao");
				    
				    tx.success();
				}
				
				j++;
				i=0;
			}
		}

	}
	
}
