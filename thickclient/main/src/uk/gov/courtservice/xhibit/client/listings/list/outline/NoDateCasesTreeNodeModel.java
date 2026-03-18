package uk.gov.courtservice.xhibit.client.listings.list.outline;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;

/**
 * Model class for data required for no date cases in the outline control.
 * 
 * @author uphillj
 *
 */
public class NoDateCasesTreeNodeModel extends AbstractTreeNodeModel {

	private static final long serialVersionUID = 1L;
	
	private XhbCourtSiteBasicValue courtSiteBasicValue;
	
	public XhbCourtSiteBasicValue getCourtSite() {
		return courtSiteBasicValue;
	}

	public void setCourtSite(XhbCourtSiteBasicValue courtSiteBasicValue) {
		this.courtSiteBasicValue = courtSiteBasicValue;
	}

	@Override
	public String getDisplayName() {
		return "No Specific Date";
	}
	
}
