package uk.gov.courtservice.xhibit.business.services.courtel.httpservices;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.courtel.exceptions.CourtelListException;
import uk.gov.courtservice.xhibit.business.services.courtel.exceptions.ExceptionMessage;
import uk.gov.courtservice.xhibit.business.services.courtel.helpers.CourtelListHelper;
import uk.gov.courtservice.xhibit.business.services.courtel.helpers.Properties;
import uk.gov.courtservice.xhibit.business.services.courtel.utilities.ExceptionMessageLogger;

public class CourtelHttpService {

    private static final Logger log = CSServices.getLogger(CourtelHttpService.class);
    
    private final Properties properties;
    
    public CourtelHttpService(){
    	this(Properties.getInstance());
    }
    
    //constructor for mock testing - not intended for production use
    public CourtelHttpService(Properties properties) {
		this.properties = properties;
	}
    ExceptionMessageLogger messageLogger;
	
    public void setMessageLogger(ExceptionMessageLogger messageLogger){
    	this.messageLogger = messageLogger;
    }
    
    public void sendFileToCourtelSSL(CourtelListHelper courtelListHelper) throws CourtelListException {
       
    	setMessageLogger(new ExceptionMessageLogger());
    	final HttpPost httpPostCourtelServer1;
    	final HttpPost httpPostCourtelServer2;
    	final String serverOne = "Server1";
    	final String serverTwo = "Server2";
    	httpPostCourtelServer1 = new HttpPost(courtelListHelper.getCourtelServer1());
    	httpPostCourtelServer2 = new HttpPost(courtelListHelper.getCourtelServer2());
    	//send to server 1  	
    	try {
    		authenticateAndSend(httpPostCourtelServer1,courtelListHelper, serverOne);
    	} catch (CourtelListException e) {
    		final String detailMessage = e.getExceptionDetails().getDetail();
    		final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition(detailMessage, e.getExceptionDetails().getReason());
    		//there's been a problem, now try to send to server 2
    		authenticateAndSend(httpPostCourtelServer2,courtelListHelper, serverTwo);
       		//re-throw the exception back to caller to log in the database
    		throw new CourtelListException(exceptionMessage);
    	}
    	//send to server 2
    	try{
    		authenticateAndSend(httpPostCourtelServer2,courtelListHelper, serverTwo);
    	}catch (CourtelListException e) {
    		final String detailMessage = e.getExceptionDetails().getDetail();
    		final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition(detailMessage, e.getExceptionDetails().getReason());
       		//re-throw the exception back to caller to log in the database
    		throw new CourtelListException(exceptionMessage);
    	}
    	//delete the file
    	deleteFile(courtelListHelper);
    }


    public CourtelListHelper authenticateAndSend(final HttpPost httpPost, final CourtelListHelper courtelListHelper, final String server) throws CourtelListException {
       	String responseBody = null;
       	CloseableHttpResponse res = null;
       	SSLContext sslContext = null;
       	KeyManager[] keyManagers = null;
       	TrustManager[] trustManagers = null;
       	setMessageLogger(new ExceptionMessageLogger());
       	int courtelTimeOut = Integer.parseInt(properties.COURTEL_TIMEOUT);
       	
       	//pre-emptive authentication for basic authentication
       	String username = properties.COURTEL_USERNAME;
       	String password = properties.COURTEL_PASSWORD;
       	byte[] credentials = Base64.encodeBase64((username + ":" + password).getBytes(Charset.forName("ISO-8859-1")));
       	String authHeader = "Basic " + new String(credentials);
       	httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
       	
       	// load up key store certificates
       	try {
       		sslContext = SSLContext.getInstance("TLS");
       		keyManagers = getKeyManagers("jks", new FileInputStream(new File(properties.COURTEL_KEY_MANAGER_JKS)), properties.COURTEL_KEY_MANAGER_PASSWORD);
       		trustManagers = getTrustManagers("jks", new FileInputStream(new File(properties.COURTEL_TRUST_MANAGER_JKS)), properties.COURTEL_TRUST_MANAGER_PASSWORD); 	
       		sslContext.init(keyManagers, trustManagers, new SecureRandom());
       	}
       	catch (FileNotFoundException fnfe) {
    		final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition("Unable to locate keystore jks file", fnfe.getMessage());
       		throw new CourtelListException(exceptionMessage);
    	}
        catch (Exception e) {
    		final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition("General exception from SSL Certificate loading", e.getMessage());
       		throw new CourtelListException(exceptionMessage);  		
    	}

    	SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
    			new DefaultHostnameVerifier());

    	final HttpHost proxy = new HttpHost(properties.COURTEL_CGI_PROXY, Integer.parseInt(properties.COURTEL_CGI_PROXY_PORT));
    	
    	DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
    	
    	final HttpClientBuilder builder = HttpClients.custom().setSSLSocketFactory(sslsf).setRoutePlanner(routePlanner);

    	// read the zip file we want to send to Courtel
    	File inFile = new File(courtelListHelper.getZipFilePath());
    	//set helper with the filename
    	courtelListHelper.setFileName(inFile.getName());
    	
    	FileInputStream fileInputStream = null;
    	
    	try {
    		fileInputStream = new FileInputStream(inFile);
    	} catch (IOException e) {
    		log.error("Unable to read file");
    		e.printStackTrace();
    	}
    	// Post using Multipart
    	HttpEntity entity = MultipartEntityBuilder.create().
    			setMode(HttpMultipartMode.BROWSER_COMPATIBLE).			
    	addBinaryBody("file2upload", fileInputStream, ContentType.create("application/zip"), courtelListHelper.getFileName()).build();
    	httpPost.setEntity(entity);

    	log.debug("HttpPost (request) has been created.");
    	log.debug("Executing post..."+httpPost.getRequestLine());
    	// now submit to remote host using an executor service to implement time out functionality
    	java.util.concurrent.ExecutorService executor = Executors.newSingleThreadExecutor();
    	Future<CloseableHttpResponse> future = executor.submit(new Callable<CloseableHttpResponse>(){
    		
    		public CloseableHttpResponse call() throws Exception {
    			
    			CloseableHttpClient httpClient;
    			httpClient = builder.build();
    			CloseableHttpResponse response = httpClient.execute(httpPost);    		
        		return response;  		
    		} 	
    	    });        			
    		try {
    			res = future.get(courtelTimeOut, TimeUnit.SECONDS);
    			responseBody = EntityUtils.toString(res.getEntity());

    			setResponse(httpPost, courtelListHelper, responseBody, res); 
    			if(checkResponseCode(courtelListHelper)){
    				courtelListHelper.setValidResponse(true);
    			}
    			//if timeout limit has been reached then cancel the request and mark the db as a failure...
    		} catch (TimeoutException e){
    			executor.shutdownNow();
    			setResponse(httpPost, courtelListHelper, responseBody, res);
    			//log.debug("Time Out Exception called waiting for response from Courtel");
    			final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition("Time out waiting for response from Courtel", e.getMessage());
    	       	//set the server name that has caused this error
    			if(server.equals("Server1")){
    				courtelListHelper.setServerOneTimeOut(true);
    			}
    			else if(server.equals("Server2")){
    				courtelListHelper.setServerTwoTimeOut(true);
    			}
    			throw new CourtelListException(exceptionMessage); 
    		} catch (CourtelListException ce){
    			executor.shutdownNow();
    			log.debug("General exception from the server request to Courtel");
    			final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition(ce.getExceptionDetails().getDetail(), ce.getMessage());
    	       	throw new CourtelListException(exceptionMessage);  
    		} catch (Exception e){
    			executor.shutdownNow();
    			log.debug("General exception from the server request to Courtel");
    			final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition("Exception waiting for response from Courtel", e.getMessage());
    	       	throw new CourtelListException(exceptionMessage);  	
    		}
    		 finally {
    		    	//release resources
    		    	if (res != null && res.getEntity()!= null) {
    		    		// response.getEntity().consumeContent();
    		    		log.debug("Closing response...");
    		    		try {    				
    		    			//log.debug("RESPONSE:::" + response);
    		    			res.getEntity().getContent().close();
    		    		} catch (UnsupportedOperationException e) {
    		    			// TODO Auto-generated catch block
    		    			e.printStackTrace();
    		    		} catch (IOException e) {
    		    			// TODO Auto-generated catch block
    		    		e.printStackTrace();
    		    		}
        		    		log.debug("Response Closed.");
    		    		}
    		    		try {
    		    			if (fileInputStream != null)
    		    				fileInputStream.close();
    		    		} catch (IOException e) {
    		    			log.error("IOException raised from closing file stream");
    		    		}
    		    		executor.shutdown();
    		    }
    		return courtelListHelper; 	    	    	
    }        		
          		
    public void setResponse(HttpPost httpPost, CourtelListHelper courtelListHelper, String responseBody, CloseableHttpResponse res ){
    	if(httpPost.getURI().toString().equals(properties.COURTEL_SERVER_1)){
    		//update the courtel helper
    		courtelListHelper.setResponseBodyServer1(responseBody);
    		courtelListHelper.setResponseServer1(res);
    	}
    	if(httpPost.getURI().toString().equals(properties.COURTEL_SERVER_2)){
    		//update the courtel helper
    		courtelListHelper.setResponseBodyServer2(responseBody);
    		courtelListHelper.setResponseServer2(res);
    	}
    }
            
    public static KeyManager[] getKeyManagers(String keyStoreType, InputStream keyStoreFile, String keyStorePassword ) throws Exception {
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, keyStorePassword.toCharArray());
    return kmf.getKeyManagers();	  
    }

    public static TrustManager[] getTrustManagers(String trustStoreType, InputStream trustStoreFile, String trustStorePassword) throws Exception {
    KeyStore trustStore = KeyStore.getInstance(trustStoreType);
    trustStore.load(trustStoreFile, trustStorePassword.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    return tmf.getTrustManagers();
    }

    public boolean checkResponseCode(CourtelListHelper courtelListHelper) throws CourtelListException {
  
    int server1Status = 0;
    int server2Status = 0;
    String responseServer1 = "";
    String responseServer2 = "";
    boolean fileNameMatch = false;
    boolean successResponseCode = false;
    boolean uploadSuccess = false;
    boolean validResponse = false;

    if(courtelListHelper.getResponseServer1() !=null){
    	server1Status = courtelListHelper.getResponseServer1().getStatusLine().getStatusCode();	
    }
    if(courtelListHelper.getResponseServer2() !=null){
    	server2Status = courtelListHelper.getResponseServer2().getStatusLine().getStatusCode();	
    }
    //if either server reports a 200 then the sending was successful
    if (server1Status == 200 || server2Status == 200 )  {
    	successResponseCode = true;
    	String fileName = courtelListHelper.getFileName();
    	if(courtelListHelper.getResponseBodyServer1() !=null){
    		responseServer1 = courtelListHelper.getResponseBodyServer1();	
    	}
    	if(courtelListHelper.getResponseBodyServer2() !=null){
    		responseServer2 = courtelListHelper.getResponseBodyServer2();	
    	}	
    	//check response contains filename that was sent
    	if(responseServer1.contains(fileName) || responseServer2.contains(fileName)){
    		fileNameMatch = true;
    		log.debug("....STATUS 200 AND FILENAME MATCH FOUND ON ONE OR MORE SERVER RESPONSES....");
    	}
    	else{
    		log.info("....FILEMATCH ERROR ON RESPONSE....");
    	}			
    	//check response contains success message
    	if(responseServer1.contains("UPLOAD SUCCESS") || responseServer2.contains("UPLOAD SUCCESS")){
    		uploadSuccess = true;
    	}
    	else if(responseServer1.contains("UPLOAD FAILED") || responseServer2.contains("UPLOAD FAILED") ){
    		log.error("Upload has failed");
    	}
    	//only update db as successfully sent when 200 code and filename match
    	if(successResponseCode && fileNameMatch && uploadSuccess){
    		validResponse = true;
    	}
    	//calculate how many servers uploaded to 
    	int numberOfServers = 0;
    	if(server1Status == 200 && responseServer1.contains(fileName) && responseServer1.contains("UPLOAD SUCCESS") ){
    		numberOfServers = numberOfServers+1;
    	}
    	if(server2Status == 200 && responseServer2.contains(fileName) && responseServer2.contains("UPLOAD SUCCESS") ){
    		numberOfServers = numberOfServers+1;
    	}
    	//SET NUMBER OF SERVERS TO COURTEL HELPER
    	courtelListHelper.setNumberOfserversUploaded(numberOfServers);
    }
    else{
    	// one of the servers has thrown an error
    	final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition("One or more servers unreachable or network down", "Server unreachable");
    		throw new CourtelListException(exceptionMessage);
    }
    return validResponse;
    }

    private void deleteFile(CourtelListHelper courtelListHelper) {
       File inFile = new File(courtelListHelper.getZipFilePath());
    	inFile.delete();

    }

    public String generateZipFilePath(byte[] decodedBytes, String fileName) throws CourtelListException{

    	String srcFilename = properties.COURTEL_FILE_PATH+"/"+fileName;
        String zipFile = fileName;
        File f = null; 
        setMessageLogger(new ExceptionMessageLogger());
        
        //remove ".xml" from filename
        int end = zipFile.indexOf(".xml");
        if(end != -1){
      	  zipFile = zipFile.substring(0, end);
        }
        //append ".zip"
        zipFile  = properties.COURTEL_FILE_PATH+"/"+zipFile+".zip";
  	   try {
  		   //this will be a bufferedinputstream for the clob..
  		   	InputStream buf = new ByteArrayInputStream(decodedBytes);          
  		   	// create output file abstraction 
  		   	try{
  		   	 f = new File(srcFilename);
  		   	}
  		   	catch(Exception e){
  		   	final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition("Filepath not found, check configuration", e.getMessage());
       		throw new CourtelListException(exceptionMessage); 
  		   	}
            //file output stream - used to write data to a file
  		   	FileOutputStream fos = new FileOutputStream(zipFile);
  		   	log.debug("zipfile version is "+zipFile);
      
  		   	ZipOutputStream zos = new ZipOutputStream(fos);
		
  		   	//begin writing a new ZIP entry, positions the stream to the start of the entry data
  		   	zos.putNextEntry(new ZipEntry(f.getName()));
  		   	int lenght;
  		   	while((lenght = buf.read(decodedBytes)) > 0){      
  		   		zos.write(decodedBytes, 0, lenght );
  		   	}
  		   	zos.closeEntry();
  		   	//close inputstream
  		   	buf.close();
  		   	//close zip outputstream
  		   	zos.close();
  	   }//try
  	   catch (IOException ioe){
  		 final ExceptionMessage exceptionMessage = messageLogger.logErrorCondition("Error loading zip file", ioe.getMessage());
    		throw new CourtelListException(exceptionMessage); 
  	   }
	return zipFile;
     
    }

}