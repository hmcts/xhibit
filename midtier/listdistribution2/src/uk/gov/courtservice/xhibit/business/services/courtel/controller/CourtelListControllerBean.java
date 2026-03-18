package uk.gov.courtservice.xhibit.business.services.courtel.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_blob.XhbBlobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_blob.XhbBlobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_courtel_list.XhbCourtelList;
import uk.gov.courtservice.xhibit.business.entities.xhb_courtel_list.XhbCourtelListBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_courtel_list.XhbCourtelListBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.courtel.exceptions.CourtelListException;
import uk.gov.courtservice.xhibit.business.services.courtel.helpers.CourtelListHelper;
import uk.gov.courtservice.xhibit.business.services.courtel.helpers.Properties;
import uk.gov.courtservice.xhibit.business.services.courtel.httpservices.CourtelHttpService;
import uk.gov.courtservice.xhibit.business.services.courtel.utilities.FileHelper;


/**
 * 
 * Controller Bean For Starting Timers For Generating Messages
 * 
 * @ejb.bean name="CourtelListController" description="Courtel list
 *           Controller Bean" type="Stateless" view-type="both"
 *           jndi-name="CourtelListControllerHome"
 *           local-jndi-name="CourtelListControllerLocalHome"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 * @author d120520
 * @version $Id: CourtelListControllerBean.java,v 1.1
 *          bzjrnl Exp $
 */
public class CourtelListControllerBean  extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 1L;
    private static final Logger log = CSServices.getLogger(CourtelListControllerBean.class);
    //create properties file instance
    Properties properties = Properties.getInstance(); 
        
    Map<String, String> map = new HashMap<String, String>();
   
    /**
     * Initialises all of the instance variables for this session bean.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSSessionBean
     *      #ejbCreate()
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate(); 
    }

    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process. This method must have the same transactional behaviour as
     * processFormattingDocument
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
	 * 
     */
    public void doTask(String taskName) {   	 
    	processMessages();
    }
    
    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process. This method must have the same transactional behaviour as
     * processFormattingDocument 
     * @throws FinderException 
     * 
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
   public void processMessages() {
	   String methodName = "processMessages()";
	   if ( log.isDebugEnabled() ) {
		   log.debug(methodName + " - entered.");
	   }
	   CourtelListHelper courtelListHelper = new CourtelListHelper();
	   CourtelHttpService httpService = new CourtelHttpService();
	   FileHelper fileHelper = new FileHelper();
	   Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	   int courtelListAmount = Integer.parseInt(properties.COURTEL_LIST_AMOUNT);
	   int numOfRetries = Integer.parseInt(properties.COURTEL_MAX_RETRY);
	   int lookupDelay = Integer.parseInt(properties.MESSAGE_LOOKUP_DELAY);
	   // get CourtelList objects from the db
	   //for each CourtelList get it's clob data and generate it's filename
	   Calendar c = Calendar.getInstance();
	   Date current = c.getTime();
	   @SuppressWarnings("unchecked")
	   ArrayList<XhbCourtelList> courtelListCollection = (ArrayList<XhbCourtelList>) XhbCourtelListBeanHelper2.findCourtelListToSend(numOfRetries, lookupDelay, current);
	  //prevent out of bounds exception if list amount > number of lists in the database
	   if(courtelListCollection.size()< courtelListAmount){		   
		   //send the lists that we do have..
		   courtelListAmount = courtelListCollection.size();
	   }

	   if ( log.isDebugEnabled() ) {
		   log.debug(methodName + " - found "+courtelListCollection.size()+" to process");
	   }
	   //Number of lists we need to send per http connection ..get value from config and select from collection. 
	   for( int i=0; i<courtelListAmount; i++){
		   	String zipFilePath = null;
		   	courtelListHelper.setServerOneTimeOut(false);
		   	courtelListHelper.setServerTwoTimeOut(false);
		   	XhbCourtelList xhbCourtelList = (XhbCourtelList) courtelListCollection.get(i);
		   	xhbCourtelList.setLastAttemptDatetime(timestamp);
		   	String clob = null;
		   	if(xhbCourtelList.getXmlDocumentClobId()!=null ) {
		   		Long clobId = xhbCourtelList.getXmlDocumentClobId();
			   	clob = XhbClobBeanHelper2.findByPrimaryKey(clobId).getData().getClobData();
			   	
		   	} else {
			   	clob = xhbCourtelList.getXhbXmlDocument().getXhbClob().getData().getClobData();

		   	}
			byte[] decodedBytes = clob.getBytes();
	
			XhbBlobBasicValue xbbv = new XhbBlobBasicValue();
	        xbbv.setBlobData(decodedBytes);
	        Long blobId = XhbBlobBeanHelper2.create(xbbv).getBlobId();
	       
	        XhbCourtelListBasicValue xhbCourtelListBasicValue  = new XhbCourtelListBasicValue();
	        
	        xhbCourtelListBasicValue.setCourtelListId(xhbCourtelList.getCourtelListId());
	        xhbCourtelListBasicValue.setXmlDocumentId(xhbCourtelList.getXmlDocumentId());
	        xhbCourtelListBasicValue.setXmlDocumentClobId(xhbCourtelList.getXmlDocumentClobId());
	        xhbCourtelListBasicValue.setBlobId(blobId); 
	        xhbCourtelListBasicValue.setCreatedBy(xhbCourtelList.getCreatedBy());
	        xhbCourtelListBasicValue.setCreationDate(xhbCourtelList.getCreationDate());
	        xhbCourtelListBasicValue.setMessageText("");
			//get filename
			try{
	        	String generatedFileName = fileHelper.getFileNameDetailsFromDB(xhbCourtelList);
	        	if ( log.isDebugEnabled() ) {
		        	if(generatedFileName!=null) {
		        		log.debug(methodName + " - Generated file name is :"+generatedFileName);
		        	} else {
		        		log.debug(methodName + " - Generated file name is null");
		        	}
	        	}
	        	xhbCourtelListBasicValue.setFilename(generatedFileName);    
				zipFilePath =  httpService.generateZipFilePath(decodedBytes, generatedFileName);
				if ( log.isDebugEnabled() ) {
					if(zipFilePath!=null) {
		        		log.debug(methodName + " - Zip file path is :"+zipFilePath);
		        	} else {
		        		log.debug(methodName + " - Zip file path is null");
		        	}
				}
				courtelListHelper.setZipFilePath(zipFilePath);
			}
			catch (CourtelListException exception) {
				final String detailMessage = exception.getExceptionDetails().getDetail();
				log.error("Exception from zip file creation" +detailMessage);
				log.error("reason : "+exception.getExceptionDetails().getReason());
				xhbCourtelListBasicValue.setMessageText(detailMessage);
			} catch(ParseException e ){ 
				if(xhbCourtelList.getXhbXmlDocument().getDocumentTitle()!=null){
					log.error("Filename is invalid :"+xhbCourtelList.getXhbXmlDocument().getDocumentTitle());
			    }
				xhbCourtelListBasicValue.setMessageText("Filename is invalid :"+xhbCourtelList.getXhbXmlDocument().getDocumentTitle());
			}
			courtelListHelper.setCourtelServer1(properties.COURTEL_SERVER_1);
			courtelListHelper.setCourtelServer2(properties.COURTEL_SERVER_2);
			
			//got the zip file, now send to Courtel using SSL
			try {
				if(zipFilePath !=null){
					httpService.sendFileToCourtelSSL(courtelListHelper);
				}	
			} catch (CourtelListException exception) {
				final String detailMessage = exception.getExceptionDetails().getDetail();
				log.error("Exception from file send to Courtel " +detailMessage);
				log.error("reason : "+exception.getExceptionDetails().getReason());
				//update db with exception details
				if(detailMessage.equals("Time out waiting for response from Courtel")){
					//need to log in appropriate server column
					if(courtelListHelper.isServerOneTimeOut()){
						xhbCourtelListBasicValue.setCourtelResponseServer1(detailMessage);
					}
					else if(courtelListHelper.isServerTwoTimeOut()){
						xhbCourtelListBasicValue.setCourtelResponseServer2(detailMessage);
					}
				}
				//log certificate/file path error in message text
				else{
					xhbCourtelListBasicValue.setMessageText(detailMessage);
				}
			}  
			//null guard around column
			if(xhbCourtelList.getNumSendAttempts()!=null) {
				xhbCourtelListBasicValue.setNumSendAttempts(xhbCourtelList.getNumSendAttempts()+1);
			} else {
				xhbCourtelListBasicValue.setNumSendAttempts(1);
			}
			timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
			xhbCourtelListBasicValue.setLastAttemptDatetime(timestamp);
			
			//log database with the response from Courtel, good or bad...but don't override column info in the case of timeout
			if(!courtelListHelper.isServerOneTimeOut()){
				if(courtelListHelper.getResponseBodyServer1()!=null && courtelListHelper.getResponseBodyServer1().length()>3000) {
					xhbCourtelListBasicValue.setCourtelResponseServer1(courtelListHelper.getResponseBodyServer1().substring(0, 3000));
				} else {
					xhbCourtelListBasicValue.setCourtelResponseServer1(courtelListHelper.getResponseBodyServer1());
				}
			} else {
				if ( log.isDebugEnabled() ) {
					log.debug(methodName + " - Courtel server one timed out");
				}
			}
			if(!courtelListHelper.isServerTwoTimeOut()){
				if(courtelListHelper.getResponseBodyServer2()!=null && courtelListHelper.getResponseBodyServer2().length()>3000) {
					xhbCourtelListBasicValue.setCourtelResponseServer2(courtelListHelper.getResponseBodyServer2().substring(0, 3000));
				} else {
					xhbCourtelListBasicValue.setCourtelResponseServer2(courtelListHelper.getResponseBodyServer2());
				}
			} else {
				if ( log.isDebugEnabled() ) {
					log.debug(methodName + " - Courtel server two timed out");
				}
			}
			xhbCourtelListBasicValue.setVersion(xhbCourtelList.getVersion());
			
			//check the response is successful and update database
			if(courtelListHelper.isValidResponse()){
				xhbCourtelListBasicValue.setSentToCourtel("Y");
			}
			else{
				//mark the list as 'N' to allow a re-send
				xhbCourtelListBasicValue.setSentToCourtel("N");
			}
			xhbCourtelListBasicValue.setNumServersUploadedTo(courtelListHelper.getNumberOfserversUploaded());
			if ( log.isDebugEnabled() ) {
			   log.debug(methodName + " - Courtel Basic Value : "+xhbCourtelListBasicValue.toString());
			}

			xhbCourtelList.setData(xhbCourtelListBasicValue);
	   }
	   //clear down the list, so that we have a fresh list, this is in case there is a bottleneck in the response back from Courtel
	   if(!(courtelListCollection.isEmpty())){
		   courtelListCollection.clear();	
	   } else {
		   if ( log.isDebugEnabled() ) {
			   log.debug(methodName + " - Courtel list collection is empty");
		   }
	   }
   }

  }