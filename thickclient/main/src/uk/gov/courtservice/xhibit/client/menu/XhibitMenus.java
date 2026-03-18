package uk.gov.courtservice.xhibit.client.menu;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.common.AboutAction;
import uk.gov.courtservice.xhibit.client.actions.common.CopyAction;
import uk.gov.courtservice.xhibit.client.actions.common.CutAction;
import uk.gov.courtservice.xhibit.client.actions.common.ExitAction;
import uk.gov.courtservice.xhibit.client.actions.common.HelpAction;
import uk.gov.courtservice.xhibit.client.actions.common.NewAction;
import uk.gov.courtservice.xhibit.client.actions.common.PasteAction;
import uk.gov.courtservice.xhibit.client.actions.menu.OpenRecentCase;
import uk.gov.courtservice.xhibit.client.actions.menu.RoamingTerminalHotSwitchAction;
import uk.gov.courtservice.xhibit.client.listeners.menu.MenuItemMouseListener;
import uk.gov.courtservice.xhibit.client.models.RecentCase;
import uk.gov.courtservice.xhibit.client.util.BlankIcon;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XToolBarHelper;
import uk.gov.courtservice.xhibit.client.util.XToolbarButton;
import uk.gov.courtservice.xhibit.client.util.XToolbarToggleButton;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.110 $
 * @history James Powell 13/03/2009 - Added line to add UnauthorisedCaseStatusAction
 *          to Adminsitration Menu
 */
public class XhibitMenus {
    public static final String tbGeneral = "General";

    public static final String tbCharge = "Charge";

    public static final String tbCourtLog = "Court Log";

    public static final String tbResults = "Results";

    public static final String tbGeneralId = "General";

    public static final String tbChargeId = "Charge";

    public static final String tbCourtLogId = "CourtLog";

    public static final String tbResultsId = "Results";

    private final Logger log = CSServices.getLogger(getClass());

    // only to be set in the constructor...
    protected final XhibitApplicationController xac;

    private final String myMenuResources = XhibitBundles.Menu;

    private Hashtable toolbarHash = new Hashtable(5);

    private ActionMap courtLogActions;

    private Icon blankIcon = new BlankIcon();

    protected int defaultButtonFormat = XToolbarButton.xbIcon;

    protected int courtLogButtonFormat = defaultButtonFormat; // XToolbarButton.xbText;

    protected int defaultOrientation = SwingConstants.HORIZONTAL;

    protected int courtLogOrientation = defaultOrientation;

    private MenuItemMouseListener menuItemMouseListener = null;

    private RecentCaseListener recentCasePropertyChangeListener = null;

    public XhibitMenus(XhibitApplicationController xac) {
        this.xac = xac;
    }

    public void buildMenus() {
        try {
            xac.setJMenuBar(buildMenuToolbar());
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public Hashtable getToolBarHash() {
        return toolbarHash;
    }

    public ActionMap getCourtLogActionMap() {
        return courtLogActions;
    }

    protected XToolbarButton getToolButton(Action a, int buttonFormat) {
        XToolbarButton tb1 = new XToolbarButton(buttonFormat);
        tb1.setAction(a);
        return tb1;
    }

    protected JMenuItem getMenuItem(Action a) {
     
        JMenuItem mi = new JMenuItem();
        mi.setAction(a);

        // If the action has no icon, set the menu icon to the blank icon so
        // that the menu text lines up.
        if (a.getValue(Action.SMALL_ICON) == null) {
            mi.setIcon(blankIcon);
        }

        if (a.getValue(Action.ACCELERATOR_KEY) != null) {
            mi.setAccelerator((KeyStroke) a.getValue(Action.ACCELERATOR_KEY));
        }
        mi.addMenuDragMouseListener(getMenuItemMouseListener());
        mi.addMouseListener(getMenuItemMouseListener());
        return mi;
    }

    private MenuItemMouseListener getMenuItemMouseListener() {
        if (menuItemMouseListener == null) {
            menuItemMouseListener = new MenuItemMouseListener(xac);
        }
        return menuItemMouseListener;
    }

    private JMenuBar buildMenuToolbar() {
        XAction activateDisplayAction = XhibitActions.getAction(xac, XhibitActions.ActivatePublicDisplay);
        JCheckBoxMenuItem activateDisplayMenu = new JCheckBoxMenuItem();
        XToolbarToggleButton activateDisplayTb = new XToolbarToggleButton(XToolbarButton.xbIcon + XToolbarButton.xbText);

        JToolBar jtGen = getToolBar(tbGeneral, tbGeneralId);

        JMenuBar jmb = new JMenuBar();
        JMenu tempMenu;

        tempMenu = getFileMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        tempMenu = getEditMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        tempMenu = getCaseMenu();
        
        // if (tempMenu.getItemCount() > 0) {
        // Add the activate display toggle menu
        if (activateDisplayAction.hasReadAccess()) {
            if (tempMenu.getItemCount() > 0)
                tempMenu.addSeparator();
            activateDisplayMenu.setAction(activateDisplayAction);
            tempMenu.add(activateDisplayMenu);
        }
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        tempMenu = getCourtLogMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        tempMenu = getChargesMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        tempMenu = getListMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        tempMenu = getAdminMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        // Neil Entwistle - Adding Tools Menu to main menu BEGIN
        tempMenu = getToolsMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);
        // Neil Entwistle - Adding Tools Menu to main menu END

        tempMenu = getViewMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);
        
        tempMenu = getCrownSupportMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        tempMenu = getHelpMenu();
        if (tempMenu.getItemCount() > 0)
            jmb.add(tempMenu);

        // Add the court log edit/delete toolbar items
        // Add these as the last two buttons on the court log toolbar.
        JToolBar jtCL = getToolBar(tbCourtLog, tbCourtLogId);
        jtCL.add(new JToolBar.Separator());
        addToMenu(null, jtCL, XhibitActions.getAction(xac, XhibitActions.EditClEvent));
        addToMenu(null, jtCL, XhibitActions.getAction(xac, XhibitActions.DeleteClEvent));

        // Add public notice to toolbar
        addToMenu(null, jtGen, XhibitActions.getAction(xac, XhibitActions.PublicNotice), XToolbarButton.xbIcon
                + XToolbarButton.xbText, XhibitSingleton.getInstance().isUserInCourtroom());

        // Add activate display to toolbar
        if (activateDisplayAction.hasReadAccess()) {
            if (jtGen.getComponentCount() > 0)
                jtGen.addSeparator();
            activateDisplayTb.setAction(activateDisplayAction);
            jtGen.add(activateDisplayTb);
        }

        // Set listener so if setScreenActive is called on XAC, then the buttons
        // autoupdate
        if (activateDisplayAction.hasReadAccess()) {
            // create listener
            ScreenActivePropertyListener sap = new ScreenActivePropertyListener();
            // add toggle components
            sap.addComponent(activateDisplayMenu);
            sap.addComponent(activateDisplayTb);
            // add listener to xac
            xac.addPropertyChangeListener(XhibitApplicationController.propertyScreenActive, sap);

            // Default state disabled
            activateDisplayAction.setEnabled(false);
        }

        return jmb;
    }

    public JToolBar getToolBar(String toolbarName, String id) {
        if (toolbarHash.containsKey(id)) {
            return (JToolBar) toolbarHash.get(id);
        }

        JToolBar tb = new JToolBar(toolbarName, defaultOrientation);
        tb.setFloatable(false);
        tb.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        // Set id property per toolbar instance, used for ToolBarHelper
        // functionality.
        tb.putClientProperty("id", id);
        toolbarHash.put(id, tb);
        return tb;
    }

    public void addToMenu(JMenu jm, JToolBar jt, XAction a, int buttonFormat, boolean enabled) {
        //90
        if (a.hasReadAccess()) {
        //if(true){
            // Set action disable by default
            a.setEnabled(enabled);
            if (jm != null)
                jm.add(getMenuItem(a));
            if (jt != null)
                jt.add(getToolButton(a, buttonFormat));
        }
    }

    public void addToMenu(JMenu jm, JToolBar jt, XAction a, boolean enabled) {
        this.addToMenu(jm, jt, a, defaultButtonFormat, enabled);
    }

    public void addToMenu(JMenu jm, JToolBar jt, XAction a, int buttonFormat) {
        this.addToMenu(jm, jt, a, buttonFormat, false);
    }

    public void addToMenu(JMenu jm, JToolBar jt, XAction a) {
        this.addToMenu(jm, jt, a, false);
    }

    private JMenu getFileMenu() {
        JMenu tempMenu;
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "FileMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "FileMenuMnemonic").charAt(0));
        JToolBar jt = getToolBar(tbGeneral, tbGeneralId);
        try {
            addToMenu(jm, jt, NewAction.getInstance(), true);

            tempMenu = getFileOpenMenu();
            if (tempMenu.getItemCount() > 0)
                jm.add(tempMenu);

            jm.add(getRecentCaseMenu());

            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.LinkCases));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.UnlinkCase));
            if (jm.getItemCount() > 0)
                jm.addSeparator();

            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.Open), true);
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.Save));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.Close));
            if (jm.getItemCount() > 0)
                jm.addSeparator();
            if (jt.getComponentCount() > 0)
                jt.addSeparator();

            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.Print));
            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.PrintToolbar));
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.PrintPreview));
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.CaseProps));
            if (jm.getItemCount() > 0)
                jm.addSeparator();

            addToMenu(jm, null, RoamingTerminalHotSwitchAction.getInstance(), true);
            addToMenu(jm, null, ExitAction.getInstance(), true);

            if (jt.getComponentCount() > 0)
                jt.addSeparator();

        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getRecentCaseMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "RecentCaseMenu"));
        this.recentCasePropertyChangeListener = new RecentCaseListener(jm);
        XhibitSingleton.getInstance().addPropertyChangeListener(XhibitSingleton.RECENTCASE,
                this.recentCasePropertyChangeListener);
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "RecentCaseMenu").charAt(0));
        jm.setIcon(blankIcon);

        ArrayList recentCases = XhibitSingleton.getInstance().getRecentCaseList();
        Iterator iter = recentCases.iterator();
        while (iter.hasNext()) {
            JMenuItem newCase = new JMenuItem();
            OpenRecentCase xa = new OpenRecentCase((RecentCase) iter.next());
            newCase.setAction(xa);
            jm.add(newCase);
        }

        if (jm.getItemCount() <= 0)
            jm.setEnabled(false);
        return jm;
    }

    private JMenu getFileOpenMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "OpenMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "OpenMenu").charAt(0));
        jm.setIcon(blankIcon);
        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.Open), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ViewTodaysSchedule), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.OpenOtherLog), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.OpenOtherDaysLog));
            if (jm.getItemCount() > 0)
                jm.addSeparator();
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.PreviewDailyList), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.PreviewTomorrowsList), true);

            // Note: Toolbar entries done in file menu
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getEditMenu() {
        JMenu jmEdit = new JMenu(XHIBITConstant.getResource(myMenuResources, "EditMenu"));
        jmEdit.setMnemonic(XHIBITConstant.getResource(myMenuResources, "EditMenuMnemonic").charAt(0));
        JToolBar jtGen = getToolBar(tbGeneral, tbGeneralId);
        // JToolBar jtCL = getToolBar(tbCourtLog,tbCourtLogId);
        try {
            // jmEdit.setMnemonic(KeyEvent.VK_E);
            addToMenu(jmEdit, jtGen, CutAction.getInstance(), true);
            addToMenu(jmEdit, jtGen, CopyAction.getInstance(), true);
            addToMenu(jmEdit, jtGen, PasteAction.getInstance(), true);
            addToMenu(jmEdit, null, XhibitActions.getAction(xac, XhibitActions.EditSelectAll), false);
            if (jmEdit.getItemCount() > 0)
                jmEdit.addSeparator();
            if (jtGen.getComponentCount() > 0)
                jtGen.addSeparator();

            addToMenu(jmEdit, null, XhibitActions.getAction(xac, XhibitActions.EditClEvent));
            addToMenu(jmEdit, null, XhibitActions.getAction(xac, XhibitActions.DeleteClEvent));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jmEdit;
    }

    private JMenu getCaseMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "CaseMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "CaseMenuMnemonic").charAt(0));

        JToolBar jtGen = getToolBar(tbGeneral, tbGeneralId);

        JMenu jmCaseCreate = getCreateCaseMenu();
        if (jmCaseCreate.getItemCount() > 0)
            jm.add(jmCaseCreate);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.MaintainCase), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddIndictmentCase), true);
		addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ReplaceDeleteDeft), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.TransferCase), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DeleteCase), true);        

        addToMenu(jm, jtGen, XhibitActions.getAction(xac, XhibitActions.LinkCases));
        addToMenu(jm, jtGen, XhibitActions.getAction(xac, XhibitActions.PleasAndDirections));
        if (jm.getItemCount() > 0)
            jm.addSeparator();
        addToMenu(jm, jtGen, XhibitActions.getAction(xac, XhibitActions.Plea));
        // Multiple pleas moved to Plea screen - indictment tab
        // addToMenu( jm, jtRes, XhibitActions.getAction(xac,
        // XhibitActions.MultiplePlea));
        // Appeal and Verdict moved to court log menu
        // addToMenu( jm, jtGen, XhibitActions.getAction(xac,
        // XhibitActions.Verdict));
        // addToMenu( jm, jtGen, XhibitActions.getAction(xac,
        // XhibitActions.AppealResult));
        addToMenu(jm, jtGen, XhibitActions.getAction(xac, XhibitActions.Sentence));
        addToMenu(jm, jtGen, XhibitActions.getAction(xac, XhibitActions.EditSkeletonSchedule));

        JMenu jmDisposals = getDisposalsMenu();
        if (jmDisposals.getItemCount() > 0)
            jm.add(jmDisposals);

        JMenu jmOrders = getOrdersMenu();
        if (jmOrders.getItemCount() > 0)
            jm.add(jmOrders);
        
        JMenu jmMonetaryOrders = getMonetaryOrdersMenu();
        if (jmMonetaryOrders.getItemCount() > 0)
            jm.add(jmMonetaryOrders);

        JMenu jmD20 = getD20Menu();
        if (jmD20.getItemCount() > 0)
            jm.add(jmD20);
        
        JToolBar jtRes = getToolBar(tbResults, tbResultsId);
        if (jtRes.getComponentCount() > 0)
            jtRes.addSeparator();
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.AppealResultOrder));
        
        if (jm.getItemCount() > 0)
            jm.addSeparator();
        // addToMenu(jm, null, XhibitActions.getAction(xac,
        // XhibitActions.VerifyResults));
        addToMenu(jm, jtGen, XhibitActions.getAction(xac, XhibitActions.AuthoriseResults));
        if (jm.getItemCount() > 0)
            jm.addSeparator();
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.OriginalCharges));
        
        return jm;
    }

    private JMenu getCreateCaseMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "CreateCaseMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "CreateCaseMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);

        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CreateTrialCase), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CreateSentenceCase), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CreateAppealCase), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CreateMiscCase), true);
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getCourtLogMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "CourtLogMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "CourtLogMenuMnemonic").charAt(0));

        ReadCourtLogXML clXML = new ReadCourtLogXML(this, jm);
        clXML.readXml("/config/xml/CourtLogMenu.xml");
        courtLogActions = clXML.getActions();
        if (courtLogActions == null) {
            if (courtLogActions.keys().length == 0) {
                log.error("courtLogActions.keys().length == 0 !!");
                JOptionPane.showMessageDialog(xac,
                        "An error occurred reading the court log action. Court Log will not be availble for update.",
                        "Court Log Menu Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return jm;
    }

    private JMenu getChargesMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "ChargesMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "ChargesMenuMnemonic").charAt(0));

        JMenu tempMenu;
        tempMenu = getChargeIndictmentMenu();
        if (tempMenu.getItemCount() > 0)
            jm.add(tempMenu);

        tempMenu = getChargeSection41Menu();
        if (tempMenu.getItemCount() > 0)
            jm.add(tempMenu);

        tempMenu = getChargeCommittalMenu();
        if (tempMenu.getItemCount() > 0)
            jm.add(tempMenu);

        tempMenu = getChargeBreachMenu();
        if (tempMenu.getItemCount() > 0)
            jm.add(tempMenu);

        tempMenu = getFail2AppearMenu();
        if (tempMenu.getItemCount() > 0)
            jm.add(tempMenu);
        
        // tempMenu = getAppealOffenceMenu();
        //	if(tempMenu.getItemCount() > 0)
        //	  jm.add(tempMenu);

        // ctx-500
        tempMenu = getAppealOffenceMenu();
        if (tempMenu.getItemCount() > 0) 
        	jm.add(tempMenu);        
        
        if (jm.getItemCount() > 0)
            jm.addSeparator();

        tempMenu = getChargeCountMenu();
        if (tempMenu.getItemCount() > 0)
            jm.add(tempMenu);

        tempMenu = getChargeDefendantMenu();
        if (tempMenu.getItemCount() > 0)
            jm.add(tempMenu);

        tempMenu = getChargeOffenceMenu();
        if (tempMenu.getItemCount() > 0)
            jm.add(tempMenu);

        if (jm.getItemCount() > 0)
            jm.addSeparator();
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ExportCharges));

        JToolBar jt = getToolBar(tbCharge, tbChargeId);
        try {
            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.tbAddCharge));
            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.tbAddOffence));
            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.tbAddDefendant));
            
            if (jt.getComponentCount() > 0)
                jt.addSeparator();

            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.AddCountsToDefendant));
            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.tbChange));
            if (jt.getComponentCount() > 0)
                jt.addSeparator();

            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.tbStay));
            if (jt.getComponentCount() > 0)
                jt.addSeparator();

            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.CountParticularsAmended));

            if (jt.getComponentCount() > 0)
                jt.addSeparator();
            addToMenu(null, jt, XhibitActions.getAction(xac, XhibitActions.ExportCharges));

        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getChargeIndictmentMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "IndictmentsMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "IndictmentsMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddIndictment));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddCount));
            //add new menu item for Add Count to Joinder Indictment
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddCountToJoinder));            
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RemoveIndictment));
            
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.StayIndictment));

            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.JoinIndictment));
            if (jm.getItemCount() > 0)
                jm.add(new JSeparator());
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CrestIndictmentLog), false);
            if (jm.getItemCount() > 0)
                jm.add(new JSeparator());
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.SOProsecutionNoEvidence));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getChargeSection41Menu() {
        // a.k.a Summary Offences
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "S41Menu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "S41MenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddS41Offence));
            if (jm.getItemCount() > 0)
                jm.add(new JSeparator());
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DefendantSummaryOffences));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getChargeCommittalMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "C4SMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "C4SMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddC4SOffence));
            if (jm.getItemCount() > 0)
                jm.add(new JSeparator());
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.C4SBringBack));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.C4SPutAndAdmitted));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.C4SNotAdmitted));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getChargeBreachMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "BreachMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "BreachMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddBreach));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.EditBreachProps));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddBreachOffence));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AdditionalBreachOffenceDefendantInfo));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RemoveBreach));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }
    
    private JMenu getFail2AppearMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "Fail2AppearMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "Fail2AppearMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddBailActOffence));
            if (jm.getItemCount() > 0)
                jm.add(new JSeparator());
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ChangeBailActOffence));
            //BAOs dont have an address, start date or end date
            //addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AdditionalOffenceInfo));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RemoveBailActOffence));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getOrdersMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "OrdersMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "OrdersMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        JToolBar jtRes = getToolBar(tbResults, tbResultsId);

        if (jtRes.getComponentCount() > 0)
            jtRes.addSeparator();
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.OrderView));
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.OrderCopy));
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.OrderCreate));

        return jm;
    }
    
    private JMenu getMonetaryOrdersMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "MonetaryOrdersMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "MonetaryOrdersMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        JToolBar jtRes = getToolBar(tbResults, tbResultsId);

        if (jtRes.getComponentCount() > 0)
            jtRes.addSeparator();
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.MonetaryOrderView));
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.MonetaryOrderCopy));
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.MonetaryOrderCreate));
        return jm;
    }
    
    private JMenu getD20Menu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "D20Menu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "D20MenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        JToolBar jtRes = getToolBar(tbResults, tbResultsId);

        if (jtRes.getComponentCount() > 0)
            jtRes.addSeparator();
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.D20View));
        addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.D20Create));
        
        return jm;
    }

    private JMenu getDisposalsMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "DisposalsMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "DisposalsMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        JToolBar jtRes = getToolBar(tbResults, tbResultsId);

        if (jtRes.getComponentCount() > 0)
            jtRes.addSeparator();
        try {
            addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.AddDisposal));
            addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.AddMagistrateDisposal));
            addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.AddVariationDisposal));
            addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.EditDisposal));
            addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.DeleteDisposal));
            addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.UndeleteDisposal));
            addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.CopyDisposal));
            addToMenu(jm, jtRes, XhibitActions.getAction(xac, XhibitActions.CopyUnrelatedDisposal));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getChargeCountMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "CountMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "CountMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AdditionalCountInfo));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AdditionalDefendantOnCountInfo));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddDefendantsToCount));
            addToMenu(jm,null, XhibitActions.getAction(xac, XhibitActions.RemoveDefendantsOnCount));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CountParticularsAmended));
            if (jm.getItemCount() > 0)
                jm.addSeparator();
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RenumberCounts));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.StayCount));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ChangeCount));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RemoveCount));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getChargeDefendantMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "DefendantMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "DefendantMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        try {
            
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ChangeDefendant));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddCountsToDefendant));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.StayDefendantOnCount));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.StayDefendantOnIndictment));
            if (jm.getItemCount() > 0)
                jm.addSeparator();
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ApplicationToSever));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.VoluntaryBillPreferred));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.LateBillOfIndictment));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.BillOfIndictment));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }

    private JMenu getChargeOffenceMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "OffenceMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "OffenceMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        try {
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AdditionalOffenceInfo));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AdditionalDefendantOnOffenceInfo));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddDefendantsToOffence));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ChangeOffence));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RemoveOffence));
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }
    
    private JMenu getAppealOffenceMenu() {
    	JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "AppealMenu"));
    	jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "AppealMenuMnemonic").charAt(0));
    	jm.setIcon(blankIcon);
    	try { 
    		addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddAppealOffence));
    	} catch (ActionNotFoundException ex) {
    		throw new CSUnrecoverableException(ex);
    	}
    	return jm;
    }

    private JMenu getListMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "ListMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "ListMenuMnemonic").charAt(0));
        // JToolBar jt = getListBar(tbGeneral,tbGeneralId);
        try {
     
        	 addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CreateList), true);
        	 addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.OpenExistingList), true);
             addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ListResults), true);
             addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.NonAvailableDays), true);
             addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CaseListingEntry), true);
             addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ListOfficersDiary), true);
             addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CaseSummary), true);
             
             if (jm.getItemCount() > 0)
                 jm.addSeparator();
             
            JMenu linkCasesMenu = getLinkCasesMenu();
            if (linkCasesMenu.getItemCount() > 0) {
            	jm.add(linkCasesMenu);
            }
             
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.MoveCase));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.AddHearing));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DistributeListLetters), true); 
            JMenu  tbMenu = getListBarMenu();
            if (tbMenu.getItemCount() > 0) {
                jm.add(tbMenu);
            }
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.MaintainListLetterRecipients), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ViewDistributionStatus), true);


        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }
    
 
    private JMenu getLinkCasesMenu() {
    	JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "LinkUnlinkCasesMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "LinkUnlinkCasesMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
    	
    	addToMenu(jm, null, XhibitActions.getAction(xac,  XhibitActions.NewLinkCases), true);
    	jm.addSeparator();
    	addToMenu(jm, null, XhibitActions.getAction(xac,  XhibitActions.NewUnlinkCases), true);
        
    	return jm;
    }

    private JMenu getListBarMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "DistributeListsMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "DistributeListsMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);

        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DailyList), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DailyListPrison), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RunningList), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.FirmList), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.WarnedList), true);

        return jm;
    }

    private JMenu getViewMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "ViewMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "ViewMenuMnemonic").charAt(0));
        JToolBar jt = getToolBar(tbGeneral, tbGeneralId);
        try {
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.ViewTodaysSchedule), true);
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.ViewCourtLog));
            // This is not required here, it is in the open menu
            // addToMenu (jm, null, XhibitActions.getAction(xac,
            // XhibitActions.ViewOtherDayLog) );
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.ViewCaseProgress));
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.ViewCharges));
            // addToMenu (jm, jt, XhibitActions.getAction(xac,
            // XhibitActions.ViewInformationPages), true );
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.ViewInformationPagesV2), true);
            
            addToMenu(jm, jt, XhibitActions.getAction(xac, XhibitActions.ImportExportNotification), true);
            // to be added
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.OpenLinkedHearingsSummary));
            // addToMenu(jm, null, XhibitActions.getAction(xac,
            // XhibitActions.ViewCrestForms));
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ViewCrestFormsBF));
            // This can be added in when skel shed is available.
            // addToMenu (jm, null, XhibitActions.getAction(xac,
            // XhibitActions.ViewSkeletonSchedule) );
            if (jt.getComponentCount() > 0)
                jt.addSeparator();
            if (jm.getItemCount() > 0)
                jm.addSeparator();

            addToMenu(jm, null, XhibitActions.getAction(xac,  XhibitActions.QueryCompletedCase), true);
            jm.addSeparator();
            JMenu tbMenu = getToolbarMenu();
            if (tbMenu.getItemCount() > 0)
                jm.add(tbMenu);

        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
        return jm;
    }
    
    private JMenu getCrownSupportMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "CrownSupportMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "CrownSupportMenuMnemonic").charAt(0));
        
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ChamberAndAdvocateDetails), true);
          
        return jm;
    }

    private JMenu getToolbarMenu() {
        // Clean up toolbar hash.
        // Remove toolbars that have no buttons
        Enumeration enumeration = toolbarHash.keys();
        while (enumeration.hasMoreElements()) {
            Object currKey = enumeration.nextElement();
            JToolBar item = (JToolBar) toolbarHash.get(currKey);
            if (item.getComponentCount() <= 0)
                toolbarHash.remove(currKey);
        }

        XToolBarHelper toolBarHelper = this.xac.getToolBarHelper();
        toolBarHelper.setToolBarHash(this.toolbarHash);
        return toolBarHelper.getMenu();
    }

    private JMenu getHelpMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "HelpMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "HelpMenuMnemonic").charAt(0));

        addToMenu(jm, null, HelpAction.getInstance(), true);
        addToMenu(jm, null, AboutAction.getInstance(), true);
        return jm;
    }

    private JMenu getAdminMenu() {
        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "AdminMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "AdminMenuMnemonic").charAt(0));

        JMenu jmSecurity = getSecurityMenu();
        if (jmSecurity.getItemCount() > 0)
            jm.add(jmSecurity);
        
        JMenu jmReferenceData = getReferenceDataMenu();
        if (jmReferenceData.getItemCount() > 0)
            jm.add(jmReferenceData);
        

        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.PublicDisplayConfigV2), true);
      
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.UnauthorisedCaseStatus), true);
        
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.MonetaryOrderAcknowledgement), true);
        
        
        JMenu onDemandReportMenu = getOnDemandReportsMenu();
        if (onDemandReportMenu.getItemCount() > 0) {
        	jm.addSeparator();
            jm.add(onDemandReportMenu);
        }
          
        JMenu periodicReportMenu = getPeriodicReportsMenu();
        if (periodicReportMenu.getItemCount() > 0) {
        	if(onDemandReportMenu.getItemCount()<=0){
                jm.addSeparator();
        	}
            jm.add(periodicReportMenu);
            jm.addSeparator();
        } else if(onDemandReportMenu.getItemCount()>0) {
            jm.addSeparator();
        }
                
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CourtOfAppeal), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RecoredCourtroomStatistics), true);
        return jm;
    }
    
    
    private JMenu getOnDemandReportsMenu() {
    	JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "OnDemandReportsMenu"));
    	
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "OnDemandReportsMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ADJSSReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CFIXReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CTLRPReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DEFSSReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DOCARReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.LFIXReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.LODReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.NFIXReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.NHAReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.NTRSFReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.OBWReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.OUTCReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.PRLISReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RAGEReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RELCJReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RUMOReport), true);  
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.UNLCReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DARTSReport), true);
        return jm;
    }
    
    private JMenu getPeriodicReportsMenu() {
    	JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "PeriodicReportsMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "PeriodicReportsMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);

        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.DRSRReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.INFTRPCReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RJSReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RRCAReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RRECReport), true);
        addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.RSITReport), true);       
        

        return jm;
    }
    
    private JMenu getReferenceDataMenu() {
    	JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "ReferenceDataMenu"));
    	jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "ReferenceDataMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);
        
        //if (jtRes.getComponentCount() > 0)
        //    jtRes.addSeparator();
        try {
        	addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ChamberAndAdvocateDetailsReadOnly), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.SolicitorFirmDetails), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.ProsecutorRespondentDetails), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.JudgeDetails), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.CourtCalendar), true);
            addToMenu(jm, null, XhibitActions.getAction(xac, XhibitActions.HomeCourtCentreAndCourtroomDetails), true);
        } catch (ActionNotFoundException ex) {
            throw new CSUnrecoverableException(ex);
        }
    	return jm;
    }

    private JMenu getSecurityMenu() {

        JMenu jm = new JMenu(XHIBITConstant.getResource(myMenuResources, "SecurityMenu"));
        jm.setMnemonic(XHIBITConstant.getResource(myMenuResources, "SecurityMenuMnemonic").charAt(0));
        jm.setIcon(blankIcon);

        return jm;
    }

    // Neil Entwistle - Adding Tools Menu to main menu BEGIN
    private JMenu getToolsMenu() {
        JMenu jmTool = new JMenu(XHIBITConstant.getResource(myMenuResources, "ToolsMenu"));
        JToolBar jtGen = getToolBar(tbGeneral, tbGeneralId);
        jmTool.setMnemonic(XHIBITConstant.getResource(myMenuResources, "ToolsMenuMnemonic").charAt(0));
        addToMenu(jmTool, jtGen, XhibitActions.getAction(xac, XhibitActions.Messaging), true);
        addToMenu(jmTool, null, XhibitActions.getAction(xac, XhibitActions.PublicNotice), XToolbarButton.xbIcon
                + XToolbarButton.xbText, XhibitSingleton.getInstance().isUserInCourtroom());
        if (jmTool.getItemCount() > 0)
            jmTool.addSeparator();
        addToMenu(jmTool, null, XhibitActions.getAction(xac, XhibitActions.CounselSignInWizard), true);
        addToMenu(jmTool, null, XhibitActions.getAction(xac, XhibitActions.FindCounselDefendant), true);

        return jmTool;
    }

    // Neil Entwistle - Adding Tools Menu to main menu END

    /**
     * Method to dispose of this object. This should be used to de-register all
     * listeners that would otherwise cause a "memory leak".
     */
    public void dispose() {
        if (this.recentCasePropertyChangeListener != null) {
            XhibitSingleton.getInstance().removePropertyChangeListener(XhibitSingleton.RECENTCASE,
                    this.recentCasePropertyChangeListener);
        }

        getToolBarHash().clear();
        getCourtLogActionMap().clear();
    }
}
