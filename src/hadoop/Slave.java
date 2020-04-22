package hadoop;

public class Slave {
	public static void main(String[] args) {
		//String mode = args[0];
		String mode = "0";
		if(mode.equals("0")){
			int a,b,res;
			a=3;
			b=5;
			res=a+b;
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(res);
		}
		else System.out.println("Mode error!");

	}
}
