package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.util.Calendar;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;

/**
 * Model class for data required for floater cases in the outline control.
 * 
 * @author uphillj
 *
 */
public class FloaterCasesTreeNodeModel extends AbstractTreeNodeModel {

	private static final long serialVersionUID = 1L;
	
	private XhbCourtSiteBasicValue courtSiteBasicValue;
	
	private Calendar listDate;

	public XhbCourtSiteBasicValue getCourtSite() {
		return courtSiteBasicValue;
	}

	public void setCourtSite(XhbCourtSiteBasicValue courtSiteBasicValue) {
		this.courtSiteBasicValue = courtSiteBasicValue;
	}
	
	public Calendar getListDate() {
		return listDate;
	}
	
	public void setListDate(Calendar listDate) {
		this.listDate = listDate;
	}
	
	@Override
	public String getDisplayName() {
		return "Floater Cases";
	}
	
}
