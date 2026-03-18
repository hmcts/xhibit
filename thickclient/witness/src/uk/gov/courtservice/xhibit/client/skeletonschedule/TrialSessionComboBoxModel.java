package uk.gov.courtservice.xhibit.client.skeletonschedule;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.TrialSessionImpl;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.client.skeletonschedule.combo.ArrayComboBoxModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Combo Box Model That stores trial sessions
 * </p>
 * <p>
 * Description: A model for trial sessions
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */
public class TrialSessionComboBoxModel extends ArrayComboBoxModel {
    private static final Logger log = CSServices.getLogger(ArrayComboBoxModel.class);

    public TrialSessionComboBoxModel(TrialSession[] newData) {
        super(createRenderers(newData));
        log.debug("ArrayComboBoxModel(" + newData + ")");
    }

    public TrialSessionComboBoxModel(TrialSession[] newData, TrialSession selectedItem) {
        super(createRenderers(newData), new TrialSessionRenderer(selectedItem));
        log.debug("ArrayComboBoxModel(" + newData + ", " + selectedItem + ")");
    }

    public TrialSession getSelectedTrialSession() {
        Object renderer = getSelectedItem();
        if (renderer == null) {
            return null;
        } else {
            return ((TrialSessionRenderer) renderer).trialSession;
        }
    }

    public void setSelectedItem(TrialSession item) {
        // log.debug("$$$ TrialSessionComboBoxModel.setSelectedItem(" + item +
        // ") $$$");
        super.setSelectedItem(new TrialSessionRenderer(item));
    }

    /*
     * Trial Session Renderer
     */

    private static final TrialSessionRenderer[] createRenderers(TrialSession[] sessions) {
        TrialSessionRenderer[] renderers = new TrialSessionRenderer[sessions.length];
        for (int i = 0; i < renderers.length; i++) {
            renderers[i] = new TrialSessionRenderer(sessions[i]);
        }
        return renderers;
    }

    private static final class TrialSessionRenderer {
        public final TrialSession trialSession;

        public TrialSessionRenderer(TrialSession newTrialSession) {
            trialSession = newTrialSession;
        }

        /**
         * The setSelectedItem method in ArrayComboBoxModel has to allow for
         * TrialSessionImpl and TrialSessionRenderer, otherwise the
         * corresponding equals methods to not set the correct item. This seems
         * over complex for what we are trying to do, so could be investigated
         * at a later date.
         * 
         * @param other
         *            Object to compare
         * @return true if matches, otherwise false
         */
        public boolean equals(Object other) {
            if (other instanceof TrialSessionImpl) {
                return other instanceof TrialSessionImpl && equals((TrialSessionImpl) other);
            } else if (other instanceof TrialSessionRenderer) {
                return other instanceof TrialSessionRenderer && equals((TrialSessionRenderer) other);
            } else {
                return false;
            }
        }

        /**
         * Compares a TrialSessionRenderer to this
         * 
         * @param other
         *            TrialSessionRenderer to compare
         * @return true if a match is found, otherwise false
         */
        public boolean equals(TrialSessionRenderer other) {
            return other != null && other.trialSession.getDayNumber() == (this.trialSession.getDayNumber())
                    && other.trialSession.getSessionType().equals(this.trialSession.getSessionType());
        }

        /**
         * Compares a TrialSessionImpl to this
         * 
         * @param other
         *            TrialSessionImpl to compare
         * @return true if a match is found, otherwise false.
         */
        public boolean equals(TrialSessionImpl other) {
            return other != null && other.getDayNumber() == (this.trialSession.getDayNumber())
                    && other.getSessionType().equals(this.trialSession.getSessionType());
        }

        public String toString() {
            return trialSession.getDayNumber()
                    + " - "
                    + XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, "skeletonschedule.session."
                            + trialSession.getSessionType());
        }
    }

}
