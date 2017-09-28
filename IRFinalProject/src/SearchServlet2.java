

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.security.auth.login.Configuration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.noggit.JSONUtil;

@WebServlet("/SearchServlet")
public class SearchServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
   

	protected void doGet(HttpServletRequest request, HttpServletResponse response, SolrDocumentList results) throws ServletException, IOException {
	
		
	}

	public static void POSTag(String result,HttpServletResponse response) throws IOException 
	{/*
		POSModel model = new POSModelLoader()	
			.load(new File("/home/arnav/Downloads/en-pos-maxent.bin"));
		//PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);
	 
		
		String input = result;
		result.replace("@", "");
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new StringReader(input));
		
		//perfMon.start();
		String line;
		while ((line = lineStream.read()) != null) {
			
			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
					.tokenize(line);
			String[] tags = tagger.tag(whitespaceTokenizerLine);
			
			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			//System.out.println(sample.toString());
			response.setContentType("text/html");
			PrintWriter pw=response.getWriter();
			pw.println(sample.toString());
			pw.println("<br>");
			
			//perfMon.incrementCounter();
		}
		//perfMon.stopAndPrintFinalResult();*/
	}
	
/*	
public static void POSTag() throws IOException {
		
		InputStream modelIn = null;

		try {
		  modelIn = new FileInputStream("/home/ashwin/Downloads/en-pos-maxent.bin");
		  POSModel model = new POSModel(modelIn);
		  POSTaggerME tagger = new POSTaggerME(model);
		  
		  String strName = "Who helps you when you are sick ?";
		  String sent[] = strName.split(" ");
		  
		  
		  
		  //String sent[] = new String[]{"Most", "large", "cities", "in", "the", "US", "had",
	                //"morning", "and", "afternoon", "newspapers", "."};		  
			String tags[] = tagger.tag(sent);
			
			LinkedList<String> query = new LinkedList();
			
			for(int i=0;i<tags.length;i++){
				System.out.print(sent[i]+"->"+tags[i]+" ");
			}
			
			int i;
			A: for(i=0;i<sent.length;i++){
				if(sent[i].contains("Who")){
					System.out.println("\nPerson or Organization that");
				}
		
				if(tags[i].equals("JJ") || tags[i].equals("PRP") || tags[i].equals("VB") || tags[i].equals("NNP") || tags[i].equals("VBD") || tags[i].equals("NNPS") || tags[i].equals("VBN") 
						|| tags[i].equals("VBP") || tags[i].equals("VBZ") || tags[i].equals("VBG") || tags[i].equals("NNS") || tags[i].equals("NN")){
					System.out.print(sent[i]+" ");
					query.add(sent[i]);
				}
			}
			System.out.println("\n"+query);
			//System.out.println(sent[i]);
			
			
			
		}
		catch (IOException e) {
		  // Model loading failed, handle the error
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		
		
	}
	
	
	
*/	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		PrintWriter pw=response.getWriter();
		InputStream modelIn = null;
		modelIn = new FileInputStream("/home/arnav/Downloads/en-pos-maxent.bin");
		POSModel model = new POSModel(modelIn);
		POSTaggerME tagger = new POSTaggerME(model);
		  
		
		
		String str=request.getParameter("tBSearch");
		pw.println(str+"<br>");
		
		String sent[] = str.split(" ");
		String tags[] = tagger.tag(sent);
		
		
		LinkedList<String> query1 = new LinkedList();
		
		for(int i=0;i<tags.length;i++){
			System.out.print(sent[i]+"->"+tags[i]+" ");
		}
		
		int i;
		A: for(i=0;i<sent.length;i++){
			if(sent[i].contains("who") || sent[i].contains("Who")){
				//System.out.println("\nPerson or Organization that");
				pw.println("Person or Organization that<br>");
			}
	
			if(tags[i].equals("JJ") || tags[i].equals("PRP") || tags[i].equals("VB") || tags[i].equals("NNP") || tags[i].equals("VBD") || tags[i].equals("NNPS") || tags[i].equals("VBN") 
					|| tags[i].equals("VBP") || tags[i].equals("VBZ") || tags[i].equals("VBG") || tags[i].equals("NNS") || tags[i].equals("NN")){
				pw.println(sent[i]+" ");
				query1.add(sent[i]);
			}
		}
		
		pw.println("<br>");
		//System.out.println("\n"+query);
		//System.out.println(sent[i]);
		
		
		
		String mainq = null;
		for(int w = 0;w<query1.size();w++){
			mainq+=query1.get(w);
			mainq+=" ";
		}
		
		
		response.setContentType("text/html");
		//PrintWriter pw=response.getWriter();
		//pw.println(str);
		 SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/proj4").build();

		    SolrQuery query = new SolrQuery(str);
		    query.setQuery(mainq);  //!!!
		    query.setRows(10);
		    
		    
		    try
		    {
		    	   QueryResponse resp = solr.query(query);
		    	    SolrDocumentList results = resp.getResults();

		    	    for (int j = 0; j < results.size(); ++j) {
		    	    	String jsonFormat=JSONUtil.toJSON(results.get(j).getFieldValues("tweet_text"));
		    	    	POSTag(jsonFormat,response);
		    	      //  pw.println(jsonFormat);
		    	       // pw.println("<br>");
		    	      }
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		    
		*/
		ArrayList<String> aList=new ArrayList<>();
		//ArrayList<SolrDocument> aList=new ArrayList<SolrDocument>();
		String str=request.getParameter("tSearch");
	    request.setAttribute("query", str);
		SolrClient solr = new HttpSolrClient.Builder("http://52.43.176.194:8983/solr/project4").build();
		 

		    SolrQuery query = new SolrQuery(str);
		    query.setQuery(str);  //!!!
		    query.setRows(10);
		    
		    
		    try
		    {
		    	   QueryResponse resp = solr.query(query);
		    	    SolrDocumentList results = resp.getResults();

		    	    for (int j = 0; j < results.size(); ++j) {
		    	    	String jsonFormat=JSONUtil.toJSON(results.get(j).getFieldValues("tweet_text"));
		    	    	aList.add(jsonFormat);
		    	    //	aList.add(results.get(j));
		    	      //  pw.println(jsonFormat);
		    	       // pw.println("<br>");
		    	      }
		    	    
		    	request.setAttribute("posts",aList);
		    	 //   request.setAttribute("posts",aList);
		    	    request.getRequestDispatcher("JAVASCRIPT/servResp2.jsp").forward(request, response);
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		
	}

}
