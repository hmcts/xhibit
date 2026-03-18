package uk.gov.courtservice.xhibit.client.search;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.util.ReflectionHelper;
import uk.gov.courtservice.xhibit.client.util.DefaultPopup;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.listeners.TablePopupListener;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;

/**
 * <p>
 * Title:
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
 * @author Frederik Vandendriessche
 * @version $Revision: 1.8 $
 */
public class XHIBITSearchResultsPanel extends XHIBITSearchPanel {
    
    private static final long serialVersionUID = 1L;

    private XHIBITSearch myParentSearchController;

    protected XHIBITSearchResults xsSearchResults;

    private final String FIELD_PATH_DELIMITER = ".";

    protected XTable theResultsTable;

    private TheResultsTableModel theResultsTableModel;

    private JPopupMenu resultsTablePopup = null;

    private ArrayList theResultsHeaders = new ArrayList();

    private ArrayList theResultsLongValues = new ArrayList();

    private Vector theResults = new Vector();

    private JScrollPane theResultsScrollPane;

    private JButton theBackButton;

    private TheBackAction theBackAction = null;

    private JButton theDetailsButton;

    private TheDetailsAction theDetailsAction = null;
    
    private boolean skipCriteriaInputFlag = false; 

    // private boolean detailScreenExists = false;

    public XHIBITSearchResultsPanel(XHIBITSearchResults xsSearchResults, XHIBITSearch xsSearch) {
        // xsSearchResults is the spec for the results - not the actual found
        // results!!
        super(xsSearch);

        this.myParentSearchController = xsSearch;
        log.debug("ready with the XHIBITSearch stuff via super() - includes the supers jbInit() call that adds step title and description");
        jbInit(xsSearchResults);
    }
    
    /**
     * XHIBITSearchResultsPanel 
     * 
     * @param xsSearchResults - The result set containing the results returned from previous Criteria Panel.
     * @param xsSearch - 
     * @param skip - 
     */
    public XHIBITSearchResultsPanel(XHIBITSearchResults xsSearchResults, XHIBITSearch xsSearch, boolean skip) {
        // xsSearchResults is the spec for the results - not the actual found
        // results!!
        super(xsSearch);

        this.myParentSearchController = xsSearch;
        this.skipCriteriaInputFlag = skip;
        log.debug("ready with the XHIBITSearch stuff via super() - includes the supers jbInit() call that adds step title and description");
        jbInit(xsSearchResults);
    }

    public void jbInit(XHIBITSearchResults xsSearchResults) {
        stepTitle = new JLabel(xsSearchResults.getStepTitle());
        stepDescription = new JLabel(xsSearchResults.getStepDescription());
        super.jbInit();

        this.xsSearchResults = xsSearchResults;
        Iterator keyIterator = xsSearchResults.resultAttributeKeys.iterator();
        while (keyIterator.hasNext()) {
            Object o = keyIterator.next();
            theResultsHeaders.add(xsSearchResults.resultsAttributeLabels.get(o));
            theResultsLongValues.add(xsSearchResults.resultsAttributeLongValues.get(o));
            XHIBITConstant.debug("############ added longValue " + xsSearchResults.resultsAttributeLongValues.get(o));
        }

        initialiseResultsTable();

        gbc = new GridBagConstraints(0, gbc.gridy + 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
        doAdd(this.theResultsScrollPane, gbc);

        this.theBackButton = new JButton();
        theBackButton.setAction(getBackAction());
        theBackButton.setMnemonic(((XAction) theBackButton.getAction()).getMnemonicKey().intValue());
        if (this.skipCriteriaInputFlag){
            /* No user inputted criteria to return to */
            theBackButton.setEnabled(false);
            theBackButton.setVisible(false);
        }
        gbc = new GridBagConstraints(0, gbc.gridy + 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.containerInsets, 0, 0);
        doAdd(getButtonPanel(), gbc);
        this.xsSearch.setSearchScreenName("Results");
    }

    /**
     * Method extracted from jbInit for clarity. Sets up table model, table and
     * scroll pane. Also adds list selection listener and pop up listener.
     */
    private void initialiseResultsTable() {
        theResultsTableModel = new TheResultsTableModel();
        String[] headers = new String[theResultsHeaders.size()];
        theResultsHeaders.toArray(headers);
        theResultsTableModel.setColumnNames(headers);
        theResultsTableModel.setLongValues(theResultsLongValues.toArray());
        theResultsTableModel.setData(theResults);

        theResultsTable = XTableFactory.getInstance().createDefaultTable(theResultsTableModel);
        theResultsTable.makeSortable();

        ListSelectionListener selectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRows[] = theResultsTable.getSelectedRows();
                setNumberOfResultsSelected(selectedRows.length);
            }
        };

        ListSelectionModel lsm = this.theResultsTable.getSelectionModel();

        int min = this.xsSearchResults.getMinimumResultsSelected();
        int max = this.xsSearchResults.getMaximumResultsSelected();

        if ((min == 1) && (min == max)) {
            lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            if (this.xsSearch.isInternalDebug())
                log.debug(" lsm.setSelectionMode(lsm.SINGLE_SELECTION) for the resultsTable");
        } else {
            lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            if (this.xsSearch.isInternalDebug())
                log.debug(" lsm.setSelectionMode(lsm.MULTIPLE_INTERVAL_SELECTION) for the resultsTable");
        }

        lsm.addListSelectionListener(selectionListener);
        this.theResultsTable.setSelectionModel(lsm);

        this.theResultsTable.addMouseListener(new TablePopupListener(getResultsTablePopup(), theResultsTable));
        this.theResultsScrollPane = new JScrollPane(this.theResultsTable);
        this.theResultsTable.setPreferredScrollableViewportSize(this.xsSearchResults.getResultsTableDimension());
        this.theResultsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        theResultsTable.initColumnSizes(theResultsTableModel.getLongValues(), this.xsSearchResults
                .getResultsTableDimension().width);
    }

    protected JPanel getButtonPanel() {
        JPanel resultButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcButtonPanel = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
        resultButtonPanel.add(XHIBITConstant.getSpacer(), gbcButtonPanel);

        gbcButtonPanel = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        resultButtonPanel.add(this.theBackButton, gbcButtonPanel);

        getDetailsAction().setEnabled(false); // by default false
        this.theDetailsButton = new JButton();
        theDetailsButton.setAction(getDetailsAction());
        theDetailsButton.setMnemonic(((XAction) theDetailsButton.getAction()).getMnemonicKey().intValue());

        gbcButtonPanel = new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        resultButtonPanel.add(this.theDetailsButton, gbcButtonPanel);
        return resultButtonPanel;
    }

    public void setNumberOfResultsSelected(int i) {
        boolean okEnabled = false;
        boolean detailsEnabled = false;

        if ((this.xsSearchResults.getMinimumResultsSelected() <= i)
                && (this.xsSearchResults.getMaximumResultsSelected() >= i)) {
            okEnabled = true;
        }

        if (i > 0)// i ==1
        {
            detailsEnabled = true;
        }
        ((OkCancelPanel) this.xsSearch.getButtonPanel()).getOkAction().setEnabled(okEnabled);
        getDetailsAction().setEnabled(detailsEnabled);
    }

    /**
	 * @return the theResults
	 */
	public Vector getTheResults() {
		return theResults;
	}

	public void setResults(Collection results) {
        log.debug(".setResults(Collection results) - collection of value objects");
        log.debug("size of results collection: " + (results == null ? "null " : "" + results.size()));

        ((XSortableTableModel) (this.theResultsTable.getModel())).setData(results);
        // theResultsTable.initColumnSizes(theResultsTableModel.getLongValues(),
        // this.xsSearchResults.getResultsTableDimension().width );

        try {
            int sortCol = xsSearchResults.getColumnIndexToSort();
            if (sortCol != XHIBITSearchResults.OFF) {
                int sortMode = xsSearchResults.getColumnSortModus();
                XSortableTableModel xSortModel = (XSortableTableModel) theResultsTable.getModel();
                xSortModel.sortByColumn(sortCol, (sortMode == XHIBITSearchResults.ASC ? true : false));
            }
        } catch (Exception e) {
            log.error("Exception whilst presorting the search results:");
            log.error(e);
        }

        // this.detailScreenExists =
        // this.myParentSearchController.hasDetailsScreen();

        setNumberOfResultsSelected(0);
    }

    protected Action getBackAction() {
        if (this.theBackAction == null) {
            this.theBackAction = new TheBackAction();
        }
        return this.theBackAction;
    }

    private class TheBackAction extends XAction {
        private static final long serialVersionUID = 1L;

        public TheBackAction() {
            populateFromBundle("WizBack");
        }

        public void xActionPerformed(ActionEvent actionEvent) {
            myParentSearchController.showXSCriteriaPanel();
        }
    }

    private Action getDetailsAction() {
        if (this.theDetailsAction == null) {
            this.theDetailsAction = new TheDetailsAction();
        }
        return this.theDetailsAction;
    }

    private class TheDetailsAction extends XAction {
        private static final long serialVersionUID = 1L;

        public TheDetailsAction() {
            populateFromBundle("btnDetails");
        }

        public void xActionPerformed(ActionEvent actionEvent) {
            CSValueObject valueObject = (CSValueObject) (((XSortableTableModel) theResultsTable.getModel())
                    .getDataAt(theResultsTable.getSelectedRow()));
            myParentSearchController.showXSDetailsPanel(valueObject);
        }
    }

    private class TheResultsTableModel extends XHIBITDefaultTableModel {
        private static final long serialVersionUID = 1L;

        public TheResultsTableModel() {
            super();
        }

        public Object getValueAt(int row, int col) {
            Object theObject = new String("n/a");
            String getterName = "not set yet";
            try {
                Object o = _data[row];
                // log.debug("____ o.getClass=" + o.getClass());

                String key = (String) xsSearchResults.resultAttributeKeys.elementAt(col);
                // Field field =
                // (Field)xsSearchResults.resultsAttributeFields.get(key);

                // Use ReflectionHelper to obtain methods to get desired
                // attribute
                Object[] parameterValues = {};
                Method[] getterMethods = ReflectionHelper.getGetterMethodForFieldPath(o.getClass(), key,
                        FIELD_PATH_DELIMITER);

                Object targetObject = o;

                // Recursively call each method that leads to the method of
                // interest
                for (int i = 0; i < getterMethods.length; i++) {
                    theObject = getterMethods[i].invoke(targetObject, parameterValues);
                    targetObject = theObject;
                }

                if (theObject == null)
                    theObject = "";

            } catch (Exception e) {
                if (xsSearch.isInternalDebug())
                    log.debug("the gettermethod was " + getterName);
                e.printStackTrace();
                theObject = new String("");
                if (xsSearch.isInternalDebug())
                    log.debug("..TheResultsTableModel.getValueAt() xTableModel not set (yet?)");
            }
            return theObject;
        }
    }

    private JPopupMenu getResultsTablePopup() {
        if (resultsTablePopup == null) {
            resultsTablePopup = new DefaultPopup(DefaultPopup.COPY);
            resultsTablePopup.add(new JMenuItem(getDetailsAction()));
        }
        return resultsTablePopup;
    }

	protected JButton getTheBackButton() {
		return theBackButton;
	}
}
