package uk.gov.courtservice.xhibit.client.listings.casesummary;

import uk.gov.courtservice.xhibit.client.util.XTextArea;

public class DisplayOnlyTextArea extends XTextArea {

	private static final long serialVersionUID = 1L;

	public DisplayOnlyTextArea() {
		super();
		this.setEditable(false);
		this.setFocusable(false);
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		this.setCaretPosition(0);
	}
}
