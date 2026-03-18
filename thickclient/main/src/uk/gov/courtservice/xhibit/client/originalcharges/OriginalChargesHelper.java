package uk.gov.courtservice.xhibit.client.originalcharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.ChargeVO;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.ChargesTableRowModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.DefendantsTableRowModel;
import uk.gov.courtservice.xhibit.client.originalcharges.tables.OriginalChargesTableRowModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.ScrollTableRowToView;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

public class OriginalChargesHelper {
    
    /**
     * Delegates the work to redisplay a table
     * @param table - XTable coresponding to the table to be re-displayed
     * @param data - ArrayList coresponding to the data to be displayed
     */
    public static void redisplayTable(XTable table, ArrayList<OriginalChargesTableRowModel> data) {
        redisplayTable(table, data.toArray());
    }
    
    /**
     * Saves the table data in the table's model and redisplays the data 
     * @param table - XTable coresponding to the table to be re-displayed
     * @param data - Object[] coresponding to the data to be displayed
     */
    public static void redisplayTable(XTable table, Object[] data) {
        XHIBITTableModelInterface model = (XHIBITTableModelInterface)table.getModel();
        model.setData(data);
        table.tableChanged(new TableModelEvent(model));
    }
    
    /**
     * Returns a filtered set of charges data.  
     * Only displayable data is returned, i.e. records that have been "DELETED" are not
     * eligible for display and will NOT be returned.
     * An optional DefendantOnCaseVO can also be supplied to further filter on defendant
     * so that the user only sees charge records for a given defendant 
     * @param data - ArrayList of ChargesTableRowModel records coresponding to the data 
     * to be filtered
     * @param defendant - optional DefendantsTableRowModel coresponding to the defendant  
     * @return ArrayList of filtered ChargesTableRowModel records
     */
    public static ArrayList<ChargesTableRowModel> filterCharges(ArrayList<ChargesTableRowModel> data, DefendantsTableRowModel defendant) {
        ArrayList<ChargesTableRowModel> filteredData = new ArrayList<ChargesTableRowModel>();
        for( ChargesTableRowModel ctrm : data ) {
            if( ctrm.displayableRecord() ) {
                if( defendant == null
                ||  defendant.getDefendantOnCaseVO() == ctrm.getDefendantOnCaseVO() ) {
                    filteredData.add(ctrm);
                }
            }
        }
        
        return filteredData;
    }
    
    /**
     * Get all the sequence numbers for Original Charges charges for the defnedant on case.
     * This also includes logically delete records. 
     * @param data - all charges for all defendants 
     * @param defendant - the defendant for whom the charges are required
     * @return TreeSet - containing the set of unique sequence numbers
     */
    public static TreeSet<Integer> getUsedSequenceNumbers(ArrayList<ChargesTableRowModel> chargeData, DefendantsTableRowModel defendant)
    {
        // Use a TreeSet to drop duplicates and ensure the records are returned in sequence
        TreeSet<Integer> sequenceNumbers = new TreeSet<Integer>();
        
        // First get the sequence numbers that are in the charges table
        for(ChargesTableRowModel ctrm : chargeData) {
            if( ctrm.databaseUpdateRequired() || ctrm.displayableRecord() )
            {
                if( defendant                        != null
                &&  defendant.getDefendantOnCaseVO() == ctrm.getDefendantOnCaseVO() )
                {
                    if( ctrm.getChargeVO().getSeqNo() != null ) {
                        sequenceNumbers.add(ctrm.getChargeVO().getSeqNo());
                    }
                }
            }
        }
        
        // Now add the sequence numbers that are obsolete
        if( defendant != null && defendant.getObsoleteCharges() != null ) {
            for(ChargeVO chargeVO : defendant.getObsoleteCharges()) {
                if( chargeVO.getSeqNo() != null ) {
                    sequenceNumbers.add(chargeVO.getSeqNo());
                }
            }
        }
        
        return sequenceNumbers;
    }
    
    /**
     * Convenience methos to return a line separator
     * @return JPanel - containing a line separator
     */
    public static JPanel getSeparator() {
        JPanel separatorPanel     = new JPanel(new GridBagLayout());
        JPanel separatorTextPanel = new JPanel(new GridBagLayout());
        separatorTextPanel.add(
            new JLabel(OriginalChargesHelper.getResource("originalChargesLbl")), 
            new GridBagConstraints(
                0, 0, 1, 1, 1.0, 1.0, 
                GridBagConstraints.WEST,
                GridBagConstraints.BOTH, 
                XHIBITConstant.containerInsets, 0, 0));        

        JPanel separatorLinePanel = new JPanel(new GridBagLayout());
        separatorLinePanel.add(
            new JSeparator(), 
            new GridBagConstraints(
                0, 0, 1, 1, 1.0, 1.0, 
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, 
                XHIBITConstant.containerInsets, 0, 0));
        
        separatorPanel.add(
            separatorTextPanel, 
            new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0, 
                GridBagConstraints.WEST,
                GridBagConstraints.NONE, 
                XHIBITConstant.nonContainerInsets, 0, 0));
        separatorPanel.add(
            separatorLinePanel, 
            new GridBagConstraints(
                1, 0, 1, 1, 1.0, 1.0, 
                GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, 
                XHIBITConstant.nonContainerInsets, 0, 0));
        
        return separatorPanel;
    }

    /**
     * Returns a label from the OriginalCharges resource bundle
     * @param item - coresponding to the "name" element from the resource bundle
     * @return String - coresponding to the "value" for the given "name" element
     */
    public static String getResource(String item) {
        return XHIBITConstant.getResource(XhibitBundles.OriginalCharges, item);
    }
    
    /**
     * Delegates the work to position the table in the scroll pane at either the top or bottom row 
     * @param table - XTable coresponding to the table to be viewed
     * @param scrollPane - JScrollPane coresponding to the scroll pane in which the table is viewed
     * @param firstRow - boolean where true indicates the first row and false indicates the last row
     */
    public static void positionRowInView(XTable table, JScrollPane scrollPane, boolean firstRow) {
        positionRowInView(table, scrollPane, firstRow ? 0 : table.getRowCount() - 1);
    }

    /**
     * Residplays the table with the selected row in view
     * @param table - XTable coresponding to the table to be viewed
     * @param scrollPane - JScrollPane coresponding to the scroll pane in which the table is viewed
     * @param row - int coresponding to the row to be viewed
     */
    public static void positionRowInView(XTable table, JScrollPane scrollPane, int row) {
        if(table.getRowCount() > 0) {
            if( row >= 0 ) {
                table.setRowSelectionInterval(row, row);
            }
            scrollPane.revalidate();
            scrollPane.repaint();
            SwingUtilities.invokeLater(
                new ScrollTableRowToView(table, scrollPane)
            );
        }
    }
}
