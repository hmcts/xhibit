package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

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
 * @authorSimon Gilmore EDS
 * @version 1.0
 */
public class BailActOffenceTableModel extends XHIBITDefaultTableModel {
    
    private static final long serialVersionUID = 1L;
    public static final int DEFENDANT_COLUMN = 0;
    public static final int OFFENCE_COLUMN = 1;
    public static final int DESCRIPTION_COLUMN = 2;

    private List arrayTableCounts;
    private ChargeValue currentCharge;

    public BailActOffenceTableModel(java.util.List failure2AppearList){
        
        // Holders for collections
        Collection colOffences;

        // Holders for value objects
        OffenceValue offenceValue;
        ChargeSummaryTableRow chargeSummaryTableRow;
          
        getColumnHeaders();

        arrayTableCounts = new ArrayList();

        // get offence/Charge pairs
        if (failure2AppearList != null) 
        {
            Iterator fail2AppIt = failure2AppearList.iterator();
            while (fail2AppIt.hasNext()) 
            {
                currentCharge = (ChargeValue) fail2AppIt.next();
                colOffences = currentCharge.getOffenceValues();
                /* This should always be populated with 1 offence as 1:1 mapping enforced */
                if (colOffences != null && colOffences.size() == 1 )  
                {
                    offenceValue = (OffenceValue) colOffences.iterator().next();

                    DefendantValue defVal = new DefendantValue();
                    DefendantControllerBeanBusinessDelegate defendantDelegate;
                    defendantDelegate = XhibitDelegateHelper.getDefendantDelegate();     
                    try {
						defVal = defendantDelegate.findByDefId(currentCharge.getDefendantID());
					} catch (FinderException e1) {
						e1.printStackTrace();
					}
                    
                    if(defVal != null) { 
                    	// defendant found (should always be 1 defendant
                        // Note that the seqNo of BAOs relates to the charge not the offence
                        String crestChargeSeqNo = currentCharge.getCrestChargeSeqNo().toString();
                        String offenceDescription = offenceValue.getOffenceDescription();
                        String defendantName = defVal.getFirstName() + " " + defVal.getMiddleName() + " " + defVal.getSurName();
                        chargeSummaryTableRow = new ChargeSummaryTableRow(offenceValue, defVal,
                                crestChargeSeqNo, offenceDescription, defendantName, "status Stay/LOF",currentCharge);
                        
                        arrayTableCounts.add(chargeSummaryTableRow);                    	
                    } else {
                        // No defendants for this offence.
                        String crestOffenceSeqNo = offenceValue.getCrestOffenceSeqNo().toString();
                        String offenceDescription = offenceValue.getOffenceDescription();
                        chargeSummaryTableRow = new ChargeSummaryTableRow(offenceValue, null, crestOffenceSeqNo,
                                offenceDescription, "", "",currentCharge);

                        arrayTableCounts.add(chargeSummaryTableRow);
                    }
                    
                    /*
                    // This should always be populated with 1 Defendant as 1:1 mapping enforced
                    if (colDefendants != null && colDefendants.size() == 1) {
                        
                        defendant = (DefendantValue) colDefendants.iterator().next();
                        // Note that the seqNo of BAOs relates to the charge not the offence
                        String crestChargeSeqNo = currentCharge.getCrestChargeSeqNo().toString();
                        String offenceDescription = offenceValue.getOffenceDescription();
                        String defendantName = defendant.getFirstName() + " " + defendant.getMiddleName() + " "
                                        + defendant.getSurName();
                        chargeSummaryTableRow = new ChargeSummaryTableRow(offenceValue, defendant,
                                crestChargeSeqNo, offenceDescription, defendantName, "status Stay/LOF",currentCharge);

                        arrayTableCounts.add(chargeSummaryTableRow);
                    } else {
                        // No defendants for this offence.
                        String crestOffenceSeqNo = offenceValue.getCrestOffenceSeqNo().toString();
                        String offenceDescription = offenceValue.getOffenceDescription();
                        chargeSummaryTableRow = new ChargeSummaryTableRow(offenceValue, null, crestOffenceSeqNo,
                                offenceDescription, "", "",currentCharge);

                        arrayTableCounts.add(chargeSummaryTableRow);
                    }
                	*/
                    
                    Sorter.sort(arrayTableCounts, new String[] { "defendant", "sortCount" });
                }
                else
                {
                    throw new CSUnrecoverableException("Wrong number of offences for this charge. BAOs should be 1:1");
                }
           } // end of While loop            
        }
        setData(arrayTableCounts.toArray());
    }

    private void getColumnHeaders() {
        String[] cols = { XHIBITConstant.getResource(XhibitBundles.MaintainCharges, "columnDefendant"),
                          XHIBITConstant.getResource(XhibitBundles.MaintainCharges, "columnNumber"),
                          XHIBITConstant.getResource(XhibitBundles.MaintainCharges, "columnOffence") };
        setColumnNames(cols);
    }

    public Object getValueAt(int r, int c) {
        String cellValue;

        if (_data.length <= 0)
            return null;

        // ChargeSummaryTableRow cell = (ChargeSummaryTableRow)
        // arrayTableCounts.get(r);
        ChargeSummaryTableRow cell = (ChargeSummaryTableRow) _data[r];
        switch (c) {
        case 0:
            cellValue = cell.getDefendant();
            break;
        case 1:
            cellValue = cell.getCount();
            break;
        case 2:
            cellValue = cell.getOffenceDescription();
            break;
        default:
            cellValue = null;
            break;
        }

        return cellValue;
    }

    public ChargeSummaryTableRow getRow(int row) {
        return (ChargeSummaryTableRow) arrayTableCounts.get(row);
    }

}
