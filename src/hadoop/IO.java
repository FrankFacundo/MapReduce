package hadoop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.SortedSet;



public class IO {
	
	
	private boolean used = false;
	private String nameFile = "out.txt";
	private boolean print;
	LocalDateTime DateTime = LocalDateTime.now();
	private HashMap<String, Integer> NumberWords;
	//private SortedMap<String, Integer> NumberWordsOrdered;
	private SortedSet<Map.Entry<String,Integer>> NumberWordsOrdered;
	//LinkedHashMap<String, String> sortedByValue
	long numberOfWords;
	
	
	
	public IO (boolean print)
	{
		//"false" is for console and "true" is for file 
		this.print = print;
		NumberWords = new HashMap<>();
        
		NumberWordsOrdered = new TreeSet<Map.Entry<String,Integer>>(
		        new Comparator<Map.Entry<String,Integer>>()
		        {
		            @Override 
		            public int compare(Map.Entry<String,Integer> e1, Map.Entry<String,Integer> e2) 
		            {
		                int number = e1.getValue().compareTo(e2.getValue());
		                //int key = e1.getKey().compareTo(e2.getKey());
		                return number != 0 ? number : (e1.getKey().compareTo(e2.getKey()));
		                //return number != 0 ? key : number;
		            }
		        }
		    );
	}
	
	public Stream< Entry<String, Integer> > sort(){
		return NumberWords.entrySet()
					.stream()
					.sorted(HashMap.Entry.comparingByKey())
					.sorted(HashMap.Entry.comparingByValue(Comparator.reverseOrder()));
	}
	
	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public HashMap<String, Integer> getNumberWords() {
		return NumberWords;
	}

	public void setNumberWords(HashMap<String, Integer> numberWords) {
		NumberWords = numberWords;
	}

	public synchronized void read(String filename)
	{
		
		try {
			//print(filename +"\n");
			File myObj = new File(filename);
			Scanner myReader = new Scanner(myObj);
			//print("*********************1.1\n");
			long startTime = System.currentTimeMillis();
			numberOfWords = 0;
			while (myReader.hasNext()) {

				NumberWords.merge(myReader.next(), 1, Integer::sum);
				numberOfWords++;

			}
			long endTime  = System.currentTimeMillis();
			long read = endTime - startTime;
			//print("Time for read : " + read + " ms\n");
			//print("*********************1.2\n");
			myReader.close();
		}catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		
	}
	
	/*
	public void addWordsToDictionary(String line)
	{
		String [] words = line.split(" ");
		//System.out.print(words.length+ " ");
		//String w;
        for (int i = 0; i < words.length; i++) { 
        	String word = words[i];
        	if(this.NumberWords.containsKey(word))
        	{
        		NumberWords.replace(word, NumberWords.get(word) + 1 );
        	}
        	else
        	{
        		NumberWords.put(word, 1 );
        	}
        	
            
        	// accessing each element of array 
            //w = words[i]; 
            //System.out.print(w + " "); 
        } 
        //System.out.print("\n");
	}
	
	
	public Boolean containsKey(SortedSet set, String s)
	{
		Iterator<Map.Entry<String,Integer>> it = NumberWordsOrdered.iterator();
	    
		
		while (it.hasNext()) {
			String key = (it.next()).getKey();
			if(key.compareTo(s) == 0)
			{
				return true;
			}
	    }
		return false;
	}
	

	*/
	public void printDictionary()
	{
		for (Map.Entry<String, Integer> entry : NumberWords.entrySet())
		{
		   System.out.println( entry.getKey() + " " + entry.getValue() ) ;
		}
	}
	
	
	public void printTriDictionary(Integer n)
	{
		//print("***************2.1\n");
		long startTime = System.currentTimeMillis();
		Stream< Entry<String, Integer> > sorted = sort();
		long endTime  = System.currentTimeMillis();
		long tri = endTime - startTime;
		print("Time for tri : " + tri + " ms\n");
		//print("***************2.2\n");

		// Display elements
		print("\nDisplay elements : \n\n");
		sorted.limit(n).forEach(System.out::println);
		//print("**************2.3\n");
	}
	
	public void printTotalWords()
	{
		print("\nTotal number of words : \n");
		print(Long.toString(numberOfWords)+"\n");
		print("**********************\n");
	}
	
	public void printTriDictionary_(Integer n)
	{
		print("***************2.1\n");
		long startTime = System.currentTimeMillis();
		Map sortedMap = sortByValues(this.NumberWords);
		long endTime  = System.currentTimeMillis();
		long tri = endTime - startTime;
		print("************************************ Time for tri : " + tri + " ms\n");
		print("***************2.2\n");
		// Get a set of the entries on the sorted map
		//Set set = sortedMap.entrySet();

		// Get an iterator
		Iterator i = sortedMap.entrySet().iterator();
		print("***************2.3\n");

		// Display elements
		print("\n********** Display elements : \n");
		int iterations = 0;
		while(i.hasNext() && iterations < n) 
		{
			Map.Entry me = (Map.Entry)i.next();
			print(me.getKey() + ": ");
			print(me.getValue()+"\n");
			iterations = iterations+1;
		}
		print("**************2.4\n");
	}
	
	public static Map<String, Integer> sortByValues(final Map<String, Integer> map) 
	{
		Comparator<String> valueComparator = new Comparator<String>() 
		{
			public int compare(String k1, String k2) 
			{
				int number = map.get(k1).compareTo(map.get(k2));
				if (number == 0) 
					return k1.compareTo(k2);
				else 
					return -number;
			}
		};

		Map<String, Integer> sortedByValues = new TreeMap<String, Integer>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}
	
	public synchronized void write(String text)
	{
		try {
			
			if (used == true) {
				wait();
			}
			else {
				used = true;
				try(FileWriter fw = new FileWriter(this.nameFile, true);
					    BufferedWriter bw = new BufferedWriter(fw);
					    PrintWriter out = new PrintWriter(bw))
					{
					    out.print(text + "\n");
					    
					} catch (IOException e) {
					    //exception handling left as an exercise for the reader
					}
				
				used = false;
				notifyAll();
				
			}
			
		} catch (Exception e) {}
	}

	public void print(String text)
	{
		if (this.print == false)
			System.out.print(text);
		else
			this.write(text);
	}
	

	public boolean verifyFileExists(String name)
	{
		File file = new File("data.txt");
		return file.isFile(); 
	}
	
	public void writeHeading()
	{
		this.print("\nThis test was done on " + DateTime.toString() + "\n");
	}
	
	
}
