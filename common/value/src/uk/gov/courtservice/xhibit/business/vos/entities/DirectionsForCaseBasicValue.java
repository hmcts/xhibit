package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: DirectionsForCaseBasicValue
 * </p>
 * <p>
 * Description: Directions For Case Basic Value
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */

public class DirectionsForCaseBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer directionsForCaseId;
	private String freetext;
	private Date dateTime;
	private Date listDate;
	private String listType;
	private String listedAs;
	private String directionsText;
	private Integer trialTimeUnit;
	private Float trialTimeEstimate;
	private String hasPanddForm;
	private Integer caseId;

    public DirectionsForCaseBasicValue() {
    }

    public DirectionsForCaseBasicValue(Integer id, Integer version) {
        super(id, version);
    }
    
	public DirectionsForCaseBasicValue(Integer directionsForCaseId,
			String freetext, Date dateTime,
			Date listDate, String listType,
			String listedAs, String directionsText,
			Integer trialTimeUnit, Float trialTimeEstimate,
			String hasPanddForm, Integer caseId )
	{      
		setDirectionsForCaseId(directionsForCaseId);
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
	}

	public Integer getDirectionsForCaseId()
	{
		return directionsForCaseId;
	}

	public void setDirectionsForCaseId( Integer directionsForCaseId )
	{
		this.directionsForCaseId = directionsForCaseId;
	}

	public String getFreetext()
	{
		return freetext;
	}
	
	public void setFreetext( String freetext )
	{
		this.freetext = freetext;
	}

	public java.util.Date getDateTime()
	{
		return dateTime;
	}
	
	public void setDateTime( Date dateTime )
	{
		this.dateTime = dateTime;
	}

	public java.util.Date getListDate()
	{
		return listDate;
	}
	
	public void setListDate( Date listDate )
	{
		this.listDate = listDate;
	}

	public String getListType()
	{
		return listType;
	}
	
	public void setListType( String listType )
	{
		this.listType = listType;
	}

	public String getListedAs()
	{
		return listedAs;
	}
	
	public void setListedAs( String listedAs )
	{
		this.listedAs = listedAs;
	}

	public String getDirectionsText()
	{
		return directionsText;
	}
	
	public void setDirectionsText( String directionsText )
	{
		this.directionsText = directionsText;
	}

	public Integer getTrialTimeUnit()
	{
		return trialTimeUnit;
	}
	
	public void setTrialTimeUnit( Integer trialTimeUnit )
	{
		this.trialTimeUnit = trialTimeUnit;
	}

	public Float getTrialTimeEstimate()
	{
		return trialTimeEstimate;
	}
	
	public void setTrialTimeEstimate( Float trialTimeEstimate )
	{
		this.trialTimeEstimate = trialTimeEstimate;
	}

	public String getHasPanddForm()
	{
		return hasPanddForm;
	}
	public void setHasPanddForm( String hasPanddForm )
	{
		this.hasPanddForm = hasPanddForm;
	}

	public Integer getCaseId()
	{
		return caseId;
	}
	public void setCaseId( Integer caseId )
	{
		this.caseId = caseId;
	}
}
