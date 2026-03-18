package uk.gov.courtservice.xhibit.business.entities.internethtml;

// JDK
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.InternetHtmlBasicValue;


public class InternetHtmlMaintainer extends AbstractEntityMaintainer {

    private static InternetHtmlHome home = null;

    private static Logger log = CSServices.getLogger(InternetHtmlMaintainer.class);

    public InternetHtmlMaintainer() {
        if (home == null) {
            home = (InternetHtmlHome) CSServices.getServiceLocator().getLocalHome(InternetHtmlHome.class);
        }
    }

    /**
     * Create and return a Basic VO from local entity.
     * 
     * @param local
     * @return InternetHtmlBasicValue
     */
    public InternetHtmlBasicValue getInternetHtmlBasicValue(InternetHtml local) {
        String methodName = "getInternetHtmlBasicValue() - ";
        log.debug(methodName + "called");

        InternetHtmlBasicValue value = createBasicVO(local);

        return value;
    }


    /**
     * Create entity from VO.
     * 
     * @param value
     * @return InternetHtmlBasicValue VO.
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        String methodName = "create() - ";
        log.debug(methodName + "called");

        if (!(value instanceof InternetHtmlBasicValue)) {
            if (log.isDebugEnabled())
                log.debug(methodName + "Unexpected type:" + value.getClass());
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        } else {
            try {
            	InternetHtmlBasicValue hbv = (InternetHtmlBasicValue) value;

                if (log.isDebugEnabled()) {
                    log.debug(methodName + "Passed in parameters ..." + " status: " + hbv.getStatus()
                    	+ " CourtId: " + hbv.getCourtId()
                        + " HtmlBlobId: " + hbv.getHtmlBlobId());
                }

                InternetHtml internetHtml = home.create(hbv.getStatus(), hbv.getCourtId(),
                		hbv.getHtmlBlobId(), userDisplayName);

                return internetHtml;
            } catch (CreateException e) {
                CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
                throw new EJBException(e);
            }
        }
    }

    /**
     * Update the Entity. Will throw an OptimisticLockException if versions in
     * VO and database differ.
     * 
     * @param value
     *            InternetHtmlBasicValue VO.
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof InternetHtmlBasicValue))
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());

        InternetHtmlBasicValue hbv = (InternetHtmlBasicValue) value;

        try {
            // Find home
            log.debug(methodName + "finding by primary key: " + hbv.getId());
            InternetHtml internetHtml = home.findByPrimaryKey(hbv.getId());
            log.debug(methodName + "VO: " + hbv.getVersion() + " Entity: " + internetHtml.getVersion());
            if (!hbv.getVersion().equals(internetHtml.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                log.debug(methodName + "updating");
                internetHtml.setStatus(hbv.getStatus());
                internetHtml.setCourtId(hbv.getCourtId());
                internetHtml.setHtmlBlobId(hbv.getHtmlBlobId());
                internetHtml.setUpdated(userDisplayName);
            }
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Delete Entity based on id/version.
     * 
     * @param id
     * @param version
     */
    public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        String methodName = "delete() - ";
        log.debug(methodName + "called");

        try {
            InternetHtml internetHtml = home.findByPrimaryKey(id);
            if (!internetHtml.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
            	internetHtml.remove();
            }
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        } catch (RemoveException r) {
            CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
            throw new EJBException(r);
        }
    }

    /**
     * Find by PK.
     * 
     * @param internetHtmlId
     * @return HearingList entity
     */
    public InternetHtml findByPrimaryKey(Integer internetHtmlId) throws ObjectNotFoundException {
        String methodName = "findByPrimaryKey() - ";
        log.debug(methodName + "called :: internetHtmlId: " + internetHtmlId);

        try {
            return home.findByPrimaryKey(internetHtmlId);
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        }
    }


    //
    // -------------------- Private Methods ------------------------
    //

    private InternetHtmlBasicValue createBasicVO(InternetHtml local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        InternetHtmlBasicValue hlbv = new InternetHtmlBasicValue(local.getInternetHtmlId(), local.getVersion());

        copyEntityPropsToVO(local, hlbv);
        return hlbv;
    }

    private void copyEntityPropsToVO(InternetHtml local, InternetHtmlBasicValue internetHtmlBasicValue) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        // PK...
        
        internetHtmlBasicValue.setStatus(local.getStatus());
        internetHtmlBasicValue.setCourtId(local.getCourtId());
        internetHtmlBasicValue.setHtmlBlobId(local.getHtmlBlobId());
    }

    private Timestamp checkForNullDate(Date date) {
        if (date != null)
            return new Timestamp(date.getTime());
        else
            return null;
    }
}