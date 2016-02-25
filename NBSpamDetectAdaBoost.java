/*
	Spam detection using a Naive Bayes classifier.

	The program is incomplete, it only reads in messages
	and creates the dictionary together
	with the word counts for each class (spam and ham).
*/

import java.io.*;
import java.util.*;
import java.lang.*;

public class NBSpamDetectAdaBoost
{

    public static double[] weights_ham;
    public static double[] weights_spam;
    public static int[] error_ham;
    public static int[] error_spam;
    

	// This a class with two counters (for ham and for spam)
	static class Multiple_Counter
	{
		int counterHam = 0;
		int counterSpam    = 0;
	}


	public static void main(String[] args)
	throws IOException
	{
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
			    String[] lineSplit =line.split(":");
			    String start ="a";
			    
			   
			    if(lineSplit.length>0)
				start = lineSplit[0];

			    int counter1=0;
								   
		
			    while (st.hasMoreTokens())
				{
				    word = st.nextToken().replaceAll("[^a-zA-Z]","");
				    if(start.equals("To") | start.equals("From") | start.equals("Cc") | start.equals("Subject"))
					{
						
					    if ( !word.equals("") && counter1>0 ) { 
						
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

			    String[] lineSplit =line.split(":");
			    
			    String start="a";

			   if(lineSplit.length>0)
				start = lineSplit[0];
			   int counter1=0;
		
			    while (st.hasMoreTokens())
				{
				    word = st.nextToken().replaceAll("[^a-zA-Z]","");
				   if(start.equals("To") | start.equals("From") | start.equals("Cc") | start.equals("Subject"))
					{
						
					    if ( !word.equals("") && counter1>0 ) { 
						
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

			MailSpam.add(WordList);
			MailSpamLower.add(WordList1);
			MailSpamParse.add(WordList2);

                	in.close();
		}

	    

		//-----------------------------Naive Bayes with AdaBoost-----------------
		//initial weights
		weights_ham = new double[listing_ham.length];
		weights_spam =new double[listing_spam.length];
		error_ham = new int[listing_ham.length];
		error_spam =new int[listing_spam.length];

		for (int i=0; i<listing_ham.length;i++)
		    weights_ham[i]=1.0/listing_ham.length;
		
		for (int i=0; i<listing_spam.length;i++)
		    weights_spam[i]=1.0/listing_spam.length;

		int total_models=10;
		
		List<int[]> Hypothesis1_ham = new ArrayList<int[]>();
		List<int[]> Hypothesis1_spam = new ArrayList<int[]>();
		List<int[]> Hypothesis2_ham = new ArrayList<int[]>();
		List<int[]> Hypothesis2_spam = new ArrayList<int[]>();
		List<int[]> Hypothesis3_ham = new ArrayList<int[]>();
		List<int[]> Hypothesis3_spam = new ArrayList<int[]>();
		List<double[]> Weight1_ham = new ArrayList<double[]>();
		List<double[]> Weight1_spam = new ArrayList<double[]>();

		for(int i_model=0; i_model<total_models;i_model++)
		    {
	       
			Hashtable<String, Multiple_Counter> vocab = AdaBoost(1,listing_ham, listing_spam).get(0);
			//Hashtable<String, Multiple_Counter> vocabParse = AdaBoost(0,listing_ham, listing_spam).get(1);
		
		      
		

			// Part (a) 
			int[] classify1= NaiveBayes(listing_ham.length,listing_spam.length, vocab, MailHam, MailSpam);
		

			
			Hypothesis1_ham.add(error_ham);
			Hypothesis1_spam.add(error_spam);
			Weight1_ham.add(weights_ham);
			Weight1_spam.add(weights_spam);

			if(i_model==total_models-1)
			    {
				classify1 = combineHypothesis(MailHam.size(), MailSpam.size(), Hypothesis1_ham, Hypothesis1_spam, Weight1_ham, Weight1_spam);
		
				System.out.println("Case 1 with Adaboost");
				for(int i=0; i<2;i++)
				    {
					for(int j=0; j<2; j++)
					    System.out.print(classify1[i*2+j] + "  ");
					System.out.println("  ");
		
				    }
			    }
			
			//weight update call
			weightsUpdate(listing_ham.length,listing_spam.length);
		    
		    }
		
		for (int i=0; i<listing_ham.length;i++)
		    weights_ham[i]=1.0/listing_ham.length;
		
		for (int i=0; i<listing_spam.length;i++)
		    weights_spam[i]=1.0/listing_spam.length;

		for(int i_model=0; i_model<total_models;i_model++)
		    {
	       
			Hashtable<String, Multiple_Counter> vocab = AdaBoost(1,listing_ham, listing_spam).get(0);
			//Hashtable<String, Multiple_Counter> vocabParse = AdaBoost(0,listing_ham, listing_spam).get(1);
			
			//part (b) for lower case
			Hashtable<String,Multiple_Counter> vocabLower = new Hashtable<String,Multiple_Counter>();

			for (Enumeration<String> e = vocab.keys() ; e.hasMoreElements() ;)
			    {	
				String word;
			
				word = e.nextElement();
				Multiple_Counter old_cnt  = vocab.get(word);

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

			int[] classify2= NaiveBayes(listing_ham.length,listing_spam.length, vocabLower, MailHamLower, MailSpamLower);
		

			Hypothesis2_ham.add(error_ham);
			Hypothesis2_spam.add(error_spam);

			if(i_model==total_models-1)
			    {
				classify2 = combineHypothesis(MailHamLower.size(), MailSpamLower.size(), Hypothesis2_ham, Hypothesis2_spam, Weight1_ham, Weight1_spam);
		
				System.out.println("Case 2 with Adaboost");
				for(int i=0; i<2;i++)
				    {
					for(int j=0; j<2; j++)
					    System.out.print(classify2[i*2+j] + "  ");
					System.out.println("  ");
		
				    }
			    }

			//weight update call
			weightsUpdate(listing_ham.length,listing_spam.length);
		    

		    }
			

		for (int i=0; i<listing_ham.length;i++)
		    weights_ham[i]=1.0/listing_ham.length;
		
		for (int i=0; i<listing_spam.length;i++)
		    weights_spam[i]=1.0/listing_spam.length;

		for(int i_model=0; i_model<total_models;i_model++)
		    {
	       
			//Hashtable<String, Multiple_Counter> vocab = AdaBoost(1,listing_ham, listing_spam).get(0);
			Hashtable<String, Multiple_Counter> vocabParse = AdaBoost(0,listing_ham, listing_spam).get(1);

			//Part (c)
			
			int[] classify3= NaiveBayes(listing_ham.length,listing_spam.length, vocabParse, MailHamParse, MailSpamParse);
	       

			Hypothesis3_ham.add(error_ham);
			Hypothesis3_spam.add(error_spam);
			    
			if(i_model==total_models-1)
			    {
				classify3 = combineHypothesis(MailHamParse.size(), MailSpamParse.size(), Hypothesis3_ham, Hypothesis3_spam, Weight1_ham, Weight1_spam);
		
				System.out.println("Case 3 with Adaboost");
				for(int i=0; i<2;i++)
				    {
					for(int j=0; j<2; j++)
					    System.out.print(classify3[i*2+j] + "  ");
					System.out.println("  ");
		
				    }
			    }

			//weight update call
			weightsUpdate(listing_ham.length,listing_spam.length);
		    
		    }
			


			
		

		
	}

    public static int[] combineHypothesis(int ham_len, int spam_len, List<int[]> Hypothesis_ham, List<int[]> Hypothesis_spam, List<double[]> Weight_ham, List<double[]> Weight_spam)
    {
       
	
	int[] classes ={0,0,0,0};
	double[] total_error_ham=new double[Hypothesis_ham.size()];
	double[] total_error_spam=new double[Hypothesis_spam.size()];

	
	
	for(int i=0; i<Hypothesis_ham.size();i++)
	    {
		total_error_ham[i]=0;
		for(int j=0; j<ham_len;j++)
		    {
			total_error_ham[i] += Weight_ham.get(i)[j]*Hypothesis_ham.get(i)[j];
			
		    }
		
		total_error_ham[i]=1.0*total_error_ham[i]/(1-total_error_ham[i]);
		
		
	      
	    }

	

	
	for(int i=0; i<ham_len; i++)
	    {
		double output=0;
	    
		for(int j=0; j<Hypothesis_ham.size(); j++)
		    {
			
			output += total_error_ham[j]*(1-Hypothesis_ham.get(j)[i]);

			/*
			if(total_error_ham[j]==0)
			    output +=0;
			else
			    output += Math.log(total_error_ham[j])*(2*(1-Hypothesis_ham.get(j)[i])-1);
			*/
		    }
		
	       
		//output = 1.0/(1+Math.exp(output));
		
		
		
		if(output>=0.5)
		    classes[3]++;
		else
		    classes[1]++;
	    }


	for(int i=0; i<Hypothesis_spam.size();i++)
	    {
		total_error_spam[i]=0;
		for(int j=0; j<spam_len;j++)
		    total_error_spam[i] += Weight_spam.get(i)[j]*Hypothesis_spam.get(i)[j];

		total_error_spam[i]=1.0*total_error_spam[i]/(1-total_error_spam[i]);

		
	    }

	
	for(int i=0; i<spam_len; i++)
	    {
		double output=0;
	    
		for(int j=0; j<Hypothesis_spam.size(); j++)
		    {
			output +=total_error_spam[j]*(1-Hypothesis_spam.get(j)[i]);
			/*
			if(total_error_spam[j]==0)
			    output +=0;
			else
			    output += Math.log(total_error_spam[j])*(2*(1-Hypothesis_spam.get(j)[i])-1);
			*/
		    }
		
		//output = 1.0/(1+Math.exp(output));
		
		if(output>=0.5)
		    classes[0]++;
		else
		    classes[2]++;
	    }

	return classes;

    }

    public static void weightsUpdate(int listing_ham_len, int listing_spam_len)
    {
	
	//total error in ham
	double total_error=0;
	for (int i=0;i<listing_ham_len;i++)
	    total_error +=weights_ham[i]*error_ham[i];
	
	total_error=1.0*total_error/(1-total_error);
	
	// weight update for ham
        double sum_weights=0;
	for (int i=0;i<listing_ham_len;i++)
	    {
		weights_ham[i] =1.0*weights_ham[i]*Math.pow((total_error),(1-error_ham[i]));
		sum_weights +=weights_ham[i];
	    }

	//normalize
	for (int i=0;i<listing_ham_len;i++)
	    {
		weights_ham[i] =1.0*weights_ham[i]/sum_weights;
		
	    }

	//total error in spam
	total_error=0;
	for (int i=0;i<listing_spam_len;i++)
	    total_error +=weights_spam[i]*error_spam[i];

	total_error=1.0*total_error/(1-total_error);
	
	// weight update for spam
        sum_weights=0;
	for (int i=0;i<listing_spam_len;i++)
	    {
		weights_spam[i] =1.0*weights_spam[i]*Math.pow((total_error),(1-error_spam[i]));
		sum_weights +=weights_spam[i];
	    }

	//normalize
	for (int i=0;i<listing_spam_len;i++)
	    {
		weights_spam[i] =1.0*weights_spam[i]/sum_weights;
		
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
	P_ham=(1.0*nMessagesHam)/(nMessagesTotal);
	P_spam=(1.0*nMessagesSpam)/(nMessagesTotal);
	
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
		

		double ProbHam = Math.log(old_cnt.counterHam + 1.0)-Math.log(nWordsHam + vocab.size());
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
		HashMap<String, Integer> WordList = MailHam.get(i);;
		
		for (String words: WordList.keySet())
		    {
		              
			
			if(CondProbHam.containsKey(words))
			    prob1 += WordList.get(words)*CondProbHam.get(words);
			
			if(CondProbSpam.containsKey(words))
			    prob2 += WordList.get(words)*CondProbSpam.get(words);
		      
		    }
		
		prob1 +=P_ham;
	       	prob2 +=P_spam;


	        

		if(prob1>=prob2)
		    {
			classes[3]++;
			error_ham[i]=0;
		    }
		else
		    {
			classes[1]++;
			error_ham[i]=1;
		    }
			  
	    }

	//Testing Spam Messages
	for (int i=0; i<MailSpam.size(); i++)
	    {
		double prob1=0, prob2=0;
		HashMap<String, Integer> WordList = MailSpam.get(i);
		
		for (String words: WordList.keySet())
		    {
			if(CondProbHam.containsKey(words))
			    prob1 += WordList.get(words)*CondProbHam.get(words);
			
			if(CondProbSpam.containsKey(words))
			    prob2 += WordList.get(words)*CondProbSpam.get(words);
			
			
		    }
		
		prob1 +=P_ham;
		prob2 +=P_spam;

		if(prob1>=prob2)
		    {
			classes[2]++;
			error_spam[i]=1;
		    }
		else
		    {
			classes[0]++;
			error_spam[i]=0;
		    }
			  
	    }
	return classes;
    }

    public static List<Hashtable<String, Multiple_Counter>> AdaBoost(int flag, File[] listing_ham, File[] listing_spam )
    throws IOException
	{

	// Create a hash table for the vocabulary (word searching is very fast in a hash table)
		Hashtable<String,Multiple_Counter> vocab = new Hashtable<String,Multiple_Counter>();
		Hashtable<String,Multiple_Counter> vocabParse = new Hashtable<String,Multiple_Counter>();
		Multiple_Counter old_cnt   = new Multiple_Counter();

		//prob distribution for ham messages
	      
		int[]  ham_num = new int[listing_ham.length];
		for(int j=0;j<ham_num.length;j++)
		    ham_num[j]=j;
		
		int[] samples_ham = Sampling(ham_num,weights_ham);

		
		
		//EnumeratedIntegerDistribution dist_ham = new EnumeratedIntegerDistribution(ham_num,weights_ham);
		//int numSample_ham=listing_ham.length;
		//int[] samples_ham = dist_ham.sample(numSample_ham);
		
		// Read the e-mail messages
		// The ham mail
		for ( int i = 0; i < listing_ham.length; i ++ )
		{
			FileInputStream i_s = new FileInputStream( listing_ham[samples_ham[i]] );
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			String word;
			
        	        while ((line = in.readLine()) != null)					// read a line
			{
				StringTokenizer st = new StringTokenizer(line);
				String[] lineSplit =line.split(":");
			    String start ="a";
			    
			   
			    if(lineSplit.length>0)
				start = lineSplit[0];
			     int counter1=0;
		
				while (st.hasMoreTokens())
				{
					word = st.nextToken().replaceAll("[^a-zA-Z]","");

					if(start.equals("To") | start.equals("From") | start.equals("Cc") | start.equals("Subject"))
					{
						
					    if ( !word.equals("") && counter1>0 ) {
					      
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

		//prob distribution for spam messages
	    
		int[]  spam_num = new int[listing_spam.length];
		for(int j=0;j<spam_num.length;j++)
		    spam_num[j]=j;
		
		int[] samples_spam = Sampling(spam_num,weights_spam);


		// The spam mail
		for ( int i = 0; i < listing_spam.length; i ++ )
		{
			FileInputStream i_s = new FileInputStream( listing_spam[samples_spam[i]] );
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			String word;
			
        	        while ((line = in.readLine()) != null)					// read a line
			{
			    StringTokenizer st = new StringTokenizer(line);
			    String[] lineSplit =line.split(":");
			    String start ="a";
			    
			   
			    if(lineSplit.length>0)
				start = lineSplit[0];
			     int counter1=0;
			
		
				while (st.hasMoreTokens())
				{
					word = st.nextToken().replaceAll("[^a-zA-Z]","");

					if(start.equals("To") | start.equals("From") | start.equals("Cc") | start.equals("Subject"))
					{
						
					    if ( !word.equals("") && counter1>0 ) {
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

		List<Hashtable<String, Multiple_Counter>> vocabList = new ArrayList<Hashtable<String, Multiple_Counter>>();
		vocabList.add(vocab);
		vocabList.add(vocabParse);

		return vocabList;
	
    }

    
    public static int[] Sampling(int[] listing_num, double[] weights)
    {
	double[] cummulative = new double[listing_num.length];
	
	cummulative[0]=weights[0];
	for(int i=1; i<cummulative.length;i++)
	    cummulative[i]=cummulative[i-1]+weights[i];
	
	int[] newList = new int[listing_num.length];
	
	for (int i=0; i<listing_num.length;i++)
	    {
		double rand=Math.random();
		
		for(int j=0;j<listing_num.length;j++)
		    {
			if(rand<=cummulative[j])
			    {
				newList[i]=j;
				break;
			    }
		    }
	    }
	return newList;
	
    }

}
