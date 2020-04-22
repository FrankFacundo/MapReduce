package deploy;

public class Deploy {

	public static void main(String[] args) {
		String pathfile = "/home/frank/eclipse-workspace/Hadoop_MapReduce/slave.jar";
		
		/**
		 * Etape 7.1
		 */
		Implementation deploy = new Implementation();
		deploy.getFunctional_PCs();
		System.out.println("####################################################");
		System.out.println("Number of fonctional PCs : " + deploy.getNumberOfPCs());
		System.out.println("####################################################");
		System.out.println();
		System.out.println();
		System.out.println();
		
		deploy.deployFile(pathfile);

		System.out.println();
		System.out.println();
		System.out.println("####################################################");
		System.out.println("DEPLOYED");
		System.out.println("####################################################");
		
	}

}
