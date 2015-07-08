package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.tartarus.snowball.ext.PorterStemmer;

public class stemm {
	public static void main(String[] args) throws IOException {
		//String s="On tap at the Springfield, PA location. Poured a deep and cloudy orange (almost a copper) color with a small sized off white head. Aromas or oranges and all around citric. Tastes of oranges, light caramel and a very light grapefruit finish. I too would not believe the 80+ IBUs - I found this one to have a very light bitterness with a medium sweetness to it. Light lacing left on the glass.";
		String s = "Handbottled from trade wth Sprinkle. Pours a nice dark copper color with medium size off white head. Aroma of bourbon, malt , hops and oak. Slight smokey flavor with a bourbon taste in the initial sip. Flavors of malt, vanilla and hops still remain although none dominate the brew. Taste is still very enjoyable with a smooth and balanced finish.";
		String finalString = "";
		FileReader f;
		f=new FileReader("util/stop-word-list.txt");
		BufferedReader b;
		String line = "";
		b=new BufferedReader(f);
		ArrayList<String> stopList = new ArrayList<String>();
		while ((line = b.readLine())!=null){
			stopList.add(line);
		}
		System.out.println(s.replaceAll("[ \t\n,\\.\"!?$~()\\[\\]\\{\\}:;/\\\\<>+=%*]", " "));
		s=s.replaceAll("[ \t\n,\\.\"!?$~()\\[\\]\\{\\}:;/\\\\<>+=%*]", " ");
		StringTokenizer itr = new StringTokenizer(s);
		while (itr.hasMoreTokens()) {
			PorterStemmer stemmer = new PorterStemmer();
			String token =itr.nextToken().split(" ")[0];
			boolean stop =false;
			if (stopList.contains(token.toLowerCase()))
				stop=true;
			
			//int i=0;
			//while (i<stopList.size()&&(stop==false)){
			//	if (token.toLowerCase().equals(stopList.))
			//		stop = true;
			//	i++;
				//System.out.println(line);
				//if (token.contains("."))
				//token=token.split(".")[0];

			//}
			if (stop==false){
				stemmer.setCurrent(token); //set string you need to stem
				stemmer.stem();  //stem the word
				//System.out.println(stemmer.getCurrent());//get the stemmed word
				finalString=finalString+" "+stemmer.getCurrent();
			}
			
		}
		System.out.println(finalString);
		b.close();
		f.close();
		/*StringTokenizer itr = new StringTokenizer(s);
		while (itr.hasMoreTokens()) {
			PorterStemmer stemmer = new PorterStemmer();
			String token =itr.nextToken();
			//if (token.contains("."))
				//token=token.split(".")[0];
			stemmer.setCurrent(token); //set string you need to stem
			stemmer.stem();  //stem the word
			System.out.println(stemmer.getCurrent());//get the stemmed word


		}*/
		/*
		InputStream is = new FileInputStream("util/en-token.bin");

		TokenizerModel model = new TokenizerModel(is);

		Tokenizer tokenizer = new TokenizerME(model);

		String tokens[] = tokenizer.tokenize(s);

		for (String a : tokens)
			System.out.println(a);

		is.close();
			}*/

	}
}
