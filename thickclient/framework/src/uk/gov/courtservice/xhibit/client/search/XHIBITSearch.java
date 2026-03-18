package uk.gov.courtservice.xhibit.client.search;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.XhibitProperties;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT2 XHIBITSearch
 * </p>
 * <p>
 * Description: This is the main controlling class for all XHIBIT2 Search
 * dialogs. This class uses the XHIBITSearchCriteria subclasses to construct the
 * 'criteria screen', the first step in the wizard dialog. The second screen in
 * the dialog is the results screen ( In the event of no machting records in the
 * datastore, the 'criteria screen' will say this and allow the user to change
 * criteria, without any unnecessary navnigation. ) An optional third screen
 * shows the details of a specific row in the results screen - the details of
 * one (of the many?) results.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version $Revision: 1.8 $
 */
public class XHIBITSearch extends XDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3009225046440425550L;

	private static final Logger log = CSServices.getLogger(XHIBITSearch.class);

    private static final int MINIMUM_SEARCHABLE_CHARACTERS = 1;

    private boolean internalDebug;

    protected boolean useDummyData;

    private AbstractSearchAction xsOpenSearchAction = null;

    protected XHIBITSearchCriteria xsCriteria;
    protected XHIBITSearchResults xsResults;
    protected XHIBITSearchDetails xsDetails;
    
    protected XHIBITSearchCriteriaPanel xsCriteriaPanel;
    protected XHIBITSearchResultsPanel xsResultsPanel;
    protected XHIBITSearchDetailsPanel xsDetailsPanel;
    protected SearchCardStack panelStack;

    private Collection theResults;

    private boolean skipCriteriaInputFlag = false; // Added to facilitate searches without without user input.
    
    private XhibitApplicationController xac;

    
    /**
     *  Constructor with Application Controller and  calling search action.
     * 
     * @param anOpenSearchAction
     * @param xac
     */
    public XHIBITSearch(AbstractSearchAction anOpenSearchAction, XhibitApplicationController xac) {
        super((java.awt.Frame) anOpenSearchAction.getController(), "XHIBIT Search", true);

        this.xac = xac;
        runFrame(anOpenSearchAction);

    }
    /**
     *  Constructor to allow search without user input. 
     * 
     * @param anOpenSearchAction
     * @param xac
     * @param skipCriteriaInput
     */ 
    public XHIBITSearch(AbstractSearchAction anOpenSearchAction, XhibitApplicationController xac, boolean skipCriteriaInput) {
        super((java.awt.Frame) anOpenSearchAction.getController(), "XHIBIT Search", true);
        
        this.skipCriteriaInputFlag = skipCriteriaInput;
        this.xac = xac;
        runFrame(anOpenSearchAction);
    }
    /**
     * Constructor with only a calling search action.
     * @param anOpenSearchAction
     */
    public XHIBITSearch(AbstractSearchAction anOpenSearchAction) {
        super((java.awt.Frame) anOpenSearchAction.getController(), "XHIBIT Search", true);
        runFrame(anOpenSearchAction);
    }

    private void runFrame(AbstractSearchAction anOpenSearchAction) {
        xsOpenSearchAction = anOpenSearchAction;
        this.xsOpenSearchAction.setSearchCancelled(false);
        this.xsOpenSearchAction.setResults(new Vector());

        setDebugLevel();
        if (isInternalDebug())
            log.debug("constructor finished.");

        jbInit(anOpenSearchAction);
    }

    public void jbInit(AbstractSearchAction anOpenSearchAction) {
        Vector xHIBITSearchSpecifications = (Vector) anOpenSearchAction.getSearchSpecification();
        if (isInternalDebug())
            log.debug("Got xHIBITSearchSpecifications:" + xHIBITSearchSpecifications);

        Iterator xHIBITSearchSpecificationIterator = xHIBITSearchSpecifications.iterator();
        while (xHIBITSearchSpecificationIterator.hasNext()) {
            Object xHIBITSearchSpecification = xHIBITSearchSpecificationIterator.next();
            if (isInternalDebug())
                log.debug("Analysing specifcation of class: " + xHIBITSearchSpecification);

            if (xHIBITSearchSpecification instanceof XHIBITSearchCriteria) {
                xsCriteria = (XHIBITSearchCriteria) xHIBITSearchSpecification;
                xsCriteria.setCriteria();
                xsCriteriaPanel = new XHIBITSearchCriteriaPanel(xsCriteria, this);
                if (isInternalDebug())
                    log.debug("created xsCriteriaPanel " + xsCriteriaPanel);
            } else if (xHIBITSearchSpecification instanceof XHIBITSearchResults) {
                xsResults = (XHIBITSearchResults) xHIBITSearchSpecification;
                xsResults.setResultFields();
                if (skipCriteriaInputFlag){
                    /* User inputs not required, so set constructor of search panel to disable back option */ 
                    xsResultsPanel = new XHIBITSearchResultsPanel(xsResults, this, skipCriteriaInputFlag );
                } else{
                    xsResultsPanel = new XHIBITSearchResultsPanel(xsResults, this);
                }   
                if (isInternalDebug())
                    log.debug("created xsResultsPanel " + xsResultsPanel);
                // do not create the panel / or create an empty table (?always
                // table?) panel //
            } else if (xHIBITSearchSpecification instanceof XHIBITSearchDetails) {
                xsDetails = (XHIBITSearchDetails) xHIBITSearchSpecification;
                xsDetailsPanel = new XHIBITSearchDetailsPanel(xsDetails, this);
                if (isInternalDebug())
                    log.debug("created xsDetailsPanel " + xsDetailsPanel);
            }
        }
        setSearchScreenName();
        setResizable(true);

        panelStack = new SearchCardStack(new XPanel[] { xsCriteriaPanel, xsResultsPanel, xsDetailsPanel });
        addBodyPanel(panelStack);
        pack();
        
        if (skipCriteriaInputFlag){
            // User inputs not required
            showXSPanel(this.xsResultsPanel);
            doSearch();
        } else {
            showXSPanel(this.xsCriteriaPanel);
        }
    	setVisible(true);    
    }

    private void showXSPanel(XPanel aPanel) {
        panelStack.show(aPanel);
    }

    protected void showXSResultsPanelAgain() {
        if (this.theResults.size() > 1) {
            showXSPanel(xsResultsPanel);
            xsResultsPanel.setNumberOfResultsSelected(xsResultsPanel.theResultsTable.getSelectedRowCount());
        } 
        else {
            this.showXSCriteriaPanel();
        }
    }

    protected void showXSResultsPanel(Collection results) {
        this.theResults = results;
        xsResultsPanel.setResults(results);
        showXSPanel(xsResultsPanel);
    }

    protected void showXSDetailsPanel(CSValueObject valueObject) {
        if (valueObject == null) {
            log.debug("showXSDetailsPanel will not activate as a null object has been passed as results object.");
        } else {
            if (!requiresOnlyOneResult(this.xsResults)) {
                log.debug("+*-+-+-+-+-**+-*+-*+-*+-*+-*+-*+-*+-*+-*+*-");
                ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(false);
            } else {
                ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(true);
            }
            xsDetailsPanel.setValueObject(valueObject);
            showXSPanel(xsDetailsPanel);
        }
    }

    protected void showXSCriteriaPanel() {
        ((OkCancelPanel) getButtonPanel()).getOkAction().setEnabled(false);
        showXSPanel(xsCriteriaPanel);
    }

    private boolean requiresOnlyOneResult(XHIBITSearchResults xsc) {
        boolean flag = false;

        int min = xsc.getMinimumResultsSelected();
        int max = xsc.getMaximumResultsSelected();
        if ((min == 1) && (min == max))
            flag = true;

        return flag;
    }

    /**
     * do not call this method before jbInit() has completed!!! so don't use it
     * in children's jbInit()!!!
     * 
     * @return boolean indicating whether or not a detail search spec is
     *         available to render the details of a specific search result.
     */
    protected boolean hasDetailsScreen() {
        return (this.xsDetails != null);
    }

    private boolean isKindOfValueObject(Class c1, Class c2) {
        boolean isKindOf = false;
        if (c2.isAssignableFrom(c1)) {
            isKindOf = true;
            if (isInternalDebug())
                log.debug("C1(" + c1 + ") is assignable from c2(" + c2 + ")");
        } else {
            if (isInternalDebug())
                log.debug("C1(" + c1 + ") is NOT assignable from c2(" + c2 + ")");
        }
        return isKindOf;
    }

    private String getValueFromComponent(Object x) {
        log.debug("getValueFromComponent(" + x + ")");
        String theValue = "n/a";
        if (x instanceof JTextComponent) {
            JTextComponent jtc = (JTextComponent) x;
            theValue = jtc.getText();
            if (isInternalDebug())
                log.debug(".getValueFromComponent(): TextComponent - Value = " + theValue);
        } else if (x instanceof javax.swing.JList) {
            theValue = ((javax.swing.JList) x).getSelectedValue().toString();
        } else if (x instanceof javax.swing.JComboBox) {
            theValue = ((javax.swing.JComboBox) x).getSelectedItem().toString();
        } else if (x instanceof ButtonGroup) {
        	 theValue = ((ButtonGroup) x).getSelection().getActionCommand();
        }
        return theValue;
    }

    protected void doSearch() {
        Collection results = new Vector();
        Vector arguments = new Vector();
        AbstractSearchCriteria criteriumObject;
        Object returnValue = null;

        if (useDummyData) { // fake results using the
            // criteria/results/details object
            CSAbstractValue csAV = this.xsResults.getResultsValueObjectClass();
            log.debug(".DoSearch(): 'useDummyData' results value objects are required to be instances of class: "
                    + csAV.getClass());
            if (isKindOfValueObject(csAV.getClass(),
                    uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.PersonValue.class)) {
                Vector dummyReturnValue = new Vector();
                uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.PersonValue p1 = new uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.PersonValue(
                        new Integer(0), "Jefke", "A.G.", "Vandendriessche");
                results.add(p1);
                dummyReturnValue.add(p1);
                uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.PersonValue p2 = new uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.PersonValue(
                        new Integer(1), "Greet", "A.G1.", "Vandendriessche1");
                results.add(p2);
                dummyReturnValue.add(p2);
                uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.PersonValue p3 = new uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.PersonValue(
                        new Integer(2), "Joske", "A.G2.", "Vandendriessche2");
                results.add(p3);
                dummyReturnValue.add(p3);
                returnValue = dummyReturnValue;
            } else {
                if (isKindOfValueObject(
                        csAV.getClass(),
                        uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue.class)) {
                    Vector dummyReturnValue = new Vector();
                    PersonValue pv1 = new PersonValue(new Integer(0), new Integer(0), new Integer(0), new Integer(0),
                            "p4-1", "p5-1");
                    dummyReturnValue.add(pv1);
                    PersonValue pv2 = new PersonValue(new Integer(0), new Integer(0), new Integer(0), new Integer(0),
                            "p4-2", "p5-2");
                    dummyReturnValue.add(pv2);
                    PersonValue pv3 = new PersonValue(new Integer(0), new Integer(0), new Integer(0), new Integer(0),
                            "p4-3", "p5-3");
                    dummyReturnValue.add(pv3);
                    returnValue = dummyReturnValue;
                } else {
                    if (isKindOfValueObject(csAV.getClass(),
                            uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue.class)) {
                        Vector v = new Vector();
                        RefJudgeBasicValue refJudge = new RefJudgeBasicValue();
                        refJudge.setCourtId(new Integer(1));
                        refJudge.setCrestJudgeId(new Integer(1));
                        refJudge.setFirstName("my first name");
                        refJudge.setFullListTitle1("my full list title 1");
                        refJudge.setFullListTitle2("my full list title 2");
                        refJudge.setFullListTitle3("my full list title 3");
                        refJudge.setHonours("honours");
                        refJudge.setInitials("ini");
                        refJudge.setMiddleName("midle name");
                        refJudge.setSurname("surname");
                        refJudge.setTitle("titlt");
                        v.add(refJudge);
                        refJudge = new RefJudgeBasicValue();
                        refJudge.setCourtId(new Integer(1));
                        refJudge.setCrestJudgeId(new Integer(1));
                        refJudge.setFirstName("2first name");
                        refJudge.setFullListTitle1("my full list title 1");
                        refJudge.setFullListTitle2("my full list title 2");
                        refJudge.setFullListTitle3("my full list title 3");
                        refJudge.setHonours("honours");
                        refJudge.setInitials("ini");
                        refJudge.setMiddleName("2midle name");
                        refJudge.setSurname("2surname");
                        refJudge.setTitle("titlt2");
                        v.add(refJudge);
                        returnValue = v;
                    } else {
                        log
                                .debug(".DoSearch(): 'useDummyData' DOES NOT PROVIDE for creation of dummy objects of class: "
                                        + csAV.getClass());
                    }
                }
            }
        } else {
            if (isInternalDebug())
                log.debug(".DoSearch(): hitting the BD");
            try {
                // getting the instance of the argument for the bd e method
                criteriumObject = this.xsCriteria.getSearchCriteria();
                if (isInternalDebug())
                    log.debug(".DoSearch(): criteriumObject=" + criteriumObject);

                // getting the method to hit on the BD
                Method searchMethod = this.getBusinessDelegateMethod(this.xsCriteria);
                if (this.isInternalDebug())
                    log.debug(".DoSearch(): searchMetdhod=" + searchMethod);

                // prepare the arguments for the method call on the BD.
                // iterate the criteria objects and populate the criteriaValue
                // object accordingly
                Iterator criteriumIterator = this.xsCriteria.getTheCriteriaKeys().iterator();
                while (criteriumIterator.hasNext()) {
                    // get the key of the criterium
                    String criteriumKey = (String) criteriumIterator.next();
                    if (isInternalDebug())
                        log.debug("criteriumKey = " + criteriumKey);

                    // get the matching UI widget
                    Object criteriumComponent = getComponentFromKey(criteriumKey);
                    if (isInternalDebug())
                        log.debug("criteriumComponent = " + criteriumComponent);

                    String criteriumValue = getValueFromComponent(criteriumComponent);
                    String fieldName = getFieldName(criteriumKey);

                    if (criteriumValue.equals("")) {
                        if (isInternalDebug())
                            log.debug("The criterium with key " + criteriumKey
                                    + " has an empty string value. Now checking whether there was a previous value");

                        // new? value is empty, only if there is an old value to
                        // be overwritten we shall write the empty string into
                        // the CriteriumValueObject

                        String prevValue = criteriumObject.getAttribute(fieldName);

                        if ((prevValue != null) && !(prevValue.equals(""))) {
                            if (isInternalDebug())
                                log.debug("The previous value was " + prevValue);
                            criteriumObject.setAttribute(fieldName, criteriumValue);
                            if (isInternalDebug())
                                log.debug("criteriumObject.setAttribute(" + fieldName + ", " + criteriumValue + ");");
                        }
                    } else {
                        // now, populate the criteriumValueObject
                        criteriumObject.setAttribute(fieldName, criteriumValue);
                        if (isInternalDebug())
                            log.debug("criteriumObject.setAttribute(" + fieldName + ", " + criteriumValue + ");");
                    }
                }

                /**
                 * Loop through the keys held in the theCriteria hashtable. For
                 * each element, determine if the key represents a
                 * user-enterable field. If it does, obtain the coresponding
                 * data value and use it to count the searchable characters
                 */
                int totalSearchable = 0;
                Enumeration parameters = this.xsCriteria.theCriteria.keys();
                while (parameters.hasMoreElements()) {
                    String key = (String) parameters.nextElement();
                    if (isUserEnterableCriteria(key)) {
                        String fieldName = getFieldName(key);
                        String data = criteriumObject.getAttribute(fieldName);
                        if (data != null && !(getComponentFromKey(key) instanceof ButtonGroup)) {
                            totalSearchable += countSearchableCharacters(data);
                        }
                    }
                }

                /**
                 * If the total number of searchable characters is less than the
                 * minimum, abort the search and inform the user
                 */
                if (totalSearchable < XHIBITSearch.MINIMUM_SEARCHABLE_CHARACTERS && !skipCriteriaInputFlag)  {
                    final Object[] items = { new Integer(XHIBITSearch.MINIMUM_SEARCHABLE_CHARACTERS) };
                    throw new CSValidationException("gui.search.tooFewSearchableCharacters", items,
                            "gui.search.tooFewSearchableCharacters: Too few searchable characters entered");
                }

                // Now the hidden parameters
                Enumeration enumeration = this.xsCriteria.getHiddenCriteria().keys();
                while (enumeration.hasMoreElements()) {
                    String key = (String) enumeration.nextElement();
                    // Find the field name from the mid tier search criteria
                    String fieldName = getFieldName(key);
                    // find the value from the hashtable
                    String value = (String) this.xsCriteria.getHiddenCriteria().get(key);
                    // the the pair to the value object to be used in the
                    // where clause
                    criteriumObject.setAttribute(fieldName, value);
                }

                XHIBITConstant.debug("Search Criteria Provided:");
                XHIBITConstant.debug("=========================");
                XHIBITConstant.debug(criteriumObject.toString());
                XHIBITConstant.debug("=========================");
                arguments.add(criteriumObject);
                if (isInternalDebug())
                    log.debug("final criteriumObject=" + criteriumObject);

                // now we invoke the method on the business delegate that does
                // the search, using the arguments
                // as collected above from the UI widgets on the the Search
                // Criteria Card.
                returnValue = searchMethod.invoke(this.xsCriteria.getBusinessDelegate(), arguments.toArray());
                if (returnValue != null) {
                    if (isInternalDebug())
                        log.debug(".DoSearch(): Return value class is " + returnValue.getClass().toString());
                    log.debug(".DoSearch(): Return value class is " + returnValue.getClass().toString());
                } else {
                    log.error("The search method " + searchMethod.getName() + " on  "
                            + this.xsCriteria.getBusinessDelegate().getClass() + " returned null! ");
                    returnValue = new Vector();
                }
            } catch (Exception e) {
                log.debug(".DoSearch(): Exception in XHIBITSearchCriteria (1): ");
                log.error(e);
                XHIBITConstant.handleError(e);
            }
        }
        try {
            Collection tempResults = new Vector();
            if (!(returnValue instanceof Collection)) {
                tempResults.add(returnValue);
                returnValue = tempResults;
            }
            // by now we're sure we've got a collection 'returned' (or not!)
            // by the BD.
            results = (Collection) returnValue;

            this.theResults = results;

            log.debug("results.size()=" + results.size());

            if (results.size() == 0) {
                if(skipCriteriaInputFlag ){
                    // No results returned from a pre determined search (no user input) then report error.
                    throw new CSValidationException("gui.search.noResultsForNonUserInputSearch",
                    "gui.search.noResultsForNonUserInputSearch: \nNo results found using search with no user inputted criteria.\n" +
                    "ERRORCODE: CCN701001.");
                }
                else {
                    if (isInternalDebug())
                        log.debug(".DoSearch(): results.size() == 0 redraw first card or popup 'no matching data'");
                    JOptionPane.showMessageDialog(this, XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                            "xs.gen.nomatchestext"), XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                            "xs.gen.nomatchestitle"), JOptionPane.INFORMATION_MESSAGE);    
                } 
            } else {
                this.theResults = results;

                if (results.size() == 1) { // go to the detail Card.
                    if (isInternalDebug())
                        log
                                .debug(".DoSearch(): results.size() == 1 go to the detail Card - if there is a detailspec.'");
                    CSValueObject csVo = (CSValueObject) results.iterator().next();
                    if ((this.xsDetails != null) && (this.xsDetailsPanel != null)) {
                        this.showXSDetailsPanel(csVo);
                    }
                } else {
                    if (isInternalDebug())
                        log.debug(".DoSearch(): results.size() == 2..n go to the list Card.'");
                    showXSResultsPanel(results);
                }
            }
        } catch (Exception e) {
            log.debug(".DoSearch(): Exception in XHIBITSearchCriteria (2): ");
            log.error(e);
            XHIBITConstant.handleError(e);
            if(skipCriteriaInputFlag ){
               xsOpenSearchAction.setSearchCancelled(true);
               if (theResults != null)  theResults.clear();
               ((OkCancelPanel) super.getButtonPanel()).getCancelAction().setEnabled(true);
               super.buttonPanel.cancelButton.doClick();
           }
        }
    }

    private Object getComponentFromKey(String key) {
    	return (Object) this.xsCriteria.getTheCriteriaComponents().get(key);
    }
    
    /**
     * Counts the number of non-% characters in the data
     * 
     * @param data -
     *            the data to be searched for '%' wildcards
     * @return - the number of non-% contained i the string
     */
    private int countSearchableCharacters(String data) {
        int total = 0;

        for (int x = 0; x < data.length(); x++) {
            if (data.charAt(x) != '%') {
                total++;
            }
        }

        return total;
    }

    private String getFieldName(String criteriumKey) throws IllegalArgumentException, IllegalAccessException {
        String fieldName;
        // first, get the field, if it is not detailIndicator
        if (criteriumKey.equals("detailIndicator")) {
            fieldName = "detailIndicator";
        } else {
            Field field = (Field) this.xsCriteria.theCriteria.get(criteriumKey);
            fieldName = (String) field.get(field);
            if (isInternalDebug())
                log.debug("(String)field.get(field) ==" + fieldName);
        }
        return fieldName;
    }

    public Method getBusinessDelegateMethod(XHIBITSearchStep xss) {
        Method m = null;
        try {
            Class[] c = xss.getParameterTypes();
            if (this.isInternalDebug()) {
                for (int x = 0; x < xss.getParameterTypes().length; x++) {
                    log.debug("XSCriteria : parameterType[" + x + "]=" + c[x]);
                }
                log.debug("XSCriteria : methodName==" + xss.getMethodName());
            }
            m = xss.getBusinessDelegate().getClass().getMethod(xss.getMethodName(), c);
        } catch (Exception e) {
            log.debug("Exception whilst getting the method on the BD.");
            log.debug(e);
            XHIBITConstant.handleError(e);
        }
        return m;
    }

    // Extend the OK process to set the selected search results
    public void okClicked(ActionEvent ae) throws Exception {
        if (this.isInternalDebug())
            log.debug("OKAction.actionPerformed(ActionEvent " + ae + ")");

        Collection theFinalResults = null;
        // if there is only one results, then return that one.
        if (theResults.size() == 1) {
            theFinalResults = theResults;
        } else {
            theFinalResults = new Vector();
            // if there are many results, then get the selected ones from
            // the table

            int[] selectedRows = xsResultsPanel.theResultsTable.getSelectedRows();

            for (int i = 0; i < selectedRows.length; i++) {
                CSValueObject object = (CSValueObject) ((XSortableTableModel) xsResultsPanel.theResultsTable.getModel())
                        .getDataAt(selectedRows[i]);
                if (this.isInternalDebug())
                    log.debug("OKAction: The Object on Row no." + i + " (" + selectedRows[i] + ") is "
                            + object.toString());
                theFinalResults.add(object);
            }
        }
        xsOpenSearchAction.setResults(theFinalResults);
        if (this.isInternalDebug())
            log.debug("Set the Collection " + theFinalResults + " in the search-opening-action's model");
        super.okClicked(ae);
    }

    // Extend the cancel process to flag the search has been cancelled
    public void cancelClicked(ActionEvent ae) throws Exception {
        xsOpenSearchAction.setSearchCancelled();
        if (theResults != null)
            theResults.clear();
        super.cancelClicked(ae);
    }

    public boolean hasMoreThanOneResult() {
        return this.theResults == null ? false : this.theResults.size() > 0;
    }

    public boolean hasOneResult() {
        return this.theResults == null ? false : this.theResults.size() == 1;
    }

    private String getInternalScreenName() {
        return XHIBITConstant.getResource(XhibitBundles.XhibitSearch, "xs.gen.name");
    }

    private void setSearchScreenName() {
        setTitle(getInternalScreenName());
    }

    public void setSearchScreenName(String theScreenName) {
        setTitle(getInternalScreenName() + " - " + theScreenName);
    }

    private void setDebugLevel() {
        try {
            String internalDebugIndicator = XHIBITConstant.getProperty(XhibitProperties.XhibitClientProject,
                    "XHIBITSearch.internalDebug");
            if (internalDebugIndicator != null) {
                if (internalDebugIndicator.indexOf(XHIBITConstant.propertyNotFoundStringStart) != -1) {
                    setInternalDebug(false);
                    log.debug("XHIBITSearch.internalDebug property key not found in "
                            + XhibitProperties.XhibitClientProject
                            + " property file - internal XHIBITSearch debug is turned off.");
                } else {
                    setInternalDebug(true);
                    log.debug("XHIBITSearch.internalDebug property key found in "
                            + XhibitProperties.XhibitClientProject
                            + " property file - internal XHIBITSearch debug is turned on!");
                }
            } else {
                setInternalDebug(false);
                log
                        .debug("XHIBITSearch.internalDebug property key not found in "
                                + XhibitProperties.XhibitClientProject
                                + " property file - internal XHIBITSearch debug is turned off. (internalDebugIndicator = null!!!)");
            }
        } catch (Exception e) {
            log.error(e);
        }

        try {
            String useDummyDataIndicator = XHIBITConstant.getProperty(XhibitProperties.XhibitClientProject,
                    "XHIBITSearch.useDummyData");
            if ((useDummyDataIndicator != null)
                    && (useDummyDataIndicator.indexOf(XHIBITConstant.propertyNotFoundStringStart) != -1)) {
                useDummyData = false;
                log.debug("XHIBITSearch.useDummyData property key not found in " + XhibitProperties.XhibitClientProject
                        + " property file - live mid tier interaction");
            } else {
                useDummyData = true;
                log.debug("XHIBITSearch.useDummyData property key found in " + XhibitProperties.XhibitClientProject
                        + " property file - using dummy data!");
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * Determines whether or not a given parameter key represents a
     * user-enterable search field. This is determined by virtue of the key's
     * existence in a hashtable of hidden keys. If the key is found in the
     * hashtable, the field is deemed NOT user-enterable.
     * 
     * @param key
     * @return true if the key represents a user-enterable field, i.e. is NOT in
     *         the hiddenCriteria hashtable
     */
    private boolean isUserEnterableCriteria(String key) {
        return (!this.xsCriteria.getHiddenCriteria().containsKey(key));
    }

    public XhibitApplicationController getXAC() {
        return this.xac;
    }

    public AbstractSearchAction getXSOpenSearchAction() {
        return this.xsOpenSearchAction;
    }
    
    public void setSkipCriteriaInputFlag(boolean flag) {
        this.skipCriteriaInputFlag = flag;
    }
	public boolean isInternalDebug() {
		return internalDebug;
	}
	public void setInternalDebug(boolean internalDebug) {
		this.internalDebug = internalDebug;
	}
	/**
	 * @return the theResults
	 */
	public Collection getTheResults() {
		return theResults;
	}
	/**
	 * @param theResults the theResults to set
	 */
	public void setTheResults(Collection theResults) {
		this.theResults = theResults;
	}
}
