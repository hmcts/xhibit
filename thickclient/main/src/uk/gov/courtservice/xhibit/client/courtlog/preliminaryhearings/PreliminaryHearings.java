package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DirectionsParentPanel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.util.CourtLogAuditPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: PreliminaryHearings
 * </p>
 * <p>
 * Description: Allows entry of prelminary hearing court log events for both
 * case and defendants on the case
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version $Revision: 1.8 $
 */
public class PreliminaryHearings extends DirectionsParentPanel implements SaveFunction {
    private static final Logger log = CSServices.getLogger(PreliminaryHearings.class);

    private final XhibitApplicationController xac;

    private PreliminaryHearingDefendantSelector defSelector;

    private CaseLevelEventsPanel caseLevelEventsPanel;

    private CourtLogAuditPanel clap;

    private TitledBorder defBorder;

    private Border border1;

    private TitledBorder caseBorder;

    private Border border2;

    public PreliminaryHearings(XhibitApplicationController xac) throws CSRecoverableException {
        this.xac = xac;

        stepInitialise();
        init();
        stepActivate();
    }

    PreliminaryHearingsValue phv;

    /**
     * Performs save processing and is called when the user selects the "save
     * "function.
     * 
     * @throws CSRecoverableException
     */
    public void save() throws CSRecoverableException {
        try {
            savePreSynchAction();
            saveSynchAction();
            savePostSynchAction();
        } catch (CSRecoverableException e) {
            throw e;
        } catch (CSUnrecoverableException e) {
            throw e;
        } catch (Exception ex) {
            throw new CSUnrecoverableException(ex);
        }
    }

    /**
     * Invokes the stepValidate on component panels followed by the
     * stepDeactivate and saves the models in the PreliminaryHearingsValue to be
     * used later
     * 
     * @throws Exception
     */
    public void savePreSynchAction() throws Exception {
        log.debug("SAVE");
        stepValidate();
        stepDeactivate();
        phv = new PreliminaryHearingsValue();
        if (defSelector.getModified()) {
            log.debug(": DEF MODIFIED");
            phv.setDirectionsForDefendantValue(defSelector.getModels());
        }

        if (caseLevelEventsPanel.getModified()) {
            log.debug(": CASE MODIFIED");
            phv.setCaseLevelEventsModel(caseLevelEventsPanel.getModel());
        }
    }

    /**
     * If there are any court log CRUDs created as a consequence of using the
     * component panels, the appropriate delegate is called to save them
     * 
     * @throws Exception
     */
    public void saveSynchAction() throws Exception {
        ArrayList courtLogCRUDValues = new ArrayList();

        // Get any case level court log events
        if (phv.getCaseLevelEventsModel() != null && phv.getCaseLevelEventsModel().getCourtLogCRUDValues().length > 0) {
            courtLogCRUDValues.addAll(getArrayAsList(phv.getCaseLevelEventsModel().getCourtLogCRUDValues()));
        }

        // Get any defendant level court log events
        if (phv.getDirectionsForDefendantValue() != null) {
            Collection defendantValues = phv.getDirectionsForDefendantValue();
            Iterator iter = defendantValues.iterator();
            while (iter.hasNext()) {
                DirectionsForDefendantValue temp = (DirectionsForDefendantValue) iter.next();
                courtLogCRUDValues.addAll(getArrayAsList(temp.getCourtLogCRUDValues()));
            }
        }

        // Sort the events so that they appear in the court log in a specific
        // order
        PHEventHelper pheh = new PHEventHelper();
        pheh.orderCourtLogEvents(courtLogCRUDValues);

        // Write the court log events in one transaction
        CourtLogCRUDValue[] crudArray = new CourtLogCRUDValue[courtLogCRUDValues.size()];
        for (int x = 0; x < courtLogCRUDValues.size(); x++) {
            crudArray[x] = (CourtLogCRUDValue) courtLogCRUDValues.get(x);
        }
        XhibitDelegateHelper.getCourtLogDelegate2().newEntries(crudArray);
    }

    private List getArrayAsList(CourtLogCRUDValue[] array) {
        return Arrays.asList(array);
    }

    /**
     * Re-initializes the component panels
     * 
     * @throws Exception
     */
    public void savePostSynchAction() throws Exception {
        resetModified();
        clearAndReloadPanels();
    }

    /**
     * Clean up listeners and revalidate the screen in order to repaint it
     * 
     * @throws CSRecoverableException
     */
    private void clearAndReloadPanels() throws CSRecoverableException {
        this.removeAll();
        stepInitialise();
        init();
        stepActivate();
        revalidate();
    }

    /**
     * Returns DirectionsForCase Panel
     * 
     * @return DirectionsForCase
     */
    public CaseLevelEventsPanel getCaseLevelEventsPanel() {
        return caseLevelEventsPanel;
    }

    /**
     * Pleaces the widgets on the screen
     * 
     * @throws CSRecoverableException
     */
    private void init() throws CSRecoverableException {
        CaseLevelEventsModel clem = new CaseLevelEventsModel();
        CaseLevelEventsValue clev = new CaseLevelEventsValue();
        clem.setXac(xac);
        clem.setCaseLevelEventsValue(clev);
        if (xac != null) {
            clev.setCaseId(xac.getApplicationCaseModel().getCaseId());
            clev.setScheduledHearingId(xac.getApplicationCaseModel().getScheduledHearingId());
        }

        PropertyChangeListener pcl = new ModifyPropertyListener();

        caseLevelEventsPanel = new CaseLevelEventsPanel(clem);
        caseLevelEventsPanel.addPropertyChangeListener(XPanel.property_modified, pcl);
        caseLevelEventsPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(
                XhibitBundles.PreliminaryHearing, "lblCaseLevelEvents")));

        defSelector = new PreliminaryHearingDefendantSelector(xac, caseLevelEventsPanel.getCourtLogAuditPanel());
        defSelector.addPropertyChangeListener(XPanel.property_modified, pcl);
        defSelector.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(
                XhibitBundles.PreliminaryHearing, "lblDefendantLevelEvents")));

        this.setLayout(new GridBagLayout());
        this.add(caseLevelEventsPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.10, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(PDHConstants.getScroll(defSelector), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.90,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(caseLevelEventsPanel.getCourtLogAuditPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    /**
     * Call restModified on child panels and set modifid boolean to false
     */
    public void resetModified() {
        defSelector.resetModified();
        caseLevelEventsPanel.resetModified();
        setModified(false);
    }

    private class ModifyPropertyListener implements PropertyChangeListener, Serializable {
        public void propertyChange(PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                modified();
            }
        }
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (getModified()) {
            int rc = JOptionPane.showConfirmDialog(xac, XHIBITConstant.getResource(XhibitBundles.PreliminaryHearing,
                    "lblSaveMessage"), XHIBITConstant.getResource(XhibitBundles.PreliminaryHearing, "lblSaveTitle"),
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (rc == JOptionPane.YES_OPTION) {
                save();
            } else if (rc == JOptionPane.NO_OPTION) {
                setModified(false);
            } else {
                throw new UserCancelException();
            }
        }
    }
}
