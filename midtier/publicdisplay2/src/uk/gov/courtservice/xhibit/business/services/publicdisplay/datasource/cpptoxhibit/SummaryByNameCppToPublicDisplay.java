package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.SummaryByNameValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;

public class SummaryByNameCppToPublicDisplay extends AbstractCppToPublicDisplay {
	
	public SummaryByNameCppToPublicDisplay(Date date, int courtId, int[] courtRoomIds) {
		super(date, courtId, courtRoomIds);
	}

	@Override
	public Collection getCppData() {
		List<SummaryByNameValue> cppData = new ArrayList<SummaryByNameValue>();
		
		// Check the court is CPP enabled and then retrieve data
		if(isCourtCppEnabled()){
			
			// Retrieve XML Document of CPP data from the latest XHB_CPP_FORMATTING row for the court supplied
			Document doc = getCppClobAsDocument();
			if ( doc != null ) {
				SummaryByNameValue summaryByNameValue;
				XhbCourtRoomBasicValue courtRoomValue;
				XhbCourtSiteBasicValue courtSiteValue;
				
				for ( int roomId : courtRoomIds ) {
					// Loop through all the Court Room Ids supplied and create a new SummaryByNameValue for each
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
												// Create a new SummaryByNameValue for each Defendant
												summaryByNameValue = new SummaryByNameValue();
												
												// Populate fields Specific to PublicDisplayValue
												populateCourtSiteRoomData(summaryByNameValue, courtRoomValue, courtSiteValue);
												
												// Populate the SummaryByNameValue specific fields
												populateData(summaryByNameValue, (Element)defendantNodes.item(di), (Element)caseNodes.item(ci));
												
												// Add the populated SummaryByNameValue to cppData
												cppData.add(summaryByNameValue);
											}
										}
									}
								}
							}
						}
					} catch (XPathExpressionException e) {
						log.error("SummaryByNameCppToPublicDisplay.getCppData() - XPathExpressionException  - " + e.toString());
					} catch (Exception e) {
						log.error("SummaryByNameCppToPublicDisplay.getCppData() - Exception  - " + e.toString());
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
											// Create a new SummaryByNameValue for each Defendant
											summaryByNameValue = new SummaryByNameValue();
											
											// Populate fields Specific to PublicDisplayValue
											populateCourtSiteRoomData(summaryByNameValue, null, courtSiteValue);
											
											// Populate the SummaryByNameValue specific fields
											populateData(summaryByNameValue, (Element)defendantNodes.item(di), (Element) floatingCaseNodes.item(fci));
											summaryByNameValue.setFloating("1");
											
											// Add the populated SummaryByNameValue to cppData
											cppData.add(summaryByNameValue);
										}
									}
								}
							}
						}
					}
				} catch (XPathExpressionException e) {
					log.error("SummaryByNameCppToPublicDisplay.getCppData() for floating cases - XPathExpressionException  - " + e.toString());
				} catch (Exception e) {
					log.error("SummaryByNameCppToPublicDisplay.getCppData() for floating cases - Exception  - " + e.toString());
				}
			}
		}
				
		return cppData;	
	}
	
	/**
	 * Populates the SummaryByNameValue object with data from the courtroom XML element
	 * @param value SummaryByNameValue
	 * @param defNode XML element for the defendant
	 * @throws XPathExpressionException
	 */
	protected void populateData(SummaryByNameValue value, Element defNode, Element caseNode) throws XPathExpressionException {
		//Populate PublicDisplayValue specific fields
		super.populateData(value, caseNode);
		
		// Populate the SummaryByNameValue specific fields
		value.setDefendantName( new DefendantName(
				getXPath().evaluate(XPATH_DEF_FIRSTNAME, defNode),
				getXPath().evaluate(XPATH_DEF_MIDDLENAME, defNode), 
				getXPath().evaluate(XPATH_DEF_SURNAME, defNode), 
				false)	// Hide from Public Display
		);
		
		value.setReportingRestricted( getDefendantReportRestriction(defNode) );
		value.setFloating("0"); // Field needs to be set otherwise the SummaryByNameValue won't appear in the Public Display
	}

}
