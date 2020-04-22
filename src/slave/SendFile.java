package slave;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class SendFile extends Thread{
	
	private String file;
	private String destination;
	public SendFile(String file, String dest) {
		this.file = file;
		this.destination = dest;
	}
	
	public void run() {
	    try {
		while (true){

		
		String[] command = {"bash", "-c", "ssh "+ this.destination + " mkdir -p /tmp/ffacundo/shufflesreceived/; " 
				+"scp /tmp/ffacundo/shuffles/" + file + ".txt " + this.destination + ":/tmp/ffacundo/shufflesreceived/" + file + ".txt ; echo file_sended"};
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		Process p = pb.start();
		p.waitFor(30, TimeUnit.SECONDS);
		
		

		BufferedReader reader = new BufferedReader(new InputStreamReader (p.getInputStream() ));
		if (reader.ready() == false)
		{
		    System.out.println ("TIMEOUT, Error sending file to " + this.destination +"\n");
		    return;
		}
		String outs = reader.readLine();

		if(outs.equals("file_sended"))
		  return;
		}

	    } catch (Exception e) {e.printStackTrace(); }
	}
}
