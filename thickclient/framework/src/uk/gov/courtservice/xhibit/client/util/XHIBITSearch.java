package uk.gov.courtservice.xhibit.client.util;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITSearchCriteria.Criterium;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */
public abstract class XHIBITSearch extends XDialog implements ActionListener {
    public ResourceBundle rsc;

    public static String SEARCH_CRITERIA_CARD_ID = "CriteriaCard";

    public static String SEARCH_RESULT_LIST_CARD_ID = "ResultListCard";

    // TODO : rename to upper case
    public static String SearchResultsItemDetailsCardID = "ResultDetailsCard";

    // construction that governs the different search Cards
    protected SearchValueObjectCardStack searchValueObjectCardStack;

    // Search Card 1 - the criteria card
    private XHIBITSearchCriteria xsc;

    // Search Card 2 - the results list card
    private XHIBITSearchResultItemsList xsrl;

    // Search Card 3 - the result detail card
    private XHIBITSearchResultItemDetail xsrd;

    private Collection searchResults;

    private int selectedItem = -1;

    public XHIBITSearch() {
        super((javax.swing.JFrame) null, "", true, OKCANCEL, DEFAULTYES);
        debug("constructor() start");
        this.rsc = XHIBITConstant.getResourceBundle("XHIBITSearch");
        debug("constructor() end");
    }

    public void debug(String text) {
        XHIBITConstant.debug(" - Search : " + text);
    }

    protected void setSearchResults(Collection c) {
        this.searchResults = c;
    }

    public void setInterfaces(XHIBITSearchCriteria xsc, XHIBITSearchResultItemsList xsrl,
            XHIBITSearchResultItemDetail xsrd) {
        debug("setInterfaces() start");
        this.xsc = xsc;
        // xsc.getValueObjectClass(); debug ("criteria - got CSValueObject
        // (sub)Class");
        xsc.setCriteria();
        debug("criteria - got Collection of Criteria objects");

        this.xsrl = xsrl;

        this.xsrd = xsrd;
        xsrd.getValueObjectClass();
        debug("detail - got CSValueObject (sub)Class");
        xsrd.setValueObjectDisplay();
        debug("detail - got Collection of Attributes to display");

        this.searchValueObjectCardStack = new SearchValueObjectCardStack(this);
        debug("setInterfaces() end");
    }

    protected abstract String getComponentResourceKey();

    public void setWindowTitle(String x) {
        setName(XHIBITConstant.getResource(rsc, getComponentResourceKey().concat(".screenName")));
        setTitle(XHIBITConstant.getResource(rsc, getComponentResourceKey().concat(".screenName") + "..."));
    }

    public int getHeightScreen() {
        int height = 400;
        try {
            height = Integer.parseInt(XHIBITConstant.getResource(rsc, "xs.gen.height"));
        } catch (Exception e) {
            XHIBITConstant.error("XHIBITSearch could not find generic height for XHIBITSearch.");
        }
        try {
            height = Integer.parseInt(XHIBITConstant.getResource(rsc, getComponentResourceKey().concat(".height")));
        } catch (Exception e) {
            XHIBITConstant.debug("XHIBITSearch could not find customized height for " + getComponentResourceKey());
        }
        return height;
    }

    public int getWidthScreen() {
        int width = 400;
        try {
            width = Integer.parseInt(XHIBITConstant.getResource(rsc, "xs.gen.width"));
        } catch (Exception e) {
            XHIBITConstant.error("XHIBITSearch could not find generic width for XHIBITSearch.");
        }

        try {
            width = Integer.parseInt(XHIBITConstant.getResource(rsc, getComponentResourceKey().concat(".height")));
        } catch (Exception e) {
            XHIBITConstant.debug("XHIBITSearch could not find customized width for " + getComponentResourceKey());
        }
        return width;
    }

    public String getLBLSearch() {
        return XHIBITConstant.getResource(rsc, "xs.gen.search");
    }

    public String getLBLBack() {
        return XHIBITConstant.getResource(rsc, "xs.gen.back");
    }

    public String getLBLDetails() {
        return XHIBITConstant.getResource(rsc, "xs.gen.details");
    }

    public void jbInit() {
        try {
            setName(XHIBITConstant.getResource(this.rsc, "xs.gen.name"));
            setTitle(XHIBITConstant.getResource(this.rsc, "xs.gen.name"));
            // debug("Name is " + XHIBITConstant.getResource(this.rsc,
            // "xs.gen.name"));
            // this.setModal(true);
            this.getContentPane().setLayout(new GridBagLayout());
            GridBagConstraints gbc = XHIBITConstant.getDefaultGridBagConstraints();
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridheight = GridBagConstraints.REMAINDER;
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            this.searchValueObjectCardStack.sop.jbInit();
            this.searchValueObjectCardStack.sorp.jbInit();
            // this.searchValueObjectCardStack.sordp.jbInit(); // delayed
            // till acutally shown (i.e. when the results where retrieved)
            super.addBodyPanel(this.searchValueObjectCardStack);

            this.showCard(XHIBITSearch.SEARCH_CRITERIA_CARD_ID);

            Dimension preferedDimension = new Dimension(getHeightScreen(), getHeightScreen());
            this.setSize(preferedDimension);
            this.getContentPane().setSize(preferedDimension);
            this.searchValueObjectCardStack.sop.setSize(preferedDimension);
            this.setResizable(false);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = this.getSize();
            if (frameSize.height > screenSize.height) {
                frameSize.height = screenSize.height;
            }
            if (frameSize.width > screenSize.width) {
                frameSize.width = screenSize.width;
            }
            this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            this.setVisible(true);
        } catch (Exception e) {
            /** @todo Remove e.printStackTrace(); */
            e.printStackTrace();
            XHIBITConstant.error(e);
        }
    }

    protected void setSelectedItem(int i) {
        this.selectedItem = i;
    }

    public void addCriteria(String criteriaName, JComponent criteriaComponent) {
        this.searchValueObjectCardStack.sop.addCriteria(criteriaName, criteriaComponent);
        debug("addCriteria(String " + criteriaName + ", JComponent " + criteriaComponent.toString() + ")");
    }

    public void showCard(String cardID) {
        if (cardID.equals(SEARCH_CRITERIA_CARD_ID)) {
            try {
                // discard any selection made in the results
                this.selectedItem = -1;
                this.searchValueObjectCardStack.sorp.resultsJTable.getSelectionModel().clearSelection();
                super.buttonPanel.okButton.setEnabled(false);
                this.searchValueObjectCardStack.sop.jbInit();
                debug("jbinit on " + SEARCH_CRITERIA_CARD_ID + " card performed");
            } catch (Exception e) {
                debug("unimportant exception whilst jbinit on criteria card");
                e.printStackTrace();
            }
        } else if (cardID.equals(SEARCH_RESULT_LIST_CARD_ID)) {
            try {
                super.buttonPanel.okButton.setEnabled(false);
                this.searchValueObjectCardStack.sorp.jbInit();
                debug("jbinit on " + SEARCH_RESULT_LIST_CARD_ID + " card performed");
                this.searchValueObjectCardStack.sorp.detailsButton.setEnabled(false);

            } catch (Exception e) {
                debug("unimportant exception whilst jbinit on result list card");
                e.printStackTrace();
            }
        } else if (cardID.equals(XHIBITSearch.SearchResultsItemDetailsCardID)) {
            try {
                super.buttonPanel.okButton.setEnabled(true);
                this.searchValueObjectCardStack.sordp.jbInit();
                debug("jbinit on " + XHIBITSearch.SearchResultsItemDetailsCardID + " performed");
            } catch (Exception e) {
                debug("unimportant exception whilst jbinit on detail card");
                e.printStackTrace();
            }
        }
        // display the card identified by cardID
        CardLayout xx = (CardLayout) this.searchValueObjectCardStack.getLayout();
        xx.show(this.searchValueObjectCardStack, cardID);
    }

    protected abstract void receiveResults(Object o);

    // start of implementation of the OkCancelPanelConsumer interface
    public void okClicked(ActionEvent e) {
        debug("received Ok ActionEvent - selected value object " + this.selectedItem);

        int x = this.searchValueObjectCardStack.sorp.resultsJTable.getSelectionModel().getLeadSelectionIndex();
        debug("received Ok ActionEvent - selected value object " + x);

        Object o = ((ArrayList) this.searchResults).get(x);
        this.receiveResults(o);

        this.dispose();
    }

    public void cancelClicked(ActionEvent e) {
        debug("received Cancel ActionEvent - selected NO value object");
        this.dispose();
    }

    // end of implementation of the OkCancelPanelConsumer interface

    // button action listener for child card buttons
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("search")) {
            debug("actionPerformed(ActionEvent) - Search (execute) click in criteria card");
            // this.showCard(this.SearchCriteriaPanelResultsID);
            this.xsc.executeSearch();
        } else if (action.equals("backToCriteria")) {
            debug("actionPerformed(ActionEvent) - Back (to criteria) click in result list card");
            this.showCard(SEARCH_CRITERIA_CARD_ID);
        } else if (action.equals("backToResults")) {
            debug("actionPerformed(ActionEvent) - Back (to results) click in result detail card");
            this.showCard(SEARCH_RESULT_LIST_CARD_ID);
        } else if (action.equals("details")) {
            debug("actionPerformed(ActionEvent) - Details clicked in result list card");
            this.showCard(XHIBITSearch.SearchResultsItemDetailsCardID);
        }
    }

    // end of buttons of child cards

    /*
     * =================== CONTAINER OF CARDS ====================
     */
    protected class SearchValueObjectCardStack extends XPanel {
        protected SearchCriteriaCard sop;

        protected SearchlResultsListCard sorp;

        protected SearchResultsItemDetailsCard sordp;

        protected SearchValueObjectCardStack(XHIBITSearch controllingContainer) {
            this.setLayout(new CardLayout());
            this.add(this.sop = new SearchCriteriaCard(controllingContainer), XHIBITSearch.SEARCH_CRITERIA_CARD_ID);
            debug("SearchValueObjectCardStack Added SearchCriteriaCard");
            this.add(this.sorp = new SearchlResultsListCard(controllingContainer),
                    XHIBITSearch.SEARCH_RESULT_LIST_CARD_ID);
            debug("SearchValueObjectCardStack Added SearchlResultsListCard");
            this.add(this.sordp = new SearchResultsItemDetailsCard(controllingContainer),
                    XHIBITSearch.SearchResultsItemDetailsCardID);
            debug("SearchValueObjectCardStack Added SearchResultsItemDetailsCard");
            debug("SearchValueObjectCardStack.SearchValueObjectCardStack() completed");
        }

        public void stepInitialise() {
        }

        public void stepActivate() {
        }

        public void stepUpdateViewState() {
        }

        public void stepValidate() throws CSValidationException {
        }

        public void stepDeactivate() {
        }

        public void stepDeinitialise(boolean update) {
        }
    }

    /*
     * ===================== SEARCH CRITERIA CARD ======================
     */
    protected class SearchCriteriaCard extends AbstractStep {
        private JPanel containingPanel;

        private JButton searchButton;

        public SearchCriteriaCard(XHIBITSearch xs) {
            super(xs);
        }

        protected void jbInit() throws Exception {
            this.removeAll();
            super.jbInit();

            setTitle(xs.getTitle());

            debug(SEARCH_CRITERIA_CARD_ID + " SearchCriteriaPanel jbInit start");
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 2.0;

            // STEP TITLE
            setStepName(xsc.getTitle(SEARCH_CRITERIA_CARD_ID));
            add(stepNameLbl, gridBagConstraints);

            // STEP DESCRIPTION
            setStepName(xsc.getDescription(SEARCH_CRITERIA_CARD_ID));
            add(stepNameLbl, gridBagConstraints);

            // SPACES
            add(XHIBITConstant.getSpacer(), super.gridBagConstraints);

            // CRITERIA
            this.containingPanel = new JPanel();
            containingPanel.setLayout(new GridBagLayout());

            GridBagConstraints gcb = XHIBITConstant.getDefaultGridBagConstraints();
            gcb.gridheight = 1;
            gcb.weightx = 1.0;
            gcb.weighty = 1.0;
            gcb.fill = GridBagConstraints.HORIZONTAL;

            int noOfCriteria = xs.xsc.criteria.size();
            for (int criteriaIndex = 0; criteriaIndex < noOfCriteria; criteriaIndex++) {
                Criterium criterium = (Criterium) xs.xsc.criteria.elementAt(criteriaIndex);
                if (!criterium.hidden) {
                    JLabel aJLabel = new JLabel(criterium.label);
                    if (criteriaIndex == noOfCriteria - 2) {
                        gcb.gridheight = GridBagConstraints.RELATIVE;
                    } // one but last row
                    else if (criteriaIndex == noOfCriteria - 1) {
                        gcb.gridheight = GridBagConstraints.REMAINDER;
                    } // last row
                    gcb.gridwidth = GridBagConstraints.RELATIVE; // one
                    // but
                    // last
                    // col
                    this.containingPanel.add(aJLabel, gcb);
                    gcb.gridwidth = GridBagConstraints.REMAINDER; // last
                    // col
                    this.containingPanel.add(criterium.view, gcb);
                }
            }
            // Adding Panel to Container
            super.gridBagConstraints.gridheight = GridBagConstraints.RELATIVE; // container
            // is
            // on
            // one
            // but
            // last
            // row
            super.gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            this.add(this.containingPanel, super.gridBagConstraints);
            // A spacer and the button on the last row
            super.gridBagConstraints.gridheight = GridBagConstraints.REMAINDER; // this
            // is
            // on
            // the
            // last
            // row
            super.gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE; // one
            // but
            // last
            // col
            this.add(XHIBITConstant.getSpacer(), super.gridBagConstraints);
            // the search button
            this.searchButton = new JButton(xs.getLBLSearch());
            this.searchButton.addActionListener((ActionListener) xs);
            super.gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // last
            // col
            super.gridBagConstraints.anchor = GridBagConstraints.EAST;
            super.gridBagConstraints.fill = GridBagConstraints.NONE;
            this.searchButton.setActionCommand("search");
            this.add(this.searchButton, super.gridBagConstraints);

            debug(SEARCH_CRITERIA_CARD_ID + " jbInit end");
        }
    }

    /*
     * ===================== SEARCH RESULTS CARD =====================
     */
    protected class SearchlResultsListCard extends AbstractStep {
        protected JTable resultsJTable;

        private JScrollPane resultsScrollPane;

        private JButton backButton;

        private JButton detailsButton;

        public SearchlResultsListCard(XHIBITSearch xs) {
            super(xs);
        }

        protected void jbInit() throws Exception {
            super.jbInit();
            debug(SEARCH_RESULT_LIST_CARD_ID + "jbInit start");

            GridBagConstraints gc = XHIBITConstant.getDefaultGridBagConstraints();
            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.weightx = 2.0;

            // STEP TITLE
            setStepName(xsrl.getTitle(SEARCH_RESULT_LIST_CARD_ID));
            add(stepNameLbl, gc);

            // STEP DESCRIPTION
            setStepName(xsrl.getDescription(SEARCH_RESULT_LIST_CARD_ID));
            add(stepNameLbl, gc);

            // SPACES
            add(XHIBITConstant.getSpacer(), gc);

            gc.gridheight = GridBagConstraints.RELATIVE;

            SearchResultsTableModel srtm = new SearchResultsTableModel(xsrl);

            Vector results = new Vector();

            if (searchResults != null) {
                Iterator resultsIterator = searchResults.iterator();
                while (resultsIterator.hasNext()) {
                    Object resultObject = resultsIterator.next();
                    Vector resultRow = new Vector();

                    Iterator attribsIt = xsrd.displayAttributes.iterator();
                    while (attribsIt.hasNext()) {
                        uk.gov.courtservice.xhibit.client.util.XHIBITValueObjectSpec.XHIBITValueObjectAttribute xVOa = (uk.gov.courtservice.xhibit.client.util.XHIBITValueObjectSpec.XHIBITValueObjectAttribute) attribsIt
                                .next();

                        String getterName = xVOa.name;
                        String firstLetterInUpperCase = getterName.substring(0, 1).toUpperCase();
                        getterName = "get".concat(firstLetterInUpperCase.concat(getterName.substring(1, getterName
                                .length())));
                        // debug(": getterName = " + getterName);

                        Class[] parameterTypes = {};
                        Method getterMethod = resultObject.getClass().getMethod(getterName, parameterTypes);

                        Object[] parameterValues = {};
                        Object attributeValue = getterMethod.invoke(resultObject, parameterValues);
                        if (attributeValue == null)
                            attributeValue = "";
                        // debug("The value of " + xVOa.name + " is ["+
                        // attributeValue.toString() +"]");

                        resultRow.add(attributeValue);
                    }
                    results.add(resultRow);
                    // debug("...added row " + resultRow.toString());
                }
            } else {
                debug(SEARCH_RESULT_LIST_CARD_ID + " nothing returned - design fault.");
            }
            srtm.setData(results);

            debug(SEARCH_RESULT_LIST_CARD_ID + "tablemodel has " + srtm.data.size() + " rows");

            this.resultsJTable = new JTable(srtm);

            ListSelectionListener selectionListener = new ListSelectionListener() {
                private boolean firstChanged;

                private boolean lastChanged;

                private int first;

                private int last;

                public void valueChanged(ListSelectionEvent e) {
                    firstChanged = false;
                    lastChanged = false;

                    int newFirst = e.getFirstIndex();
                    int newLast = e.getLastIndex();

                    firstChanged = first == newFirst ? false : true;
                    lastChanged = last == newLast ? false : true;

                    if (firstChanged) {
                        selectedItem = newFirst;
                        first = newFirst;
                        debug("frist and selected is " + xs.selectedItem);
                    }
                    if (lastChanged) {
                        selectedItem = newLast;
                        last = newLast;
                        debug("last and selected is " + xs.selectedItem);
                    }
                    if (firstChanged && lastChanged) {
                        selectedItem = first;
                        debug("first is " + first + ", last is " + last + " and selected is " + first
                                + " - hope its ok");
                    }
                    xs.buttonPanel.okButton.setEnabled(true);
                    xs.searchValueObjectCardStack.sorp.detailsButton.setEnabled(true);

                }
            };

            ListSelectionModel lsm = this.resultsJTable.getSelectionModel();
            lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lsm.addListSelectionListener(selectionListener);
            this.resultsJTable.setSelectionModel(lsm);
            this.resultsJTable.setPreferredScrollableViewportSize(new Dimension(300, 200));
            this.resultsScrollPane = new JScrollPane(this.resultsJTable);

            /*
             * Dimension x =
             * this.resultsJTable.getPreferredScrollableViewportSize(); double
             * d1 = x.getHeight(); double d2 = x.getWidth();
             */
            this.resultsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            this.resultsScrollPane.setMinimumSize(new Dimension(320, 220));

            // this.add(this.resultsJTable, gc);
            add(this.resultsScrollPane, gc);

            gc.weightx = 1.0;
            gc.gridheight = GridBagConstraints.REMAINDER;
            gc.gridwidth = GridBagConstraints.RELATIVE;
            backButton = new JButton(xs.getLBLBack());
            backButton.setActionCommand("backToCriteria");
            backButton.addActionListener((ActionListener) xs);
            add(backButton, gc);

            gc.gridwidth = GridBagConstraints.REMAINDER;
            gc.anchor = GridBagConstraints.EAST;
            detailsButton = new JButton(xs.getLBLDetails());
            detailsButton.setActionCommand("details");
            detailsButton.addActionListener((ActionListener) xs);
            detailsButton.setEnabled(false);
            add(detailsButton, gc);

            debug("SearchlResultsListCard jbInit end");
        }
    }

    /*
     * =============================== SEARCH RESULTS ITEM DETAIL CARD
     * ===============================
     */

    private class SearchResultsItemDetailsCard extends AbstractStep {
        private JPanel containingPanel;

        private JButton backButton;

        public SearchResultsItemDetailsCard(XHIBITSearch xs) {
            super(xs);
        }

        protected void jbInit() throws Exception {
            this.removeAll();
            super.jbInit();
            debug(SearchResultsItemDetailsCardID + " jbInit start");
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.weightx = 2.0;

            // STEP TITLE
            setStepName(xs.xsrd.getTitle(SEARCH_RESULT_LIST_CARD_ID));
            add(stepNameLbl, gridBagConstraints);

            // STEP DESCRIPTION
            setStepName(xs.xsrd.getDescription(SEARCH_RESULT_LIST_CARD_ID));
            add(stepNameLbl, gridBagConstraints);

            // SPACES
            add(XHIBITConstant.getSpacer(), gridBagConstraints);

            // CRITERIA
            this.containingPanel = new JPanel();
            containingPanel.setLayout(new GridBagLayout());

            GridBagConstraints gcb = XHIBITConstant.getDefaultGridBagConstraints();
            gcb.gridheight = 1;
            gcb.weightx = 1.0;
            gcb.weighty = 1.0;
            gcb.fill = GridBagConstraints.HORIZONTAL;

            int x = xs.searchValueObjectCardStack.sorp.resultsJTable.getSelectionModel().getLeadSelectionIndex();
            debug("received Ok ActionEvent - selected value object " + x);

            Object resultObject = ((ArrayList) this.xs.searchResults).get(x);
            debug(" resultObject = " + resultObject.toString());

            int noOfAttributes = xs.xsrd.displayAttributes.size();
            for (int attrIndex = 0; attrIndex < noOfAttributes; attrIndex++) {
                uk.gov.courtservice.xhibit.client.util.XHIBITValueObjectSpec.XHIBITValueObjectAttribute voa = (uk.gov.courtservice.xhibit.client.util.XHIBITValueObjectSpec.XHIBITValueObjectAttribute) xs.xsrd.displayAttributes
                        .elementAt(attrIndex);

                JLabel aJLabel = new JLabel(voa.label);
                if (attrIndex == noOfAttributes - 2) {
                    gcb.gridheight = GridBagConstraints.RELATIVE;
                } // one but last row
                else if (attrIndex == noOfAttributes - 1) {
                    gcb.gridheight = GridBagConstraints.REMAINDER;
                } // last row
                gcb.gridwidth = GridBagConstraints.RELATIVE; // one but last
                // col
                containingPanel.add(aJLabel, gcb);
                gcb.gridwidth = GridBagConstraints.REMAINDER; // last col

                String getterName = voa.name;
                String firstLetterInUpperCase = getterName.substring(0, 1).toUpperCase();
                getterName = "get".concat(firstLetterInUpperCase.concat(getterName.substring(1, getterName.length())));
                Class[] parameterTypes = {};
                Method getterMethod = resultObject.getClass().getMethod(getterName, parameterTypes);
                Object[] parameterValues = {};
                Object attributeValue = getterMethod.invoke(resultObject, parameterValues);
                if (attributeValue == null)
                    attributeValue = "";
                voa.widget = new JTextField((String) attributeValue);
                containingPanel.add(voa.widget, gcb);
            }

            // Adding Panel to Container
            gridBagConstraints.gridheight = GridBagConstraints.RELATIVE; // container
            // is
            // on
            // one
            // but
            // last
            // row
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(containingPanel, gridBagConstraints);

            // A spacer and the button on the last row
            gridBagConstraints.gridheight = GridBagConstraints.REMAINDER; // this
            // is
            // on
            // the
            // last
            // row
            gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE; // one
            // but
            // last
            // col
            add(XHIBITConstant.getSpacer(), gridBagConstraints);

            // the back to resultsbutton
            backButton = new JButton(xs.getLBLBack());
            backButton.setActionCommand("backToResults");
            backButton.addActionListener((ActionListener) xs);
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // last
            // col
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            add(backButton, gridBagConstraints);

            debug(SearchResultsItemDetailsCardID + " jbInit end");
        }
    }

    /*
     * =============== ABSTRACT STEP ================
     */
    private class AbstractStep extends JPanel {
        protected GridBagConstraints gridBagConstraints;

        protected XHIBITSearch xs;

        protected JLabel stepNameLbl;

        protected JLabel stepDescriptionLbl;

        protected Vector criteriaLabels = new Vector();

        protected Vector criteriaWidgets = new Vector();

        AbstractStep(XHIBITSearch xs) {
            this.xs = xs;
        }

        protected void jbInit() throws Exception {
            this.removeAll();
            this.setLayout(new GridBagLayout());
            this.gridBagConstraints = XHIBITConstant.getDefaultGridBagConstraints();
            this.gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            setName(XHIBITConstant.getResource(xs.rsc, "xs.gen.name"));
            setTitle(XHIBITConstant.getResource(xs.rsc, "xs.gen.name"));
        }

        public void setStepName(String name) {
            this.stepNameLbl = new JLabel(name);
        }

        public void setStepDescription(String description) {
            this.stepDescriptionLbl = new JLabel(description);
        }

        public void addCriteria(String criteriaName, JComponent criteriaComponent) {
            this.criteriaLabels.add(criteriaName);
            this.criteriaWidgets.add(criteriaComponent);

        }
    }

    private class SearchResultsTableModel extends XHIBITTableModel {
        boolean internalDebug = false;

        protected SearchResultsTableModel(XHIBITSearchResultItemsList xsrl) {
            // creating a table model to display the results as specified in
            // the list class/card impl
            super();
            Iterator attributeIterator = xsrl.displayAttributes.iterator();
            Vector columnNames = new Vector();
            while (attributeIterator.hasNext()) {
                XHIBITValueObjectSpec.XHIBITValueObjectAttribute xVOa = (XHIBITValueObjectSpec.XHIBITValueObjectAttribute) attributeIterator
                        .next();

                String colName = xVOa.label;
                colName = colName.trim();
                if (colName.endsWith(":")) {
                    colName = colName.substring(0, colName.length() - 1);
                    colName = colName.trim();
                }
                if (this.internalDebug)
                    XHIBITConstant.debug("SearchResultsTableModel : column name = " + colName);
                columnNames.add(colName);
            }
            if (internalDebug)
                debug("colnames=" + columnNames.toString());
            if (internalDebug)
                debug("longvalues=" + xsrl.getLongValues());
            String[] s = new String[columnNames.size()];
            columnNames.toArray(s);
            this.setColumnNames(s);
            this.setLongValues(xsrl.getLongValues().toArray());
            XHIBITConstant.debug("SearchResultsTableModel : created");

        }

        protected void addRow(Vector v) {
            data.add(v);
            XHIBITConstant.debug("SearchResultsTableModel : added " + v.toString());
        }

        public Object getValueAt(int row, int col) {
            // if (internalDebug) XHIBITConstant.debug("CELL[" + row + ", "
            // + col + "] ?");
            // Object o = super.getValueAt(row, col);
            Vector v = (Vector) data.elementAt(row);
            Object o = v.elementAt(col);
            if (internalDebug)
                XHIBITConstant.debug("CELL[" + row + ", " + col + "] = [" + o.toString() + "]");
            return o;
        }

    }
}

/*
 * use this code to enable double click to behave as select + OK dialog
 * MouseListener mouseListener = new MouseAdapter() { public void
 * mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) { int index =
 * resultsJList.locationToIndex(e.getPoint()); } } };
 * this.resultsJList.addMouseListener(mouseListener);
 */
/*
 * ListSelectionModel lsm = this.resultsJTable.getSelectionModel();
 * 
 * if (xsrl.noOfSelectableResults == 1) {
 * lsm.setSelectionMode(lsm.SINGLE_SELECTION); } else {
 * lsm.setSelectionMode(lsm.MULTIPLE_INTERVAL_SELECTION); }
 * 
 * ListSelectionListener selectionListener = new ListSelectionListener() {
 * public void valueChanged(ListSelectionEvent e) { int index =
 * e.getFirstIndex(); if (index >= 0) {
 * //((XHIBITSearch)controllingContainer).ocp.okButton.setEnabled(true); } else {
 * //((XHIBITSearch)controllingContainer).ocp.okButton.setEnabled(false); } } };
 * this.resultsJTable.getSelectionModel().addListSelectionListener(selectionListener);
 */