package uk.gov.courtservice.xhibit.client.menu;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.courtlog.EditDirectionsActions;
import uk.gov.courtservice.xhibit.client.actions.courtlog.EndBWEventAction;
import uk.gov.courtservice.xhibit.client.actions.courtlog.EndHearingAction;
import uk.gov.courtservice.xhibit.client.actions.courtlog.FreeTextEventAction;
import uk.gov.courtservice.xhibit.client.actions.courtlog.BWEventAction;
import uk.gov.courtservice.xhibit.client.actions.courtlog.MediumMDEventAction;
import uk.gov.courtservice.xhibit.client.actions.courtlog.MediumMLEventAction;
import uk.gov.courtservice.xhibit.client.actions.courtlog.MediumMTEventAction;
import uk.gov.courtservice.xhibit.client.actions.courtlog.SimpleEventAction;
import uk.gov.courtservice.xhibit.client.actions.courtlog.BWEventAction;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogEventLevelPanel;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogVOBundle;
import uk.gov.courtservice.xhibit.client.courtlog.EndBWMediumEventMLModel;
import uk.gov.courtservice.xhibit.client.courtlog.EndHearingModel;
import uk.gov.courtservice.xhibit.client.courtlog.FreeTextModel;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMDModel;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMLModel;
import uk.gov.courtservice.xhibit.client.courtlog.MediumEventMTModel;
import uk.gov.courtservice.xhibit.client.courtlog.SimpleEventModel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings.PHConstants;
import uk.gov.courtservice.xhibit.client.util.BlankIcon;
import uk.gov.courtservice.xhibit.client.util.CourtLogXmlReader;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMenu;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Parses the court log menu xml building the menu structure and
 * toolbar
 * </p>
 * <p>
 * This class is tightly coupled with XHIBIT Menus and can not run without it.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author: Frederik Vandendriessche and Rakesh Lakhani
 * @version $Revision: 1.23 $
 */
public class ReadCourtLogXML {
    /** <code>Logger</code> used for logging of debug/info statements */
    private static final Logger log = CSServices.getLogger(ReadCourtLogXML.class);

    private XhibitMenus myParent;

    private JMenu clMenu;

    private ActionMap clActions = new ActionMap();

    private ActionMap clNoEditActions = new ActionMap();

    private Hashtable clToolbars = new Hashtable(5);

    private CourtLogVOBundle voBundle = new CourtLogVOBundle();

    private Icon blankIcon = new BlankIcon();

    public ReadCourtLogXML(XhibitMenus parent, JMenu jm) {
        myParent = parent;
        clMenu = jm;
        clActions.setParent(clNoEditActions);
    }

    public void readXml(String xmlFileName) {
        XMLServicesImpl xml = XMLServicesImpl.getInstance();
        CourtLogXmlReader clXMLR;
        try {
            InputStream is = xml.loadDocument(xmlFileName);
            clXMLR = new CourtLogXmlReader(is);
            clXMLR.initialize();
            Element el = new Element("el");
            Class[] parameterTypes = { el.getClass() };
            try {
                clXMLR.executeWhen("TOOLBARS", this, this.getClass().getMethod("xmlToolbarHandler", parameterTypes));
                clXMLR.executeWhen("EVENTS", this, this.getClass().getMethod("xmlEventsHandler", parameterTypes));
            } catch (Exception e) {
                XHIBITErrorHandler.handleError(e);
            }
            clXMLR.parseXML();
        } catch (Exception e) {
            XHIBITErrorHandler.handleError(e);
        }
    }

    public void xmlToolbarHandler(Element e) {
        Iterator it = e.getChildren().iterator();
        while (it.hasNext()) {
            Element te = (Element) it.next();
            String toolbarId = te.getAttributeValue("id");
            String toolbarName = te.getAttributeValue("name");
            clToolbars.put(toolbarId, myParent.getToolBar(toolbarName, toolbarId));
        }
    }

    public void xmlEventsHandler(Element e) {
        buildActions(clMenu, e);
    }

    public void buildActions(JMenu currMenu, Element currElem) {
        Iterator it = currElem.getChildren().iterator();
        if (it.hasNext()) {
            buildAction(currMenu, (Element) it.next());
            while (it.hasNext()) {
                buildAction(currMenu, (Element) it.next());
            }
            // Add edit-only events to action map
            addEditOnlyEvents();
        }
    }

    private void buildAction(JMenu currMenu, Element ce) {
        if (ce.getName().equals("SIMPLEEVENT")) {
            handleSimpleEvent(currMenu, ce);
        } else if (ce.getName().equals("MEDIUMCHOICEEVENT")) {
            handleMediumEventML(currMenu, ce);
        } else if (ce.getName().equals("BWEVENT")) {
        	handleBWEventMD(currMenu, ce);
        }else if (ce.getName().equals("ENDBWEVENT")) {
        	handleEndBWEventMD(currMenu, ce);
        }else if (ce.getName().equals("MEDIUMDATEEVENT")) {
            handleMediumEventMD(currMenu, ce);
        } else if (ce.getName().equals("MEDIUMTEXTEVENT")) {
            handleMediumEventMT(currMenu, ce);
        } else if (ce.getName().equals("COMPLEXEVENT")) {
            handleComplexEvent(currMenu, ce);
        } else if (ce.getName().equals("ADHOCACTION")) {
            handleAdHocAction(currMenu, ce);
        } else if (ce.getName().equals("FREETEXTEVENT")) {
            handleFreeTextEvent(currMenu, ce);
        } else if (ce.getName().equals("SUBMENU")) {
            String subMenuName = ResourceBundleHelper.getResource(XhibitBundles.Menu, ce
                    .getAttributeValue("propertyname"));
            XMenu subMenu = new XMenu(subMenuName);
            subMenu.setMnemonic(ResourceBundleHelper.getResource(XhibitBundles.Menu,
                    ce.getAttributeValue("propertyname") + "Mnemonic").charAt(0));
            subMenu.setIcon(blankIcon);
            buildActions(subMenu, ce);
            if (subMenu.getItemCount() > 0)
                currMenu.add(subMenu);
        } else if (ce.getName().equals("MENUSPACER")) {
            if (currMenu.getItemCount() > 0)
                currMenu.addSeparator();
            if (ce.getAttributeValue("toolbarname") != null) {
                if (clToolbars.contains(ce.getAttributeValue("toolbarname"))) {
                    JToolBar tempTb = ((JToolBar) clToolbars.get(ce.getAttributeValue("toolbarname")));
                    if (tempTb.getComponentCount() > 0)
                        tempTb.addSeparator();
                }
            }
        }
    }

	
	
    private void addEditOnlyEvents() {
        XAction action = new EditDirectionsActions();
        action.setController(myParent.xac);
        // Add empty model to avoid potential null pointers
        action.setModel(new FreeTextModel());
        String[] idList = new String[] { PDHConstants.CASE_DIRECTIONS.toString(), PDHConstants.CASE_PDFORM.toString(),
                PDHConstants.CASE_TRIALTIME.toString(), PDHConstants.DEF_ARRAIGNMENT.toString(),
                PDHConstants.DEF_BAIL.toString(), PDHConstants.DEF_CERTATTENDANCE.toString(),
                PDHConstants.DEF_FORMB.toString(), PDHConstants.DEF_IDENTIFICATION.toString() };
        for (int i = 0; i < idList.length; i++) {
            clActions.put(idList[i], action);
        }

        // This is a special case to handle case level EndHearing events as the
        // case level version( 30500 ) does not appear in the court log menu and
        // toolbar. The appropriate version of the screen that the user sees is
        // determined by the action and is based on the event code
        XAction endHearingAction = new EndHearingAction();
        endHearingAction.setController(myParent.xac);
        endHearingAction.setModel(new EndHearingModel());
        clActions.put("30500", endHearingAction);

        // This is a special case to handle PreliminaryHearing events not
        // already
        // covered by existing court log event actions - at the moment this is
        // the
        // Indictment By event only.
        XAction indictmentBy = new MediumMDEventAction();
        indictmentBy.setController(myParent.xac);
        indictmentBy.setShortDescription(PHConstants.INDICTMENT_BY_SHORT_DESC);
        MediumEventMDModel vo = new MediumEventMDModel();
        vo.setEventType(PHConstants.INDICTMENT_BY.toString());
        vo.setSchema(PHConstants.INDICTMENT_BY_SCHEMA);
        vo.setPanelText(indictmentBy.getShortDescription());
        indictmentBy.setModel(vo);
        clActions.put(vo.getEventType(), indictmentBy);
    }

    public void handleAdHocAction(JMenu currMenu, Element currElem) {
        // Get the action bundle name
        Element ta = currElem.getChild("ACTIONBUNDLENAME");

        String actionBundle = ta.getAttributeValue("BundleName");
        // By default add this action to the menu
        boolean addToMenu = true;
        try {
            addToMenu = ta.getAttribute("AddToMenu").getBooleanValue();
        } catch (DataConversionException ex) {
            // ignore and continue
        }
        // Get the action
        XAction a = XhibitActions.getAction(myParent.xac, actionBundle);

        if (a.hasReadAccess()) {
            if (addToMenu) {
                myParent.addToMenu(currMenu, null, a);
            }
            // Check and add to toolbar
            Element te = currElem.getChild("TOOLBARENTRY");
            if (te != null) {
                if (clToolbars.containsKey(te.getAttributeValue("toolbarname"))) {
                    ((JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"))).add(myParent.getToolButton(a,
                            myParent.courtLogButtonFormat));
                }
            }
        }
    }

    public void handleFreeTextEvent(JMenu currMenu, Element currElem) {
        // Set up model
        FreeTextModel vo = new FreeTextModel();
        String id = currElem.getAttributeValue("id");
        vo.setEventType(id);
        vo.setSchema(currElem.getAttributeValue("schema"));
        // Set up action
        XAction a = new FreeTextEventAction();
        if (a.hasReadAccess()) {
            a.setController(myParent.xac);
            populateAction(currElem.getChild("ACTION"), a);
            // Put Model in Action
            a.setModel(vo);
            // Store Action in Hashtable
            addToMap(currElem, id, a);
            // Add to menu
            myParent.addToMenu(currMenu, null, a);
            // Check and add to toolbar
            Element te = currElem.getChild("TOOLBARENTRY");
            if (te != null) {
                if (clToolbars.contains(te.getAttributeValue("toolbarname"))) {
                    ((JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"))).add(myParent.getToolButton(a,
                            myParent.courtLogButtonFormat));
                }
            }
        }
    }

    public void handleSimpleEvent(JMenu currMenu, Element currElem) {
        // Set up model
        SimpleEventModel vo = new SimpleEventModel();
        String id = currElem.getAttributeValue("id");
        vo.setEventType(id);
        vo.setSchema(currElem.getAttributeValue("schema"));

        // Set up action
        XAction a = new SimpleEventAction();

        if (a.hasReadAccess()) {
            a.setController(myParent.xac);
            populateAction(currElem.getChild("ACTION"), a);

            vo.setPanelText(a.getShortDescription());

            // Put Model in Action
            a.setModel(vo);
            // Store Action in Hashtable
            addToMap(currElem, id, a);
            // Check and add to toolbar
            JToolBar jt = null;
            Element te = currElem.getChild("TOOLBARENTRY");
            if (te != null) {
                if (clToolbars.containsKey(te.getAttributeValue("toolbarname"))) {
                    jt = (JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"));
                }
            }
            // Add to menu
            myParent.addToMenu(currMenu, jt, a, myParent.courtLogButtonFormat);
        }
    }

    public void handleMediumEventML(JMenu currMenu, Element currElem) {
        // Set up model
        MediumEventMLModel vo = new MediumEventMLModel();
        String id = currElem.getAttributeValue("id");
        vo.setEventType(id);
        vo.setSchema(currElem.getAttributeValue("schema"));
        vo.setSubSchema(currElem.getAttributeValue("subschema"));
        vo.setSelectionRequired(getSelectionRequired(currElem));
        // Set up action
        XAction a = new MediumMLEventAction();
        if (a.hasReadAccess()) {
            a.setController(myParent.xac);
            populateAction(currElem.getChild("ACTION"), a);

            vo.setPanelText(a.getShortDescription());

            // Put Model in Action
            a.setModel(vo);
            // Store Action in Hashtable
            addToMap(currElem, id, a);
            // Check and add to toolbar
            JToolBar jt = null;
            Element te = currElem.getChild("TOOLBARENTRY");
            if (te != null) {
                if (clToolbars.containsKey(te.getAttributeValue("toolbarname"))) {
                    jt = (JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"));
                }
            }

            Element multiDefendantEntry = currElem.getChild("MULTIDEFENDANT");
            if (multiDefendantEntry != null) {
                try {
                    if (multiDefendantEntry.getAttribute("required").getBooleanValue()) {
                        vo.setDefendantDisplayType(CourtLogEventLevelPanel.DEFENDANT_LIST);
                    }
                } catch (DataConversionException dce) {
                    log.error("Data Conversion Error of the required attribute on the element MULTIDEFENDANT", dce);
                }
            }

            // Add to menu
            myParent.addToMenu(currMenu, jt, a, myParent.courtLogButtonFormat);
        }
    }

    
    public void handleBWEventMD(JMenu currMenu, Element currElem) {
        // Set up model
    	MediumEventMLModel vo = new MediumEventMLModel();
        String id = currElem.getAttributeValue("id");
        vo.setEventType(id);
        vo.setSchema(currElem.getAttributeValue("schema"));
        vo.setSubSchema(currElem.getAttributeValue("subschema"));
        vo.setSelectionRequired(getSelectionRequired(currElem));
        vo.setEventType(id);
          // Set up action
        XAction a = new BWEventAction();
        if (a.hasReadAccess()) {
            a.setController(myParent.xac);
            populateAction(currElem.getChild("ACTION"), a);

            vo.setPanelText(a.getShortDescription());

            // Put Model in Action
            a.setModel(vo);
            // Store Action in Hashtable
            addToMap(currElem, id, a);
            // Check and add to toolbar
            JToolBar jt = null;
            Element te = currElem.getChild("TOOLBARENTRY");
            if (te != null) {
                if (clToolbars.containsKey(te.getAttributeValue("toolbarname"))) {
                    jt = (JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"));
                }
            }
            // Add to menu
            myParent.addToMenu(currMenu, jt, a, myParent.courtLogButtonFormat);
        }
    }
            

    public void handleEndBWEventMD(JMenu currMenu, Element currElem) {
        // Set up model
    	EndBWMediumEventMLModel vo = new EndBWMediumEventMLModel();	
        String id = currElem.getAttributeValue("id");
        vo.setEventType(id);
        vo.setSchema(currElem.getAttributeValue("schema"));
        vo.setSubSchema(currElem.getAttributeValue("subschema"));
        vo.setSelectionRequired(getSelectionRequired(currElem));
          // Set up action
        XAction a = new EndBWEventAction();
        if (a.hasReadAccess()) {
            a.setController(myParent.xac);
            populateAction(currElem.getChild("ACTION"), a);

            vo.setPanelText(a.getShortDescription());

            // Put Model in Action
            a.setModel(vo);
            // Store Action in Hashtable
            addToMap(currElem, id, a);
            // Check and add to toolbar
            JToolBar jt = null;
            Element te = currElem.getChild("TOOLBARENTRY");
            if (te != null) {
                if (clToolbars.containsKey(te.getAttributeValue("toolbarname"))) {
                    jt = (JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"));
                }
            }
            // Add to menu
            myParent.addToMenu(currMenu, jt, a, myParent.courtLogButtonFormat);
        }
    }
 
    
    public void handleMediumEventMD(JMenu currMenu, Element currElem) {
        // Set up model
        MediumEventMDModel vo = new MediumEventMDModel();
        String id = currElem.getAttributeValue("id");
        vo.setEventType(id);
        vo.setSchema(currElem.getAttributeValue("schema"));
        vo.setSubSchema(currElem.getAttributeValue("subschema"));
        vo.setSelectionRequired(getSelectionRequired(currElem));
        // Set up action
        XAction a = new MediumMDEventAction();
        if (a.hasReadAccess()) {
            a.setController(myParent.xac);
            populateAction(currElem.getChild("ACTION"), a);

            vo.setPanelText(a.getShortDescription());

            // Put Model in Action
            a.setModel(vo);
            // Store Action in Hashtable
            addToMap(currElem, id, a);
            // Check and add to toolbar
            JToolBar jt = null;
            Element te = currElem.getChild("TOOLBARENTRY");
            if (te != null) {
                if (clToolbars.containsKey(te.getAttributeValue("toolbarname"))) {
                    jt = (JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"));
                }
            }
            // Add to menu
            myParent.addToMenu(currMenu, jt, a, myParent.courtLogButtonFormat);
        }
    }

    public void handleMediumEventMT(JMenu currMenu, Element currElem) {
        // Set up model
        MediumEventMTModel vo = new MediumEventMTModel();
        String id = currElem.getAttributeValue("id");
        vo.setEventType(id);
        vo.setSchema(currElem.getAttributeValue("schema"));
        vo.setSubSchema(currElem.getAttributeValue("subschema"));
        vo.setSelectionRequired(getSelectionRequired(currElem));
        // Set up action
        XAction a = new MediumMTEventAction();
        if (a.hasReadAccess()) {
            a.setController(myParent.xac);
            populateAction(currElem.getChild("ACTION"), a);

            vo.setPanelText(a.getShortDescription());

            // Put Model in Action
            a.setModel(vo);
            // Store Action in Hashtable
            addToMap(currElem, id, a);
            // Check and add to toolbar
            JToolBar jt = null;
            Element te = currElem.getChild("TOOLBARENTRY");
            if (te != null) {
                if (clToolbars.containsKey(te.getAttributeValue("toolbarname"))) {
                    jt = (JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"));
                }
            }
            // Add to menu
            myParent.addToMenu(currMenu, jt, a, myParent.courtLogButtonFormat);
        }
    }

    public void handleComplexEvent(JMenu currMenu, Element currElem) {
        // Get action
        String actionName = currElem.getChild("ACTIONBUNDLE").getAttributeValue("value");
        XAction a = getAction(actionName);
        if (a != null) {
            if (a.hasReadAccess()) {
                a.setController(myParent.xac);
                populateAction(currElem.getChild("ACTION"), a);
                // Set up model and put in action
                FreeTextModel vo = (FreeTextModel) voBundle
                        .getObject(currElem.getChild("VO").getAttributeValue("name"));
                String id = currElem.getAttributeValue("id");
                vo.setEventType(id);
                vo.setSchema(currElem.getAttributeValue("schema"));
                a.setModel(vo);
                // Store Action in Hashtable
                addToMap(currElem, id, a);
                // Check and add to toolbar
                JToolBar jt = null;
                Element te = currElem.getChild("TOOLBARENTRY");
                if (te != null) {
                    if (clToolbars.containsKey(te.getAttributeValue("toolbarname"))) {
                        jt = (JToolBar) clToolbars.get(te.getAttributeValue("toolbarname"));
                    }
                }
                // Add to menu
                myParent.addToMenu(currMenu, jt, a, myParent.courtLogButtonFormat);
            }
        }
    }

    private boolean getSelectionRequired(Element currElem) {
        boolean selectionRequired = true;
        String selReq = currElem.getAttributeValue("selectionRequired");
        if (selReq != null) {
            try {
                selectionRequired = new Boolean(selReq).booleanValue();
            } catch (Exception ex) {
                selectionRequired = true;
            }
        }
        return selectionRequired;
    }

    /**
     * Register the action in the map. If edittable adds to map clActions If not
     * edittable adds to map clActions.getParent()
     * 
     * @param currElem
     * @param id
     * @param action
     */
    private void addToMap(Element currElem, String id, XAction action) {
        boolean isEditable = false;
        // if the attribute is not set, assume it is editable
        Attribute editable = currElem.getAttribute("IsEditable");
        if (editable == null) {
            isEditable = true;
        } else {
            try {
                isEditable = editable.getBooleanValue();
            } catch (DataConversionException ex) {
                // If boolean value is incorrect assume edittable
                log.error("Error converting the IsEditable attribute", ex);
                isEditable = true;
            }
        }

        if (isEditable) {
            // This action can be editted
            clActions.put(id, action);
        } else {
            // This action is for create only, not edit
            clActions.getParent().put(id, action);
        }
    }

    public XAction getAction(String actionName) {
        XAction xa = null;
        try {
            xa = XhibitActions.getAction(myParent.xac, actionName);
        } catch (ActionNotFoundException ex) {
            log.debug("Court Log Action could not be found for " + actionName);
            log.error(ex);
        }
        return xa;
    }

    public void populateAction(Element currElem, XAction xa) {
        KeyStroke ks;
        Iterator iter = currElem.getChildren().iterator();
        while (iter.hasNext()) {
            Element item = (Element) iter.next();
            if (item.getName().equals("QUICKPROPERTY")) {
                xa.populateFromBundle(item.getAttributeValue("propertyname"));
            } else if (item.getName().equals("NAME")) {
                xa.setName(item.getAttributeValue("propertyname"));
            } else if (item.getName().equals("LONGDESC")) {
                xa.setLongDescription(item.getAttributeValue("propertyname"));
            } else if (item.getName().equals("SHORTDESC")) {
                xa.setShortDescription(item.getAttributeValue("propertyname"));
            } else if (item.getName().equals("ICON")) {
                xa.setIcon(XHIBITConstant.imageRoot + item.getAttributeValue("value"));
            } else if (item.getName().equals("MNEMONICKEY")) {
                xa.setMnemonicKeyFromBundle(item.getAttributeValue("propertyname"));
            } else if (item.getName().equals("ACCELERATORKEY")) {
                int modifiers = -1;
                String mod = item.getAttributeValue("modifiers");
                if (mod != null) {
                    if (mod.equals("ALT"))
                        modifiers = ActionEvent.ALT_MASK;
                    if (mod.equals("CTRL"))
                        modifiers = ActionEvent.CTRL_MASK;
                    if (mod.equals("SHIFT"))
                        modifiers = ActionEvent.SHIFT_MASK;
                    if (mod.equals("CTRLALT"))
                        modifiers = ActionEvent.CTRL_MASK + ActionEvent.ALT_MASK;
                    if (mod.equals("SHIFTALT"))
                        modifiers = ActionEvent.SHIFT_MASK + ActionEvent.ALT_MASK;
                    if (mod.equals("SHIFTCTRL"))
                        modifiers = ActionEvent.SHIFT_MASK + ActionEvent.CTRL_MASK;
                    if (mod.equals("SHIFTCTRLALT"))
                        modifiers = ActionEvent.SHIFT_MASK + ActionEvent.CTRL_MASK + ActionEvent.ALT_MASK;
                }
                if (modifiers >= 0) {
                    ks = KeyStroke.getKeyStroke((int) (item.getAttributeValue("key").charAt(0)), modifiers);
                } else {
                    ks = KeyStroke.getKeyStroke(item.getAttributeValue("key").charAt(0));
                }
                xa.setAccelaratorKey(ks);
            }
        }
    }

    public ActionMap getActions() {
        return clActions;
    }

}
