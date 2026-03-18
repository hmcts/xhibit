package uk.gov.courtservice.xhibit.client.results;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.directions.Directions;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaController;
import uk.gov.courtservice.xhibit.client.util.SelectAllPropertyListener;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SelectAllFunction;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Pleas and Directions Controller
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PleasAndDirectionsController.java,v 1.16 2004/10/26 17:12:42
 *          sz0t7n Exp $
 */

public class PleasAndDirectionsController extends XPanel implements SaveFunction, SelectAllFunction {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JTabbedPane jTabPlea = null;

    private PleaController pc = null;

    private Directions dir = null;

    private XhibitApplicationController xac;

    private boolean pleaModified = false;

    private boolean directionModified = false;

    private Component previousComponent = null;

    public PleasAndDirectionsController(XhibitApplicationController parent) throws CSRecoverableException {
        xac = parent;
        jbInit();
    }

    /**
     * Non-synchronised save
     * 
     * @throws CSRecoverableException
     */
    public void save() throws CSRecoverableException {
        try {
            try {
                if (pc != null && pleaModified)
                    pc.save();
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
            try {
                if (dir != null && directionModified)
                    dir.save();
            } catch (CSRecoverableException ex) {
                XHIBITConstant.handleError(ex);
            }
            // savePreSynchAction();
            // saveSynchAction();
            // savePostSynchAction();
            // } catch (CSRecoverableException e) {
            // throw e;
        } catch (CSUnrecoverableException e) {
            throw e;
        } catch (Exception ex) {
            throw new CSUnrecoverableException(ex);
        }
    }

    /**
     * As this delegates the saves it creates two new sync actions to handle it
     * 
     * @throws Exception
     */
    public void savePreSynchAction() throws Exception {
        ActionEvent ae = new ActionEvent(this, 0, "SAVE");
        if (pc != null && pleaModified) {
            (new SurrogateSaveAction(pc)).actionPerformed(ae);
        }
        if (dir != null && directionModified) {
            (new SurrogateSaveAction(dir)).actionPerformed(ae);
        }
    }

    public void saveSynchAction() throws Exception {
    }

    public void savePostSynchAction() throws Exception {
    }

    class SurrogateSaveAction extends SynchXAction {
        private SaveFunction _surrogatePanel;

        public SurrogateSaveAction(SaveFunction surrogatePanel) {
            _surrogatePanel = surrogatePanel;
        }

        /**
         * Called on the current thread before synchActionPerformed is called
         */
        public void preSynchActionPerformed(ActionEvent e) throws Exception {
            _surrogatePanel.savePreSynchAction();
        }

        /**
         * Called on the sync thread (DO NOT UPDATE GUI IN THIS CALL)
         */
        public void synchActionPerformed(ActionEvent e) throws Exception {
            _surrogatePanel.saveSynchAction();
        }

        /**
         * Called on the awt thread after synchActionPerformed is called
         */
        public void postSynchActionPerformed(ActionEvent e) throws Exception {
            _surrogatePanel.savePostSynchAction();
        }
    }

    public void selectAll() {
        Component c = getTabPane().getSelectedComponent();
        if (c instanceof PleaController) {
            ((PleaController) c).selectAll();
        }
    }

    private void jbInit() throws CSRecoverableException {
        boolean editDirections = FunctionList.hasAccess(FunctionList.ECourtLog);
        boolean editPleas = FunctionList.hasAccess(FunctionList.EPlea);

        this.setLayout(gridBagLayout1);
        if (editDirections) {
            dir = new Directions(xac);
            dir.addPropertyChangeListener(XPanel.property_modified, new DirectionModifyListener());
            // } else {
            // dir = new JPanel();
        }

        if (editPleas) {
            pc = new PleaController(xac.getApplicationCaseModel());
            pc.addPropertyChangeListener(XPanel.property_modified, new PleaModifyListener());
            pc.addPropertyChangeListener(XPanel.property_selectAll, new SelectAllPropertyListener(xac));
            // } else {
            // pc = new JPanel();
        }

        getTabPane().addTab(XHIBITConstant.getResource(XhibitBundles.Directions, "PleaTab"),
                pc == null ? new JPanel() : pc);
        getTabPane().addTab(XHIBITConstant.getResource(XhibitBundles.Directions, "DirectionTab"),
                dir == null ? new JPanel() : dir);
        if (editPleas) {
            getTabPane().setSelectedIndex(0);
        } else {
            getTabPane().setEnabledAt(0, false);
        }

        if (editDirections) {
            if (!editPleas)
                getTabPane().setSelectedIndex(1);
        } else {
            getTabPane().setEnabledAt(1, false);
        }

        this.add(getTabPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    public PleaController getPleaController() {
        return pc;
    }

    public Directions getDirections() {
        return dir;
    }

    private JTabbedPane getTabPane() {
        if (jTabPlea == null) {
            jTabPlea = new JTabbedPane();
            jTabPlea.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent ce) {

                    Component c = jTabPlea.getSelectedComponent();
                    if (c instanceof XPanel) {
                        try {
                            uk.gov.courtservice.xhibit.client.util.XHIBITConstant
                                    .debug("[PleasAndDirectionsController] getTabPane() c.isDisplayable() = "
                                            + c.isDisplayable());
                            if (c.isDisplayable()) {
                                ((XPanel) jTabPlea.getSelectedComponent()).stepActivate();
                            }
                        } catch (CSRecoverableException ex) {
                            XHIBITConstant.handleError(ex);
                        }
                    }
                    if (previousComponent instanceof PleaController) {
                        try {
                            ((PleaController) previousComponent).stepDeactivate();
                        } catch (CSRecoverableException csre) {
                            XHIBITConstant.handleError(csre);
                        }
                    } else {
                        previousComponent = c;
                    }
                }
            });
        }
        return jTabPlea;
    }

    class PleaModifyListener implements java.beans.PropertyChangeListener, java.io.Serializable {
        public PleaModifyListener() {
        }

        public void propertyChange(java.beans.PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                pleaModified = true;
                modified();
            } else {
                pleaModified = false;
                if (!pleaModified && !directionModified)
                    setModified(false);
            }
        }
    }

    class DirectionModifyListener implements java.beans.PropertyChangeListener, java.io.Serializable {
        public DirectionModifyListener() {
        }

        public void propertyChange(java.beans.PropertyChangeEvent ev) {
            if (((Boolean) ev.getNewValue()).booleanValue()) {
                directionModified = true;
                modified();
            } else {
                directionModified = false;
                if (!pleaModified && !directionModified)
                    setModified(false);
            }
        }
    }

    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
    }

    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

    public void stepDeinitialise(boolean update) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        if (getModified()) {
            int rc = JOptionPane.showConfirmDialog(xac, XHIBITConstant.getResource(XhibitBundles.Directions,
                    "SaveMessage"), XHIBITConstant.getResource(XhibitBundles.Directions, "SaveTitle"),
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (rc == JOptionPane.YES_OPTION) {
                save();
            } else if (rc == JOptionPane.NO_OPTION) {
                if (pc != null)
                    pc.setModified(false);
                if (dir != null)
                    dir.setModified(false);
            } else {
                throw new UserCancelException();
            }
        }
    }

    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
    }

}