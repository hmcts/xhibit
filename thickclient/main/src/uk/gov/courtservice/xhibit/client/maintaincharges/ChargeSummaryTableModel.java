package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT 2
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
 * @author Bal Bhamra EDS
 * @version 1.0
 */
public class ChargeSummaryTableModel extends AbstractTableModel {
    private static final int TABLE_COLUMNS = 5;

    public static final int COUNT_COLUMN = 0;

    public static final int DESCRIPTION_COLUMN = 1;

    public static final int DEFENDANT_COLUMN = 2;

    public static final int STATUS_COLUMN = 3;
    
    public static final int CODE_COLUMN = 4;

    private ResourceBundle resources = null;

    // private String[] columnNames = {"Count", "Offence", "Defendant",
    // "Status"};
    private String[] columnNames = new String[TABLE_COLUMNS];

    private OffenceValue count;

    private uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendant;

    private List arrayTableCounts;

    private Collection colIndictmentCounts;

    private Collection colCountDefendants;

    private ChargeSummaryTableRow chargeSummaryTableRow;

    public ChargeSummaryTableModel(ChargeValue indictment) {
        getColumnHeaders();

        arrayTableCounts = new ArrayList();

        // get counts
        if (indictment != null) {
            if (indictment.getOffenceValues() != null) {
                colIndictmentCounts = indictment.getOffenceValues();

                // loop through counts and get defendants
                Iterator countIterator = colIndictmentCounts.iterator();
                Integer seqNo = Integer.valueOf(0);
                while (countIterator.hasNext()) {
                    count = (OffenceValue) countIterator.next();
                    colCountDefendants = count.getDefendantValues();
                    
                    seqNo++;
                    if (count.getCrestOffenceSeqNo() == null){
                    	count.setCrestOffenceSeqNo(seqNo);
                    }
                    
                    if (colCountDefendants != null && colCountDefendants.size() > 0) {
                        Iterator defendantIterator = colCountDefendants.iterator();
                        while (defendantIterator.hasNext()) {
                            defendant = (DefendantValue) defendantIterator.next();
                            XhbDefendantOnOffenceBasicValue dobv = count.getDefendantOnOffence(defendant.getDefendantID());              

                            if(dobv.getObsInd()==null ||(dobv.getObsInd()!=null && dobv.getObsInd().equals("N")))
                                chargeSummaryTableRow = new ChargeSummaryTableRow(count, defendant, count.getCrestOffenceSeqNo().toString(), 
                                		count.getOffenceDescription(), defendant.getSurName()
                                    + ", " + defendant.getFirstName(), getStatus(dobv),null, count.getOffenceCode());
                            else if (dobv.getObsInd()!=null && dobv.getObsInd().equals("Y")){                            	
                        		 chargeSummaryTableRow = new ChargeSummaryTableRow(count, null,count.getCrestOffenceSeqNo().toString(),
                        				 count.getOffenceDescription(), "", "",null, count.getOffenceCode());
                            }
                            arrayTableCounts.add(chargeSummaryTableRow);
                        }
                    } else {
                        // this does not have any defendants.                    	
                        chargeSummaryTableRow = new ChargeSummaryTableRow(count, null, count.getCrestOffenceSeqNo().toString(),
                        		count.getOffenceDescription(), "", "",null, count.getOffenceCode());
                        arrayTableCounts.add(chargeSummaryTableRow); 
                    }
                }
                Sorter.sort(arrayTableCounts, new String[] { "sortCount" });
            }
        }
    }

    private String getStatus(XhbDefendantOnOffenceBasicValue dobv) {
        if ((dobv != null) && "Y".equals(dobv.getIsStayed())) {
            return XHIBITConstant.getResource(XhibitBundles.MaintainCharges, "Stayed");
        }

        return "";
    }

    private void getColumnHeaders() {
    	
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

        columnNames[0] = XHIBITConstant.getResource(resources, "columnCount");
        columnNames[1] = XHIBITConstant.getResource(resources, "columnCode");
        columnNames[2] = XHIBITConstant.getResource(resources, "columnOffence");
        columnNames[3] = XHIBITConstant.getResource(resources, "columnDefendant");
        columnNames[4] = XHIBITConstant.getResource(resources, "columnStatus");
       

    }

    public String getColumnName(int c) {
        return columnNames[c];
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int r, int c) {
        String cellValue;

        if (arrayTableCounts.size() <= 0)
            return null;
        
        switch (c) {
        
        case 0:
            cellValue = ((ChargeSummaryTableRow) arrayTableCounts.get(r)).getCount();
            break;
        case 1:
        	cellValue = ((ChargeSummaryTableRow) arrayTableCounts.get(r)).getOffenceCode();
            break;
        case 2:
        	cellValue = ((ChargeSummaryTableRow) arrayTableCounts.get(r)).getOffenceDescription();
            break;
        case 3:
        	cellValue = ((ChargeSummaryTableRow) arrayTableCounts.get(r)).getDefendant();
            break;
        case 4:
        	cellValue = ((ChargeSummaryTableRow) arrayTableCounts.get(r)).getStatus();
        	break;
        default:
            cellValue = null;
            break;
        }

        return cellValue;
    }

    public int getRowCount() {
        return arrayTableCounts.size();
    }

    public ChargeSummaryTableRow getRow(int row) {
        return (ChargeSummaryTableRow) arrayTableCounts.get(row);
    }

    // private String checkNull(String toCheck)
    // {
    // if (toCheck == null)
    // {
    // return "";
    // }
    // else
    // {
    // return toCheck;
    // }
    // }
}