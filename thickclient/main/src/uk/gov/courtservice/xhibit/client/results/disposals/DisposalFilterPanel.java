package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.renderers.TooltipComboBoxRenderer;
/**
 * Title: Disposal Filter Panel
 * Description: Show Filter options panel for the DisposalController screen. This
 * filter panel has 1 combo box that can filter defendants. 
 * @author Pete Twibill
 * @version 1.0
 */
public class DisposalFilterPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(DisposalFilterPanel.class);
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private TitledBorder titledBorder1;
    private JComboBox defendantCb = null;

    private DisposalController model = null;
    private DisposalFilterSelectionModel dfs = null;

    private String comboSelectAll;
    private String defendantFilter;
    /** The table the filtering is going to be done on. */
    private JTable table;
    /**
     * Stores the original state of the defendant filter before it is disabled.
     */
    boolean isDefendantSelectionAllowed = true;
    
    public DisposalFilterPanel(DisposalFilterSelectionModel dfs, DisposalController model, JTable table) {
    	log.debug("constructor");
    	try {
            this.dfs = dfs;
            this.model = model;
            comboSelectAll = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "filter.combos.select.all");
            defendantFilter = comboSelectAll;
            this.dfs.setDefendmentFilter(comboSelectAll);
            this.table = table;

            jbInit();
        } catch (Exception ex) {
            XHIBITErrorHandler.handleError(ex);
        }
    }

	private void jbInit() {
		this.setLayout(gridBagLayout1);
		titledBorder1 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlLtHighlight,
                SystemColor.controlShadow),"Filters");
    	this.setBorder(titledBorder1);
    	
		this.add(new JLabel("Defendant:"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, 
    			GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    	
        this.add(getDefendantCb(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
	}
	
	 /**
     * Creates, populates and returns the Defendant combo box.
     * 
     * @return the Defendant filter combo box.
     */
    protected JComboBox getDefendantCb() {
        if (defendantCb == null) {
            Collection c = dfs.getDefendants();
            //List dfsList = new ArrayList(c);
            defendantCb = new JComboBox(getComboBoxModel(c));
            defendantCb.setSelectedIndex(0);
            String[] tempArray = (String[]) c.toArray(new String[c.size()]);
            String[] tipList = new String[tempArray.length + 1];
            tipList[0] = "";
            System.arraycopy(tempArray, 0, tipList, 1, tempArray.length);
            TooltipComboBoxRenderer defRenderer = new TooltipComboBoxRenderer(tipList);
            defRenderer.setPreferredSize(new Dimension(250, XHIBITConstant.getLineHeight()));
            defRenderer.setMaximumSize(new Dimension(500, XHIBITConstant.getLineHeight()));
            defendantCb.setRenderer(defRenderer);
            defendantCb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    defendantCb_changed(ae);
                }
            });
        }
        return defendantCb;
    }
    
    /**
     * Action performed when the Defendant filter combo box selection changes.
     * 
     * @param ae
     *            Semantic event which indicates that a component-defined action
     *            occured.
     */
    private void defendantCb_changed(ActionEvent ae) {
        defendantFilter = (String) getDefendantCb().getSelectedItem();
        log.debug("filter selection changed to " + defendantFilter);
        setFilterDisposals();
        dfs.setDefendmentFilter(defendantFilter);
        //table.clearSelection(); //added
    }
    
    /**
     * Creates a DefaultComboBoxModel with a "select all" at index zero followed
     * by all of the elements of the collection c.
     * 
     * @param c
     *            collection to put into a DefaultComboBoxModel
     * @return the populated DefaultComboBoxModel.
     */
    private DefaultComboBoxModel getComboBoxModel(Collection c) {
        Vector v = new Vector();
        v.add(comboSelectAll);
        v.addAll(c);
        return new DefaultComboBoxModel(v);
    }
    
    /**
     * Sets the filter that will be applied to the table, based on the
     * defendant filter.
     */
    private void setFilterDisposals() {
        if (defendantFilter.equals(comboSelectAll)) {
            dfs.setFilter(DisposalFilterSelectionModel.FILTER_NONE);
        } else {
            dfs.setFilter(DisposalFilterSelectionModel.FILTER_DEFENDANT);
        }
    }

    /**
     * Disables the filters, saving their current state first.
     */
    public void disableFilters() {
        isDefendantSelectionAllowed = getDefendantCb().isEnabled();
        getDefendantCb().setEnabled(false);
    }

    /**
     * Restores the filters to the state they had before they were all disabled.
     */
    public void restoreFilters() {
        getDefendantCb().setEnabled(isDefendantSelectionAllowed);
    }
}
