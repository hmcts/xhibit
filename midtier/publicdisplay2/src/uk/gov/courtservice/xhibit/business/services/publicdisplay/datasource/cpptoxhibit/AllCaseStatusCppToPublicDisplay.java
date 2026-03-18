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
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCaseStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;

public class AllCaseStatusCppToPublicDisplay extends SummaryByNameCppToPublicDisplay {
	
	public AllCaseStatusCppToPublicDisplay(Date date, int courtId, int[] courtRoomIds) {
		super(date, courtId, courtRoomIds);
	}

	@Override
	public Collection getCppData() {
		List<AllCaseStatusValue> cppData = new ArrayList<AllCaseStatusValue>();
		
		// Check the court is CPP enabled and then retrieve data
		if(isCourtCppEnabled()){
			
			// Retrieve XML Document of CPP data from the latest XHB_CPP_FORMATTING row for the court supplied
			Document doc = getCppClobAsDocument();
			if ( doc != null ) {
				AllCaseStatusValue allCaseStatusValue;
				XhbCourtRoomBasicValue courtRoomValue;
				XhbCourtSiteBasicValue courtSiteValue;
				
				for ( int roomId : courtRoomIds ) {
					// Loop through all the Court Room Ids supplied and create a new AllCaseStatusValue for each
					// Using court room id, get the XhbCourtRoomBasicValue so we can lookup the CPP <courtroom> node using Court Room Name
					try {
						courtRoomValue = getCourtRoomObjectById(roomId);
						if (null != courtRoomValue) {
							courtSiteValue = getCourtSiteObjectById(courtRoomValue.getCourtSiteId());
							
							// Search for matching nodes in the Document based upon the court room name
							NodeList nodes = (NodeList)getXPath().evaluate("//courtsite[courtsitename='"+courtSiteValue.getCourtSiteName()+"']/courtrooms/courtroom[courtroomname='"+courtRoomValue.getCourtRoomName()+"']", doc, XPathConstants.NODESET);
							if ( nodes.getLength() > 0 && null != nodes.item(0) ) {
								
								// Return all the cases present in that court room
								NodeList caseNodes = (NodeList)getXPath().evaluate("cases/caseDetails", (Element)nodes.item(0), XPathConstants.NODESET);
								if ( caseNodes.getLength() > 0 && null != caseNodes.item(0) ) {
									
									for ( int ci=0; ci<caseNodes.getLength(); ci++ ) {
										// For each case, extract all the defendants for that case node
										NodeList defendantNodes = (NodeList)getXPath().evaluate(XPATH_DEFENDANT, (Element)caseNodes.item(ci), XPathConstants.NODESET);
										
										if ( defendantNodes.getLength() > 0 && null != defendantNodes.item(0) ) {
											for ( int di=0; di<defendantNodes.getLength(); di++ ) {
												// Create a new AllCaseStatusValue for each Defendant
												allCaseStatusValue = new AllCaseStatusValue();
												
												// Populate fields Specific to PublicDisplayValue
												populateCourtSiteRoomData(allCaseStatusValue, courtRoomValue, courtSiteValue);
												
												// Populate the AllCaseStatusValue specific fields
												populateData(allCaseStatusValue, (Element)defendantNodes.item(di), (Element) caseNodes.item(ci));
												
												// Add the populated AllCaseStatusValue to cppData
												cppData.add(allCaseStatusValue);
											}
										}
									}
								}
							}
						}
					} catch (XPathExpressionException e) {
						log.error("AllCaseStatusCppToPublicDisplay.getCppData() - XPathExpressionException  - " + e.toString());
					} catch (Exception e) {
						log.error("AllCaseStatusCppToPublicDisplay.getCppData() - Exception  - " + e.toString());
					}
				}
				
				try {
					NodeList courtSiteNodes = (NodeList)getXPath().evaluate("//courtsites/courtsite", doc, XPathConstants.NODESET);
					if ( courtSiteNodes.getLength() > 0 && null != courtSiteNodes.item(0) ) {
						// Loop through all the court sites looking for floating cases at each site
						for ( int csi=0; csi<courtSiteNodes.getLength(); csi++ ) {
							
							// Floating cases
							NodeList floatingCaseNodes = (NodeList)getXPath().evaluate("floating/cases/caseDetails", (Element)courtSiteNodes.item(csi), XPathConstants.NODESET);
							if ( floatingCaseNodes.getLength() > 0 && null != floatingCaseNodes.item(0) ) {
								courtSiteValue = getCourtSiteObjectByName( getXPath().evaluate(XPATH_COURTSITENAME, (Element)courtSiteNodes.item(csi)) );
								for ( int fci=0; fci<floatingCaseNodes.getLength(); fci++ ) {
									// For each case, extract all the defendants for that case node
									NodeList defendantNodes = (NodeList)getXPath().evaluate(XPATH_DEFENDANT, (Element)floatingCaseNodes.item(fci), XPathConstants.NODESET);
									
									if ( defendantNodes.getLength() > 0 && null != defendantNodes.item(0) ) {
										for ( int di=0; di<defendantNodes.getLength(); di++ ) {
											// Create a new AllCaseStatusValue for each Defendant
											allCaseStatusValue = new AllCaseStatusValue();
											
											// Populate fields Specific to PublicDisplayValue
											populateCourtSiteRoomData(allCaseStatusValue, null, courtSiteValue);
											
											// Populate the AllCaseStatusValue specific fields
											populateData(allCaseStatusValue, (Element)defendantNodes.item(di), (Element) floatingCaseNodes.item(fci));
											allCaseStatusValue.setFloating("1");
											
											// Add the populated AllCaseStatusValue to cppData
											cppData.add(allCaseStatusValue);
										}
									}
								}
							}
						}
					}
				} catch (XPathExpressionException e) {
					log.error("AllCaseStatusCppToPublicDisplay.getCppData() for floating cases - XPathExpressionException  - " + e.toString());
				} catch (Exception e) {
					log.error("AllCaseStatusCppToPublicDisplay.getCppData() for floating cases - Exception  - " + e.toString());
				}
			}
		}
				
		return cppData;	
	}
	
	/**
	 * Populates the AllCaseStatusValue object with data from the courtroom XML element
	 * @param value AllCaseStatusValue
	 * @param defNode XML element for the defendant
	 * @throws XPathExpressionException
	 */
	protected void populateData(AllCaseStatusValue value, Element defNode, Element caseNode) throws XPathExpressionException {
		// Populate the SummaryByNameValue specific fields
		super.populateData(value, defNode, caseNode);
		
		// Set Case Number
    	String cppurn = getXPath().evaluate(XPATH_CPPURN, caseNode);
    	String caseNumber = getXPath().evaluate(XPATH_CASENUMBER, caseNode);
    	String caseType = getXPath().evaluate(XPATH_CASETYPE, caseNode);
    	
    	if ( !"".equals(cppurn) ) {
    		value.setCaseNumber(cppurn);
    	} else {
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

		String hearingDesc = getXPath().evaluate(XPATH_HEARINGTYPE, caseNode);
    	value.setHearingDescription(hearingDesc);
    	
    	String hearingProgress = getXPath().evaluate(XPATH_HEARINGPROGRESS, caseNode);
    	if ( null != hearingProgress && !"".equals(hearingProgress) ) {
    		value.setHearingProgress(Integer.parseInt(hearingProgress));
    	}
        
        // Set the List Court Room Id
        String listCourtRoomName = getXPath().evaluate(XPATH_LISTCOURTROOM, caseNode);
        XhbCourtRoomBasicValue listCourtRoomValue = getCourtRoomObjectByName(listCourtRoomName);
        if ( null != listCourtRoomValue ) {
        	value.setListCourtRoomId(listCourtRoomValue.getCourtRoomId());
        }
	}
}
