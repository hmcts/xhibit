package uk.gov.courtservice.xhibit.business.vos.services.listing;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;

/**
* <p>
* Title: ListValue
* </p>
* <p>
* Description: 
* </p>
* <p>
* Company: CGI
* </p>
* 
* @author John Uphill 
* @version 1.0
*/
public class ListValue extends CSAbstractValue {
	
	private static final long serialVersionUID = 1L;
	
	private ListBasicValue list;
	private Collection<SittingOnListBasicValue> sittingsOnList;
	private Collection<CaseOnListBasicValue> casesOnList;
	private Collection<DefOnCaseOnListBasicValue> defOnCasesOnList;
	
	public ListValue(ListBasicValue list){
		setList(list);
		setSittingsOnList(new ArrayList<SittingOnListBasicValue>());
		setCasesOnList(new ArrayList<CaseOnListBasicValue>());
		setDefOnCasesOnList(new ArrayList<DefOnCaseOnListBasicValue>());
	}
	
	public ListValue(ListBasicValue list,
		   Collection<SittingOnListBasicValue> sittingsOnList,
		   Collection<CaseOnListBasicValue> casesOnList,
		   Collection<DefOnCaseOnListBasicValue> defOnCasesOnList){
		setList(list);
		setSittingsOnList(sittingsOnList);
		setCasesOnList(casesOnList);
		setDefOnCasesOnList(defOnCasesOnList);
	}

	public ListBasicValue getList() {
		return list;
	}

	public void setList(ListBasicValue list) {
		this.list = list;
	}

	public Collection<SittingOnListBasicValue> getSittingsOnList() {
		return sittingsOnList;
	}

	public void setSittingsOnList(Collection<SittingOnListBasicValue> sittingsOnList) {
		this.sittingsOnList = sittingsOnList;
	}

	public Collection<CaseOnListBasicValue> getCasesOnList() {
		return casesOnList;
	}

	public void setCasesOnList(Collection<CaseOnListBasicValue> casesOnList) {
		this.casesOnList = casesOnList;
	}

	public Collection<DefOnCaseOnListBasicValue> getDefOnCasesOnList() {
		return defOnCasesOnList;
	}

	public void setDefOnCasesOnList(Collection<DefOnCaseOnListBasicValue> defOnCasesOnList) {
		this.defOnCasesOnList = defOnCasesOnList;
	}
}