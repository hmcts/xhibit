package uk.gov.courtservice.xhibit.business.services.shjustice;

import java.util.Collection;
import java.util.Iterator;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;

/**
 * <p>
 * Title: SHJusticeControllerBean
 * </p>
 * <p>
 * Description: SHJusticeControllerBean.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author KudzinC
 * @version 1.0
 * 
 * @ejb.bean name="SHJusticeController" description="SH Justice Session Bean"
 *           type="Stateless" view-type="both"
 *           jndi-name="SHJusticeControllerHome"
 *           local-jndi-name="SHJusticeControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 */
public class SHJusticeControllerBean extends CSSessionBean implements SessionBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SHJusticeHelper shJusticeHelper = new SHJusticeHelper();


    /**
	 * Update the justice (either create or amend)
	 * And also create entry in shced hearing attendee if it's a create(not an update)
	 * 
	 * @param hearingRecordValue
	 *            details for the new Hearing
	 * @throws SHJusticeException
     * @throws SQLException 
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public void update(HearingRecordValue hearingRecordValue, String userDisplayName, Integer scheduledHearingId) throws SHJusticeException, SQLException {
        try {
			shJusticeHelper.update(hearingRecordValue, userDisplayName, scheduledHearingId);
		} catch (ObjectNotFoundException el) {
			ctx.setRollbackOnly();
			CSServices.getDefaultErrorHandler().handleError(el, getClass());
			throw new EJBException(el);
		} catch (SHJusticeException e) {
            ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw e;
		}  catch (IllegalArgumentException el) {
			ctx.setRollbackOnly();
			throw el;
		} catch (EJBException el) {
			ctx.setRollbackOnly();
			throw el;
		} catch (OptimisticLockException el) {
			ctx.setRollbackOnly();
			throw el;
		} catch (SQLException e) {
			ctx.setRollbackOnly();
			throw e;
		} 
    }
	
   /**
	 * Find by hearing id the SH justice values
	 * 
	 * @param hearingId
	 *            hearing id
	 * @throws FinderException
	 * 
	 * @return the sh justices 
	 * 
	 * @ejb.interface-method view-type="remote"
	 */
	public Collection findByHearingId(Integer hearingId) throws FinderException {
		return shJusticeHelper.findByHearingId(hearingId);
	}
}