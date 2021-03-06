package SocialBeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
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
import org.tartarus.snowball.ext.PorterStemmer;

public class ParseBeer {

	private static final String PATH_review10 = "util/ratebeer.txt";
	private static final String DB_PATH = "/home/roberto/neo4j-community-2.2.3/data/graph.db";
	//private static final String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";

	public enum RelationType implements RelationshipType{
		review;
	}

	public static void main(String[] args) throws IOException{

		System.out.println( "Starting database ..." );
		FileUtils.deleteRecursively( new File( DB_PATH ) );

		FileWriter fw = new FileWriter("tempo di esecuzione creazione db.txt");	
		java.util.Date start = new java.util.Date();

		// START SNIPPET: startDb
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
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
		String textReview = "";
		int k =0;
		while(j<10) {
			s=b.readLine();
			//while ((s=b.readLine())!=null){


			splittedLine = s.split(": ");
			if (splittedLine[0].equals("beer/name"))
				beer.setBeerName(splittedLine[1]);
			if (splittedLine[0].equals("beer/beerId"))
				beer.setBeerId(splittedLine[1]);
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
				reviewBeer.setOverall(Double.parseDouble(splittedLine[1].split("/")[0]));
			if (splittedLine[0].equals("review/time")){
				long input = Long.parseLong(splittedLine[1]);
				Timestamp ts = new Timestamp(input*1000);
				reviewBeer.setTime(timestampToDate(ts).toString());
			}
			if (splittedLine[0].equals("review/text")){
				String[]text=splittedLine;
				if (text.length>1){
					textReview = text[1];
					reviewBeer.setLengthText(textReview.length());
					if (!(textReview.equals("")))
						reviewBeer.setText(cleanText(textReview));
					else
						reviewBeer.setText("");
				}
			}
			//System.out.println(s);
			i++;
			if (i==14){

				// START SNIPPET: createIndex


				resultIterator = null;
				try ( Transaction tx = graphDb.beginTx() )
				{
					Label labelUser = DynamicLabel.label( "User" );
					Label labelBeer = DynamicLabel.label( "Beer" );

					queryString = "MERGE (n:Beer {Name: {Name},ABV: {ABV},numberReview:{numberReview},beerID:{beerID}}) RETURN n";
					parameters.put( "Name", beer.getBeerName() );
					parameters.put("ABV", beer.getABV());
					parameters.put("numberReview", 0 );
					parameters.put("beerID", beer.getBeerId());
					resultIterator = graphDb.execute( queryString, parameters ).columnAs( "n" );
					beerNode = resultIterator.next();
					/*
					queryString = "MERGE (m:User {username: {username},numberReview:{numberReview}}) RETURN m";
					parameters.put( "username", user.getUserName() );
					parameters.put("numberReview", 0 );
					resultIterator = graphDb.execute( queryString, parameters ).columnAs( "m" );
					userNode = resultIterator.next();
					Relationship relationship = userNode.createRelationshipTo(beerNode, RelationType.review);
					*/
					
					ArrayList<Node> userNodes = new ArrayList<>();
					Relationship relationship ;
					try ( ResourceIterator<Node> users = graphDb.findNodes( labelUser, "username", user.getUserName() ) ){
						
						while ( users.hasNext() ){
							userNodes.add( users.next() );
						}
						if (userNodes.size()!=0){
							System.out.println( "The username of user  is " + userNodes.get(0).getProperty("username") );
							relationship = userNodes.get(0).createRelationshipTo(beerNode, RelationType.review);
						}
						else{
							System.out.println("utente non trovato");
							queryString = "MERGE (m:User {username: {username},numberReview:{numberReview},userID:{userID}}) RETURN m";
							parameters.put( "username", user.getUserName() );
							parameters.put("numberReview", 0 );
							parameters.put("userID", user.getUserName().hashCode() );
							resultIterator = graphDb.execute( queryString, parameters ).columnAs( "m" );
							userNode = resultIterator.next();
							relationship = userNode.createRelationshipTo(beerNode, RelationType.review);
						}
					}
					
					
					

					relationship.setProperty("appearance", reviewBeer.getAppearance());
					relationship.setProperty("aroma", reviewBeer.getAroma());
					relationship.setProperty("palate", reviewBeer.getPalate());
					relationship.setProperty("taste", reviewBeer.getTaste());
					relationship.setProperty("overall", reviewBeer.getOverall());
					relationship.setProperty("time", reviewBeer.getTime());
					relationship.setProperty("text", reviewBeer.getText());
					relationship.setProperty("lengthText", reviewBeer.getLengthText());
					tx.success();
				}

				j++;
				System.out.println(j);
				i=0;
			}
		}
		
		
		System.out.println( "Shutting down database ..." );
		// START SNIPPET: shutdownDb
		graphDb.shutdown();
		// END SNIPPET: shutdownDb

		java.util.Date end = new java.util.Date();
		fw.write("Tempo di esecuzione creazione db in ms: "+(end.getTime()-start.getTime()));
		fw.close();

	}


	public static Date timestampToDate(Timestamp ts) {
		try {
			return new Date(ts.getTime());
		} catch (Exception e) { return null; }
	}

	private static String cleanText(String text) throws IOException{
		FileReader f=new FileReader("util/stop-word-list.txt");
		BufferedReader b=new BufferedReader(f);
		String line = "";
		String cleanText = "";
		ArrayList<String> stopList = new ArrayList<String>();
		while ((line = b.readLine())!=null){
			stopList.add(line);
		}
		text = text.toLowerCase();
		text = text.replaceAll("[ \t\n,\\.\"!?$~()\\[\\]\\{\\}:;/\\\\<>+=%*]", " ");
		StringTokenizer itr = new StringTokenizer(text);
		while (itr.hasMoreTokens()) {
			PorterStemmer stemmer = new PorterStemmer();
			String x = itr.nextToken();
			
			String[]a=x.split(" ");
			String token="";
			if (a.length>0)
				token=a[0];
			else {
				System.out.println(x);
				System.out.println(text);
			}
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


	}


}