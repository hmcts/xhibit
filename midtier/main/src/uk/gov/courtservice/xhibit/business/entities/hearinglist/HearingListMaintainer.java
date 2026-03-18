package uk.gov.courtservice.xhibit.business.entities.hearinglist;

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
import uk.gov.courtservice.xhibit.business.vos.entities.HearingListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingListComplexValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: HearingList Entity Maintainer Class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Babad
 * @version $Id: HearingListMaintainer.java,v 1.15 2005/02/11 16:17:27 sz0t7n
 *          Exp $
 * 
 * <Change History/>
 * 
 * <P>
 * 10/02/03 - JB - First issue.
 * </P>
 * <P>
 * 10/02/03 - PDF - Change return type for create method.
 * </P>
 * <P>
 * 13/02/03 - JB - Various changes. Added implementations of methods.
 * </P>
 * <P>
 * 13/02/03 - JB - Changed VO creation.
 * </P>
 * <P>
 * 27/02/03 - JB - Using getId
 * </P>
 */

public class HearingListMaintainer extends AbstractEntityMaintainer {

    private static HearingListHome home = null;

    private static Logger log = CSServices.getLogger(HearingListMaintainer.class);

    public HearingListMaintainer() {
        if (home == null) {
            home = (HearingListHome) CSServices.getServiceLocator().getLocalHome(HearingListHome.class);
        }
    }

    /**
     * Create and return a Basic VO from local entity.
     * 
     * @param local
     * @return HearingListBasicValue
     */
    public HearingListBasicValue getHearingListBasicValue(HearingList local) {
        String methodName = "getHearingListBasicValue() - ";
        log.debug(methodName + "called");

        HearingListBasicValue value = createBasicVO(local);

        return value;
    }

    /**
     * Create/return a complex VO from a local entity.
     * 
     * @param local
     * @return HearingListComplexValue
     */
    public HearingListComplexValue getHearingListComplexValue(HearingList local) {
        String methodName = "getHearingListComplexValue() - ";
        log.debug(methodName + "called");

        HearingListComplexValue value = (HearingListComplexValue) createComplexVO(local);

        return value;
    }

    /**
     * Create entity from VO.
     * 
     * @param value
     * @return HearingListBasicValue VO.
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        String methodName = "create() - ";
        log.debug(methodName + "called");

        if (!(value instanceof HearingListBasicValue)) {
            if (log.isDebugEnabled())
                log.debug(methodName + "Unexpected type:" + value.getClass());
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        } else {
            try {
                HearingListBasicValue hbv = (HearingListBasicValue) value;

                if (log.isDebugEnabled()) {
                    log.debug(methodName + "Passed in parameters ..." + " listType: " + hbv.getListType()
                            + " StartDate: " + hbv.getStartDate() + " EndDate: " + checkForNullDate(hbv.getEndDate())
                            + " status: " + hbv.getStatus() + " editionNo: " + hbv.getEditionNo() + " publishedTime: "
                            + checkForNullDate(hbv.getPublishedTime()) + " printReference: " + hbv.getPrintReference()
                            + " crestListId: " + hbv.getCrestListId() +
                            // " dailyListXMLId: " + hbv.getDailyListXMLId()
                            // +
                            " gcourtId: " + hbv.getCourtId() + " listCourtType: " + hbv.getListCourtType());
                }

                HearingList hearingList = home.create(hbv.getListType(), checkForNullDate(hbv.getStartDate()),
                        checkForNullDate(hbv.getEndDate()), hbv.getStatus(), hbv.getEditionNo(), checkForNullDate(hbv
                                .getPublishedTime()), hbv.getPrintReference(), hbv.getCrestListId(),
                        // hbv.getDailyListXMLId(),
                        hbv.getCourtId(), hbv.getListCourtType(), userDisplayName);

                return hearingList;
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
     *            HearingListBasicValue VO.
     */
    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof HearingListBasicValue))
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());

        HearingListBasicValue hbv = (HearingListBasicValue) value;

        try {
            // Find home
            log.debug(methodName + "finding by primary key: " + hbv.getId());
            HearingList hearingList = home.findByPrimaryKey(hbv.getId());
            log.debug(methodName + "VO: " + hbv.getVersion() + " Entity: " + hearingList.getVersion());
            if (!hbv.getVersion().equals(hearingList.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                log.debug(methodName + "updating");
                hearingList.setCourtId(hbv.getCourtId());
                hearingList.setCrestListId(hbv.getCrestListId());
                // hearingList.setDailyListXmlId( hbv.getDailyListXMLId());
                hearingList.setEditionNo(hbv.getEditionNo());
                log.debug(methodName + "End Date: " + hbv.getEndDate());
                hearingList.setEndDate(checkForNullDate(hbv.getEndDate()));
                hearingList.setListCourtType(hbv.getListCourtType());
                hearingList.setListType(hbv.getListType());
                hearingList.setPrintReference(hbv.getPrintReference());
                hearingList.setPublishedTime(checkForNullDate(hbv.getPublishedTime()));
                hearingList.setStartDate(checkForNullDate(hbv.getStartDate()));
                hearingList.setStatus(hbv.getStatus());
                hearingList.setUpdated(userDisplayName);
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
            HearingList hearingList = home.findByPrimaryKey(id);
            if (!hearingList.getVersion().equals(version)) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                hearingList.remove();
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
     * @param listId
     * @return HearingList entity
     */
    public HearingList findByPrimaryKey(Integer listId) throws ObjectNotFoundException {
        String methodName = "findByPrimaryKey() - ";
        log.debug(methodName + "called :: listId: " + listId);

        try {
            return home.findByPrimaryKey(listId);
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            if (f instanceof ObjectNotFoundException)
                throw (ObjectNotFoundException) f;
            throw new EJBException(f);
        }
    }

    /**
     * 
     * @param courtId
     * @param date
     * @return HearingList entity
     */
    public HearingList findByCourtIdAndDate(Integer courtId, Date date) throws ObjectNotFoundException {
        String methodName = "findByCourtIdAndDate() - ";
        log.debug(methodName + "called :: courtId: " + courtId + " date: " + date);

        try {
            Timestamp ts = new Timestamp(date.getTime());
            log.debug(methodName + "Using timestamp: " + ts);
            HearingList hearingList = home.findByCourtIdAndDate(courtId, ts);
            log.debug(methodName + "Returned hearingList");
            return hearingList;
        } catch (ObjectNotFoundException f) {
            throw f;
        } catch (FinderException f) {
            throw new EJBException(f);
        }
    }

    /**
     * 
     * @param courtId
     * @param date
     * @param listType
     * @return HearingList entity
     */
    public HearingList findByCourtIdDateAndListType(Integer courtId, Date date, String listType)
            throws ObjectNotFoundException {
        String methodName = "findByCourtIdDateAndType() - ";
        log.debug(methodName + "called :: courtId: " + courtId + " date: " + date + " listType: " + listType);

        try {
            Timestamp ts = new Timestamp(date.getTime());
            HearingList hearingList = home.findByCourtIdDateAndListType(courtId, ts, listType);
            return hearingList;
        } catch (ObjectNotFoundException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            throw f;
        } catch (FinderException f) {
            CSServices.getDefaultErrorHandler().handleError(f, getClass(), f.toString());
            throw new EJBException(f);
        }
    }

    //
    // -------------------- Private Methods ------------------------
    //

    private HearingListBasicValue createBasicVO(HearingList local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        HearingListBasicValue hlbv = new HearingListBasicValue(local.getListId(), local.getVersion());

        copyEntityPropsToVO(local, hlbv);
        return hlbv;
    }

    private HearingListComplexValue createComplexVO(HearingList local) {
        String methodName = "createComplexVO() - ";
        log.debug(methodName + "called");

        HearingListComplexValue hlcv = new HearingListComplexValue(local.getListId(), local.getVersion());

        copyEntityPropsToVO(local, hlcv);
        return hlcv;
    }

    private void copyEntityPropsToVO(HearingList local, HearingListBasicValue sittingBasicValue) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        // PK...
        // sittingBasicValue.setListId(local.getListId());

        sittingBasicValue.setCourtId(local.getCourtId());
        sittingBasicValue.setCrestListId(local.getCrestListId());
        // sittingBasicValue.setDailyListXMLId(local.getDailyListXmlId());
        sittingBasicValue.setEditionNo(local.getEditionNo());
        sittingBasicValue.setEndDate(local.getEndDate());
        sittingBasicValue.setListCourtType(local.getListCourtType());
        sittingBasicValue.setListType(local.getListType());
        sittingBasicValue.setPrintReference(local.getPrintReference());
        sittingBasicValue.setPublishedTime(local.getPublishedTime());
        sittingBasicValue.setStartDate(local.getStartDate());
        sittingBasicValue.setStatus(local.getStatus());
    }

    private Timestamp checkForNullDate(Date date) {
        if (date != null)
            return new Timestamp(date.getTime());
        else
            return null;
    }
}