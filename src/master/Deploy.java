package master;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import deploy.Command;


public class Deploy {
	
	HashMap<String, String> UMx_Machines;
	ArrayList<String> splits;
	ArrayList<String> machines;
	ArrayList<String> machines_with_splits;
	
	public Deploy() {
		UMx_Machines = new HashMap<String, String>();
		splits = new ArrayList<String>();
		machines = new ArrayList<String>();
		machines_with_splits = new ArrayList<String>();
	}
	
	public void getFunctional_PCs()
	{
		System.out.println("####################################################");
		System.out.println("Getting functional PCs: ");
		System.out.println("####################################################");
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
		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandSSH(PC, " mkdir -p /tmp/ffacundo; "
						+ "scp" + "/home/frank/eclipse-workspace/Hadoop_MapReduce/slave2secFacundo.jar" 
						+ PC + ":/tmp/ffacundo");
				//System.out.print(str + " \n");
				command.start();
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void Clean()
	{
		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandSSH(PC, " rm -rf /tmp/ffacundo");
				command.start();
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void map()
	{
		System.out.println("####################################################");
		System.out.println("MAP");
		System.out.println("####################################################");
		ArrayList<Command> threads = new ArrayList<Command>();
		//System.out.print("thread");
		try {
			/**
			 * This execute all the functional PCs
			 */
			/*
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandSSH(PC, " java -jar /tmp/ffacundo/slave.jar ");
				threads.add(command);
				//command.start();
			}
			myReader.close();
			*/
			/**
			 * This execute just PCs where is a split
			 */

			UMx_Machines.entrySet().forEach(entry->{
			    //System.out.println(entry.getKey() + " : " + entry.getValue());
				Command command = new Command();
				command.setCommandSSH(entry.getValue(), " java -jar /tmp/ffacundo/slave.jar 0 /tmp/ffacundo/splits/" + entry.getKey() );
				threads.add(command);
			 });
			
			for (Thread thread : threads) {
				thread.start();
			}
			for (Thread thread : threads) {
			    thread.join();
			}
			System.out.println();
			System.out.println("####################################################");
			System.out.println("MAP FINISHED");
			System.out.println("####################################################");
		} catch ( InterruptedException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<String> getSplits()
	{
		//ArrayList<String> splits = new ArrayList<String>();
		
		File folder = new File("splits/");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    //System.out.println("File " + listOfFiles[i].getName());
		    splits.add(listOfFiles[i].getName());
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}
		
		return splits;
	}
	
	public ArrayList<String> getMachines()
	{
		//ArrayList<String> machines = new ArrayList<String>();
		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				machines.add(PC);
			}
			myReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return machines;
	}
	
	public HashMap<String, String> getSplitMachine (){
		ArrayList<String> splits = this.getSplits();
		ArrayList<String> machines = this.getMachines();
		//HashMap<String, String> UMx_Machines = new HashMap<String, String>();
		
		int len_UMx = splits.size();
		int len_machines = machines.size();
		for(int i=0; i<len_UMx; i++) {
			String fileName = splits.get(i);
			String machineName = machines.get(i%len_machines);
			//machines_with_splits.add(machineName);
			UMx_Machines.put(fileName, machineName);
		}
		
		return UMx_Machines;
	}
	
	/**
	 * UM : Unsorted Maps
	 * @return
	 * @throws IOException
	 */
	public void deploySplits() {
		System.out.println("####################################################");
		System.out.println("Deploy Splits: ");
		System.out.println("####################################################");
		splits = this.getSplits();
		machines = this.getMachines();
		//HashMap<String, String> UMx_Machines = new HashMap<String, String>();
		ArrayList<Command> threads = new ArrayList<Command>();
		
		int len_UMx = splits.size();
		int len_machines = machines.size();
		
		try {
			for(int i=0; i<len_UMx; i++) {
				String fileName = splits.get(i);
				String machineName = machines.get(i%len_machines);
				UMx_Machines.put(fileName, machineName);
				machines_with_splits.add(machineName);
				
				Command command = new Command();
				command.setCommandMultiple("ssh " +  machineName + " mkdir -p /tmp/ffacundo/splits/; "
						+ "scp splits/" + fileName + " " + machineName + ":/tmp/ffacundo/splits/");
				threads.add(command);

			}
			for (Thread thread : threads) {
			    thread.start();
			}
			for (Thread thread : threads) {
			    thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
	}
	
	public void createFolder(String pathfile)
	{
		ArrayList<Command> threads = new ArrayList<Command>();
		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandSSH(PC, " mkdir -p " + pathfile);
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
	
	
	public void printSplits() throws IOException {
		ArrayList<String> splits = this.getSplits();
		for(String s : splits){
			System.out.print(s + "\n");
		}
	}
	
	public void printMachines() throws IOException {
		ArrayList<String> machines = this.getMachines();
		
		for(String s : machines){
			System.out.print(s + "\n");
		}
		/**
		 * Another way to print :
		 * System.out.println("getMachines : " + machines.toString());
		 */
	}
	
	public void printSplitMachine() {
		System.out.println("####################################################");
		System.out.println("Split-Machine relation : ");
		System.out.println("####################################################");
		//HashMap<String, String> splitMachine = getSplitMachine ();
		UMx_Machines.entrySet().forEach(entry->{
		    System.out.println(entry.getKey() + " : " + entry.getValue());  
		 });
	}

	
	public void deployFunctionalPCs()
	{
		System.out.println("####################################################");
		System.out.println("Deploy Functional PCs: ");
		System.out.println("####################################################");
		
		String pathfile = "/home/frank/eclipse-workspace/Hadoop_MapReduce/Functional_PCs.txt";
		ArrayList<Command> threads = new ArrayList<Command>();

		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandMultiple("ssh " +  PC + " mkdir -p /tmp/ffacundo/; "
						+ "scp " + pathfile + " " 
						+ PC + ":/tmp/ffacundo/machines.txt");
				/*command.setCommandLocal(
						 "scp " + pathfile + " " 
						+ PC + ":/tmp/ffacundo/");*/
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
	
	public void shuffle()
	{
		System.out.println("####################################################");
		System.out.println("Shuffle: ");
		System.out.println("####################################################");
		ArrayList<Command> threads = new ArrayList<Command>();
		//System.out.print("thread");
		try {
		
			/**
			 * This execute just PCs where is a split
			 */

			UMx_Machines.entrySet().forEach(entry->{
				Command command = new Command();
				String UMx = "UM" + entry.getKey().substring(1,entry.getKey().length() - 4);
				command.setCommandSSH(entry.getValue(), " java -jar /tmp/ffacundo/slave.jar 1 " + UMx );
				threads.add(command);
			 });
			
			for (Thread thread : threads) {
				thread.start();
			}
			for (Thread thread : threads) {
			    thread.join();
			}
			System.out.println();
			System.out.println("####################################################");
			System.out.println("SHUFFLE FINISHED");
			System.out.println("####################################################");
		} catch ( InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public void reduce() {
		System.out.println("####################################################");
		System.out.println("REDUCE");
		System.out.println("####################################################");
		ArrayList<Command> threads = new ArrayList<Command>();
		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandSSH(PC, " java -jar /tmp/ffacundo/slave.jar 2");
				threads.add(command);
			}
			myReader.close();
			
			for (Thread thread : threads) {
				thread.start();
			}
			for (Thread thread : threads) {
			    thread.join();
			}
			System.out.println();
			System.out.println("####################################################");
			System.out.println("REDUCE FINISHED");
			System.out.println("####################################################");
		} catch ( InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
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
	
	public void deployFile(String pathfile)
	{
		System.out.println("####################################################");
		System.out.println("Deploying slave");
		System.out.println("####################################################");
		ArrayList<Command> threads = new ArrayList<Command>();
		try {
			String PC;
			File myObj = new File("Functional_PCs.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				PC = myReader.nextLine();
				Command command = new Command();
				command.setCommandSSH(PC, " mkdir -p /tmp/ffacundo/splits ");
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
						 "scp " + pathfile + " " 
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
	
}
