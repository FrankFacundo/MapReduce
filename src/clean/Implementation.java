package clean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Implementation {
	
	public Implementation() {
		
	}
	
	public void getFunctional_PCs()
	{
		ArrayList<Command> threads = new ArrayList<Command>();
		try {
			Command delete = new Command();
			delete.setCommandLocal("rm Functional_PCs.txt");
			delete.start();
			Thread.sleep(500);
			
			Scanner sc = new Scanner(new File("listPCs.txt")); 
			
			while (sc.hasNext())
			{
				String str = sc.nextLine(); 
				Command command = new Command();
				command.setCommandSSH(str, " hostname");
				threads.add(command);
			} 
			sc.close(); 
			
			for (Thread thread : threads) {
			    thread.start();
			}
			for (Thread thread : threads) {
			    thread.join();
			}
			
		} catch (IOException e) {
			e.printStackTrace(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getNumberOfPCs()
	{
		int number = 0;
		try {
			
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				myReader.nextLine();
				number += 1;
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return number;
	}
	
	public void deployFile()
	{
		ArrayList<Command> threads = new ArrayList<Command>();
		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandSSH(PC, " mkdir -p /tmp/ffacundo/ ");
					//	+ "scp " + "/home/frank/eclipse-workspace/Hadoop_MapReduce/slave2secFacundo.jar " 
						//+ PC + ":/tmp/ffacundo/");
				//command.setCommandSSH(PC, " mkdir -p /tmp/ffacundo ; "
					//	+ "hostname");
				threads.add(command);
			}
			myReader.close(); 
			for (Thread thread : threads) {
			    thread.start();
			}
			for (Thread thread : threads) {
			    thread.join();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		threads.clear();

		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandLocal(
						 "scp " + "/home/frank/eclipse-workspace/Hadoop_MapReduce/slave2secFacundo.jar " 
						+ PC + ":/tmp/ffacundo/");
				threads.add(command);
			}
			myReader.close(); 
			for (Thread thread : threads) {
			    thread.start();
			}
			for (Thread thread : threads) {
			    thread.join();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void clean()
	{
		ArrayList<Command> threads = new ArrayList<Command>();
		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandSSH(PC, " rm -rf /tmp/ffacundo");
				threads.add(command);
			}
			myReader.close();
			for (Thread thread : threads) {
			    thread.start();
			}
			for (Thread thread : threads) {
			    thread.join();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	

}
