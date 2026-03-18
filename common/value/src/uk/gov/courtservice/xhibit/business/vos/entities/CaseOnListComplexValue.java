package uk.gov.courtservice.xhibit.business.vos.entities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Title: CaseOnListComplexValue
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
public class CaseOnListComplexValue extends CaseOnListBasicValue {

	private static final long serialVersionUID = 1L;

	private CaseBasicValue caseBasicValue;
	
	private RefHearingTypeBasicValue hearingTypeBasicValue;
	
	private Collection<DefOnCaseOnListBasicValue> defOnCaseOnListBasicValues = new ArrayList<DefOnCaseOnListBasicValue>();
	
	private CaseListingEntryBasicValue caseListingEntryBasicValue;

	private DirectionsForCaseBasicValue directionsForCaseBasicValue;

	private RefSystemCodeBasicValue timeMarkingBasicValue;
	
	public CaseOnListComplexValue() {
        super();
    }

    public CaseOnListComplexValue(CaseOnListBasicValue basicValue) {
        this(basicValue.getCaseOnListId(), basicValue.getVersion());
        cloneFromBasicValue(basicValue);
    }

    public CaseOnListComplexValue(Integer id, Integer version) {
        super(id, version);
    }

	public CaseBasicValue getCase() {
		return caseBasicValue;
	}

	public void setCase(CaseBasicValue caseBasicValue) {
		this.caseBasicValue = caseBasicValue;
	}

	public RefHearingTypeBasicValue getHearingType() {
		return hearingTypeBasicValue;
	}

	public void setHearingType(RefHearingTypeBasicValue hearingTypeBasicValue) {
		this.hearingTypeBasicValue = hearingTypeBasicValue;
	}

	public Collection<DefOnCaseOnListBasicValue> getDefOnCaseOnLists() {
		return defOnCaseOnListBasicValues;
	}

	public void setDefOnCaseOnLists(Collection<DefOnCaseOnListBasicValue> defOnCaseOnListBasicValues) {
		this.defOnCaseOnListBasicValues = defOnCaseOnListBasicValues;
	}

	public CaseListingEntryBasicValue getCaseListingEntry() {
		return caseListingEntryBasicValue;
	}

	public void setCaseListingEntry(CaseListingEntryBasicValue caseListingEntryBasicValue) {
		this.caseListingEntryBasicValue = caseListingEntryBasicValue;
	}
    
	public DirectionsForCaseBasicValue getDirectionsForCase() {
		return directionsForCaseBasicValue;
	}

	public void setDirectionsForCase(DirectionsForCaseBasicValue directionsForCaseBasicValue) {
		this.directionsForCaseBasicValue = directionsForCaseBasicValue;
	}

	public RefSystemCodeBasicValue getTimeMarking() {
		return timeMarkingBasicValue;
	}

	public void setTimeMarking(RefSystemCodeBasicValue timeMarkingBasicValue) {
		this.timeMarkingBasicValue = timeMarkingBasicValue;
	}
	
	private void cloneFromBasicValue(CaseOnListBasicValue basicValue) {
		List<Field> originalFields = Arrays.asList(basicValue.getClass().getDeclaredFields());
		for (Field originalField : originalFields) {
			Field matchingField = getMatchingSuperclassField(originalField);
			Object value = getFieldValue(basicValue, originalField);
			setFieldValue(matchingField, value);			
		}
	}
	
	private Object getFieldValue(CaseOnListBasicValue source, Field field) {
		Object value = null;
		try {
			boolean isAccessible = field.isAccessible(); 
			field.setAccessible(true);
			value = field.get(source);
			field.setAccessible(isAccessible);
		} catch (IllegalArgumentException e) { 
		} catch (IllegalAccessException e) {			
		}
		return value;
	}
	
	private void setFieldValue(Field field, Object value) {
		if (field != null) {
			try {
				boolean isAccessible = field.isAccessible(); 
				field.setAccessible(true);
				field.set(this, value);
				field.setAccessible(isAccessible);
			} catch (IllegalArgumentException e) { 
			} catch (IllegalAccessException e) {
			}
		}
	}
	
	private Field getMatchingSuperclassField(Field originalField) {
		try {
			Field matchingField = this.getClass().getSuperclass().getDeclaredField(originalField.getName());
			if (originalField.getType().equals(matchingField.getType())) {
				return matchingField;
			}
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
		return null;
	} 
}