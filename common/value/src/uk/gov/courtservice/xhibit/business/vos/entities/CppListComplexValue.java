package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;

public class CppListComplexValue extends CppListBasicValue {

	private static final long serialVersionUID = 1L;

	private XhbClobBasicValue listClob;
	private XhbClobBasicValue mergedClob;
	
	public CppListComplexValue() {
	}

	public CppListComplexValue(Integer id, Integer version) {
		super(id, version);
	}

	public XhbClobBasicValue getListClob() {
		return listClob;
	}

	public void setListClob(XhbClobBasicValue listClob) {
		this.listClob = listClob;
	}

	public XhbClobBasicValue getMergedClob() {
		return mergedClob;
	}

	public void setMergedClob(XhbClobBasicValue mergedClob) {
		this.mergedClob = mergedClob;
	}
}