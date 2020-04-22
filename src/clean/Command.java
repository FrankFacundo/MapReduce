package clean;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


public class Command extends Thread
{
	private String cmd ;
	private String ssh ;
	private String cmd_ssh ;
	private String [] cmdByWords;
	private int numberOfPCs;
	
	public Command()
	{
		this.numberOfPCs = 0;
		this.cmd_ssh = "";
	}
	
	public int getnumberOfPCs()
	{
		return numberOfPCs;
	}
	
	public void setCommandLocal(String cmd) 
	{
		this.cmd = cmd;
		this.cmdByWords = cmd.split(" ");
	}
	
	public void setCommandSSH(String ssh, String cmd_ssh) 
	{
		this.ssh = ssh;
		this.cmd_ssh = cmd_ssh;
		this.cmd = "ssh " + this.ssh + this.cmd_ssh;
		this.cmdByWords = cmd.split(" ");
	}
	public void run() 
	{
		
		String response = "";
		//ProcessBuilder pb = new ProcessBuilder("ls", "-al", "/tmp");
		ProcessBuilder pb = new ProcessBuilder(cmdByWords);
		IO io = new IO(true);
		try {
			String line = "";
			pb.redirectErrorStream(true);
			//pb.redirectError();
			//pb.redirectOutput();
			//pb.redirectInput();
			//pb.inheritIO();
			
			
			
			if (this.cmd_ssh.equals(" hostname"))
			{
				Process p = pb.start();
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				boolean b = p.waitFor(5, TimeUnit.SECONDS);
				if (!b)
				{
					response += "TIMEOUT"; 
					p.destroy();
				}
				else {
			        while ((line = input.readLine()) != null) {
			        	response += line /* + "\n" */;
			        }
			        if (	response.startsWith("tp-")	) 
			        {
			        	io.write(this.ssh);
			        }
				}
			}
			else if (this.cmd_ssh.equals(""))
			{
				Process p = pb.start();
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				boolean b = p.waitFor(5, TimeUnit.SECONDS);
				if (!b)
				{
					response += "TIMEOUT"; 
					p.destroy();
				}
				else {
			        while ((line = input.readLine()) != null) {
			        	response += line /* + "\n" */;
			        }
			        //System.out.println(response);
				}
			}
			
			else if (this.cmd_ssh.equals(" mkdir /tmp/hadoop_frank"))
			{
				Process p = pb.start();
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				//getOutputStream pour eviter les bloquages
				p.waitFor();
				
				//this.setCommandSSH(this.ssh, " scp /home/frank/eclipse-workspace/Hadoop_MapReduce/src/slave/slave2sec.jar ");
				//pb.command(this.cmd);
				//p = pb.start();
				//input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				//p.waitFor();
				

		        while ((line = input.readLine()) != null) {
		        	response += line /* + "\n" */;
		        }
		        //System.out.println(response);
				
			}
			else 
			{
				Process p = pb.start();
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				//getOutputStream pour eviter les bloquages
				boolean b = p.waitFor(5, TimeUnit.SECONDS);
				if (!b)
				{
					response += "TIMEOUT"; 
					p.destroy();
				}
				else {
			        while ((line = input.readLine()) != null) {
			        	response += line /* + "\n" */;
			        }
			        //System.out.println(response);
				}
			}
			
			
			
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("IO Exception -> Error in Process Builder (Verify command)");
		} catch (InterruptedException e) {
			//e.printStackTrace();
			System.out.println("Interrupted Exception -> Error in Buffered Reader");
		}
		System.out.print(this.cmd + ": ");
		System.out.println("*******" + response + "*********");
		//return response;
		
	}
	
	
}
