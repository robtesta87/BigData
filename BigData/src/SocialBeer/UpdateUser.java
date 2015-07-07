package SocialBeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

public class UpdateUser {
	private static String userReview_path = "util/UserReview.txt";
	private static String maxText_path = "util/MaxText.txt";
	private static final String DB_PATH = "/home/roberto/neo4j-community-2.2.3/data/graph.db";

	
	public static void main(String[] args) throws IOException {
		
		int maxLengthReview = getMaxLengthReview(maxText_path);
		System.out.println("maxLength: "+maxLengthReview);
		

		System.out.println( "Starting database ..." );
		
		
		// START SNIPPET: startDb
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		
		FileReader f=new FileReader(userReview_path);
		BufferedReader b;
		b=new BufferedReader(f);
		String line="";
		String username = "";
		int numberReview = 0;
		int totCharText = 0;
		double qualityReview = 0;
		while ((line=b.readLine())!=null){
			username = line.split("\t")[0];
			System.out.println(username);
			numberReview = Integer.parseInt((line.split("\t")[1].split(": ")[1].split(" ")[0]));
			System.out.println("number review: "+numberReview);
			totCharText = Integer.parseInt((line.split("\t")[1].split(": ")[2]));
			System.out.println("tot char: "+totCharText);
			
			qualityReview = (double)(totCharText/numberReview)*(1/(double)maxLengthReview);
					
			System.out.println(qualityReview);
			try ( Transaction tx = graphDb.beginTx() ){
				Label labelUser = DynamicLabel.label( "User" );
				
				ArrayList<Node> userNodes = new ArrayList<>();
				try ( ResourceIterator<Node> users = graphDb.findNodes( labelUser, "username", username ) ){
					
					while ( users.hasNext() ){
						userNodes.add( users.next() );
					}
					if (userNodes.size()!=0)
						System.out.println( "The username of user  is " + userNodes.get(0).getProperty("username") );
					else
						System.out.println("utente non trovato");
				}
				
				userNodes.get(0).setProperty("qualityReview", qualityReview );
				userNodes.get(0).setProperty("numberReview", numberReview );
			
				tx.success();
			}
			
			
			
		}
		System.out.println( "Shutting down database ..." );
		// START SNIPPET: shutdownDb
		graphDb.shutdown();
		// END SNIPPET: shutdownDb
	}
	
	private static int getMaxLengthReview (String path) throws IOException{
		int maxLengthReview = 0;
		FileReader f;
		try {
			f=new FileReader(path);
			BufferedReader b;
			b=new BufferedReader(f);
			String s = b.readLine();
			maxLengthReview= Integer.parseInt(s.split("\t")[1]);
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return maxLengthReview;
	}
}
