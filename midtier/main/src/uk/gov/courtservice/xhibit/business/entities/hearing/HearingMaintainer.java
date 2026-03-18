package uk.gov.courtservice.xhibit.business.entities.hearing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Hearing Entity Maintainer Class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version $Id: HearingMaintainer.java,v 1.20 2014/06/20 17:05:10 atwells Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 25/02/03 - JB - Minor mod to update to check for null times.
 * </P>
 */
public class HearingMaintainer extends AbstractEntityMaintainer {

    private static HearingMaintainer instance;

    private static final Logger log = CSServices.getLogger(HearingMaintainer.class);

    private static final String FOUND = "found ";
    
    private static HearingHome home = null;

    public HearingMaintainer() {
        if (home == null) {
            home = (HearingHome) CSServices.getServiceLocator().getLocalHome(HearingHome.class);
        }
    }

    public static HearingMaintainer getInstance() {
        if (instance == null) {
            synchronized (HearingMaintainer.class) {
                if (instance == null) {
                    HearingMaintainer localInstance = new HearingMaintainer();
                    instance = localInstance;
                }
            }
        }

        return instance;
    }

    /**
     * Create and return a Basic VO from local entity.
     * 
     * @param local
     * @return
     */
    public HearingBasicValue getHearingBasicValue(Hearing local) {
        String methodName = "getHearingBasicValue() - ";
        log.debug(methodName + "called");

        HearingBasicValue bv = createBasicVO(local);

        log.debug(methodName + "exited - OK");
        return bv;
    }

    /**
     * Create entity from VO.
     * 
     * @param value
     * @return
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        String methodName = "create() - ";

        log.debug(methodName + "called");
        if (!(value instanceof HearingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
            HearingBasicValue bv = (HearingBasicValue) value;
            Timestamp startDate = null;
            Timestamp endDate = null;

            if (bv.getHearingStartDate() != null) {
                startDate = new Timestamp(bv.getHearingStartDate().getTime());
            }
            if (bv.getHearingEndDate() != null) {
                endDate = new Timestamp(bv.getHearingEndDate().getTime());
            }

            return home.create(bv.getCaseID(), bv.getRefHearingTypeID(), bv.getCourtID(), bv.getMpHearingType(),
                    startDate, endDate, bv.getLinkedHearingID(), userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    /**
     * Update using VO.
     * 
     * @param value
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof HearingBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        HearingBasicValue bv = (HearingBasicValue) value;
        try {
            // Find home...
            Hearing local = home.findByPrimaryKey(bv.getId());

            // Locking check...
            if (!local.getVersion().equals(bv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.setCourtId(bv.getCourtID());
                local.setCaseId(bv.getCaseID());
                if (bv.getHearingEndDate() != null)
                    local.setHearingEndDate(new Timestamp(bv.getHearingEndDate().getTime()));
                if (bv.getHearingStartDate() != null)
                    local.setHearingStartDate(new Timestamp(bv.getHearingStartDate().getTime()));
                local.setLinkedHearingId(bv.getLinkedHearingID());
                local.setMpHearingType(bv.getMpHearingType());
                local.setRefHearingTypeId(bv.getRefHearingTypeID());
                local.setUpdated(userDisplayName);
            }

            log.debug(methodName + "exited - OK");
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Update using collection of VO.
     * 
     * @param value
     */
    public void update(HearingBasicValue[] values, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        try {
            for (int i = 0; i < values.length; i++) {
                update(values[i], userDisplayName);
            }

            log.debug(methodName + "exited - OK");
        } catch (ObjectNotFoundException ex) {
            // ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            // ctx.setRollbackOnly();
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    /**
     * Delete based on ID/Version
     * 
     * @param id
     * @param version
     */
    public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        String methodName = "delete() - ";
        log.debug(methodName + "called");

        try {
            Hearing local = home.findByPrimaryKey(id);

            if (!local.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.remove();
                log.debug(methodName + "exited - OK");
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
     * Find by Primary key
     * 
     * @param hearingId
     * @return
     */
    public Hearing findByPK(Integer id) throws ObjectNotFoundException {
        String methodName = "findByPK() - ";

        log.debug(methodName + "called - hearingId: " + id);
        try {
            Hearing local = home.findByPrimaryKey(id);

            log.debug(methodName + "exited - OK");
            return local;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * Find by LinkedHEaring ID
     * 
     * @param linkedhearingId
     * @return Collection
     */
    public Collection findByLinkedHearingId(Integer linkedHearingId) {
        String methodName = "findByLinkedHearingId() - ";

        log.debug(methodName + "called - linkedhearingId: " + linkedHearingId);
        try {
            Collection hearings = home.findByLinkedHearingId(linkedHearingId);
            log.debug(methodName + "exited - OK");
            return hearings;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public Collection findByCaseId(Integer caseId) throws ObjectNotFoundException {
        String methodName = "findByCaseId() - ";

        log.debug(methodName + "called - linkedhearingId: " + caseId);
		Collection<HearingBasicValue> hearingBV = new ArrayList<HearingBasicValue>();
        try {
            Collection hearings = home.findByCaseId(caseId);
			log.debug(methodName + "exited - OK");
			Iterator iter = hearings.iterator();
			while (iter.hasNext()) {
				Hearing hearing = (Hearing) iter.next();
				HearingBasicValue val = getHearingBasicValue(hearing);
				hearingBV.add(val);
			}
			return hearingBV;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    public Collection<ScheduledHearing> findScheduledByCaseId(Integer caseId) throws ObjectNotFoundException {
        String methodName = "findScheduledByCaseId() - ";

        log.debug(methodName + "called - linkedhearingId: " + caseId);
		Collection<ScheduledHearing> results = new ArrayList<ScheduledHearing>();
        try {
            Collection hearings = home.findByCaseId(caseId);
			Iterator iter = hearings.iterator();
			while (iter.hasNext()) {
				Hearing hearing = (Hearing) iter.next();
				Collection scheduledHearingBeans = hearing.getScheduledHearings();
				if (log.isDebugEnabled()) {
					log.debug(FOUND + scheduledHearingBeans.size() + " scheduled hearings for hearing=" + hearing);
				}
				// Add the scheduled hearings
				Iterator scheduledHearingsIterator = scheduledHearingBeans.iterator();
				while (scheduledHearingsIterator.hasNext()) {
					ScheduledHearing scheduledHearing = (ScheduledHearing) scheduledHearingsIterator.next();
					results.add(scheduledHearing);
				}
			}
			log.debug(methodName + "exited - OK");
			return results;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }
    

    private HearingBasicValue createBasicVO(Hearing local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        HearingBasicValue hbv = new HearingBasicValue(local.getHearingId(), local.getVersion());

        copyEntityPropsToVO(local, hbv);
        return hbv;
    }

    private void copyEntityPropsToVO(Hearing local, HearingBasicValue hearingBasicValue) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        hearingBasicValue.setCourtID(local.getCourtId());
        hearingBasicValue.setCaseID(local.getCaseId());
        hearingBasicValue.setHearingEndDate(local.getHearingEndDate());
        hearingBasicValue.setHearingStartDate(local.getHearingStartDate());
        hearingBasicValue.setLinkedHearingID(local.getLinkedHearingId());
        hearingBasicValue.setMpHearingType(local.getMpHearingType());
        hearingBasicValue.setRefHearingTypeID(local.getRefHearingTypeId());
    }
}