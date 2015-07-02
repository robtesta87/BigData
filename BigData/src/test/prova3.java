package test;

import java.io.File;
import java.io.IOException;
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

public class prova3 {
	
	private static final String DB_PATH = "/home/roberto/neo4j-community-2.3.0-M02/data/graph.db";
	
	public enum RelationType implements RelationshipType{
		drink;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println( "Starting database ..." );
		FileUtils.deleteRecursively( new File( DB_PATH ) );

		// START SNIPPET: startDb
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		// END SNIPPET: startDb

		{
			// START SNIPPET: createIndex
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
		}

		{
			// START SNIPPET: addUsers
			ResourceIterator<Node> resultIterator = null;
			try ( Transaction tx = graphDb.beginTx() )
			{
				Label labelUser = DynamicLabel.label( "User" );
				Label labelBeer = DynamicLabel.label( "Beer" );
				
				
				String queryString = "MERGE (m:User {username: {username}}) MERGE (n:Beer {Name: {Name},Gradi: {Gradi}}) MERGE (n)-[:drink]-(m)";
				Map<String, Object> parameters = new HashMap<>();
				parameters.put( "username", "roberto" );
				parameters.put( "Name", "Moretti" );
			    parameters.put("Gradi", "4,2");
				resultIterator = graphDb.execute( queryString, parameters ).columnAs( "m" );
				
				queryString = "MERGE (m:User {username: {username}}) MERGE (n:Beer {Name: {Name},Gradi: {Gradi}}) MERGE (n)-[:drink]-(m)";
				
				parameters.put( "username", "roberto2" );
				parameters.put( "Name", "Moretti" );
			    parameters.put("Gradi", "4,2");
				resultIterator = graphDb.execute( queryString, parameters ).columnAs( "m" );
				/*
				queryString = "MERGE (m:User {username: {username}}) RETURN m";
				parameters.put( "username", "roberto2" );
				resultIterator = graphDb.execute( queryString, parameters ).columnAs( "m" );

				queryString = "MERGE (n:Beer {Name: {Name},Gradi: {Gradi}}) RETURN n";
			    parameters.put( "Name", "Moretti" );
			    parameters.put("Gradi", "4,2");
			    resultIterator = graphDb.execute( queryString, parameters ).columnAs( "n" );
			    
			    queryString = "MERGE (n:Beer {Name: {Name},Gradi: {Gradi}}) RETURN n";
			    parameters.put( "Name", "Moretti" );
			    parameters.put("Gradi", "4,2");
			    resultIterator = graphDb.execute( queryString, parameters ).columnAs( "n" );
			    
			    queryString = "MERGE (n:Beer {Name: {Name},Gradi: {Gradi}})-[:drink]-(m:User {username: {username}}) RETURN n";
			    parameters.put( "Name", "Moretti" );
			    parameters.put("Gradi", "4,2");
			    parameters.put( "username", "roberto" );
			    resultIterator = graphDb.execute( queryString, parameters ).columnAs( "n" );*/
				/*
               // Create some users
               for ( int id = 0; id < 100; id++ )
               {
                   Node userNode = graphDb.createNode( label );
                   userNode.setProperty( "username", "user" + id + "@neo4j.org" );
               }*/
				/*System.out.println( "Users created" );
				ArrayList<Node> userNodes = new ArrayList<>();
				try ( ResourceIterator<Node> users = graphDb.findNodes( labelUser, "username", "roberto" ) ){
					
					while ( users.hasNext() ){
						userNodes.add( users.next() );
					}
					if (userNodes.size()!=0)
						System.out.println( "The username of user  is " + userNodes.get(0).getProperty("username") );
					else
						System.out.println("utente non trovato");
				}
				ArrayList<Node> beerNodes = new ArrayList<>();
				try ( ResourceIterator<Node> beers = graphDb.findNodes( labelBeer, "Name", "Moretti" ) ){
					
					while ( beers.hasNext() ){
						beerNodes.add( beers.next() );
					}
					if (beerNodes.size()!=0)
						System.out.println( "The beer is " + beerNodes.get(0).getProperty("Name") );
					else
						System.out.println("birra non trovata");
				}
				Relationship relationship = userNodes.get(0).createRelationshipTo(beerNodes.get(0), RelationType.drink);
				relationship.setProperty("review", "ciao");*/
				tx.success();
			}
			// END SNIPPET: addUsers
		}
/*
		{
			// START SNIPPET: findUsers
			Label label = DynamicLabel.label( "User" );
			String nameToFind = "roberto";
			try ( Transaction tx = graphDb.beginTx() )
			{
				try ( ResourceIterator<Node> users =
						graphDb.findNodes( label, "username", nameToFind ) )
						{
					ArrayList<Node> userNodes = new ArrayList<>();
					while ( users.hasNext() )
					{
						userNodes.add( users.next() );
					}
					if (userNodes.size()!=0)
						System.out.println( "The username of user  is " + userNodes.get(0).getProperty("username") );
					else
						System.out.println("utente non trovato");
						}
			}
			// END SNIPPET: findUsers
		}*/

		{
			// START SNIPPET: resourceIterator
			Label label = DynamicLabel.label( "User" );
			int idToFind = 45;
			String nameToFind = "user" + idToFind + "@neo4j.org";
			try ( Transaction tx = graphDb.beginTx();
					ResourceIterator<Node> users = graphDb.findNodes( label, "username", nameToFind ) )
					{
				Node firstUserNode;
				if ( users.hasNext() )
				{
					firstUserNode = users.next();
				}
				users.close();
					}
			// END SNIPPET: resourceIterator
		}





		{
			// START SNIPPET: dropIndex
			try ( Transaction tx = graphDb.beginTx() )
			{
				Label label = DynamicLabel.label( "User" );
				for ( IndexDefinition indexDefinition : graphDb.schema()
						.getIndexes( label ) )
				{
					// There is only one index
					indexDefinition.drop();
				}

				tx.success();
			}
			// END SNIPPET: dropIndex
		}

		System.out.println( "Shutting down database ..." );
		// START SNIPPET: shutdownDb
		graphDb.shutdown();
		// END SNIPPET: shutdownDb
	}
}
