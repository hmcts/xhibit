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
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JudgeName;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JuryStatusDailyListValue;

public class JuryCurrentStatusCppToPublicDisplay extends CourtListCppToPublicDisplay {
	
	public JuryCurrentStatusCppToPublicDisplay(Date date, int courtId, int[] courtRoomIds) {
		super(date, courtId, courtRoomIds);
	}

	@Override
	public Collection getCppData() {		
		Collection<JuryStatusDailyListValue> cppData = new ArrayList<JuryStatusDailyListValue>();
		
		// Check the court is CPP enabled and then retrieve data
		if(isCourtCppEnabled()){
			// Retrieve XML Document of CPP data from the latest XHB_CPP_FORMATTING row for the court supplied
			Document cppDocument = getCppClobAsDocument();
			if ( cppDocument != null ) {

				XhbCourtRoomBasicValue courtRoomValue;
				XhbCourtSiteBasicValue courtSiteValue;
				for ( int roomId : courtRoomIds ) {
					// Loop through all the Court Room Ids supplied and create a new JuryStatusDailyListValue for each case
					try {						
						// Using court room id, get the XhbCourtRoomBasicValue so we can lookup the CPP <courtroom> node using Court Room Name
						courtRoomValue = getCourtRoomObjectById(roomId);
						if (null != courtRoomValue) {
							// Using court site id, get the XhbCourtSiteBasicValue so we can add details to the object
							courtSiteValue = getCourtSiteObjectById(courtRoomValue.getCourtSiteId());
						
							// Search for matching nodes in the Document based upon the court room name
							Node courtRoom = getCourtRoomNode(cppDocument, courtRoomValue);
							if (courtRoom != null) {
								// retrieve the list of cases and add them
								NodeList cases = (NodeList) getXPath().evaluate(XPATH_CASE, courtRoom, XPathConstants.NODESET);
								for (int i=0; i<cases.getLength(); i++) {
									Node caze = cases.item(i);
									if (null != caze){
										JuryStatusDailyListValue juryStatusDailyListValue = new JuryStatusDailyListValue();
																			
										populateData(juryStatusDailyListValue, caze, courtRoomValue, courtSiteValue);
										
										//add to the collection
										cppData.add(juryStatusDailyListValue);
									}
								}					    	
							}
						}
					} catch (XPathExpressionException e) {
						log.error("JuryCurrentStatusCppToPublicDisplay.getCppData() - XPathExpressionException  - " + e.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				try {
					NodeList courtSiteNodes = (NodeList)getXPath().evaluate("//courtsites/courtsite", cppDocument, XPathConstants.NODESET);
					if ( courtSiteNodes.getLength() > 0 && null != courtSiteNodes.item(0) ) {
						// Loop through all the court sites looking for floating cases at each site
						for ( int csi=0; csi<courtSiteNodes.getLength(); csi++ ) {
							
							// Floating cases
							NodeList floatingCaseNodes = (NodeList)getXPath().evaluate("floating/cases/caseDetails", (Element)courtSiteNodes.item(csi), XPathConstants.NODESET);
							if ( floatingCaseNodes.getLength() > 0 && null != floatingCaseNodes.item(0) ) {
								courtSiteValue = getCourtSiteObjectByName( getXPath().evaluate(XPATH_COURTSITENAME, (Element)courtSiteNodes.item(csi)) );
								for ( int fci=0; fci<floatingCaseNodes.getLength(); fci++ ) {
									Node caze = floatingCaseNodes.item(fci);
									if (null != caze){
										JuryStatusDailyListValue juryStatusDailyListValue = new JuryStatusDailyListValue();
																			
										populateData(juryStatusDailyListValue, caze, null, courtSiteValue);
										juryStatusDailyListValue.setFloating("1");
										//add to the collection
										cppData.add(juryStatusDailyListValue);
									}
								}
							}
						}
					}
				} catch (XPathExpressionException e) {
					log.error("JuryCurrentStatusCppToPublicDisplay.getCppData() for floating cases - XPathExpressionException  - " + e.toString());
				} catch (Exception e) {
					log.error("JuryCurrentStatusCppToPublicDisplay.getCppData() for floating cases - Exception  - " + e.toString());
				}
			}	
		}
				
		return cppData;		
	}

	private void populateData(JuryStatusDailyListValue juryStatusDailyListValue, Node caze, 
			XhbCourtRoomBasicValue courtRoomValue, XhbCourtSiteBasicValue courtSiteValue) {			

		try {
			super.populateData(juryStatusDailyListValue, caze, courtRoomValue, courtSiteValue);
			
			// Populate fields Specific to JuryStatusDailyListValue
			// Retrieve data from the xml structure
			Element caseElement = (Element)caze;
			
			String judgeNameStr = getXPath().evaluate(XPATH_JUDGENAME, caseElement);
			juryStatusDailyListValue.setJudgeName(new JudgeName(judgeNameStr));
			
			juryStatusDailyListValue.setFloating("0");

		} catch (XPathExpressionException e) {
			log.error("JuryCurrentStatusCppToPublicDisplay.populateData() - XPathExpressionException  - " + e.toString());
		}
	}

}
