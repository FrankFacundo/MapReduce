package master;

import java.io.*;

public class Split {
	public Split(){
		
	}
	public static void split(String filename, int numberOfSplits) {
	    try {
	    	
	    	ProcessBuilder pb = new ProcessBuilder();
	    	pb.command("mkdir", "-p" , "splits");
	    	try {
	    		pb.start();
	    	} catch (IOException e1) {
	    		e1.printStackTrace();
	    	}
	    	
	      File file = new File("bigfile/" + filename);
		  //best way to be performant
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
	      //InputStream is = new FileInputStream(file);
	      FileWriter out = null;

	      long avaragePartitionSize = file.length() / numberOfSplits;
	      long lastSplit = 0;
	      long readerCounter = 0;

	      int i;
	      int c_in;
	      for (i = 0; i < numberOfSplits; i++)
	      {
	        out = new FileWriter("splits/S" + i + ".txt");

	        c_in = bis.read();
	        readerCounter++;
	        while (c_in != -1 && ( (readerCounter < avaragePartitionSize + lastSplit) ||
	                                (c_in != '\n' && c_in != ' ') ))
	        {
	          out.write((char) c_in);
	          c_in = bis.read();
	          readerCounter++;
	        }
	        out.close();
	        lastSplit = readerCounter;
	      }
	      bis.close();
	    } catch (Exception e) {e.printStackTrace();}
	}
	
}
