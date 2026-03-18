package uk.gov.courtservice.xhibit.business.services.darts;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.naming.NamingException;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.caze.CaseHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefDarRetentionPoliciesBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefDispRetentionPolicyBasicValue;



/**
 * <p>
 * Title: XhibitDartsControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating Darts objects
 * </p>
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="XhibitDartsController" description="Darts Session Bean"
 *           type="Stateless" view-type="both" jndi-name="XhibitDartsControllerHome"
 *           local-jndi-name="XhibitDartsControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class XhibitDartsControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;
	
	private DartsHelper dartsHelper = new DartsHelper();
	
	private CaseHelper caseHelper = new CaseHelper();
	
	public XhibitDartsControllerBean() {
	}

	/**
	 * Used to fetch the DARTS retention policy
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @param caseId
	 * @param darRetentionPolicyId
	 * @return DarRetentionPolicyComplexValue
	 */
	public DarRetentionPolicyComplexValue findDartsRetentionPolicy(final Integer caseId, final Integer darRetentionPolicyId) {
		DarRetentionPolicyComplexValue result = null;
		
		if (darRetentionPolicyId != null) {
			try {
				result = dartsHelper.findDartsRetentionPolicy(caseId, darRetentionPolicyId);
			} catch (FinderException e) {
				CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
	            throw new EJBException(e);
			}
		}
		return result;	
	}
	
	/**
     * Find RefDarRetentionPolicy by Id 
	 * 
	 * @param refDarRetentionPolicyId
	 * 
     * @ejb.interface-method view-type="both"
     * 
	 * @throws FinderException 
     */
	public RefDarRetentionPoliciesBasicValue findRefDarRetentionPolicy(final Integer refDarRetentionPolicyId) throws FinderException {
		return dartsHelper.findRefDarRetentionPolicy(refDarRetentionPolicyId);
	}

	/**
     * Find DarRetentionPolicy by Id 
	 * 
	 * @param darRetentionPolicyId
	 * 
     * @ejb.interface-method view-type="both"
     * 
	 * @throws FinderException 
     */
	public DarRetentionPolicyBasicValue findDarRetentionPolicy(final Integer darRetentionPolicyId) throws FinderException {
		return dartsHelper.findDarRetentionPolicy(darRetentionPolicyId);
	}

	/**
     * Find default RefDarRetentionPolicy 
	 * 
     * @ejb.interface-method view-type="both"
     */
	public RefDarRetentionPoliciesBasicValue getDefaultRefDarPolicy() {
		return dartsHelper.getDefaultRefDarPolicy();
	}
	
	/**
     * Find RefDispRetentionPolicy by Id 
	 * 
	 * @param refDispRetentionPolicyId
	 * 
     * @ejb.interface-method view-type="both"
     * 
	 * @throws FinderException 
     */
	public RefDispRetentionPolicyBasicValue findRefDispRetentionPolicy(final Integer refDispRetentionPolicyId) throws FinderException {
		return dartsHelper.findRefDispRetentionPolicy(refDispRetentionPolicyId);
	}
	
	/**
     * Check whether to send an outgoing retention policy
	 * 
     * @ejb.interface-method view-type="both" 
     * 
     */
	public boolean isSendOutGoingRetentionPolicyRequired(final Integer xhibitEventType) {
		return dartsHelper.isSendOutGoingRetentionPolicyRequired(xhibitEventType);
	}
	
	/**
     * Update case retention policy when sending darts message 
	 * 
	 * @param xhibitEventType
	 * @param caseDarRetentionPolicyId
	 * @param caseId
	 * @param userDisplayName
	 * 
     * @ejb.interface-method view-type="both"
     * 
	 * @throws FinderException 
     */
	public Integer updateRetentionPolicyOnMsgSend(final Integer xhibitEventType, final Integer caseDarRetentionPolicyId, 
			final Integer caseId, final String caseType, final String userDisplayName) throws FinderException {
		return dartsHelper.updateRetentionPolicyOnMsgSend(xhibitEventType, caseDarRetentionPolicyId, caseId, 
				caseType, userDisplayName);
	}
	
	/**
     * Recalculate Darts retention policy for Case
	 * 
     * @ejb.interface-method view-type="both"
     * 
	 * @throws FinderException 
     */
	public Integer recalculateCaseRetentionPolicy(final Integer caseId, final String userDisplayName) throws FinderException {
		return dartsHelper.recalculateCaseRetentionPolicy(caseId, userDisplayName);
	}
	
	/**
     * Get scheduled hearings for Case
	 * 
     * @ejb.interface-method view-type="both"
     * 
	 * @throws FinderException 
     */
	public Collection getScheduledHearings(Integer caseId) {
		try {
			return caseHelper.findScheduledHearingsByCaseId(caseId);	
		} catch (NamingException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw new EJBException(ex);
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw new EJBException(ex);
		}		
	}
}