package uk.gov.courtservice.xhibit.client.search.judge;

import javax.swing.JTextField;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;

public class SearchJudgeUpdate extends XHIBITSearchDetails {

	@Override
	public CSValueObject getDetailsValueObject() {
		return new RefJudgeComplexValue();
	}

	@Override
	public void setDetails() {
		addDetail("solicitorFirmName", "solicitorFirm.update.firmName");
		addDetail("address1", "solicitorFirm.update.address");
		addDetail("address2", "solicitorFirm.update.address");
		addDetail("address3", "solicitorFirm.update.address");
		addDetail("address4", "solicitorFirm.update.address");
		addDetail("town", "solicitorFirm.update.town");
		addDetail("county", "solicitorFirm.update.county");
		addDetail("postcode", "solicitorFirm.update.postCode");
		addDetail("dxRef", "solicitorFirm.update.docExRef");
		addDetail("telephoneNumber", "solicitorFirm.update.telephone");
		addDetail("faxNumber", "solicitorFirm.update.faxNo");
		addDetail("secureEmailAddress", "solicitorFirm.update.secureEmail");
		addDetail("nonSecureEmailAddress", "solicitorFirm.update.nonSecureEmail");
		addDetail("laCode", "solicitorFirm.update.rOCode");
		addDetail("shortName", "solicitorFirm.update.shortNameLocation");
	}

	
	@Override
	protected void addDetail(String fieldName, String resourceKey) {
		// TODO Auto-generated method stub
		super.addDetail(fieldName, resourceKey, new JTextField());
	}

	@Override
	public String getStepTitleResourceKey() {
		return "solicitorFirm.update.xxtitle";
	}

	@Override
	public String getStepDescriptionResourceKey() {
		return "solicitorFirm.details.xxdescription";
	}	
}
