package hadoop;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Frank Enrique FACUNDO RAIME
 */

public class Hadoop {

	public static void main(String[] args) {
		
		String file = "Files/CC-MAIN-20170322212949-00140-ip-10-233-31-227.ec2.internal.warc.wet";
		//String file = "Files/sante_publique.txt";
		
		/**
		 * This part of code tests all the directory.
		 */
		/*
		File folder = new File("Files");
		File[] listOfFiles = folder.listFiles();
		for(File fileText : listOfFiles) {
			
			IO io = new IO(false);
			long startTime = System.currentTimeMillis();
			io.read(fileText.getPath());
			//io.printDictionary();
			//io.print("******************read\n");
			
			//io.printTriDictionary(5);
			
			long endTime  = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			io.print("Total time : " + totalTime + "ms\n");
			//io.printTotalWords();
			
		}
		*/
		
		
		IO io = new IO(false);
		long startTime = System.currentTimeMillis();
		io.read(file);
		
		//io.printDictionary();
		io.print("******************read\n");
		io.printTriDictionary(20);
		
		long endTime  = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		io.print("Total time : " + totalTime + "ms");
		io.printTotalWords();
		
	}

}
