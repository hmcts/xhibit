package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

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
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtDetailValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JudgeName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.PublicNoticeValue;

public class CourtDetailCppToPublicDisplay extends AllCourtStatusCppToPublicDisplay {

	public CourtDetailCppToPublicDisplay(Date date, int courtId, int[] courtRoomIds) {
		super(date, courtId, courtRoomIds);
	}

	/**
	 * Returns a collection of CourtDetailValue CPP Data
	 */
	@Override
	public Collection getCppData() {
		List<CourtDetailValue> cppData = new ArrayList<CourtDetailValue>();
		
		// Check the court is CPP enabled and then retrieve data
		if(isCourtCppEnabled()){
			
			// Retrieve XML Document of CPP data from the latest XHB_CPP_FORMATTING row for the court supplied
			Document doc = getCppClobAsDocument();
			if ( doc != null ) {
				CourtDetailValue courtDetailValue;
				XhbCourtRoomBasicValue courtRoomValue;
				XhbCourtSiteBasicValue courtSiteValue;
				for ( int roomId : courtRoomIds ) {
					// Loop through all the Court Room Ids supplied and create a new CourtDetailValue for each
					try {	
						// Using court room id, get the XhbCourtRoomBasicValue so we can lookup the CPP <courtroom> node using Court Room Name
						courtRoomValue = getCourtRoomObjectById(roomId);
						if (null != courtRoomValue) {
							courtSiteValue = getCourtSiteObjectById(courtRoomValue.getCourtSiteId());
							
							// Search for matching nodes in the Document based upon the court room name
							NodeList nodes = (NodeList)getXPath().evaluate("//courtsite[courtsitename='"+courtSiteValue.getCourtSiteName()+"']/courtrooms/courtroom[courtroomname='"+courtRoomValue.getCourtRoomName()+"']", doc, XPathConstants.NODESET);
							
							if ( nodes.getLength() > 0 && null != nodes.item(0) ) {
								// Using the <courtroom> node from the XML Document, use the information inside to populate the CourtDetailValue
						    	// Retrieve the active case node and populate the CourtDetailValue with the case data
								NodeList caseNodes = (NodeList)getXPath().evaluate("cases/caseDetails[activecase=1]", (Element)nodes.item(0), XPathConstants.NODESET);
								if ( caseNodes.getLength() != 0 && null != caseNodes.item(0) ) {
									Node caseNode = caseNodes.item(0);
									courtDetailValue = new CourtDetailValue();
									
									// Populate fields Specific to PublicDisplayValue
									populateCourtSiteRoomData(courtDetailValue, courtRoomValue, courtSiteValue);
									
									// Populate the rest of the data
						    		populateData(courtDetailValue, (Element)caseNode);
						    		
						    		// Add the populated CourtDetailValue to cppData
									cppData.add(courtDetailValue);
								}
								else {
									if ( log.isDebugEnabled() ) {
										log.debug("CourtDetailCppToPublicDisplay.getCppData() - no active case data for court room: " + courtRoomValue.getCourtRoomName());
									}
								}
							}
						}
					} catch (XPathExpressionException e) {
						log.error("CourtDetailCppToPublicDisplay.getCppData() - XPathExpressionException  - " + e.toString());
					} catch (Exception e) {
						log.error("CourtDetailCppToPublicDisplay.getCppData() - Exception  - " + e.toString());
					}
				}
			}
		}
				
		return cppData;		
	}
	
	/**
	 * Populates the CourtDetailValue object with data from the courtroom XML element
	 * @param value CourtDetailValue
	 * @param caseNode XML element for the case
	 * @throws XPathExpressionException
	 */
    protected void populateData(CourtDetailValue value, Element caseNode) throws XPathExpressionException {
    	super.populateData(value, caseNode);
    	
    	// Set Data Specific to CourtDetailValue
    	String judgeName = getXPath().evaluate(XPATH_JUDGENAME, caseNode);
    	value.setJudgeName(new JudgeName(judgeName));
    	String hearingDesc = getXPath().evaluate(XPATH_HEARINGTYPE, caseNode);
    	value.setHearingDescription(hearingDesc);
    	populatePublicNotices(value, caseNode);
    }
    
    /**
     * Adds all public notices to the CourtDetailValue
     * @param value CourtDetailValue
     * @param caseNode XML element for the case
     * @throws XPathExpressionException
     */
    protected void populatePublicNotices(CourtDetailValue value, Element caseNode) throws XPathExpressionException {	
    	// Loop through all Public Notices
    	ArrayList<PublicNoticeValue> publicNotices = new ArrayList<PublicNoticeValue>();
    	NodeList publicNoticeList = (NodeList)getXPath().evaluate(XPATH_PUBLICNOTICE, caseNode, XPathConstants.NODESET);
    	PublicNoticeValue pNvalue;
    	for (int i=0; i<publicNoticeList.getLength(); i++) {
    		pNvalue = new PublicNoticeValue();
    		pNvalue.setPublicNoticeDesc( getXPath().evaluate(".", (Element)publicNoticeList.item(i)) );
    		publicNotices.add(pNvalue);
    	}
        value.setPublicNotices(publicNotices.toArray(new PublicNoticeValue[publicNotices.size()]));
    }
    
}
