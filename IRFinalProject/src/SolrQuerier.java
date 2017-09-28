

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.language.v1.CloudNaturalLanguage;
import com.google.api.services.language.v1.CloudNaturalLanguageScopes;
import com.google.api.services.language.v1.model.AnalyzeEntitiesRequest;
import com.google.api.services.language.v1.model.AnalyzeEntitiesResponse;
import com.google.api.services.language.v1.model.Document;
import com.google.api.services.language.v1.model.Entity;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Span;

import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kedar on 12/3/16.
 */
public class SolrQuerier {
    private static final int MAX_DOCS_TO_RETURN = 20;
    static String urlString = "http://52.43.176.194:8983/solr/project4";//
    static SolrClient client = new HttpSolrClient.Builder(urlString).build();

    public List<String> getSearchResults(String rawQuery) {
        List<String> finalResults = new ArrayList<String>();   //max size twenty
        try {
            /*          Alchemy API (UNUSED)
            AlchemyLanguage service = new AlchemyLanguage();
            service.setApiKey("4b7df666e7a512f4976dcf9ed6a721dc5f23a595");
            Map<String,Object> params = new HashMap<String, Object>();*/

            //Google NLP:
            NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory factory = JacksonFactory.getDefaultInstance();
            GoogleCredential cred=GoogleCredential.fromStream(new FileInputStream(new File("/home/ubuntu/nlptrial-88daaad64ce6.json"))).createScoped(CloudNaturalLanguageScopes.all());
            CloudNaturalLanguage language= new CloudNaturalLanguage.Builder(transport, factory, cred).setApplicationName("Tweet").build();

            //OpenNLP:
            InputStream posModelIS= new FileInputStream("/home/ubuntu/en-pos-maxent.bin"); ///home/ubuntu/en-pos-maxent.bin
            POSModel model1 = new POSModel(posModelIS);
            POSTaggerME tagger = new POSTaggerME(model1);

            InputStream dateIS= new FileInputStream("/home/ubuntu/en-ner-date.bin"); ///home/ubuntu/en-ner-date.bin
            InputStream timeIS= new FileInputStream("/home/ubuntu/en-ner-time.bin"); ///home/ubuntu/en-ner-time.bin
            TokenNameFinderModel modelDate = new TokenNameFinderModel(dateIS);
            TokenNameFinderModel modelTime = new TokenNameFinderModel(timeIS);

            NameFinderME dateFinder=new NameFinderME(modelDate);
            NameFinderME timeFinder=new NameFinderME(modelTime);

//            String rawQuery="when did demonetization occur";//who has suffered due to demonetization

            String splitRawQuery[]=rawQuery.split(" ");
            String [] taggedString= tagger.tag(splitRawQuery);
            String processedQuery="";
            if(sanitizeQuery(rawQuery).equals(null)) {
                System.out.println("Error in query");
                //abort query. keep user on same page
            }

            for(int i=0;i<taggedString.length;i++) {
                String element=taggedString[i];
                String newTerm="";
                if(element.equals("NNP")) {
                    newTerm=splitRawQuery[i];// + "^5";
                }
                else if(element.equals("NN") || element.equals("NNS") || element.equals("NNPS") || element.equals("NNP")) {
                    newTerm=splitRawQuery[i];    //boost?
                }
                else if(element.equals("VB") || element.equals("VBD") || element.equals("VBG") || element.equals("VBN") || element.equals("VBP") || element.equals("VBZ")) {
                    newTerm=splitRawQuery[i];
                }
                else if(element.equals("JJ") || element.equals("JJS") || element.equals("JJR")) {
                    newTerm=splitRawQuery[i];
                }
                else if(element.equals("RB") || element.equals("RBR") || element.equals("RBS")) {
                    newTerm=splitRawQuery[i];
                }
                if(!newTerm.isEmpty())
                    processedQuery = processedQuery + " " + newTerm;
            }
            System.out.println("Original query:"+rawQuery);
            System.out.println("Processed query:"+processedQuery);
            System.out.println();

            ArrayList<String> solrQueryResults = new ArrayList<String>();
            ArrayList<String> cleanedSolrQueryResults = new ArrayList<String>();
            QueryResponse resp = client.query(new SolrQuery(processedQuery).setRows(10000));

            //clean tweets somewhat including basic deduplication
            for(SolrDocument d:resp.getResults()) {
            	
                String rawTweetText= (String) d.getFieldValue("tweet_text");
            	
                if(rawTweetText.contains("milkman")) {
                    System.out.println();
                }
                rawTweetText = rawTweetText.replaceAll("http(s)*:(\\/\\/)*[A-Za-z0-9./?=&-_…]*","");
                rawTweetText = rawTweetText.replaceAll("…","");
                rawTweetText = rawTweetText.replaceAll("\n","");
                rawTweetText=rawTweetText.replaceFirst("RT ([a-zA-Z@_-])+:","");    //remove RT @mention:
                rawTweetText = rawTweetText.trim();
//                rawTweetText = rawTweetText.toLowerCase();

                boolean substringExists=false;
                for(String s:cleanedSolrQueryResults) {
                    if(s.contains(rawTweetText) || rawTweetText.contains(s)) {
                        substringExists=true;
                        break;
                    }
                }
                if(substringExists) continue;
                if(!cleanedSolrQueryResults.contains(rawTweetText)) {   //do duplicate checking on cleaned tweets
                    cleanedSolrQueryResults.add(rawTweetText);

                    solrQueryResults.add((String) d.getFieldValue("tweet_text"));   //add original tweet as well
                }

            }

            System.out.println("Results from Solr:"+solrQueryResults.size()+" tweets matched");


            if(rawQuery.toLowerCase().indexOf("who")==0 || (rawQuery.toLowerCase().indexOf("where")==0)) {
            for(int i=0;i<50 && i<cleanedSolrQueryResults.size();i++) {

                AnalyzeEntitiesRequest req=new AnalyzeEntitiesRequest();
                Document doc=new Document();
                doc.setContent(cleanedSolrQueryResults.get(i)); //solrQueryResults.get(i)
                doc.setType("PLAIN_TEXT");
                req.setDocument(doc);
                AnalyzeEntitiesResponse entities;
                try {
                entities = language.documents().analyzeEntities(req).execute();
                }catch(GoogleJsonResponseException ex) {
                	System.out.println("Parse exception!");
                	continue;
                }
                //EVENT, ORGANIZATION, CONSUMER GOOD
                if(rawQuery.toLowerCase().indexOf("who")==0) {  //who type of question
                    boolean found=false;
                for(Entity e:entities.getEntities()) {
                    if(e.getType().equals("PERSON")) {
                        found=true;
                        break;
//                        System.out.println("Person found:"+e.getName());
                    }
                }
                if(found) {
//                    System.out.println(solrQueryResults.get(i));
                    finalResults.add(solrQueryResults.get(i));
                }
                }
                else if(rawQuery.toLowerCase().indexOf("where")==0) {
                    boolean found=false;
                    for(Entity e:entities.getEntities()) {
                        if(e.getType().equals("LOCATION")) {
                            found=true;
                            break;
                        }
                    }
                    if(found) {
//                    System.out.println(solrQueryResults.get(i));
                        finalResults.add(solrQueryResults.get(i));
                    }
                }
                }

            }else if(rawQuery.toLowerCase().indexOf("when")==0) {


                for(int i=0;i<cleanedSolrQueryResults.size();i++) {            //process all documents
                    String input = cleanedSolrQueryResults.get(i);
                    Span[] s = dateFinder.find(input.split(" "));
                    String []splitOutput = (Span.spansToStrings(s,input.split(" ")));
                    if(splitOutput.length>0) {
//                        System.out.println(cleanedSolrQueryResults.get(i));
                        finalResults.add(solrQueryResults.get(i));
                    }
                }
            } else {	//no wh- type of question
            	
            	finalResults = solrQueryResults;
            	
            		
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        if(finalResults.size()>MAX_DOCS_TO_RETURN)
            finalResults = finalResults.subList(0,MAX_DOCS_TO_RETURN);
        return finalResults;
    }

    private static String sanitizeQuery(String query) {
        String sanitized = query;
        if(sanitized.trim().isEmpty())
            return null;
        sanitized ="";
        String words[] = {"Narendra","Modi","Arvind","Kejriwal","Nitish","Kumar","India","Pakistan","Gujarat",
        "Axis","Balakrishna","Chennai","Mumbai","Delhi","Gandhi","Rahul","Namo","Pappu","Sonia","Forbes","ATM","Congress","Bangalore",
                "Ludhiana","Punjab","SBI","Mayawati","Karnataka","Andhra"};
      /*  for(String w:words) {
            if(sanitized.toLowerCase().contains(w.toLowerCase()))
                sanitized+=w;
            else
                sanitized
        }*/

        int j=0;
        String splitString[] = query.split(" ");
        for(int i=0;i<splitString.length;i++) {
          boolean substringFound = false;
          for(j=0;j<words.length;j++) {
              if (splitString[i].toLowerCase().equals(words[j].toLowerCase())) {
                    substringFound = true;
                    break;
              }
            }
            if(substringFound)
                sanitized = sanitized + " " + words[j];
              else
                sanitized = sanitized + " " + splitString[i];


      }

    return stripNonAlphaNumericsChars(sanitized);
    }



    private static String stripNonAlphaNumericsChars(String orig) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < orig.length(); ++i) {
            char c = orig.charAt(i);
            if(c >= 97 && c <= 122 || c >= 65 && c <= 90 || c >= 48 && c <= 57 || c==32) {
                sb.append(c);
            }
        }

        return sb.toString();
    }


}
