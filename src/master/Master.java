package master;

/**
 * @author Frank Enrique FACUNDO RAIME
 */

public class Master {

	public static void main(String[] args) {
		/**
		 * WRITE BIG FILE NAME TO MAP-REDUCE
		 * This file have to be located the directory "bigfile"
		 */
		String filename = "bigfile1.txt";
		
		/**
		 * Get Functional PCs
		 */
		Deploy deploy = new Deploy();
		deploy.getFunctional_PCs();
		System.out.println("####################################################");
		System.out.println("Number of fonctional PCs : " + deploy.getNumberOfPCs());
		System.out.println("####################################################");
		
		/**
		 * Clean
		 */
		deploy.clean();
		
		/**
		 * Deploy Functional PCs
		 */
		deploy.deployFunctionalPCs();
		
		/**
		 * Deploy Slave
		 */
		String pathfile = "/home/frank/eclipse-workspace/Hadoop_MapReduce/slave.jar";
		deploy.deployFile(pathfile);
		
		/**
		 * Split
		 */
		int numberOfSplits = 30;
		System.out.println("####################################################");
		System.out.println("Generating splits");
		System.out.println("####################################################");
		long startTime = System.currentTimeMillis();
		Split.split(filename, numberOfSplits);
		long endTime  = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total time: " + totalTime + " ms\n");
		
		/**
		 * Deploy Split
		 */
		deploy.deploySplits();
		deploy.printSplitMachine();
		
		/**
		 * MAP
		 */
		startTime = System.currentTimeMillis();
		deploy.map();
		endTime  = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Total time: " + totalTime + " ms\n");
		//End map
		

		/**
		 * SHUFFLE
		 */
		startTime = System.currentTimeMillis();
		deploy.shuffle();	
		endTime  = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Total time: " + totalTime + " ms\n");
		//End shuffle
		/**
		 * REDUCE
		 */
		startTime = System.currentTimeMillis();
		deploy.reduce();
		endTime  = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Total time: " + totalTime + " ms\n");
		//End reduce
		
		
		/**
		 * Etape 5.3 (Old etape)
		 */
		/*
		//String cmd = "java -jar /tmp/ffacundo/slave2secFacundo.jar";		
		String cmd = "java -jar /home/frank/eclipse-workspace/Hadoop_MapReduce/slave2sec.jar";
		Command command = new Command();
		command.setCommandLocal(cmd);
		command.start();
		*/
		/*
		String cmd = "ssh ffacundo@tp-1a222-03 java -jar /tmp/ffacundo/slave2secFacundo.jar";
		Command command = new Command();
		command.setCommandLocal(cmd);
		command.run();
		*/
		
	}

}
