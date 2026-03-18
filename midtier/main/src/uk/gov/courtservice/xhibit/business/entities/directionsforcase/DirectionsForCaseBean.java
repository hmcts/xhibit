package uk.gov.courtservice.xhibit.business.entities.directionsforcase;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Directions For Case Entry bean.
 */
abstract public class DirectionsForCaseBean extends CSEntityBean implements EntityBean {

	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(
			String freetext, Date dateTime,
			Date listDate, String listType,
			String listedAs, String directionsText,
			Integer trialTimeUnit, Float trialTimeEstimate,
			String hasPanddForm, Integer caseId, String userDisplayName) throws CreateException {		

		setFreetext(freetext);
		setDateTime(dateTime);
		setListDate(listDate);
		setListType(listType);
		setListedAs(listedAs);
		setDirectionsText(directionsText);
		setTrialTimeUnit(trialTimeUnit);
		setTrialTimeEstimate(trialTimeEstimate);
		setHasPanddForm(hasPanddForm);
		setCaseId(caseId);		
		setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
		return null;
	}
    
    public void ejbPostCreate(
			String freetext, Date dateTime,
			Date listDate, String listType,
			String listedAs, String directionsText,
			Integer trialTimeUnit, Float trialTimeEstimate,
			String hasPanddForm, Integer caseId, String userDisplayName) throws CreateException {
    }
	
	public abstract Integer getDirectionsForCaseId();
	public abstract String getFreetext();
	public abstract Date getDateTime();
	public abstract Date getListDate();
	public abstract String getListType();
	public abstract String getListedAs();
	public abstract String getDirectionsText();
	public abstract Integer getTrialTimeUnit();
	public abstract Float getTrialTimeEstimate();
	public abstract String getHasPanddForm();
	public abstract Integer getCaseId();

	public abstract void setDirectionsForCaseId(Integer directionsForCaseId);
	public abstract void setFreetext(String freetext);
	public abstract void setDateTime(Date dateTime);
	public abstract void setListDate(Date listDate);
	public abstract void setListType(String listType);
	public abstract void setListedAs(String listedAs);
	public abstract void setDirectionsText(String directionsText);
	public abstract void setTrialTimeUnit(Integer trialTimeUnit);
	public abstract void setTrialTimeEstimate(Float trialTimeEstimate);
	public abstract void setHasPanddForm(String hasPanddForm);
	public abstract void setCaseId(Integer caseId);
	
	
}