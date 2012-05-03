/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facebooklinkfinder;


//imports in the program

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author ajith
 */
public class Facebooklinkfinder {
    
    
    
    private static final Logger log = Logger.getLogger(Facebooklinkfinder.class);
    private OAuthConsumer consumer = null;
    private String responseBody = "";

    protected static String yahooServer = "http://yboss.yahooapis.com/ysearch/";


// Please provide your consumer key here
private static String consumer_key = "dj0yJmk9RFFwYVpaeXJwa29IJmQ9WVdrOU4zWkROa3haTXpBbWNHbzlNVGM1TURBeU1EazJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD0xOA--";

// Please provide your consumer secret here
private static String consumer_secret = "bf6cca46d109013c0a0c0da0050a44651936da0b";
/** Encode Format */
private static final String ENCODE_FORMAT = "UTF-8";

/** Call Type */
private static final String callType = "web";


 private static  String replaceurl(String content) {
        
   content= content.replace("/","%2F");
   content = content.replace(":", "%3A");
   return content;
    }
    
    
    public int returnHttpData(String query) throws UnsupportedEncodingException, Exception{
        int status = 0;
       
// Start with call Type
        String params = callType;
//String params = "";
// Add query
        params = params.concat("?q=" + query+"&count=10&sites=facebook.com%2Ctwitter.com");

        System.out.println(params);
// Encode Query string before concatenating
//params = params.concat(URLEncoder.encode(this.getSearchString(), "UTF-8"));

// Create final URL
        String url = yahooServer + params;

        OAuthConsumer consumer = new DefaultOAuthConsumer(consumer_key, consumer_secret);

        setOAuthConsumer(consumer);
               
        URLDecoder.decode(url, ENCODE_FORMAT);
        
        int responseCode = sendGetRequest(url); 
        return status;
        
    }
    
    public int sendGetRequest(String url) throws IOException,OAuthMessageSignerException,OAuthExpectationFailedException,OAuthCommunicationException {
    
    System.out.println("url in sendrequest= " + url);
        int responseCode = 500;
        try {
            HttpURLConnection uc = getConnection(url);
            
            responseCode = uc.getResponseCode();
            
            if(200 == responseCode || 401 == responseCode || 404 == responseCode){
                BufferedReader rd = new BufferedReader(new InputStreamReader(responseCode==200?uc.getInputStream():uc.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                    
                }
                String response = sb.toString();
     try{
   JSONObject json = new JSONObject(response);
 
   System.out.println("\nResults:");
   System.out.println("Total results = " +
           json.getJSONObject("bossresponse").getJSONObject("web").getString("totalresults"));

   
           System.out.println();
          
           JSONArray ja = json.getJSONObject("bossresponse").getJSONObject("web").getJSONArray("results");
           System.out.println("ja =" + ja);
          
           BufferedWriter out = new BufferedWriter(new FileWriter("outfile.txt",true));
           
           homepage(out);
           
           String str = "";
           System.out.println("\nResults:");
          // for(int i=0;i<3;i++){
           
           for (int i = 0; i < ja.length(); i++) {
            // System.out.print((i+1) + ". ");
             JSONObject j = ja.getJSONObject(i);
             str += j.getString("url")+ "," +j.getString("abstract") + "\n";
             out.write(str);
             
            
            }
            System.out.println("the string is ");
            
            System.out.println(str);
            
            out.close();
                    
  }
  catch (Exception e) {
   System.err.println("Something went wrong...");
   e.printStackTrace();
  }       
                rd.close();
                setResponseBody(sb.toString());
            }
         } catch (MalformedURLException ex) {
            throw new IOException( url + " is not valid");
        } catch (IOException ie) {
            throw new IOException("IO Exception " + ie.getMessage());
        }
        
        return responseCode;
    }
    
    
    
    
     public HttpURLConnection getConnection(String url) 
    throws IOException,
        OAuthMessageSignerException,
        OAuthExpectationFailedException, 
        OAuthCommunicationException
    {
     try {
             URL u = new URL(url);

             HttpURLConnection uc = (HttpURLConnection) u.openConnection();
             
             if (consumer != null) {
                 try {
                     log.info("Signing the oAuth consumer");
                     consumer.sign(uc);
                     
                 } catch (OAuthMessageSignerException e) {
                     log.error("Error signing the consumer", e);
                     throw e;

                 } catch (OAuthExpectationFailedException e) {
                 log.error("Error signing the consumer", e);
                 throw e;
                 
                 } catch (OAuthCommunicationException e) {
                 log.error("Error signing the consumer", e);
                 throw e;
                 }
                 uc.connect();
             }
             return uc;
     } catch (IOException e) {
     log.error("Error signing the consumer", e);
     throw e;
     }
    }
    
    
    
    
    public void setOAuthConsumer(OAuthConsumer consumer) {
        this.consumer = consumer;
    }
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
   

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }

     public String getResponseBody() {
        return responseBody;
    }
    
    public void setResponseBody(String responseBody) {
        if (null != responseBody) {
            
            this.responseBody = responseBody;
        }
    }
    
    public void homepage(BufferedWriter out1)throws UnsupportedEncodingException, Exception{
    
       
        
        String url1 = "http://www.logica.in";
       // String content = "http://logica.in";
      ////  content=replaceurl(content);
        
      //  out.write(url + " " +content);
        Document doc = Jsoup.connect(url1).get();
        
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        
         Document doc1 = Jsoup.parse(url1);
         
         
         
        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            String urltext = doc.body().text();
            String linkhref = link.attr("href");
            String linktext = link.text();
            String linkabshref = link.attr("abs:href");
            
            
            if ( linkabshref.contains("www.facebook.com/") || linkabshref.contains("www.twitter.com/") ){
                 System.out.println("found link");
                 System.out.println("urltext = " +  urltext);
                 System.out.println("linkhref = " + linkhref);
                 System.out.println("linktext = " + linktext);
            
                 System.out.println("linkabshref = " + linkabshref);
                
                 
                 System.out.println();
                 out1.write("\nlinkabshref = " + linkabshref);
//                 out.write("\n");
//                 
                     
           }
        }
        return;
    }
    
    
     public static void main(String[] args) throws IOException, UnsupportedEncodingException {
        
        BasicConfigurator.configure();
        try{
        
            
            
//        
//        String url = "http://www.logica.in";
        String content = "http://logica.in";
        content=replaceurl(content);
//        
//        out.write(url + " " +content);
//        Document doc = Jsoup.connect(url).get();
//        
//        Elements links = doc.select("a[href]");
//        Elements media = doc.select("[src]");
//        Elements imports = doc.select("link[href]");
//        
//         Document doc1 = Jsoup.parse(url);
//         
//         
//         
//        print("\nLinks: (%d)", links.size());
//        for (Element link : links) {
//            String urltext = doc.body().text();
//            String linkhref = link.attr("href");
//            String linktext = link.text();
//            String linkabshref = link.attr("abs:href");
//            
//            
//            if ( linkabshref.contains("www.facebook.com/") || linkabshref.contains("www.twitter.com/") ){
//                 System.out.println("found link");
//                 System.out.println("urltext = " +  urltext);
//                 System.out.println("linkhref = " + linkhref);
//                 System.out.println("linktext = " + linktext);
//            
//                 System.out.println("linkabshref = " + linkabshref);
//                
//                 
//                 System.out.println();
//                 out.write("\nlinkabshref = " + linkabshref);
////                 out.write("\n");
////                 
//                     
          // }
        //}
        Facebooklinkfinder facebooklinkfinder = new Facebooklinkfinder();
        
        
        facebooklinkfinder.returnHttpData(content);
        //out.close();
        
        }catch(Exception e)
        {}
    }
}
        
    