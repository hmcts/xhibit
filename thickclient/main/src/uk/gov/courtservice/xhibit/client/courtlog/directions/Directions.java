package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: The full directions panel to be used in the PDH screen
 * </p>
 * <p>
 * Description: Allows entry of directions for both case and defendants on the
 * case The screen also generates court log events for the directions
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.23 $
 */
public class Directions extends DirectionsParentPanel implements SaveFunction {
    private static final Logger log = CSServices.getLogger(Directions.class);

    private final XhibitApplicationController xac;

    private DirectionDefendantSelector defDirection;

    private DirectionsForCase caseDirection;

    private TitledBorder defBorder;

    private Border border1;

    private TitledBorder caseBorder;

    private Border border2;

    public Directions(XhibitApplicationController xac) throws CSRecoverableException {
        this.xac = xac;

        stepInitialise();
        init();
        stepActivate();
    }

    DirectionsValue dv;

    /**
     * Retrieves directions for defendant and case from child panels and creates
     * a DirectionsValue object which is then passed in as a parameter into a
     * saveDirections method call on teh delegate
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

    public void savePreSynchAction() throws Exception {
        log.debug("SAVE");
        stepValidate();
        stepDeactivate();
        dv = new DirectionsValue();
        if (defDirection.getModified()) {
            log.debug(": DEF MODIFIED");
            dv.setDirectionsForDefendantValue(defDirection.getModels());

        }

        if (caseDirection.getModified()) {
            log.debug(": CASE MODIFIED");
            dv.setDirectionsForCaseValue(caseDirection.getModel());
        }
    }

    public void saveSynchAction() throws Exception {
        XhibitDelegateHelper.getDirectionsDelegate().saveDirections(dv);
    }

    public void savePostSynchAction() throws Exception {
        resetModified();
        clearAndReloadPanels();
    }

    private void clearAndReloadPanels() throws CSRecoverableException {
        // The following line will clean up listeners
        // Uncomment if there is a memory leak identified in this refresh.
        // Everything should be cleaned up as the known problems are with
        // JPopups and MouseListeners, and Directions doesn't have them.
        // SwingUtilities.invokeLater(new RemoveListeners(this));
        this.removeAll();
        stepInitialise();
        init();
        stepActivate();
        // The following added as on some machines the screen does
        // not repaint event though the contents have changed.
        revalidate();
    }

    /*
     * class RemoveListeners implements Runnable { Component component; public
     * RemoveListeners(Component component) { this.component = component; }
     * public void run() { long start = System.currentTimeMillis(); //
     * System.err.println("START REMOVING LISTENERS::: " + (new
     * Date(start)).toGMTString()); recurseRemoveListener(component);
     * 
     * long end = System.currentTimeMillis(); // System.err.println("END
     * REMOVING LISTENERS::: " + (new Date(end)).toGMTString()); //
     * System.err.println(" :::TIME TAKEN ::: " + (end-start) + "
     * milliseconds"); }
     * 
     * private void recurseRemoveListener(Component component) { //
     * System.err.println("REMOVING LISTENERS ON ::: " +
     * component.getClass().getName()); if (component instanceof Container) {
     * Component[] components = ((Container)component).getComponents(); for (int
     * i = 0; i < components.length; i++) { removeListeners(component);
     * recurseRemoveListener(components[i]); } } }
     * 
     * private void removeListeners(Component component) { Method[] methods =
     * component.getClass().getMethods(); for (int i = 0; i < methods.length;
     * i++) { String name = methods[i].getName(); if (name.startsWith("remove") &&
     * name.endsWith("Listener")) { Class[] parameters =
     * methods[i].getParameterTypes(); // check to exclude
     * removePropertyChangeListener(String, PropertyChangeListener) if
     * (parameters.length == 1) { EventListener[] listeners =
     * component.getListeners(parameters[0]); for (int j = 0; j <
     * listeners.length; j++) { try { methods[i].invoke(component, new Object[] {
     * listeners[j]}); } catch (Throwable t) { log.error("Error invoking: " +
     * methods[i], t); } } } } } } }
     */

    /**
     * Returns DirectionsForCase Panel
     * 
     * @return DirectionsForCase
     */
    public DirectionsForCase getCaseDirections() {
        return caseDirection;
    }

    private void init() throws CSRecoverableException {
        DirectionsForCaseValue defCaseVO = new DirectionsForCaseValue();
        XhbDirectionsForCaseBasicValue bv = new XhbDirectionsForCaseBasicValue();
        defCaseVO.setDirectionsForCaseBasicValue(bv);
        Integer shId = null;
        if (xac != null) {
            bv.setCaseId(xac.getApplicationCaseModel().getCaseId());
            shId = xac.getApplicationCaseModel().getScheduledHearingId();
        }

        PropertyChangeListener pcl = new ModifyPropertyListener();

        caseDirection = new DirectionsForCase(defCaseVO, shId);
        caseDirection.addPropertyChangeListener(XPanel.property_modified, pcl);
        caseDirection.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions,
                "DirectionsForCase")));

        defDirection = new DirectionDefendantSelector(xac, caseDirection.getCourtLogAuditPanel());
        defDirection.addPropertyChangeListener(XPanel.property_modified, pcl);
        defDirection.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions,
                "DirectionsByDefendant")));

        this.setLayout(new GridBagLayout());
        this.add(PDHConstants.getScroll(defDirection), new GridBagConstraints(0, 0, 1, 1, 0.61, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(PDHConstants.getScroll(caseDirection), new GridBagConstraints(1, 0, 1, 1, 0.39, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    /**
     * Call restModified on child panels and set modifid boolean to false
     */
    public void resetModified() {
        defDirection.resetModified();
        caseDirection.resetModified();
        setModified(false);
    }

    private class ModifyPropertyListener implements PropertyChangeListener, Serializable {
        public void propertyChange(PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                modified();
            }
        }
    }
}