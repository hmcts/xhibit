package uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSession;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonSessionHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitness;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessHelper;
import uk.gov.courtservice.xhibit.business.entities.witness.XhbWitnessValue;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessModificationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessDetailImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessSessionImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: .
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author qzd3k3
 * 
 */
public class WitnessHelper {
    private static final Logger log = CSServices.getLogger(WitnessHelper.class);

    public static WitnessDetail getWitnessDetail(final Integer witnessId) throws WitnessNotFoundException {
        try {
            return new WitnessDetailImpl(XhbWitnessHelper.findByPrimaryKey(witnessId));
        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new WitnessNotFoundException("WITNESS_XXX", "Could not find witness: " + witnessId, e);
        }
    }

    public static WitnessDetail updateWitnessDetail(final WitnessDetail detail) throws WitnessNotFoundException {
        try {
            XhbWitnessValue witnessValueBefore = ((WitnessDetailImpl) detail).getWitnessValue();
            log.debug("Before: " + witnessValueBefore);
            XhbWitnessValue witnessValueAfter = XhbWitnessHelper.update(witnessValueBefore);
            log.debug("After: " + witnessValueAfter);
            return new WitnessDetailImpl(witnessValueAfter);
        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new WitnessNotFoundException("WITNESS_XXX", "Could not update: " + detail, e);
        }

    }

    public WitnessSession updateWitnessSession(WitnessSession witnessSession) throws WitnessModificationException {
        XhbWitnessValue witnessValue = ((WitnessSessionImpl) witnessSession).getWitnessValue();
        Integer skeletonSessionId = witnessValue.getXhbSkeletonSession().getSkeletonSessionId();

        XhbWitness witness = null;
        XhbSkeletonSession session = null;
        try {
            witness = XhbWitnessHelper.getLocalHome().findByPrimaryKey(witnessSession.getId());
            session = XhbSkeletonSessionHelper.getLocalHome().findByPrimaryKey(skeletonSessionId);
        } catch (javax.ejb.FinderException e) {
            log.error(e);
            throw new WitnessModificationException("WITNESS_XXX", "Could not locate witness or session during update.",
                    e);
        }

        witness.setData(witnessValue);
        witness.setXhbSkeletonSession(session);
        return new WitnessSessionImpl(witness.getData(true));
        // XhbWitnessValue witnessValueBefore = ((WitnessDetailImpl)
        // witnessSession).getWitnessValue();
        // log.debug("updateWitnessSession Before: " + witnessValueBefore);
        // XhbWitnessValue witnessValueAfter = null;
        // XhbSkeletonSessionValue skeletonSessionAfter = null;
        // try
        // {
        // witnessValueAfter = XhbWitnessHelper.update(witnessValueBefore);
        // XhbSkeletonSessionValue skeletonSessionBefore =
        // witnessValueBefore.getXhbSkeletonSession();
        //
        // if (skeletonSessionBefore != null)
        // {
        // skeletonSessionAfter=
        // XhbSkeletonSessionHelper.update(skeletonSessionBefore);
        // }
        // else
        // {
        // throw new SkeletonSessionNotFoundException("WITNESS_XXX", "No
        // skeleton session for this witness.");
        // }
        // log.debug("updateWitnessSession After: " + witnessValueAfter);
        // }
        // catch (javax.ejb.FinderException e)
        // {
        // log.error(e);
        // throw new WitnessModificationException("WITNESS_XXX", "Could not
        // update: " + witnessSession, e);
        // }
        //
        // witnessValueAfter.setXhbSkeletonSession(skeletonSessionAfter);
        // return new WitnessSessionImpl(witnessValueAfter);

    }

}
