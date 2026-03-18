package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.services.cppformatting.CppFormattingControllerException;
import uk.gov.courtservice.xhibit.business.services.cppformatting.CppFormattingHelper;
import uk.gov.courtservice.xhibit.business.services.formatting.AbstractXMLUtils;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.PublicDisplayValue;

/**
 * The AbstractCppToPublicDisplay class is used to retrieve CPP XML,
 * extract data from the CPP XML and update the XHB_CPP_FORMATTING 
 * table's STATUS column
 * 
 * @author groenm
 *
 */
public abstract class AbstractCppToPublicDisplay {
	
	protected static final Logger log = CSServices.getLogger(AbstractCppToPublicDisplay.class);
	
	protected static final String XPATH_COURTSITENAME = "courtsitename";
	protected static final String XPATH_CASE = "cases/caseDetails";
	protected static final String XPATH_CPPURN = "cppurn";
	protected static final String XPATH_CASENUMBER = "casenumber";
	protected static final String XPATH_CASETYPE = "casetype";
	protected static final String XPATH_JUDGENAME = "judgename";
	protected static final String XPATH_HEARINGTYPE = "hearingtype";
	protected static final String XPATH_HEARINGPROGRESS = "hearingprogress";
	protected static final String XPATH_NOTBEFORETIME = "notbeforetime";
	protected static final String XPATH_TIMESTATUSSET = "timestatusset";
	protected static final String XPATH_MOVEDFROMCOURTSITE = "movedfromcourtsitename";
	protected static final String XPATH_MOVEDFROMCOURTROOM = "movedfromcourtroomname";
	protected static final String XPATH_LISTCOURTROOM = "listcourtroomname";
	
	protected static final String XPATH_EVENT = "currentstatus/event";
	protected static final String XPATH_EVENTDATE = "currentstatus/event/date";
	protected static final String XPATH_EVENTTIME = "currentstatus/event/time";
	
	protected static final String XPATH_DEFENDANT = "defendants/defendant";
	protected static final String XPATH_DEF_FIRSTNAME = "firstname";
	protected static final String XPATH_DEF_MIDDLENAME = "middlename";
	protected static final String XPATH_DEF_SURNAME = "lastname";
	protected static final String XPATH_DEF_RESTRICTIONS = "reportingrestrictions";
	
	protected static final String XPATH_PUBLICNOTICE = "publicnotices/publicnotice";

	protected final Date date;
	protected final int courtId;
	protected String courtName;
	protected final int[] courtRoomIds;
	protected XhbClobBasicValue cppClob;
	protected CourtStructureValue xhbCourtStructure;
	protected CppFormattingBasicValue cppFormattBV;
	protected XPath xp;
	
	public AbstractCppToPublicDisplay(final Date date, final int courtId, final int[] courtRoomIds){
		this.date = date;
		this.courtId = courtId;
		this.courtRoomIds = courtRoomIds;		
	}
	
	/**
	 * @param courtId
	 * @return boolean representing if the court id defined as being CPP ready
	 */
	protected boolean isCourtCppEnabled() {
		boolean isCppSite = false;
		
		XhbCourtBasicValue xhbCourt = XhbCourtBeanHelper2.findByPrimaryKeyValue(courtId);
		
		if(xhbCourt.getCppCourt() != null && xhbCourt.getCppCourt().equals("Y")){
			isCppSite = true;
			
			// set the court name as will be continuing with the process and have this information to hand
			this.setCourtName(xhbCourt.getCourtName());
			retrieveCourtStructure();
		}
		
		return isCppSite;
	}
	
	/**
	 * Populates the PublicDisplayValue object with data from the courtroom XML element
	 * @param value PublicDisplayValue
	 * @param caseNode XML element for the case
	 * @throws XPathExpressionException
	 */
    protected void populateData(PublicDisplayValue value, Element caseNode) throws XPathExpressionException {
    	String movedFromCourtRoomName = getXPath().evaluate(XPATH_MOVEDFROMCOURTROOM, caseNode);
    	if ( !"".equals(movedFromCourtRoomName) ) {
    		value.setMovedFromCourtRoomName(movedFromCourtRoomName);
    	}
    	
    	String movedFromCourtSiteName = getXPath().evaluate(XPATH_MOVEDFROMCOURTSITE, caseNode);
    	if ( !"".equals(movedFromCourtSiteName) ) {
    		value.setMovedFromCourtSiteShortName(movedFromCourtSiteName);
    	}
        
    	String notBeforeTime = getXPath().evaluate(XPATH_NOTBEFORETIME, caseNode);
    	if ( !"".equals(notBeforeTime) ) {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	    	String dateString = dateFormat.format(this.date);	// Combine the time with the date to complete the timestamp otherwise uses 1970
	        value.setNotBeforeTime(convertStringToTimestamp(dateString+" "+notBeforeTime));
    	}
    }
	
	/**
	 * Get a court structure object to use with retrieving the CPP data from the XML
	 */
	protected void retrieveCourtStructure(){
		xhbCourtStructure = XhbCourtBeanHelper2.getCourtStructure(courtId);
	}	
	
	/**
	 * @return the cppClob as a document for manipulation
	 */
	public Document getCppClobAsDocument() {
		String methodName = "getCppClobAsDocument - ";
		Document cppXml = null;
		if(cppClob == null){
			this.getCppClob();
		}
		
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = AbstractXMLUtils.getDocBuilder();
		} catch (ParserConfigurationException e) {
			log.error(methodName + "ParserConfigurationException - Unable to retrieve CPP XML - " + e.getMessage());
		}		

		try {
			if(docBuilder != null && cppClob != null){
				cppXml = docBuilder.parse(new InputSource(new StringReader(cppClob.getClobData())));
			}
		} catch (SAXException e) {
			log.error(methodName + "SAXException - Unable to retrieve CPP XML - " + e.getMessage());
		} catch (IOException e) {
			log.error(methodName + "IOException - Unable to retrieve CPP XML - " + e.getMessage());
		} 
				
		return cppXml;
	}


	/**
	 * Get a list of court rooms from the xhbCourtStructure
	 */
	protected XhbCourtRoomBasicValue[] getCourtRooms(final String newStatus){		
		XhbCourtRoomBasicValue[] allCourtRooms = null;
		if(getXhbCourtStructure() != null){
			allCourtRooms = getXhbCourtStructure().getAllCourtRooms();
		}
		
		return allCourtRooms;
	}
	
	/**
	 * Update the status on the database XHB_CPP_FORMATTING table
	 */
	protected void updateCPPFormattingStatus(final String newStatus){		
		CppFormattingHelper helper = new CppFormattingHelper();
		helper.updateCppFormattingStatus(cppFormattBV, newStatus);
	}
	
	/**
	 * @return Collection of the relevant display objects
	 * @throws XPathExpressionException 
	 */
	public abstract Collection getCppData() throws XPathExpressionException;

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the courtId
	 */
	public int getCourtId() {
		return courtId;
	}

	/**
	 * @return the courtRoomIds
	 */
	public int[] getCourtRoomIds() {
		return courtRoomIds;
	}

	/**
	 * @return the courtName
	 */
	public String getCourtName() {
		return courtName;
	}

	/**
	 * @param courtName the courtName to set
	 */
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	
	/**
	 * @return the cppClob
	 */
	public XhbClobBasicValue getCppClob() {
		if(cppClob == null){
			// retrieve from the database
			CppFormattingHelper helper = new CppFormattingHelper();			 
			try {
				cppFormattBV = helper.getLatestPublicDisplayDocument(courtId);
				if ( null != cppFormattBV && null != cppFormattBV.getXmlDocumentClobId() ) {
					cppClob = XhbClobBeanHelper2.findByPrimaryKeyValue(cppFormattBV.getXmlDocumentClobId());
				}
			} catch (CppFormattingControllerException e) {
				log.error("CppFormattingControllerException - Unable to retrieve CPP XML - " + e.getMessage());
			}			
		}	
		
		return cppClob;
	}

	/**
	 * @param cppClob the cppClob to set
	 */
	public void setCppClob(XhbClobBasicValue cppClob) {
		this.cppClob = cppClob;
	}

	/**
	 * @return the xhbCourtStructure
	 */
	public CourtStructureValue getXhbCourtStructure() {
		return xhbCourtStructure;
	}

	/**
	 * @param xhbCourtStructure the xhbCourtStructure to set
	 */
	public void setXhbCourtStructure(CourtStructureValue xhbCourtStructure) {
		this.xhbCourtStructure = xhbCourtStructure;
	}
	
	/**
	 * Returns a XhbCourtRoomBasicValue object that matches the courtRoomId specified
	 * @param courtRoomId Court Room Id to search for
	 * @return XhbCourtRoomBasicValue
	 */
	protected XhbCourtRoomBasicValue getCourtRoomObjectById(int courtRoomId) {
    	XhbCourtRoomBasicValue roomValue = null;
    	for ( XhbCourtRoomBasicValue room : xhbCourtStructure.getAllCourtRooms() ) {
    		if ( room.getCourtRoomId().equals(courtRoomId) ) {
    			roomValue = new XhbCourtRoomBasicValue(room);
    			roomValue.setDisplayNameNoSite(room.getDisplayNameNoSite());
    			break;
    		}
    	}
    	return roomValue;
    }
	
	/**
	 * Returns a XhbCourtRoomBasicValue object that matches the courtRoomName specified
	 * @param courtRoomName Court Room Name to search for
	 * @return XhbCourtRoomBasicValue
	 */
	protected XhbCourtRoomBasicValue getCourtRoomObjectByName(String courtRoomName) {
    	XhbCourtRoomBasicValue roomValue = null;
    	if ( null != courtRoomName && !"".equals(courtRoomName) ) {
	    	for ( XhbCourtRoomBasicValue room : xhbCourtStructure.getAllCourtRooms() ) {
	    		if ( room.getCourtRoomName().equals(courtRoomName) ) {
	    			roomValue = new XhbCourtRoomBasicValue(room);
	    			roomValue.setDisplayNameNoSite(room.getDisplayNameNoSite());
	    			break;
	    		}
	    	}
    	}
    	return roomValue;
    }
    
	/**
	 * Returns a XhbCourtSiteBasicValue object that matches the courtSiteId specified
	 * @param courtSiteId Court Site Id to search for
	 * @return XhbCourtSiteBasicValue
	 */
    protected XhbCourtSiteBasicValue getCourtSiteObjectById(int courtSiteId) {
    	XhbCourtSiteBasicValue siteValue = null;
    	for ( XhbCourtSiteBasicValue site : xhbCourtStructure.getCourtSites() ) {
    		if ( site.getCourtSiteId().equals(courtSiteId) ) {
    			siteValue = new XhbCourtSiteBasicValue(site);
    			break;
    		}
    	}
    	return siteValue;
    }
    
    /**
	 * Returns a XhbCourtSiteBasicValue object that matches the court site name specified
	 * @param courtSiteName Court Site Name to search for
	 * @return XhbCourtSiteBasicValue
	 */
	protected XhbCourtSiteBasicValue getCourtSiteObjectByName(String courtSiteName) {
		XhbCourtSiteBasicValue siteValue = null;
    	if ( null != courtSiteName && !"".equals(courtSiteName) ) {
    		
    		for ( XhbCourtSiteBasicValue site : xhbCourtStructure.getCourtSites() ) {
        		if ( site.getCourtSiteName().equals(courtSiteName) ) {
        			siteValue = new XhbCourtSiteBasicValue(site);
        			break;
        		}
        	}
    	}
    	return siteValue;
    }
    
    /**
     * Populate court details on the PublicDisplayValue
     * @param value
     * @param room
     * @param site
     */
    protected void populateCourtSiteRoomData(PublicDisplayValue value, XhbCourtRoomBasicValue room, XhbCourtSiteBasicValue site) {
    	// Court room data
    	if ( null == room ) {
    		value.setCourtRoomName("");
        	value.setCourtRoomId(-1);
        	value.setCrestCourtRoomNo(99);
    	}
    	else {
    		value.setCourtRoomName(room.getDisplayNameNoSite());
        	value.setCourtRoomId(room.getCourtRoomId());
        	value.setCrestCourtRoomNo(room.getCrestCourtRoomNo());
    	}
    	
    	// Court Site data
    	if ( null == site ) {
    		value.setCourtSiteName("");
        	value.setCourtSiteShortName("");
        	value.setCourtSiteCode("Z");
    	}
    	else {
    		value.setCourtSiteName(site.getCourtSiteName());
        	value.setCourtSiteShortName(site.getShortName());
        	value.setCourtSiteCode(site.getCourtSiteCode());
    	}
    }
	
    /**
     * Returns the XPath object reference
     * @return XPath
     */
	protected XPath getXPath() {
		if (xp == null) {
			xp = XPathFactory.newInstance().newXPath();
		}
		return xp;
	}
	
	/**
	 * Converts a String in the format dd-MM-yyyy HH:mm to a Timestamp object
	 * @param dateTime Timestamp string
	 * @return Timestamp
	 */
	protected Timestamp convertStringToTimestamp(String dateTime) {
    	return convertStringToTimestamp(dateTime, "dd/MM/yy HH:mm");
    }
	
	/**
	 * Converts a String in the date mask passed in to a Timestamp object
	 * @param dateTime Timestamp string
	 * @return Timestamp
	 */
	protected Timestamp convertStringToTimestamp(String dateTime, String mask) {
    	Timestamp timestamp = null;
    	String methodName = "convertStringToTimestamp(dateTime=>"+dateTime+", mask=>"+mask+") ";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(mask);
			Date parsedDate = dateFormat.parse(dateTime);
			timestamp = new Timestamp(parsedDate.getTime());
		} catch (ParseException e) {
			log.error(methodName + "ParseException - " + e.getMessage());
		} catch (Exception e) {
			log.error(methodName + "Exception - " + e.getMessage());
		}
    	return timestamp;
    }
	
	/**
	 * Retrieves the boolean value of the Defendant reportingrestriction node which should be 
	 * 1 if restricted, else 0
	 * @param defNode Defendant parent node
	 * @return true if the reporting is restricted else false.
	 * @throws XPathExpressionException
	 */
	protected boolean getDefendantReportRestriction(Element defNode) throws XPathExpressionException {
		String methodName = "getDefendantReportRestriction(defNode=>"+defNode+") ";
		boolean reportingRestricted = false;
		String nodeValue = getXPath().evaluate(XPATH_DEF_RESTRICTIONS, defNode);
		if ( null != nodeValue && !"".equals(nodeValue) ) {
			// Convert the String to an Integer and use it to set the boolean flag
			try {
				Integer defReportingRestriction = Integer.parseInt(nodeValue);
				if ( defReportingRestriction.equals(1) ) {
					// Reporting is restricted if the node value is 1
					reportingRestricted = true;
				}
			} catch (NumberFormatException e) {
				log.error(methodName + "Exception - " + e.getMessage());
			}
		}
		return reportingRestricted;
	}
}