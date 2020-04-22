package slave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Reduce {
	public Reduce(){
	}
	
	static void reduce() {
		ProcessBuilder pb = new ProcessBuilder();
		pb.command("mkdir", "-p" , "/tmp/ffacundo/reduces");
		try {
			Process p = pb.start();
			p.waitFor(2, TimeUnit.SECONDS);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		File folder = new File("/tmp/ffacundo/shufflesreceived/");
		File[] listOfFiles = folder.listFiles();
		FileWriter fw;
		//System.out.println(listOfFiles[1].getAbsolutePath());
		int numberLines = 0;
		String line ;
		try {
			// This can cause problem if the shuffle file is empty
			String key = null;
			int i = 0 ;
			for(File file : listOfFiles) {
				i++;
				System.out.println("#loop " + i + " ");
				if (file.isFile()) {
					numberLines = 0;
					// We can optimize the counting using a call of command "wc -l {{file}}"
					BufferedReader br = new BufferedReader(new FileReader(file));
					if((line = br.readLine()) != null) {
						numberLines++;
						key = line;
					}
					while((line = br.readLine()) != null) {
						numberLines++;
					}
					br.close();
					String hash = file.getName().split("-")[0];
					System.out.println("Hash: " + hash + " ");
					
					File result = new File("/tmp/ffacundo/reduces/" + hash +".txt");
					if(result.isFile()) {
						
						BufferedReader br2 = new BufferedReader(new FileReader(result));
						String alreadyLines = br2.readLine().split(" ")[1];
						System.out.println("Already lines : " + alreadyLines + " ");
						numberLines = numberLines + Integer.parseInt(alreadyLines);
						br2.close();
					}
					
					fw = new FileWriter("/tmp/ffacundo/reduces/" + hash +".txt");
					
					System.out.println("Result: " +key.split(" ")[0] + " " + Integer.toString(numberLines));
					fw.write(key.split(" ")[0] + " " + Integer.toString(numberLines));
					fw.close();
					 
				}
				else if(file.isDirectory()) {
				    System.out.println("WARNING: This is a directory " + file.getName());
				}
			}
		} catch (Exception e) {
		}
		
		
	}
	
}
