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



public class ParseBeerThread extends Thread{
	
	GraphDatabaseService graphDb;
	BufferedReader b;

	
	
	//costruttore
	public ParseBeerThread(GraphDatabaseService graphDb,BufferedReader b){
		this.graphDb=graphDb;
		this.b=b;
	}
	public enum RelationType implements RelationshipType{
		review;
	}
	public void run(){
		String s="";

		int i = 0;
		int j =0;
		String[] splittedLine;

		Beer beer = new Beer();
		ReviewBeer reviewBeer = new ReviewBeer();
		User user = new User();


		
		
		// END SNIPPET: wait
		Node beerNode = null;
		Node userNode = null;

		ResourceIterator<Node> resultIterator = null;
		Map<String, Object> parameters = new HashMap<>();
		String queryString = "";
		String textReview = "";
		int k =0;
		while(j<500000) {
			try {
				s=b.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//while ((s=b.readLine())!=null){


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
						try {
							reviewBeer.setText(cleanText(textReview));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
					relationship.setProperty("lengthText", reviewBeer.getLengthText());
					tx.success();
				}

				j++;
				System.out.println(j);
				i=0;
			}
		}
		//fine run
	}
	//jkhlggkgkhgkhjgkhkghgkhghkghkj
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
	
	

	public static void main(String[] args) throws IOException {
		String DB_PATH = "util/neo4j-community-2.2.3/data/graph.db";
		String PATH_review500000 = "util/ratebeer500000.txt";
		String PATH_review500000t01000000 = "util/ratebeer500000to1000000.txt";
		String PATH_review1000000to1500000 = "util/ratebeer1000000to1500000.txt";
		String PATH_review1500000to2000000 = "util/ratebeer1500000to2000000.txt";
		String PATH_review2000000to2500000 = "util/ratebeer2000000to2500000.txt";
		String PATH_review2500000to3000000 = "util/ratebeer2500000to3000000.txt";

		
		System.out.println( "Starting database ..." );
		FileUtils.deleteRecursively( new File( DB_PATH ) );

		FileWriter fw = new FileWriter("tempo di esecuzione creazione db.txt");	
		java.util.Date start = new java.util.Date();

		// START SNIPPET: startDb
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		// END SNIPPET: startDb
		
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
		
		FileReader f1;
		f1=new FileReader(PATH_review500000);

		BufferedReader b1;
		b1=new BufferedReader(f1);
		
		FileReader f2;
		f2=new FileReader(PATH_review500000t01000000);

		BufferedReader b2;
		b2=new BufferedReader(f2);
		
		FileReader f3;
		f3=new FileReader(PATH_review1000000to1500000);

		BufferedReader b3;
		b3=new BufferedReader(f3);
		
		FileReader f4;
		f4=new FileReader(PATH_review1500000to2000000);

		BufferedReader b4;
		b4=new BufferedReader(f4);
		
		FileReader f5;
		f5=new FileReader(PATH_review2000000to2500000);

		BufferedReader b5;
		b5=new BufferedReader(f5);
		
		FileReader f6;
		f6=new FileReader(PATH_review2500000to3000000);

		BufferedReader b6;
		b6=new BufferedReader(f6);
		
		ParseBeerThread p1= new ParseBeerThread(graphDb, b1);
		ParseBeerThread p2= new ParseBeerThread(graphDb, b2);
		ParseBeerThread p3= new ParseBeerThread(graphDb, b3);
		ParseBeerThread p4= new ParseBeerThread(graphDb, b4);
		ParseBeerThread p5= new ParseBeerThread(graphDb, b5);
		ParseBeerThread p6= new ParseBeerThread(graphDb, b6);
		
		p1.start();
		p2.start();
		p3.start();
		p4.start();
		p5.start();
		p6.start();
		//p4.start();
		java.util.Date end = new java.util.Date();
		fw.write("Tempo di esecuzione creazione db in ms: "+(end.getTime()-start.getTime()));
		fw.close();
		//graphDb.shutdown();
	}
	
	

	
}
