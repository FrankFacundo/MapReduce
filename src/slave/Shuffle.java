package slave;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Shuffle {
	
	public Shuffle(){
		
	}

  static int hashFunction(String s)
  {
    return (s.hashCode() == Integer.MIN_VALUE) ? 0 : Math.abs(s.hashCode());
  }
	
  static String[] getMachinesArray()
  {
    List<String> list = null;
    try{
      File file = new File("/tmp/ffacundo/machines.txt");
      BufferedReader br = new BufferedReader(new FileReader(file));

      String str;
      list = new ArrayList<String>();

      while ((str = br.readLine()) != null)
      {
        list.add(str);
      }
      br.close();
    } catch(Exception e) {e.printStackTrace(); }
    return list.toArray(new String[0]);
  }
  
  static void sendFile(String file, String dest)
  {
    try {
    	//int i = 1;
	while (true){

	
	String[] command = {"bash", "-c", "ssh "+ dest + " mkdir -p /tmp/ffacundo/shufflesreceived/; " 
			+"scp /tmp/ffacundo/shuffles/" + file + ".txt " + dest + ":/tmp/ffacundo/shufflesreceived/" + file + ".txt ; echo file_sended"};
	ProcessBuilder pb = new ProcessBuilder(command);
	pb.redirectErrorStream(true);
	Process p = pb.start();
	p.waitFor(30, TimeUnit.SECONDS);
	
	

	BufferedReader reader = new BufferedReader(new InputStreamReader (p.getInputStream() ));
	if (reader.ready() == false)
	{
	    System.out.println ("TIMEOUT, Error sending file to " + dest +"\n");
	    return;
	}
	String outs = reader.readLine();

	if(outs.equals("file_sended"))
	  return;
	}

    } catch (Exception e) {e.printStackTrace(); }
  }
	
  static void prepareShuffle(String maps)
  {
	ProcessBuilder pb = new ProcessBuilder();
	pb.command("mkdir", "-p" , "/tmp/ffacundo/shuffles");
	try {
		pb.start();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	
	ProcessBuilder pb2 = new ProcessBuilder();
	pb2.command("mkdir", "-p" , "/tmp/ffacundo/shufflesreceived");
	try {
		pb2.start();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
    
	  
	try{
      File file = new File("/tmp/ffacundo/maps/" + maps + ".txt");
      BufferedReader br = new BufferedReader(new FileReader(file));

      String str;
      String[] splited = null;
      FileWriter fw = null;
      int hash;

      String[] machines = getMachinesArray();
      print("####################################\n");
      print(java.net.InetAddress.getLocalHost().getHostName() + " sending files:\n");
      print("####################################\n");
      //int i =0;
      while ((str = br.readLine()) != null)
      {
        splited = str.split(" ");
        hash = hashFunction(splited[0]);
        fw = new FileWriter("/tmp/ffacundo/shuffles/" + Integer.toString(hash) + "-" +
          java.net.InetAddress.getLocalHost().getHostName() + ".txt", true);
        fw.write(splited[0] + " 1\n");
        fw.close();
        
        /*
        
        sendFile(Integer.toString(hash) + "-" +
          java.net.InetAddress.getLocalHost().getHostName(),
          machines[hash % machines.length]);
          */
      }
      
		File folder = new File("/tmp/ffacundo/shuffles/");
		File[] listOfFiles = folder.listFiles();
		
		ArrayList<SendFile> threads = new ArrayList<SendFile>();
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {

			  hash = Integer.parseInt(listOfFiles[i].getName().split("-")[0]);
			  String filename = listOfFiles[i].getName().substring(0,listOfFiles[i].getName().length()-4);
			  String destination = machines[hash % machines.length];
				print("loop " + i + ": ");
				print("file = ");
				print(filename);
				print(" dest = ");
				print(destination);
				print("\n");
				//i=i+1;
				
				
				//////////////////////////////
				// TODO CORREGIR SENDFILE EN THREADS
				SendFile sendfile = new SendFile(filename, destination);
				threads.add(sendfile);
			  //sendFile(filename,destination);
				////////////////////////////////////////
				
				
				
				
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}
		
		//////////////////////////////
		for (Thread thread : threads) {
		    thread.start();
		}
		for (Thread thread : threads) {
		    thread.join();
		}
		////////////////////////////////
		
      
      print("####################################\n");
      print(java.net.InetAddress.getLocalHost().getHostName() + " SHUFFLE FINISHED\n");
      print("####################################\n");
      br.close();
    } catch(Exception e) {e.printStackTrace(); }
	
  }
  
	public static void print(String text)
	{
		System.out.print(text);
	}
}


