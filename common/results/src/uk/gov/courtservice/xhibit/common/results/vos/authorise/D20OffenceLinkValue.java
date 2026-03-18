package uk.gov.courtservice.xhibit.common.results.vos.authorise;

import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;



public class D20OffenceLinkValue extends CSAbstractValue {

	private static final long serialVersionUID = 1432492614923647511L;

	private static final Logger LOG = CSServices.getLogger(D20OffenceLinkValue.class);

	private String dvlaOffence;
	private String offence;
	private Date dateOfConviction;
	private String offenceDescription;
	private Date dateOffence;
	private String finalD20;
	private String interimD20;
	private Integer sequenceNo;
	private Integer refOffenceID;
	private String obsInd;
	
	/** Overwitten object match method 
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @returns true if properties of object match, otherwise false
	 */
	public boolean equals(Object obj)
	{
		if(obj instanceof D20OffenceLinkValue)
			return equals((D20OffenceLinkValue)obj);
		else 
			return false;
	}
	
	/**
	 * Method conducts comparison checks on current object and candidate object
	 * @param obj candidate object
	 * @return true if the objects match exactly, otherwise false
	 */
	public boolean equals(D20OffenceLinkValue obj)
	{
		boolean valid=true;

		if (obj == null) {
			return false;
		}
		
		try {
		if(dateOffence !=null) {
			valid = valid && dateOffence.equals(obj.getDateOffence());
		}
		
		if(this.dvlaOffence==null) {
			valid = valid && (obj.getDvlaOffence()==null);
		} else {
			valid = valid && dvlaOffence.equals(obj.getDvlaOffence());
		}
		
		if(this.obsInd==null) {
			valid = valid && (obj.getObsInd()==null);
		} else 		{
			valid = valid && obsInd.equals(obj.getObsInd());
		}
		
		if(this.offence==null) {
			valid = valid && (obj.getOffence()==null);
		} else {
			valid = valid && offence.equals(obj.getOffence());
		}
		
		if(this.offenceDescription==null) {
			valid = valid && (obj.getOffenceDescription()==null);
		} else {
			valid = valid && offenceDescription.equals(obj.getOffenceDescription());
		}
		
		if(this.refOffenceID==null) {
			valid = valid && (obj.getRefOffenceID()==null);
		} else {
			valid = valid && refOffenceID.equals(obj.getRefOffenceID());
		}
		if(this.sequenceNo==null) {
			valid = valid && (obj.getSequenceNo()==null);
		} else {
			valid = valid && sequenceNo.equals(obj.getSequenceNo());
		}
		
		if(this.dateOfConviction==null) {
			valid = valid && (obj.getDateOfConviction()==null);
		}
		} catch (NullPointerException npe) {
			LOG.error("Got a null when trying to compare a D20OffenceLinkValue - automatically returning false");
			return false;
		}
		
		return valid;
	}
	
	
	public String getDvlaOffence() {
		return dvlaOffence;
	}
	public void setDvlaOffence(String dvlaOffence) {
		this.dvlaOffence = dvlaOffence;
	}
	public String getOffence() {
		return offence;
	}
	public void setOffence(String offence) {
		this.offence = offence;
	}
	public Date getDateOfConviction() {
		return dateOfConviction;
	}
	public void setDateOfConviction(Date dateOfConviction) {
		this.dateOfConviction = dateOfConviction;
	}
	
	public String getOffenceDescription() {
		return offenceDescription;
	}
	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}
	public Date getDateOffence() {
		return dateOffence;
	}
	public void setDateOffence(Date dateOffence) {
		this.dateOffence = dateOffence;
	}
	public Integer getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public Integer getRefOffenceID() {
		return refOffenceID;
	}
	public void setRefOffenceID(Integer refOffenceID) {
		this.refOffenceID = refOffenceID;
	}
	public String getObsInd() {
		return obsInd;
	}
	public void setObsInd(String obsInd) {
		if(obsInd==null)
		{
			obsInd="";
		}
		this.obsInd = obsInd;
	}
	public String getFinalD20() {
		return finalD20;
	}
	public void setFinalD20(String finalD20) {
		this.finalD20 = finalD20;
	}
	public String getInterimD20() {
		return interimD20;
	}
	public void setInterimD20(String interimD20) {
		this.interimD20 = interimD20;
	}
	
}