package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.EventXMLNodeHelper;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;

public class AllCourtStatusCppToPublicDisplay extends AbstractCppToPublicDisplay {
	
	public AllCourtStatusCppToPublicDisplay(Date date, int courtId, int[] courtRoomIds) {
		super(date, courtId, courtRoomIds);
	}

	/**
	 * Returns a collection of AllCourtStatusValue CPP Data
	 * @throws XPathExpressionException 
	 */
	@Override
	public Collection getCppData() {
		List<AllCourtStatusValue> cppData = new ArrayList<AllCourtStatusValue>();
		
		// Check the court is CPP enabled and then retrieve data
		if(isCourtCppEnabled()){
			
			// Retrieve XML Document of CPP data from the latest XHB_CPP_FORMATTING row for the court supplied
			Document doc = getCppClobAsDocument();
			if ( doc != null ) {
				AllCourtStatusValue allCourtStatusValue;
				XhbCourtRoomBasicValue courtRoomValue;
				XhbCourtSiteBasicValue courtSiteValue;
				for ( int roomId : courtRoomIds ) {
					// Loop through all the Court Room Ids supplied and create a new AllCourtStatusValue for each
					try {
						
						// Using court room id, get the XhbCourtRoomBasicValue so we can lookup the CPP <courtroom> node using Court Room Name
						courtRoomValue = getCourtRoomObjectById(roomId);
						if (null != courtRoomValue) {
							courtSiteValue = getCourtSiteObjectById(courtRoomValue.getCourtSiteId());
							
							// Search for matching nodes in the Document based upon the court room name
							NodeList nodes = (NodeList)getXPath().evaluate("//courtsite[courtsitename='"+courtSiteValue.getCourtSiteName()+"']/courtrooms/courtroom[courtroomname='"+courtRoomValue.getCourtRoomName()+"']", doc, XPathConstants.NODESET);
							
							if ( nodes.getLength() > 0 && null != nodes.item(0) ) {
								// Using the <courtroom> node from the XML Document, use the information inside to populate the AllCourtStatusValue
						    	// Retrieve the active case node and populate the AllCourtStatusValue with the case data
								NodeList caseNodes = (NodeList)getXPath().evaluate("cases/caseDetails[activecase=1]", (Element)nodes.item(0), XPathConstants.NODESET);
								if ( caseNodes.getLength() != 0 && null != caseNodes.item(0) ) {
									Node caseNode = caseNodes.item(0);
									allCourtStatusValue = new AllCourtStatusValue();
									
									// Populate fields Specific to PublicDisplayValue
									populateCourtSiteRoomData(allCourtStatusValue, courtRoomValue, courtSiteValue);
									
									// Populate the rest of the data
						    		populateData(allCourtStatusValue, (Element)caseNode);
						    		
						    		// Add the populated AllCourtStatusValue to cppData
									cppData.add(allCourtStatusValue);
								}
								else {
									if ( log.isDebugEnabled() ) {
										log.debug("AllCourtStatusCppToPublicDisplay.getCppData() - no active case data for court room: " + courtRoomValue.getCourtRoomName());
									}
								}
							}
						}
						
					} catch (XPathExpressionException e) {
						log.error("AllCourtStatusCppToPublicDisplay.getCppData() - XPathExpressionException  - " + e.toString());
					} catch (Exception e) {
						log.error("AllCourtStatusCppToPublicDisplay.getCppData() - Exception  - " + e.toString());
					}
				}
			}
		}
				
		return cppData;		
	}
	
	/**
	 * Populates the AllCourtStatusValue object with data from the courtroom XML element
	 * @param value AllCourtStatusValue
	 * @param caseNode XML element for the case
	 * @throws XPathExpressionException
	 */
	protected void populateData(AllCourtStatusValue value, Element caseNode) throws XPathExpressionException {
		// Set Case Number
    	String cppurn = getXPath().evaluate(XPATH_CPPURN, caseNode);
    	String caseNumber = getXPath().evaluate(XPATH_CASENUMBER, caseNode);
    	String caseType = getXPath().evaluate(XPATH_CASETYPE, caseNode);
    	
    	if ( !"".equals(cppurn) ) {
    		// Use the CPP URN as the case number
    		value.setCaseNumber(cppurn);
    	}
    	else {
    		// No CPP URN so check the Xhibit case number and case type fields instead
    		if ( !"".equals(caseNumber) && !"".equals(caseType) ) {
        		value.setCaseNumber(caseType+caseNumber);
        	}
    	}
    	
    	// Set the Event Node
    	value.setEvent((BranchEventXMLNode) EventXMLNodeHelper.buildEventNode( (Node)getXPath().evaluate(XPATH_EVENT, caseNode, XPathConstants.NODE) ));
    	
    	// Set the event date/time
    	String eventDate = getXPath().evaluate(XPATH_EVENTDATE, caseNode);
    	String eventTime = getXPath().evaluate(XPATH_EVENTTIME, caseNode);
    	String eventDateTime = null;
    	if ( !"".equals(eventDate) && !"".equals(eventTime) ) {
    		eventDateTime = eventDate + " " + eventTime;
    	}
    	else {
    		// No event date/time so use the timestatusset node
    		String timeStatusSet = getXPath().evaluate(XPATH_TIMESTATUSSET, caseNode);
        	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        	String dateString = dateFormat.format(this.date);	// Combine the time with the date to complete the timestamp otherwise uses 1970
        	eventDateTime = dateString + " " + timeStatusSet;
    	}
    	value.setEventTime( convertStringToTimestamp(eventDateTime) );
    	
    	// Loop through all Defendants and add them
    	NodeList defendantList = (NodeList)getXPath().evaluate(XPATH_DEFENDANT, caseNode, XPathConstants.NODESET);
    	boolean reportingRestricted = false;
    	for (int i=0; i<defendantList.getLength(); i++) {
    		boolean defReportingRestricted = getDefendantReportRestriction((Element)defendantList.item(i));
    		if ( !reportingRestricted && defReportingRestricted ) {
    			reportingRestricted = true;
    		}
    		value.addDefendantName( new DefendantName(
    				getXPath().evaluate(XPATH_DEF_FIRSTNAME, (Element)defendantList.item(i)),
    				getXPath().evaluate(XPATH_DEF_MIDDLENAME, (Element)defendantList.item(i)), 
    				getXPath().evaluate(XPATH_DEF_SURNAME, (Element)defendantList.item(i)), 
    				false)	// Hide from Public Display
    		);
    	}
    	// If any of the defendants have reportingrestricted set to 1, then set reporting restricted to true
    	value.setReportingRestricted(reportingRestricted);
	}
}