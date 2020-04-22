package slave;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Count {
	//private HashMap<String, Integer> NumberWords;
	private String filename;
	private String output;
	String text_one_per_word = "";
	
	public Count(String filename) {
		this.filename = filename;
		this.output = "UM" + filename.substring(22, filename.length() - 4) + ".txt";
		//this.NumberWords = new HashMap<>();
		
	}
	
	
	public void read()
	{
		try {
			File split = new File(filename);
			Scanner myReader = new Scanner(split);
			print(split.getAbsolutePath()+"\n");
			long startTime = System.currentTimeMillis();
			while (myReader.hasNext()) {
				String tmp = myReader.next();
				//NumberWords.merge(tmp, 1, Integer::sum);
				text_one_per_word += tmp + " 1\n";

			}
			long endTime  = System.currentTimeMillis();
			long read = endTime - startTime;
			print("\n**************************************** Time for read : " + read + " ms\n");
			myReader.close();
		}catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public void write()
	{
		/*
		String text = "";
		
		for (Entry<String, Integer> entry : NumberWords.entrySet()) {
		    //System.out.println(entry.getKey() + " " + Integer.toString(entry.getValue()));
		    text += entry.getKey() + " " + Integer.toString(entry.getValue()) + "\n";
		}
		*/

//		print(test);

		ProcessBuilder pb = new ProcessBuilder();
		pb.command("mkdir", "-p" , "/tmp/ffacundo/maps");
		try {
			pb.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try(FileWriter fw = new FileWriter("/tmp/ffacundo/maps/" + this.output, false);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
				print(text_one_per_word);
				//out.print(text_one_per_word + "\n");
				out.print(text_one_per_word );
			    
			} catch (IOException e) {
			}
			
	}
	
	public static void print(String text)
	{
		System.out.print(text);
	}
	
}
