package clean;

public class Clean {

	public static void main(String[] args) {
		Implementation deploy = new Implementation();
		deploy.getFunctional_PCs();
		System.out.println("####################################################");
		System.out.println("Number of fonctional PCs : " + deploy.getNumberOfPCs());
		System.out.println("####################################################");
		System.out.println();
		
		deploy.clean();
		System.out.println();
		System.out.println("####################################################");
		System.out.println("CLEANED");
		System.out.println("####################################################");
	}
}
