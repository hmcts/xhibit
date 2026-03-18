package uk.gov.courtservice.xhibit.client.results.disposals.insertcomponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;

public class V25InsertComponent extends DefaultInsertComponent {
	
	private static final long serialVersionUID = 1L;
	boolean mandatory;
	
	@Override
	public boolean isMandatory() {
		return mandatory;
	}

	@Override
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
		if (mandatory) {
			getTextArea().setBackground(DataComponent.MANDATORY_COLOR);
		}
	}

	@Override
	public boolean isComplete() {
		if (isEnabled() && isMandatory()) {
			return isPopulated();
		}
		return super.isComplete();
	}
}