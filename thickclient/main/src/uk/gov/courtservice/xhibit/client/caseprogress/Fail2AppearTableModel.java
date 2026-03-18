package uk.gov.courtservice.xhibit.client.caseprogress;

import java.util.Date;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Fail2AppearTableModel
 * </p>
 * <p>
 * Description: This class represents the tableModel for BAO offences shown in the CaseProgress screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class Fail2AppearTableModel extends CaseProgressTableModel{
    private static final long serialVersionUID = 1L;
    //Columns
    public static final int NO = 0;
    public static final int DEFENDANT = 1;
    public static final int PLEA = 2;
    public static final int OFFENCE = 3;
    public static final int DISPOSAL = 4;
    
    //Constructor
    public Fail2AppearTableModel(Object[] resultsRowValue){
        super(resultsRowValue);
        Sorter.sort(resultsRowValue, new String[] {"chargeSequenceNumber","defendantOnChargeId"}, Boolean.TRUE);
    }
    
    //GetValueAt
    public Object getValueAt(int row, int column){
        ResultsRowValue resultsRowValue = (ResultsRowValue) getDataAt(row);
        
        String content = "";
        
        switch(column){
        case NO:
            if(resultsRowValue.getChargeValue() == null){
                content = "";
            }else{
                if(resultsRowValue.getChargeValue().getCrestChargeSeqNo() == null){
                    content = "";
                }else{
                    content = resultsRowValue.getChargeValue().getCrestChargeSeqNo().toString();
                }
            }
            break;
        case DEFENDANT:
            content = CaseProgressHelper.buildDefendantName(resultsRowValue.getDefendantValue());
            break;
        case PLEA:
            if (resultsRowValue.getPleaValue() != null) {
                if (resultsRowValue.getPleaValue().getBreachAdmitted() == null) {
                    content = "";
                } else if (resultsRowValue.getPleaValue().getBreachAdmitted().equals(Boolean.TRUE)) {
                    content = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.true");
                } else if (resultsRowValue.getPleaValue().getBreachAdmitted().equals(Boolean.FALSE)) {
                    content = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.false");
                }
            }
            break;
        case OFFENCE:
            //content = resultsRowValue.getOffenceDescription();
            content = resultsRowValue.getBreachOffencesText();
            break;
        case DISPOSAL:
            if (super.detailsOn) {
                content = CaseProgressHelper.getDisposalText(resultsRowValue.getDisposalValue());
            } else {
                content = CaseProgressHelper.getSummaryDisposalText(resultsRowValue.getDisposalValue());
            }
            break;
        default:
            break;
        }
        if(super.truncateMode){
            content = super.truncate(content);
        }
        return content;
    }
    
    public void initColumnNames() {
        setColumnNames(new String[] { getResource(CaseProgressConstants.NO_TXT),
                getResource(CaseProgressConstants.DEFENDANT_TXT), getResource(CaseProgressConstants.ADMITTED_TXT),
                getResource(CaseProgressConstants.OFFENCE_TXT),getResource(CaseProgressConstants.DISPOSAL_TXT) });
                
    }
    
    /**
     * Get a case progress resource for the given resource key.
     * 
     * @param key
     *            the key to lookup in the case progress resources.
     * @return the case progress resource string.
     */
    private String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, key);
    }
}
