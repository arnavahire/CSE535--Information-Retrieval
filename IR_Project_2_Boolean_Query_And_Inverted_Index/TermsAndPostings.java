	import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class TermsAndPostings{
	
//     Member variables	
	
static int count=0;
static String Arr[]=new String[66993];

static LinkedList<Integer>[] postings=new LinkedList[66993];
static ArrayList<LinkedList> parray = new ArrayList<LinkedList>();


//     Member Functions

	public static void main(String[] args) throws IOException  // Calls create postings method to create an inverted index
	{
		String path=args[0];
		String outputPath=args[1];
		String inputPath=args[2];

		createPostings("text_nl",path);
		createPostings("text_fr",path);
		createPostings("text_de",path);
		createPostings("text_ja",path);
		createPostings("text_ru",path);
		createPostings("text_pt",path);
		createPostings("text_es",path);
		createPostings("text_it",path);
		createPostings("text_sv",path);
		createPostings("text_no",path);
		createPostings("text_da",path);
		
		System.out.println("Created Postings");
		
/*		for(int i=0;i<Arr.length;i++)
		{
			System.out.println(i+1+" "+Arr[i]+postings[i]);
		}
*/	
		readFile(inputPath,outputPath);  //Reads the created inverted index for a given query term
		
	}
	
	//------------------------------------------------------------------------------------------------------------
	
	public static void createPostings(String field,String pathString) throws IOException
	{
		String path =pathString;
		Path p = Paths.get(path);
		Directory d = FSDirectory.open(p);
		IndexReader ir = DirectoryReader.open(d); //reads from the directory
		
		Terms terms = MultiFields.getTerms(ir,field);  //Collects all the terms for a specified field and stores it in a variable 'terms'
		TermsEnum te = terms.iterator();               //TermsEnum iterates over each term present in the 'terms' variable
		BytesRef term;
		try
		{
			while((term=te.next())!=null)  //each term is of type BytesRef, so for each term we iterate
			{
				PostingsEnum e=MultiFields.getTermDocsEnum(ir, field, term);  // get postings list one at a time for each term in PostingsEnum e
				
				Arr[count]=term.utf8ToString();  // Store each term in an array in order to be printed to an output file
				
			
	//			System.out.print(count+" "+term.utf8ToString());
				
				while((e.nextDoc())!=e.NO_MORE_DOCS) // For every term get all the documents in which the term is present
				{
					
	//				System.out.print(" "+e.docID());
					if(postings[count]==null)
					{
						postings[count]=new LinkedList<Integer>();
					}
					postings[count].add(e.docID());  // append the document IDs to the term's corresponding postings list 
				}
				count++;
	//			System.out.println();
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	//---------------------------------------------------------------------------------------------------
	
	static void GetPostings(String qterm) throws IOException
	{
		//--- for writing in file
		
		

		System.out.println("GetPostings ");
		System.out.println(qterm);
		System.out.print("Postings list: ");
		
		for(int i=0;i<Arr.length;i++)  //Iterate over every term in the inverted index
		{
			if(Arr[i].equals(qterm))  // if the term in index and the input file match
			{
				//System.out.println(postings[i]);
				for(int k=0;k<postings[i].size();k++)
				{
					System.out.print(postings[i].get(k)+" "); 

					
					
				}
				parray.add(postings[i]);  //append the corresponding postings list for that query term to a postings array for comparison in Taat and Daat
			}
		}
		System.out.println();
		
		
	}
	
	//---------------------------------------------------------------------------------------------------
	
	public static void readFile(String inputPath,String outputPath) throws IOException 
	{
		File output=new File(outputPath);
		FileOutputStream fos=new FileOutputStream(output);
		PrintStream ps=new PrintStream(fos);
		System.setOut(ps);
		

		
		File file=new File(inputPath);
	    BufferedReader reader = new BufferedReader(new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");
	    
	    try 
	    {
	        while((line = reader.readLine()) != null) 
	        {
	       		stringBuilder.append(line);       //take all the words on a single line and stores it in a stringbuilder
	        
	        String query=stringBuilder.toString();  //converts the stringbuilder to string in order to split the words
	        String[] splitted=query.split(" ");
	        	for(String part:splitted)        // for reading all the query terms from input file split the words and take one term at a time
	        	{
	        		GetPostings(part);          // obtain the postings list for that term
	        	
	        	}
	       // 	System.out.println("Temp Array : "+parray);
	        	TaatAnd(splitted);							// Perform TaatAnd
	        	TaatOr(splitted);                           // Perform TaatOr
	        	DaatAnd(splitted);							//Perform DaatAnd
	        	DaatOr(splitted);							//Perform DaatOr
	        	stringBuilder.delete(0, line.length());
	        	parray.clear();
	        }
	    }
	    finally 
	    {
	        reader.close();
	    }
	}
	
	public static void TaatAnd(String[] terms) throws IOException
	{
		
		
		if(parray.size()==1||parray.size()==0)          // if it contains only single term
		{
			System.out.println("TaatAnd");
			for(int i=0;i<terms.length;i++)
			{
				System.out.print(terms[i]+" ");
			}
			System.out.println();
			System.out.print("Results: empty");
			
			System.out.println();
			System.out.println("Number of documents in results: 0");

			System.out.println("Number of comparisons: 0");
			
		}
		else
		{
			int n=0;
//			System.out.println("You entered taat"+" "+parray);
			int[] temp=new int[parray.get(0).size()];
			for(int i=0;i<parray.get(0).size();i++)    //parray.get(0).size gives us the size of first postings list
			{
				temp[i]=(Integer)parray.get(0).get(i); // store the first posting list in a temporary array 'temp'
			}
			LinkedList<Integer> temp2=new LinkedList();
			int totalmatches=0;
			int othercount=parray.size()-1;  // for iteration over other terms to compare their postings
			int currentdoc=0;
			int comparison=0;
	A:		for(int i=0;i<temp.length;i++)  // for every doc id in first posting list of first term
			{
				currentdoc=temp[i];
	B:			for(int j=1;j<=othercount;j++)  // check for the next terms
				{
					for(int k=0;k<parray.get(j).size();k++)   // iterate over the posting list of the next term
	C:				{
						if(j==2&&totalmatches==0)
							break B;
						if(currentdoc==(Integer)parray.get(j).get(k))  // if doc id match
						{
							totalmatches++;       //increment count , this is not the final count however as we will increment the finalcount after the doc id is present in all the terms and not just 2
							comparison++;                             
							break;
						}
						else
						{
							comparison++;
						}
					}
			    }
			
				if(totalmatches==othercount)  //if the count matches the other terms count that means the same doc id is present in every term
				{
					temp2.add(currentdoc);
					n++;                    // Number of matched documents is incremented
				}
				totalmatches=0;    // set count to zero for next doc id to be checked
		    }
			
			System.out.println("TaatAnd");
			for(int i=0;i<terms.length;i++)
			{
				System.out.print(terms[i]+" ");
			}
			System.out.println();
			System.out.print("Results: ");
			if(temp2.size()==0)
			{
				System.out.print("empty");
			}
			for(int i=0;i<n;i++)
			{
				System.out.print(temp2.get(i)+" ");
			}
			System.out.println();
			System.out.print("Number of documents in results: ");
			System.out.println(n);
			System.out.print("Number of comparisons: ");
			System.out.println(comparison);
			
		}	
	}
	
	//-----------------------------------------------------------------------------------------------------------------------
	
	
	public static void TaatOr(String[] terms)throws IOException
	{
		if(parray.size()==0)
		{
			System.out.println("TaatOr");
			for(int i=0;i<terms.length;i++)
			{
				System.out.print(terms[i]+" ");
			}
			System.out.println();
			System.out.print("Results: empty");
			
			System.out.println();
			System.out.println("Number of documents in results: 0");

			System.out.println("Number of comparisons: 0");
		}
		else if(parray.size()==1)          // if it contains only single term
		{
			System.out.println("TaatOr");
			for(int i=0;i<terms.length;i++)
			{
				System.out.print(terms[i]+" ");
			}
			System.out.println();
			System.out.print("Results: ");
			if(parray.get(0).size()==0)
			{
				System.out.print("empty");
			}
			for(int i=0;i<parray.get(0).size();i++)
			{
				System.out.print(parray.get(0).get(i)+" ");
			}
			System.out.println();
			System.out.println("Number of documents in results: 0");

			System.out.println("Number of comparisons: 0");
			
		}
		else
		{
			LinkedList<Integer> temp=new LinkedList();
			for(int x=0;x<parray.get(0).size();x++)
			{
				temp.add((Integer)parray.get(0).get(x));    // store the first posting list in a temporary linked list temp
			}
			LinkedList<Integer> temp2=new LinkedList<Integer>(); //create another linked list, this will be an intermediate list storing temporary result
				int x=0,y=0,no=0;int doc=0,finalcount=0;
				int comparison=0;
				int oCount=parray.size()-1;
				for(int j=1;j<=oCount;j++)           // for all the other terms except first
				{
				
					while(x<temp.size()&&y<parray.get(j).size())  // if first posting list or second/third are not completely traversed
					{
						if(temp.get(x)< (Integer) parray.get(j).get(y))  //if doc id in first list is less than second, add it to temp2
						{
							temp2.add(temp.get(x));
							x++;
							doc++;
							comparison++;
						}
						else if(temp.get(x)>(Integer)parray.get(j).get(y)) //if doc id in second list is less, copy to temp2
						{
							temp2.add((Integer)parray.get(j).get(y));
							y++;
							doc++;
							comparison++;
						}
						else       // if doc id of first and second lists are equal, append to temp2, increment matched document counter
						{
							temp2.add(temp.get(x));
							x++;
							y++;
							doc++;
							no++;
							comparison++;
						}
					}
					while(x<temp.size())  //if first list is not completely traversed
					{
						temp2.add(temp.get(x));
						x++;
						doc++;
						//comparison++;
					}
					while(y<parray.get(j).size()) // if second list is not completely traversed
					{
						temp2.add((Integer)parray.get(j).get(y));
						y++;
						doc++;
						//comparison++;
					}
					x=0;        // set the counters to 0 for comparing the next postings from the beginning
					y=0;
					temp.clear(); // clear the first temp array and add the obtained temporary array to the first for checking with other terms postings in next iteration
					for(int i=0;i<temp2.size();i++){
						temp.add(temp2.get(i));
					}
					temp2.clear();
					if(no==oCount)
					{
						finalcount++;
					}
				}
		
			
			System.out.println("TaatOr");
			for(int i=0;i<terms.length;i++){
				System.out.print(terms[i]+" ");
			}
			System.out.println();
			System.out.print("Results: ");
			if(temp.size()==0)
			{
				System.out.print("empty");
			}
			for(int i=0;i<temp.size();i++)
			{
				System.out.print(temp.get(i)+" ");
			}
			System.out.println();
			System.out.print("Number of documents in results: ");
			System.out.println(temp.size());
			System.out.print("Number of comparisons: ");
			System.out.println(comparison);
			
		}
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------

	public static void DaatAnd(String[] terms)throws IOException
	{
		
		LinkedList<Integer> temp2=new LinkedList<Integer>(); //create another linked list, this will be an intermediate list storing temporary result
		int j=1,x=0,y=0,no=0;int doc=0; int finalcount=0;
		int comparison=0;
	
	
		boolean flag=false;
		int tempo=0,tempo1=0,tempo2=0;;
/*		
		LinkedList<Integer> sort=new LinkedList<Integer>();
		for(tempo=0;tempo<parray.size();tempo++)
		{
			sort.add(parray.get(tempo).size());
		}
//		System.out.println(sort);
		
		Collections.sort(sort);
		Collections.reverse(sort);
//		System.out.println(sort);
		
		ArrayList<LinkedList> temparray = new ArrayList<LinkedList>();
		
		ArrayList<LinkedList> comparearray = new ArrayList<LinkedList>();
		
		
		for(int z=0;z<parray.size();z++)
		{
			temparray.add(parray.get(z));
		}
//		System.out.println(temparray);
		
		
		while(x<sort.size())
		{
			if(sort.get(x)==temparray.get(y).size())
			{
				comparearray.add(temparray.get(y));
				x++;
				y++;
				if(y>=temparray.size())
				{
					y=0;
				}
			}
			else
			{
				y++;
			}
		}
		sort.clear();
		temparray.clear();
//		System.out.println(comparearray);  
		
		int oCount=comparearray.size()-1;
		LinkedList<Integer> temp=new LinkedList();
		for(int z=0;z<comparearray.get(0).size();z++)
		{
			temp.add((Integer)comparearray.get(0).get(z));    // store the first posting list in a temporary linked list temp
		}
		
		/*
		 * 
		 */
		//------ New Logic
		
/*		
		
	
		
		LinkedList<Integer> input1=new LinkedList<Integer>();
		input1.add(6);
		input1.add(7);
		input1.add(8);
		
		LinkedList<Integer> input2=new LinkedList<Integer>();
		input2.add(1);
		input2.add(2);
		input2.add(3);
		
		LinkedList<Integer> input3=new LinkedList<Integer>();
		input3.add(1);
		input3.add(3);
		input3.add(4);
		input3.add(5);
		
		ArrayList<LinkedList> sample=new ArrayList<LinkedList>();
		sample.add(input1);
		sample.add(input2);
		sample.add(input3);
		System.out.println(sample);
		
		int[] a=new int[sample.size()];
		for(int z=0;z<a.length;z++)
		{
			a[z]=0;
			//System.out.println(a[z]);
		}
		int min=99999999;
		int endcount=0;
		while(endcount<sample.size())
		{
			for(int i=0;i<sample.size();i++)
			{
				if(a[i]<sample.get(i).size())
				{
					if(min>(Integer)sample.get(i).get(a[i]))
					{
						min=(Integer)sample.get(i).get(a[i]);
					}
				}
				System.out.println(min);
				
			}
			
			for(int i=0;i<sample.size();i++)
			{
				if(a[i]<sample.get(i).size())
				{
					if(min==(Integer)sample.get(i).get(a[i]))
					{
						a[i]=a[i]+1;
						if(a[i]==sample.get(i).size())
						{
							endcount++;
						}
						comparison++;
					}
				}
				System.out.println(a[i]);
			}
			temp2.add(min);
			System.out.println(temp2);
			min=9999999;
		}
		*/
		int[] a=new int[parray.size()];
		for(int z=0;z<a.length;z++)
		{
			a[z]=0;
			//System.out.println(a[z]);
		}
		int min=99999999;
		int endcount=0;
		int andcount=0;
		while(endcount<parray.size())  // this loop will iterate when each posting list for each term reaches to an end
		{
			for(int i=0;i<parray.size();i++)  //check for each element vertically in every list one by one
			{
				if(a[i]<parray.get(i).size()) // we dont check if pointer of any postings list has reached its end
				{
					if(min>(Integer)parray.get(i).get(a[i]))  // if pointer is within the postings list check if the element on that position is equal to > or < or equal to min
					{
						min=(Integer)parray.get(i).get(a[i]);  // set minimum as that element
						comparison++;
					}
				}
//				System.out.println(min);
				
			}
			
			for(int i=0;i<parray.size();i++)  //for all postings iterate to see if any element is equal to minimum
			{
				if(a[i]<parray.get(i).size())
				{
					if(min==(Integer)parray.get(i).get(a[i]))  //if element=minimum increment pointer
					{
						andcount++;
						a[i]=a[i]+1;
						if(a[i]>=parray.get(i).size())
						{
							endcount++;
						}
					//	comparison++;
						
					}
				}
//				System.out.println(a[i]);
			}
			if(andcount==parray.size())
			{
			temp2.add(min);
//			System.out.println(temp2);
			
			}
			min=9999999;
			andcount=0;
		}
		
		System.out.println("DaatAnd");
		for(int z=0;z<terms.length;z++){
			System.out.print(terms[z]+" ");
		}
		System.out.println();
		System.out.print("Results: ");
		if(temp2.size()==0)
		{
			System.out.print("empty");
		}
		for(int z=0;z<temp2.size();z++)
		{
			System.out.print(temp2.get(z)+" ");
		}
		System.out.println();
		System.out.print("Number of documents in results: ");
		System.out.println(temp2.size());
		System.out.print("Number of comparisons: ");
		if(comparison>1)
		{
			System.out.println(comparison-1);
		}
		else
		{
			System.out.println(comparison);
		}
		
	}
	
	//-------------------------------------------------------------------------------------------------------------------------------
	
	public static void DaatOr(String[] terms)throws IOException
	{
		
		LinkedList<Integer> temp2=new LinkedList<Integer>(); //create another linked list, this will be an intermediate list storing temporary result
		int j=1,x=0,y=0,no=0;int doc=0; int finalcount=0;
		int comparison=0;
	
	
		boolean flag=false;
		int tempo=0,tempo1=0,tempo2=0;;
/*		
		LinkedList<Integer> sort=new LinkedList<Integer>();
		for(tempo=0;tempo<parray.size();tempo++)
		{
			sort.add(parray.get(tempo).size());
		}
//		System.out.println(sort);
		
		Collections.sort(sort);
		Collections.reverse(sort);
//		System.out.println(sort);
		
		ArrayList<LinkedList> temparray = new ArrayList<LinkedList>();
		
		ArrayList<LinkedList> comparearray = new ArrayList<LinkedList>();
		
		
		for(int z=0;z<parray.size();z++)
		{
			temparray.add(parray.get(z));
		}
//		System.out.println(temparray);
		
		
		while(x<sort.size())
		{
			if(sort.get(x)==temparray.get(y).size())
			{
				comparearray.add(temparray.get(y));
				x++;
				y++;
				if(y>=temparray.size())
				{
					y=0;
				}
			}
			else
			{
				y++;
			}
		}
		sort.clear();
		temparray.clear();
//		System.out.println(comparearray);  
		
		int oCount=comparearray.size()-1;
		LinkedList<Integer> temp=new LinkedList();
		for(int z=0;z<comparearray.get(0).size();z++)
		{
			temp.add((Integer)comparearray.get(0).get(z));    // store the first posting list in a temporary linked list temp
		}
		
		/*
		 * 
		 */
		//------ New Logic
		
/*		
		
	
		
		LinkedList<Integer> input1=new LinkedList<Integer>();
		input1.add(6);
		input1.add(7);
		input1.add(8);
		
		LinkedList<Integer> input2=new LinkedList<Integer>();
		input2.add(1);
		input2.add(2);
		input2.add(3);
		
		LinkedList<Integer> input3=new LinkedList<Integer>();
		input3.add(1);
		input3.add(3);
		input3.add(4);
		input3.add(5);
		
		ArrayList<LinkedList> sample=new ArrayList<LinkedList>();
		sample.add(input1);
		sample.add(input2);
		sample.add(input3);
		System.out.println(sample);
		
		int[] a=new int[sample.size()];
		for(int z=0;z<a.length;z++)
		{
			a[z]=0;
			//System.out.println(a[z]);
		}
		int min=99999999;
		int endcount=0;
		while(endcount<sample.size())
		{
			for(int i=0;i<sample.size();i++)
			{
				if(a[i]<sample.get(i).size())
				{
					if(min>(Integer)sample.get(i).get(a[i]))
					{
						min=(Integer)sample.get(i).get(a[i]);
					}
				}
				System.out.println(min);
				
			}
			
			for(int i=0;i<sample.size();i++)
			{
				if(a[i]<sample.get(i).size())
				{
					if(min==(Integer)sample.get(i).get(a[i]))
					{
						a[i]=a[i]+1;
						if(a[i]==sample.get(i).size())
						{
							endcount++;
						}
						comparison++;
					}
				}
				System.out.println(a[i]);
			}
			temp2.add(min);
			System.out.println(temp2);
			min=9999999;
		}
		*/
		int[] a=new int[parray.size()];
		for(int z=0;z<a.length;z++)
		{
			a[z]=0;
			//System.out.println(a[z]);
		}
		int min=99999999;
		int endcount=0;
		while(endcount<parray.size())  // this loop will iterate when each posting list for each term reaches to an end
		{
			for(int i=0;i<parray.size();i++)  //check for each element vertically in every list one by one
			{
				if(a[i]<parray.get(i).size()) // we dont check if pointer of any postings list has reached its end
				{
					if(min>(Integer)parray.get(i).get(a[i]))  // if pointer is within the postings list check if the element on that position is equal to > or < or equal to min
					{
						min=(Integer)parray.get(i).get(a[i]);  // set minimum as that element
						comparison++;
					}
				}
//				System.out.println(min);
				
			}
			
			for(int i=0;i<parray.size();i++)  //for all postings iterate to see if any element is equal to minimum
			{
				if(a[i]<parray.get(i).size())
				{
					if(min==(Integer)parray.get(i).get(a[i]))  //if element=minimum increment pointer
					{
						a[i]=a[i]+1;
						if(a[i]>=parray.get(i).size())
						{
							endcount++;
						}
					//	comparison++;
					}
				}
//				System.out.println(a[i]);
			}
			temp2.add(min);
//			System.out.println(temp2);
			min=9999999;
		}
		
		System.out.println("DaatOr");
		for(int z=0;z<terms.length;z++){
			System.out.print(terms[z]+" ");
		}
		System.out.println();
		System.out.print("Results: ");
		if(temp2.size()==0)
		{
			System.out.print("empty");
		}
		for(int z=0;z<temp2.size();z++)
		{
			System.out.print(temp2.get(z)+" ");
		}
		System.out.println();
		System.out.print("Number of documents in results: ");
		System.out.println(temp2.size());
		System.out.print("Number of comparisons: ");
		if(comparison>1)
		{
			System.out.println(comparison-1);
		}
		else
		{
			System.out.println(comparison);
		}
		
	}
	
	
	
	
	
}


