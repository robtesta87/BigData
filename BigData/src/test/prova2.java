package test;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.index.UniqueFactory;

public class prova2 {
	public enum NodeType implements Label{
		Beer,User;
	}

	public enum RelationType implements RelationshipType{
		drink;
	}
	
	public static void main(String[] args) {
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		GraphDatabaseService db= dbFactory.newEmbeddedDatabase("/home/roberto/neo4j-community-2.3.0-M02/data/graph.db");
		
		Node result = null;
		ResourceIterator<Node> resultIterator = null;
		try ( Transaction tx = db.beginTx() )
		{
			IndexManager index = db.index();
		    Index<Node> beers = index.forNodes("Beer");
		    boolean indexExists = index.existsForNodes( "Beer" );
		    System.out.println(indexExists);
		    IndexHits<Node> hits = beers.get("Name", "Moretti");
		    System.out.println(hits.size());
		    //Node beer = hits.getSingle();
		    //System.out.println(beer.getProperty("Name"));
		}	
		try ( Transaction tx = db.beginTx() )
		{
			String queryString = "MERGE (n:Beer {Name: {Name},Gradi: {Gradi}}) RETURN n";
		    Map<String, Object> parameters = new HashMap<>();
		    parameters.put( "Name", "Moretti" );
		    parameters.put("Gradi", "4,2");
		    resultIterator = db.execute( queryString, parameters ).columnAs( "n" );
		    
		    queryString = "MERGE (m:User {NameUser: {NameUser}}) RETURN m";
		    parameters.put( "NameUser", "roberto" );
		    resultIterator = db.execute( queryString, parameters ).columnAs( "m" );
		    		    
		    tx.success();
		}
		
			
		
		db.shutdown();
		System.out.println("Done successfully");
	}
}
