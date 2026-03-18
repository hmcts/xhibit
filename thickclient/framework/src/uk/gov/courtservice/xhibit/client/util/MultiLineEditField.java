package uk.gov.courtservice.xhibit.client.util;

public class MultiLineEditField extends XTextArea{

	private static final long serialVersionUID = 1L;
	
	public MultiLineEditField(int rows, int maxLength) {
		super();
		this.setRows(rows);
		this.setLimit(maxLength);
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		this.setStyleXTextField();
	}
}
