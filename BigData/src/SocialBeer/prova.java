package SocialBeer;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.Node;

public class prova {

	public enum NodeType implements Label{
		Beer,User;
	}

	public enum RelationType implements RelationshipType{
		drink;
	}

	public static void main(String[] args) {
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService db= dbFactory.newEmbeddedDatabase("/home/roberto/neo4j-community-2.3.0-M02/data/graph.db");
		
		try (Transaction tx = db.beginTx()) {
			db.schema().constraintFor(NodeType.Beer).assertPropertyIsUnique("Name").create();
			tx.success();
		}
		
		Node result = null;
		ResourceIterator<Node> resultIterator = null;
		try ( Transaction tx = db.beginTx() )
		{
		    String queryString = "MERGE (n:Beer {Name: {Name}}) RETURN n";
		    Map<String, Object> parameters = new HashMap<>();
		    parameters.put( "Name", "Moretti" );
		    resultIterator = db.execute( queryString, parameters ).columnAs( "n" );
		    
		    parameters.put( "Name", "Moretti" );
		    resultIterator = db.execute( queryString, parameters ).columnAs( "n" );

		    
		    parameters.put( "Name", "peroni" );
		    resultIterator = db.execute( queryString, parameters ).columnAs( "n" );

		    
		    result = resultIterator.next();
		    tx.success();
		}
		/*
		try (Transaction tx = db.beginTx()) {
			Node BeerNode = db.createNode(NodeType.Beer);
			BeerNode.setProperty("Name", "peroni");
			BeerNode.setProperty("gradi", "5.2");

			Node BeerNode2 = db.createNode(NodeType.Beer);
			BeerNode2.setProperty("Name", "moretti");
			BeerNode2.setProperty("gradi", "6.2");
			
			
			Node UserNode = db.createNode(NodeType.User);
			UserNode.setProperty("nome", "roberto");

			Node UserNode2 = db.createNode(NodeType.User);
			UserNode2.setProperty("nome", "pippo");

			UserNode.createRelationshipTo(BeerNode, RelationType.drink);
			UserNode.createRelationshipTo(BeerNode2, RelationType.drink);
			UserNode2.createRelationshipTo(BeerNode2, RelationType.drink);

			// Perform DB operations	
			tx.success();
		}	*/
		db.shutdown();
		System.out.println("Done successfully");
	}
}
