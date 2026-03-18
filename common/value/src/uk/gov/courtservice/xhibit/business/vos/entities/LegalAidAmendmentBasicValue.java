package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: LegalAidAmendmentBasicValue
 * </p>
 * <p>
 * Description: A Value Object where the attributes map one to one with the
 * LegalAidOrder enitity CMP fields.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Nia Walters
 * @version 1.0
 * 
 */

public class LegalAidAmendmentBasicValue extends CSAbstractValue{
	
	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private Integer legalAidAmendmentId;
	private Integer legalAidOrderId;
	private Date amendmentDate;
	private String obsInd;
	private String amendmentType;
	
	
	
	public LegalAidAmendmentBasicValue() {
		super();
	}
	
	/**
	 * Constructor to set id and version.
	 * @param id Integer
	 * @param version Integer
	 */
	public LegalAidAmendmentBasicValue(Integer id, Integer version) {
		super(id, version);
	}

   public Integer getLegalAidAmendmentId(  ) {
	   return legalAidAmendmentId;
   }
   public void setLegalAidAmendmentId(Integer legalAidAmendmentId ) {
	   this.legalAidAmendmentId=legalAidAmendmentId;
   }

   public Integer getLegalAidOrderId(  ) {
	   return legalAidOrderId;
   }
   public void setLegalAidOrderId(Integer legalAidOrderId) {
	   this.legalAidOrderId=legalAidOrderId;
   }

   public Date getAmendmentDate(  ) {
	   return amendmentDate;
   }
   public  void setAmendmentDate(Date amendmentDate ) {
	   this.amendmentDate=amendmentDate;
   }


   public String getObsInd(  ) {
	   return obsInd;
   }
   public void setObsInd(String obsInd ) {
	   this.obsInd=obsInd;
   }

   public String getAmendmentType(  ) {
	   return amendmentType;
   }
   public void setAmendmentType(String amendmentType ) {
	   this.amendmentType=amendmentType;
   }

}
