package uk.gov.courtservice.xhibit.business.vos.entities;

public class AggravatingReasonsComplexValue extends AggravatingReasonsBasicValue {
	private static final long serialVersionUID = 1L;
	private RefAggravatingReasonsBasicValue refAggravatingReasonsBasicValue;
 	
	public AggravatingReasonsComplexValue() {
		super();
	}
	
	public AggravatingReasonsComplexValue(Integer id, Integer version) {
		super(id, version);
	}

	public RefAggravatingReasonsBasicValue getRefAggravatingReasonsBasicValue() {
		return refAggravatingReasonsBasicValue;
	}

	public void setRefAggravatingReasonsBasicValue(RefAggravatingReasonsBasicValue refAggravatingReasonsBasicValue) {
		this.refAggravatingReasonsBasicValue = refAggravatingReasonsBasicValue;
	}
}
