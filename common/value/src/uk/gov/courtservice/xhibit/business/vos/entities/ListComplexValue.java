package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Collection;

/**
 * <p>
 * Title: ListComplexValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class ListComplexValue extends ListBasicValue {

	private static final long serialVersionUID = 1L;

	private Collection<SittingOnListComplexValue> sittingsOnList;
	private Collection<CaseOnListComplexValue> casesOnList;
	
	public ListComplexValue() {
        super();
    }

    public ListComplexValue(Integer id, Integer version) {
        super(id, version);
    }

	public Collection<SittingOnListComplexValue> getSittingsOnList() {
		return sittingsOnList;
	}

	public void setSittingsOnList(Collection<SittingOnListComplexValue> sittingsOnList) {
		this.sittingsOnList = sittingsOnList;
	}

	public Collection<CaseOnListComplexValue> getCasesOnList() {
		return casesOnList;
	}

	public void setCasesOnList(Collection<CaseOnListComplexValue> casesOnList) {
		this.casesOnList = casesOnList;
	}
}