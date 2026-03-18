package uk.gov.courtservice.xhibit.business.services.defendantreference;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;

/**
 * <p>
 * Title: DefendantReferenceControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating defendant reference details
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * 
 * @ejb.bean name="DefendantReferenceController" description="Defendant Reference Session Bean"
 *           type="Stateless" view-type="both"
 *           jndi-name="DefendantReferenceControllerHome"
 *           local-jndi-name="DefendantReferenceControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 */
public class DefendantReferenceControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private DefendantReferenceHelper defendantReferenceHelper = new DefendantReferenceHelper();

	/**
	 * Find if a defendant reference exists for a given defendant and reference
	 * name
	 * 
	 * @param defendantId
	 * @param referenceName
	 * @ejb.interface-method view-type="both"
	 * @return DefendantReferenceBasicValue
	 * @throws ObjectNotFoundException
	 * 
	 */
	public DefendantReferenceBasicValue findByDefendantIdAndReferenceName(Integer defendantId, String referenceName) {
		DefendantReferenceBasicValue dR = defendantReferenceHelper.findByDefendantIdAndReferenceName(defendantId, referenceName);
		if(dR == null){
			return null;
		} else {
			return dR;
		}
	}

}