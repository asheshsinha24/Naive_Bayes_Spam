/*
	Spam detection using a Naive Bayes classifier.

	The program is incomplete, it only reads in messages
	and creates the dictionary together
	with the word counts for each class (spam and ham).
*/

import java.io.*;
import java.util.*;
import java.lang.*;

public class NBSpamDetect
{
	// This a class with two counters (for ham and for spam)
	static class Multiple_Counter
	{
		int counterHam = 0;
		int counterSpam    = 0;
	}


	public static void main(String[] args)
	throws IOException
	{
	        int counter1=0;
		// Location of the directory (the path) taken from the cmd line (first arg)
		File dir_location      = new File( args[0] );
		
		// Listing of the directory (should contain 2 subdirectories: ham/ and spam/)
		File[] dir_listing     = new File[0];

		// Check if the cmd line arg is a directory and list it
		if ( dir_location.isDirectory() )
		{
			dir_listing = dir_location.listFiles();
		}
		else
		{
			System.out.println( "- Error: cmd line arg not a directory.\n" );
		        Runtime.getRuntime().exit(0);
		}
		
		// Listings of the two subdirectories (ham/ and spam/)
		File[] listing_ham = new File[0];
		File[] listing_spam    = new File[0];
		
		// Check that there are 2 subdirectories
		if ( dir_listing.length == 2 )
		{
			listing_ham = dir_listing[0].listFiles();
			listing_spam    = dir_listing[1].listFiles();
		}
		else
		{
			System.out.println( "- Error: specified directory does not contain two subdirectories.\n" );
		        Runtime.getRuntime().exit(0);
		}

		// Print out the number of messages in ham and in spam
		System.out.println( "\t number of ham messages is: " + listing_ham.length );
		System.out.println( "\t number of spam messages is: "    + listing_spam.length );
		
		// Create a hash table for the vocabulary (word searching is very fast in a hash table)
		Hashtable<String,Multiple_Counter> vocab = new Hashtable<String,Multiple_Counter>();
		Hashtable<String,Multiple_Counter> vocabParse = new Hashtable<String,Multiple_Counter>();
		Multiple_Counter old_cnt   = new Multiple_Counter();

		// Read the e-mail messages
		// The ham mail
		for ( int i = 0; i < listing_ham.length; i ++ )
		{
			FileInputStream i_s = new FileInputStream( listing_ham[i] );
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			String word;
			
        	        while ((line = in.readLine()) != null)					// read a line
			{
				StringTokenizer st = new StringTokenizer(line);
				String[] lineSplit =line.split(" ");
			    String start ="a";
			    
			   
			    if(lineSplit.length>0)
				start = lineSplit[0];

			    counter1=0;
		
				while (st.hasMoreTokens())
				{
					word = st.nextToken().replaceAll("[^a-zA-Z]",""); 
				       

					if(start.equals("To:") | start.equals("From:") | start.equals("Cc:") | start.equals("Subject:"))
					{
					     
					    
					    
					    if ( !word.equals("") && counter1>0)  {
						
					      
						if ( vocabParse.containsKey(word) )				// check if word exists already in the vocabulary
						{
							old_cnt = vocabParse.get(word);	// get the counter from the hashtable
							old_cnt.counterHam ++;			// and increment it
					
							vocabParse.put(word, old_cnt);
						}
						else
						{
							Multiple_Counter fresh_cnt = new Multiple_Counter();
							fresh_cnt.counterHam = 1;
							fresh_cnt.counterSpam    = 0;
						
							vocabParse.put(word, fresh_cnt);			// put the new word with its new counter into the hashtable
						}
						
					    }
					    

					    counter1++;
					} 
					
					if ( !word.equals("") ) {                               // if string isn't empty
						if ( vocab.containsKey(word) )		       	// check if word exists already in the vocabulary
						{
							old_cnt = vocab.get(word);	        // get the counter from the hashtable
							old_cnt.counterHam ++;			// and increment it
					
							vocab.put(word, old_cnt);
						}
						else
						{
							Multiple_Counter fresh_cnt = new Multiple_Counter();
							fresh_cnt.counterHam = 1;
							fresh_cnt.counterSpam    = 0;
						
							vocab.put(word, fresh_cnt);	        // put the new word with its new counter into the hashtable
						}
	        }
				}
			}

                	in.close();
		}
		// The spam mail
		for ( int i = 0; i < listing_spam.length; i ++ )
		{
			FileInputStream i_s = new FileInputStream( listing_spam[i] );
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			String word;

		      
			
        	        while ((line = in.readLine()) != null)					// read a line
			{
			    StringTokenizer st = new StringTokenizer(line);
			    String[] lineSplit =line.split(" ");
			    String start ="a";
			    
			   
			    if(lineSplit.length>0)
				start = lineSplit[0];
			    counter1=0;

			    
		
				while (st.hasMoreTokens())
				{
					word = st.nextToken().replaceAll("[^a-zA-Z]","");

					if(start.equals("To:") | start.equals("From:") | start.equals("Cc:") | start.equals("Subject:"))
					{
					   
				       
						
					    if ( !word.equals("") && counter1>0)  {
						if ( vocabParse.containsKey(word) )				// check if word exists already in the vocabulary
						{
							old_cnt = vocabParse.get(word);	// get the counter from the hashtable
							old_cnt.counterSpam ++;			// and increment it
					
							vocabParse.put(word, old_cnt);
						}
						else
						{
							Multiple_Counter fresh_cnt = new Multiple_Counter();
							fresh_cnt.counterHam = 0;
							fresh_cnt.counterSpam    = 1;
						
							vocabParse.put(word, fresh_cnt);			// put the new word with its new counter into the hashtable
						}
						
					    }
					    
					    counter1++;
					} 
				
					
				  if ( ! word.equals("") ) {	
						if ( vocab.containsKey(word) )				// check if word exists already in the vocabulary
						{
							old_cnt = vocab.get(word);	// get the counter from the hashtable
							old_cnt.counterSpam ++;			// and increment it
					
							vocab.put(word, old_cnt);
						}
						else
						{
							Multiple_Counter fresh_cnt = new Multiple_Counter();
							fresh_cnt.counterHam = 0;
							fresh_cnt.counterSpam    = 1;
						
							vocab.put(word, fresh_cnt);			// put the new word with its new counter into the hashtable
						}
					}
				}
			}

                	in.close();
		}

		// Print out the hash table

	   
		/*
		for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements() ;)
		{	
			String word;
			
			word = e.nextElement();
			old_cnt  = vocab.get(word);			
			System.out.println( word + " | in ham: " + old_cnt.counterHam + 
			                             " in spam: "    + old_cnt.counterSpam);
		}
		*/
		

		// -------------------------------------Test Data---------------------------------------------------------------------------------------

		// Location of the directory (the path) taken from the cmd line (first arg)
		File dir_location1      = new File( args[1] );
		
		// Listing of the directory (should contain 2 subdirectories: ham/ and spam/)
		File[] dir_listing1     = new File[0];

		// Check if the cmd line arg is a directory and list it
		if ( dir_location1.isDirectory() )
		{
			dir_listing1 = dir_location1.listFiles();
		}
		else
		{
			System.out.println( "- Error: cmd line arg not a directory.\n" );
		        Runtime.getRuntime().exit(0);
		}
		
		// Listings of the two subdirectories (ham/ and spam/)
		File[] listing_ham1 = new File[0];
		File[] listing_spam1    = new File[0];
		
		// Check that there are 2 subdirectories
		if ( dir_listing1.length == 2 )
		{
			listing_ham1 = dir_listing1[0].listFiles();
			listing_spam1    = dir_listing1[1].listFiles();
		}
		else
		{
			System.out.println( "- Error: specified directory does not contain two subdirectories.\n" );
		        Runtime.getRuntime().exit(0);
		}

		
	       
		List<HashMap<String, Integer>> MailHam = new ArrayList<HashMap<String, Integer>>();
		List<HashMap<String, Integer>> MailHamLower = new ArrayList<HashMap<String, Integer>>();
		List<HashMap<String, Integer>> MailHamParse = new ArrayList<HashMap<String, Integer>>();

		// Read the e-mail messages
		// The ham mail
		for ( int i = 0; i < listing_ham1.length; i ++ )
		{
		    HashMap<String, Integer> WordList = new HashMap<String, Integer>();
		    HashMap<String, Integer> WordList1 = new HashMap<String, Integer>();
		    HashMap<String, Integer> WordList2 = new HashMap<String, Integer>();
		     FileInputStream i_s = new FileInputStream( listing_ham1[i] );
		     BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
		     String line;
		     String word;
		     
		     while ((line = in.readLine()) != null)					// read a line
			{
			    StringTokenizer st = new StringTokenizer(line);
			    String[] lineSplit =line.split(" ");
			    String start ="a";
			    
			   
			    if(lineSplit.length>0)
				start = lineSplit[0];
			    counter1=0;
			    
			    while (st.hasMoreTokens())
				{
				    word = st.nextToken().replaceAll("[^a-zA-Z]","");

				    if(start.equals("To:") | start.equals("From:") | start.equals("Cc:") | start.equals("Subject:"))
					{
					 					 					
					    if ( !word.equals("") && counter1>0)  {
						
						if(!WordList2.containsKey(word))
						    WordList2.put(word, 1);
						else
						    WordList2.put(word, WordList2.get(word)+1);
						
					    }
					    
					    counter1++;
					    
					}

					
				    if ( !word.equals("") ) { // if string isn't empty
					if(!WordList.containsKey(word))
					    WordList.put(word, 1);
					else
					    WordList.put(word, WordList.get(word)+1);

					if(!WordList1.containsKey(word.toLowerCase()))
					    WordList1.put(word.toLowerCase(), 1);
					else
					    WordList1.put(word.toLowerCase(), WordList1.get(word.toLowerCase())+1);

					   
				    }
	        
				}
				    
			}

		     MailHam.add(WordList);
		     MailHamLower.add(WordList1);
		     MailHamParse.add(WordList2);

                	in.close();
		}

		List<HashMap<String, Integer>> MailSpam = new ArrayList<HashMap<String, Integer>>();
		List<HashMap<String, Integer>> MailSpamLower = new ArrayList<HashMap<String, Integer>>();
		List<HashMap<String, Integer>> MailSpamParse = new ArrayList<HashMap<String, Integer>>();

		// The spam mail
		for ( int i = 0; i < listing_spam1.length; i ++ )
		{
		    HashMap<String, Integer> WordList = new HashMap<String, Integer>();
		    HashMap<String, Integer> WordList1 = new HashMap<String, Integer>();
		    HashMap<String, Integer> WordList2 = new HashMap<String, Integer>();
		    FileInputStream i_s = new FileInputStream( listing_spam1[i] );
		    BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
		    String line;
		    String word;
			
        	        while ((line = in.readLine()) != null)					// read a line
			{
			    StringTokenizer st = new StringTokenizer(line);

			    String[] lineSplit =line.split(" ");
			    
			    String start="a";

			   if(lineSplit.length>0)
				start = lineSplit[0];
			   counter1=0;
		
			    while (st.hasMoreTokens())
				{
				    word = st.nextToken().replaceAll("[^a-zA-Z]","");
				    
				    if(start.equals("To:") | start.equals("From:") | start.equals("Cc:") | start.equals("Subject:"))
					{
						
					    if ( !word.equals("") && counter1>0)  {
						if(!WordList2.containsKey(word))
						    WordList2.put(word, 1);
						else
						    WordList2.put(word, WordList2.get(word)+1);

					    }
					    
					    counter1++;
					    
					}

					
				    if ( ! word.equals("") ) {	
					if (!WordList.containsKey(word))
					    WordList.put(word, 1);
					else
					    WordList.put(word, WordList.get(word)+1);


					if(!WordList1.containsKey(word.toLowerCase()))
					    WordList1.put(word.toLowerCase(), 1);
					 else
					     WordList1.put(word.toLowerCase(), WordList1.get(word.toLowerCase())+1);

						
				    }
				}
			
			}

			MailSpam.add(WordList);
			MailSpamLower.add(WordList1);
			MailSpamParse.add(WordList2);

                	in.close();
		}

		

		// Part (a) 
		int[] classify= NaiveBayes(listing_ham.length,listing_spam.length, vocab, MailHam, MailSpam);
		System.out.println("");
		//printing confusion matrix
		System.out.println("Case 1");
		for(int i=0; i<2;i++)
		    {
			for(int j=0; j<2; j++)
			    System.out.print(classify[i*2+j] + "  ");
		        System.out.println("  ");
		
	            }

		System.out.println("");
		//part (b) for lower case
		Hashtable<String,Multiple_Counter> vocabLower = new Hashtable<String,Multiple_Counter>();

		for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements() ;)
		{	
			String word;
			
			word = e.nextElement();
			old_cnt  = vocab.get(word);

			if(vocabLower.containsKey(word.toLowerCase()))
			    {
				Multiple_Counter counter = vocabLower.get(word.toLowerCase()); 
				
				counter.counterHam += old_cnt.counterHam;  
				counter.counterSpam += old_cnt.counterSpam;  
				vocabLower.put(word.toLowerCase(), counter);
			    }
			else
			    {
				Multiple_Counter fresh_cnt = new Multiple_Counter();
				fresh_cnt.counterHam += old_cnt.counterHam;  
				fresh_cnt.counterSpam += old_cnt.counterSpam;  
				vocabLower.put(word.toLowerCase(), fresh_cnt);
				
			    }
			
		}

		classify= NaiveBayes(listing_ham.length,listing_spam.length, vocabLower, MailHamLower, MailSpamLower);
		
		//printing confusion matrix
		System.out.println("Case 2");
		for(int i=0; i<2;i++)
		    {
			for(int j=0; j<2; j++)
			    System.out.print(classify[i*2+j] + "  ");
		        System.out.println("  ");
		
	            }
		System.out.println("");

		//Part (c)
		classify= NaiveBayes(listing_ham.length,listing_spam.length, vocabParse, MailHamParse, MailSpamParse);
		
		//printing confusion matrix
		System.out.println("Case 3");
		for(int i=0; i<2;i++)
		    {
			for(int j=0; j<2; j++)
			    System.out.print(classify[i*2+j] + "  ");
		        System.out.println("  ");
		
	            }

		
		
	}


    
    public static int[] NaiveBayes(int listing_ham_len, int listing_spam_len, Hashtable<String, Multiple_Counter> vocab, List<HashMap<String, Integer>> MailHam, List<HashMap<String, Integer>> MailSpam )
    {
	Multiple_Counter old_cnt   = new Multiple_Counter();

	int nWordsHam=0, nWordsSpam=0;
		
		for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements() ;)
		{	
			String word;
			
			word = e.nextElement();
			old_cnt  = vocab.get(word);

			nWordsHam +=old_cnt.counterHam;
			nWordsSpam +=old_cnt.counterSpam;
			
		}
       
		
	// Now all students must continue from here
	// Prior probabilities must be computed from the number of ham and spam messages
	int nMessagesHam, nMessagesSpam, nMessagesTotal;
	nMessagesHam=listing_ham_len;
	nMessagesSpam=listing_spam_len;
	nMessagesTotal=nMessagesHam+nMessagesSpam;
	
	double P_ham, P_spam;
	P_ham=1.0*(nMessagesHam)/(nMessagesTotal);
	P_spam=1.0*(nMessagesSpam)/(nMessagesTotal);
	
	P_ham=Math.log(P_ham);
	P_spam=Math.log(P_spam);

       

	// Conditional probabilities must be computed for every unique word
	// add-1 smoothing must be implemented
	// Probabilities must be stored as log probabilities (log likelihoods).

	HashMap<String, Double> CondProbHam =new HashMap<String,Double>();
	HashMap<String, Double> CondProbSpam =new HashMap<String,Double>();

	for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements();)
	    {
		String word;

		word = e.nextElement();
		old_cnt  = vocab.get(word);
		

		double ProbHam =Math.log(old_cnt.counterHam + 1.0)-Math.log(nWordsHam + vocab.size());
		double ProbSpam = Math.log(old_cnt.counterSpam + 1.0)-Math.log(nWordsSpam + vocab.size());

		CondProbHam.put(word, ProbHam);
		CondProbSpam.put(word, ProbSpam);

	    }

	int[] classes ={0,0,0,0};
	// Bayes rule must be applied on new messages, followed by argmax classification (using log probabilities)

	//Testing Ham Messages
	for (int i=0; i<MailHam.size(); i++)
	    {
		double prob1=0, prob2=0;
		HashMap<String, Integer> WordList = MailHam.get(i);
		
		prob1 +=P_ham;
		prob2 +=P_spam;
		
		for (String words: WordList.keySet())
		    {
			
			if(CondProbHam.containsKey(words))
			    prob1 += WordList.get(words)*CondProbHam.get(words);
			
			if(CondProbSpam.containsKey(words))
			    prob2 += WordList.get(words)*CondProbSpam.get(words);

		      
		    }

	     

		if(prob1>=prob2)
		    classes[3]++;
		else
		    classes[1]++;
			  
	    }

	//Testing Spam Messages
	for (int i=0; i<MailSpam.size(); i++)
	    {
		double prob1=0, prob2=0;
		HashMap<String, Integer> WordList = MailSpam.get(i);
		
		prob1 +=P_ham;
		prob2 +=P_spam;

		for (String words: WordList.keySet())
		    {
			if(CondProbHam.containsKey(words))
			    prob1 += WordList.get(words)*CondProbHam.get(words);
			
			if(CondProbSpam.containsKey(words))
			    prob2 += WordList.get(words)*CondProbSpam.get(words);
			
		    }

		if(prob1>=prob2)
		    classes[2]++;
		else
		    classes[0]++;
			  
	    }
	return classes;
    }

}
