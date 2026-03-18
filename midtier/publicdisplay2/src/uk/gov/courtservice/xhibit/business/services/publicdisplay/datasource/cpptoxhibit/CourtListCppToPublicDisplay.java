package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtListValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;

public class CourtListCppToPublicDisplay extends AbstractCppToPublicDisplay {
	

	public CourtListCppToPublicDisplay(Date date, int courtId, int[] courtRoomIds) {
		super(date, courtId, courtRoomIds);
	}

	@Override
	public Collection getCppData() {
		Collection<CourtListValue> cppData = new ArrayList<CourtListValue>();

		log.info("CourtListCppToPublicDisplay.getCppData()");
		
		// Check the court is CPP enabled and then retrieve data
		if(isCourtCppEnabled()){
			log.info("CourtListCppToPublicDisplay.getCppData() - courtid: " + courtId +" - is a CPP enabled court");

			// get the public display document as a Document for manipulation
			Document cppDocument = getCppClobAsDocument();
			if(null != cppDocument){
				for ( int roomId : courtRoomIds ) {
					// Retrieve the required court room - will only be one				
					// Using court room id, get the XhbCourtRoomBasicValue so we can lookup the CPP <courtroom> node using Court Room Name
					XhbCourtRoomBasicValue courtRoomValue = getCourtRoomObjectById(roomId);
					
					if(null != courtRoomValue){
						// Search for matching nodes in the Document based upon the court room name
						Node courtRoom = getCourtRoomNode(cppDocument, courtRoomValue);
						if ( null != courtRoom ) {
							// retrieve the list of cases
							try {
								NodeList cases = (NodeList) getXPath().evaluate(XPATH_CASE, courtRoom, XPathConstants.NODESET);
								for (int i=0; i<cases.getLength(); i++) {
									Node caze = cases.item(i);
									if (null != caze){
										CourtListValue courtListValue = new CourtListValue();
										// Using court site id, get the XhbCourtSiteBasicValue so we can add details to the object
										XhbCourtSiteBasicValue courtSiteValue = getCourtSiteObjectById(courtRoomValue.getCourtSiteId());
										
										populateData(courtListValue, caze, courtRoomValue, courtSiteValue);
										
										//add to the collection
										cppData.add(courtListValue);
									}
								}
							} catch (XPathExpressionException e) {
								log.error("CourtListCppToPublicDisplay.populateCourtListDataFromCppXml() - XPathExpressionException  - " + e.toString());
							}					
						}
					}
				}
			}
			else{
				log.debug("CourtListCppToPublicDisplay.getCppData() - latest xml not retrieved for today - i.e. getCppClobAsDocument() returned null");
			}
		}
		return cppData;		
	}
	
	protected Node getCourtRoomNode(Document cppDocument, XhbCourtRoomBasicValue courtRoomValue) {
		NodeList courtRoomNodeList = null;
		Node courtRoom = null;
		try{
			XhbCourtSiteBasicValue courtSiteValue = getCourtSiteObjectById(courtRoomValue.getCourtSiteId());
			courtRoomNodeList = ((NodeList)getXPath().evaluate("//courtsite[courtsitename='"+courtSiteValue.getCourtSiteName()+"']/courtrooms/courtroom[courtroomname='"+courtRoomValue.getCourtRoomName()+"']", cppDocument, XPathConstants.NODESET));
			if (courtRoomNodeList != null){
				courtRoom = courtRoomNodeList.item(0);
			}
		} catch (XPathExpressionException e) {
			log.error("CourtListCppToPublicDisplay.getCourtRoomNode() - latest xml not retrieved for today - i.e. getCppClobAsDocument() returned null");
		}		
		return courtRoom;
	}
	
	protected void populateData(	CourtListValue courtListValue, Node caze, 
													XhbCourtRoomBasicValue courtRoomValue, XhbCourtSiteBasicValue courtSiteValue) {			
			
		try {
			// Populate fields Specific to PublicDisplayValue
			populateCourtSiteRoomData(courtListValue, courtRoomValue, courtSiteValue);

			// Retrieve data from the xml structure
			Element caseElement = (Element)caze;
			super.populateData(courtListValue, caseElement);
			
			// set the case number correctly.
			// if xhibit casenumber exists use it, if not use the cppurn
			String caseNumber = null;
			String caseNumberCpp = getXPath().evaluate(XPATH_CPPURN, caseElement);
			if(null != caseNumberCpp && caseNumberCpp != ""){
				caseNumber = caseNumberCpp;
			}
			else{
				caseNumber = getXPath().evaluate(XPATH_CASETYPE, caseElement) + getXPath().evaluate(XPATH_CASENUMBER, caseElement);
				
			}

			String hearingProgress = getXPath().evaluate(XPATH_HEARINGPROGRESS, caseElement);
			String hearingDescription = getXPath().evaluate(XPATH_HEARINGTYPE, caseElement);
			
			// set courtListValues from data retrieved from xml
			if(caseNumber != null && caseNumber != ""){
				courtListValue.setCaseNumber(caseNumber);					
			}
			else{
				log.error("CourtListCppToPublicDisplay.populateCourtListDataFromCppXml() - no case number provided for - court site id" + 
									courtRoomValue.getCourtSiteId().toString() + " and court room id - " + courtRoomValue.getCourtRoomId().toString());
			}
			
			if(hearingProgress != null && hearingProgress != ""){
				courtListValue.setHearingProgress(Integer.parseInt(hearingProgress));							
			}
		
			if(hearingDescription != null){			
				courtListValue.setHearingDescription(hearingDescription);
			}
		
			//get the defendants
			// Loop through all Defendants and add them
	    	NodeList defendantList = (NodeList)getXPath().evaluate(XPATH_DEFENDANT, caseElement, XPathConstants.NODESET);
	    	boolean reportingRestrictions = false;
	    	for (int i=0; i<defendantList.getLength(); i++) {
	    		
				courtListValue.addDefendantName( new DefendantName(
						getXPath().evaluate(XPATH_DEF_FIRSTNAME, (Element)defendantList.item(i)),
						getXPath().evaluate(XPATH_DEF_MIDDLENAME, (Element)defendantList.item(i)), 
						getXPath().evaluate(XPATH_DEF_SURNAME, (Element)defendantList.item(i)),
						false
						)
				);
				
				boolean defReportingRestricted = getDefendantReportRestriction((Element)defendantList.item(i));
				if ( !reportingRestrictions && defReportingRestricted ) {
					reportingRestrictions = true;
	    		}  		
	    	}
	    	// If reporting restrictions on any defendants set here
		    courtListValue.setReportingRestricted(reportingRestrictions);
		    
		    // Set the List Court Room Id
	        String listCourtRoomName = getXPath().evaluate(XPATH_LISTCOURTROOM, caseElement);
	        XhbCourtRoomBasicValue listCourtRoomValue = getCourtRoomObjectByName(listCourtRoomName);
	        if ( null != listCourtRoomValue ) {
	        	courtListValue.setListCourtRoomId(listCourtRoomValue.getCourtRoomId());
	        }
		} catch (XPathExpressionException e) {
			log.error("CourtListCppToPublicDisplay.populateCourtListDataFromCppXml() - XPathExpressionException  - " + e.toString());
		}
	}

}
