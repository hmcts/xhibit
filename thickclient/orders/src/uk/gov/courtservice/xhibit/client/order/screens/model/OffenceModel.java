package uk.gov.courtservice.xhibit.client.order.screens.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The details available for the offence that you can add to the D20 order
 * The details is dvla offence, offence, description of the offence and the date of the offence
 * 
 * @author guthriec
 *
 */
public class OffenceModel{
	private String dvlaOffence;
	private String offence;
	private Date dateOfConviction;
	private String offenceDescription;
	private Date dateOffence;
	private Integer offenceID;
	private Integer seqNo; 
	private boolean check;
	private boolean intD20;
	private boolean finD20;
	
	/**
	 * Empty constructor needed and added explicitly as we have another constructor
	 */
	public OffenceModel() {}
	
	public OffenceModel(String dvlaOffence, String offence, Date date, String offenceDescription, Date dateOffence,
			boolean intD20, boolean finD20, Integer offenceID){
		this.dvlaOffence = dvlaOffence;
		this.offence = offence;
		this.dateOfConviction = date;
		this.setOffenceDescription(offenceDescription);
		this.dateOffence = dateOffence;
		this.check = false;
		this.intD20 = intD20;
		this.finD20 = finD20;
		this.offenceID = offenceID;
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof OffenceModel)
			return equals((OffenceModel)o);
		else 
			return false;
			
	}
	
 
	public boolean equals(OffenceModel data)
	{
		boolean matching = true;
		
		if(dvlaOffence!=null) {
			matching= matching && dvlaOffence.equals(data.getDvlaOffence());
		}
		
		if(offence==null) {
			matching = matching && data.getOffence()==null;
		} else {
			matching= matching && offence.equals(data.getOffence());
		}
		
		if(offenceDescription==null) {	
			matching = matching && data.getOffenceDescription()==null; 
		} else {
			matching= matching && offenceDescription.equals(data.getOffenceDescription());
		}
		
		if(dateOffence!=null)
		{
			matching= matching &&  dateOffence.equals(data.getDateOffence());
		}
		
		if(offenceID==null) {
			matching = matching && data.getOffenceID()!=null;
		} else {
			matching= matching &&  offenceID.equals(data.getOffenceID());
		}
		
		if(dateOfConviction!=null) {
			matching= matching && dateOfConviction.equals(data.getDateOfConviction());
		}
		
		return matching;
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
	
	public Date getDateOffence() {
		return dateOffence;
	}
	public void setDateOffence(Date dateOffence) {
		this.dateOffence = dateOffence;
	}
	/**
	 * Sets check boolean
	 * 
	 * @param check
	 */
	public void setCheck(boolean check){
		this.check = check;
	}
	
	public Integer getOffenceID() {
		return offenceID;
	}
	public void setOffenceID(Integer offenceID) {
		this.offenceID = offenceID;
	}
	
	
	/**
	 * @return if checkbox is checked
	 */
	public boolean isChecked(){
		return check;
	}
	public Date getDateOfConviction() {
		return dateOfConviction;
	}
	public void setDateOfConviction(Date dateOfConviction) {
		this.dateOfConviction = dateOfConviction;
	}
	
	/**
	 * @return Conviction Date as a String in format dd-MMM-yyyy
	 */
	public String getConvictionDateString(){
		if(dateOfConviction != null){
			return getDateAsString(dateOfConviction);
		}
		return "";
	}
	
	public String getDateAsString(Date originalDate) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String date = dateFormat.format(originalDate);
		return date;
	}
	
	public String getOffenceDescription() {
		return offenceDescription;
	}
	public void setOffenceDescription(String offenceDescription) {
		this.offenceDescription = offenceDescription;
	}
	/**
	 * @return Offence Date as a String in format dd-MMM-yyyy
	 */
	public String getOffenceDateString(){
		if(dateOffence != null){
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String date = dateFormat.format(dateOffence);
		return date;
		}
		return "";
	}
	
	public boolean isIntD20() {
		return intD20;
	}
	public void setIntD20(boolean intD20) {
		this.intD20 = intD20;
	}
	public boolean isFinD20() {
		return finD20;
	}
	public void setFinD20(boolean finD20) {
		this.finD20 = finD20;
	}
	public Integer getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	
}
