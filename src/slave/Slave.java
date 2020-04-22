package slave;

/**
 * @author Frank Enrique FACUNDO RAIME
 */

public class Slave {
	
	public static void main(String[] args) {
		
		String mode = args[0];
		String arg1;
		if (mode.equals("2")) {
			arg1 = "";
		}
		else {
			arg1 = args[1]; 
		}
		
		//String mode = "2";
		//String arg1 = "/tmp/ffacundo/splits/S2.txt"; //mode=0
		//String arg1 = "UM0"; //mode=1
				
		if(mode.equals("0")){
			/*
			int a,b,res;
			a=3; b=5; res=a+b;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();}
			
			System.out.println(res);
			*/
			/**
			 * Counter : read split and write a file in map directory
			 */
			
			Count count = new Count(arg1);
			count.read();
			count.write();
			


		}
		else if (mode.equals("1")) {
			
			Shuffle.prepareShuffle(arg1);
		}
		
		else if (mode.equals("2")) {
			
			Reduce.reduce();
		}
		
		
		else System.out.println("Mode error!");
	}
	

	


}
