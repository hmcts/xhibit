package uk.gov.courtservice.xhibit.business.services.formatting;

import javax.xml.xpath.XPathExpressionException;

public class IWPXMLMergeUtils extends AbstractXMLMergeUtils {		
	
	private static final String IWP = "IWP";

	public IWPXMLMergeUtils() throws XPathExpressionException {
		super(new String[] {"currentcourtstatus/court/courtsites/courtsite/courtrooms"});
	}
	
	@Override 
	public String[] getNodeMatchArray() {
		return new String[] {"courtsitename"};
	}	
	
	@Override 
	public String[] getNodePositionArray() {
		return new String[] {"courtroomname"};
	}	
	
	@Override
	public String getMergeType () {
		return IWP;
	}
}
