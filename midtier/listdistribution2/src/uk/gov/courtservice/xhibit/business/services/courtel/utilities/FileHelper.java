package uk.gov.courtservice.xhibit.business.services.courtel.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_courtel_list.XhbCourtelList;

public class FileHelper {

	private static final Logger log = CSServices.getLogger(FileHelper.class);
	private static final String ENTER_METHOD = "Entering method ";
	private static final String EXIT_METHOD = "Exiting method ";
	private static final String COURTEL_CODE = "COURTEL_COURT_";

    
	public String getFileNameDetailsFromDB(XhbCourtelList xhbcl) throws ParseException{
	   String shortName = null;
	   String documentType = null; 
	   String documentTitle = null;	
	   String dateCreated = null;
	   
	   if(xhbcl.getXhbXmlDocument().getXhbCourt().getShortName()!=null){
	    	shortName = xhbcl.getXhbXmlDocument().getXhbCourt().getShortName();
	    }
	    if(xhbcl.getXhbXmlDocument().getDocumentType() !=null){
	    	documentType = getDocumentType( xhbcl.getXhbXmlDocument().getDocumentType());
	    }
	    if(xhbcl.getXhbXmlDocument().getDocumentTitle()!=null){
	        documentTitle = xhbcl.getXhbXmlDocument().getDocumentTitle();
	    }
	    if (xhbcl.getXhbXmlDocument().getDateCreated()!=null) {
	    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    	dateCreated = formatter.format(xhbcl.getXhbXmlDocument().getDateCreated());
	    }
	    if(xhbcl.getXhbXmlDocument().getXhbCourt()!=null && xhbcl.getXhbXmlDocument().getXhbCourt().getCourtName()!=null) {
	    	String toSend = COURTEL_CODE+""+xhbcl.getXhbXmlDocument().getXhbCourt().getCourtName();
	    	XhbConfigPropBasicValue[] props = XhbConfigPropBeanHelper2.findByPropertyNameValue(toSend);
	    	if(props.length>0){
	    		if(props[0].getPropertyValue()!=null && !props[0].getPropertyValue().equals("")) {
	    			shortName = props[0].getPropertyValue();
	    		}
	    	}
	    	
	    }
	    return generateFileName (shortName, documentType, documentTitle, dateCreated );
   }
	
	public String generateFileName (String shortName, String documentType, String documentTitle, String dateCreated ) throws ParseException{
		log.debug(ENTER_METHOD+"generateFileName with sName : "+shortName+", docType: "+documentType+" and docTitle"+ documentTitle);

		String status = null;
    	String versionNumber = null; 
    	String fileName=null;
    	String unformattedDate = null;
    	String theDate = null;
    	String listType = null;
    	boolean fileNameValues = true; 
    	    	
    	if(shortName!=null && documentType!=null && documentTitle !=null) {
        	status = getStatus(documentTitle);
        	//versionNumber = getVersionumber(documentTitle);
        	listType = getListType(documentTitle);
        	versionNumber = getVersionNumber(documentTitle);
        	if(listType.equals("Daily") || listType.equals("Firm") || listType.equals("Warned")){
        		theDate = dateCreated;
        	}
        	else{
        		//listtype must be a report, so document title will not have a version number
        		unformattedDate = getDocumentDate(documentTitle, listType, null);
        		theDate = formatDate(unformattedDate);
        	}
        	
    	}
    	else{
    		fileNameValues = false; 
    	}
    	if(fileNameValues){
    		//only list types will have a version number
    		if(listType.equals("Daily") || listType.equals("Firm") || listType.equals("Warned")){
    			if(Integer.parseInt(versionNumber) <10){
    				versionNumber = "-0"+versionNumber;
    			}
    			else{
    				versionNumber="-"+versionNumber;
    			}
    		}
    		fileName = shortName+"_"+documentType+theDate+versionNumber+status+".xml";
    	}
    	//something went wrong..
    	else{
    		//value is null, log in application log
    		log.error("Filename improperly constructed, one or more values from db is null");	
    	}  	
    	
		log.debug(EXIT_METHOD+"generateFileName");

    	return fileName; 	
    }
	
	/**
	 * 
	 * @param documentTitle
	 * @param listType
	 * @param versionNumber
	 * @return String returns the date in the format yyyy-mm-dd
	 */
	public String getDocumentDate(String documentTitle, String listType, String versionNumber) throws ParseException {
		String theDate = null;
		log.debug(ENTER_METHOD+"getDocumentDate with"+listType+" "+documentTitle+" "+versionNumber);

		//find end point of date
		int hyphenIndex = documentTitle.indexOf("-");
		int dateEndIndex = documentTitle.indexOf(" ", hyphenIndex);
		if(hyphenIndex<0 || dateEndIndex<0) {
			log.error("Failed to find the date in getDocumentDate");
			throw new ParseException("Invalid file name", dateEndIndex);
		}
		String date = documentTitle.substring(hyphenIndex+1,dateEndIndex);
		
		//find year value
		String year = documentTitle.substring(hyphenIndex-4, hyphenIndex);
		theDate = year+"-"+date;
		
		log.debug(EXIT_METHOD+"getDocumentDate");
		return theDate;
	}
	
	public String formatDate(String date) throws ParseException{
		log.debug(ENTER_METHOD+"formatDate with"+date);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date theDate = null;
		theDate = df.parse(date);
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		
    	String formatted = formatter.format(theDate);
    	
    	log.debug(EXIT_METHOD+"formatDate");
    	return formatted;
	}
    
	public String getStatus(String documentTitle){
		log.debug(ENTER_METHOD+"getStatus with"+documentTitle);
		if(documentTitle.contains("FINAL")){
			documentTitle = "F";
		}
		else if(documentTitle.contains("DRAFT")){
			documentTitle = "D";
		}
		else{
			return "";
		}

	log.debug(EXIT_METHOD+"getStatus");
	return documentTitle;
	}
    
	//list type
	public String getListType(String documentTitle){
		log.debug(ENTER_METHOD+"getListType with"+documentTitle);
		String listType = null;
		if(documentTitle.contains("Daily List")){
			listType = "Daily";
		}
		else if(documentTitle.contains("Firm List")){
			listType = "Firm";
		}
		else if(documentTitle.contains("Warned List")){
			listType = "Warned";
		}
		else if(documentTitle.contains("Running List")){
			listType = "Running";
		}
		else{
			return "";
		}

	log.debug(EXIT_METHOD+"getListType");
	return listType;
	}
    
    public String getVersionNumber(String documentTitle){
    	log.debug(ENTER_METHOD+" getVersionNumber for "+documentTitle);

    	String finalOrDraft = ""; 
    	String versionNumber = "-01";
		if(documentTitle.contains("FINAL")){
			finalOrDraft = "FINAL";
		}
		if (documentTitle.contains("DRAFT")){
			finalOrDraft = "DRAFT";
		}
		if( (finalOrDraft.equals("FINAL") || (finalOrDraft.equals("DRAFT") ))){
			//find the start and end of the version number string
			int versionStartIndex = documentTitle.indexOf(" ", documentTitle.indexOf(finalOrDraft));
			int versionEndIndex = documentTitle.indexOf(" ", versionStartIndex+1);
			
			versionNumber = documentTitle.substring(versionStartIndex+2, versionEndIndex);		
		}
		log.debug(EXIT_METHOD+" getVersionNumber");
		return versionNumber;	  	
	}
		
    public String getDocumentType(String documentType){
    	log.debug(ENTER_METHOD+" getDocumentType with type "+documentType);

    	if(documentType !=null){
    		if(documentType.startsWith("DLP")){
    			documentType = "P";
    		}
    		else if (documentType.startsWith("FL")){
    			documentType = "F";
    		}
    		else if (documentType.startsWith("DL")){
    			documentType = "D";
    		}
    		else if (documentType.startsWith("RL")){
    			documentType = "R";
    		}
    		else if (documentType.startsWith("WL")){
    			documentType = "W";
    		}
    	}
		log.debug(EXIT_METHOD+" getDocumentType");
    	return documentType;   	
    }
}
