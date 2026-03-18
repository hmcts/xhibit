package uk.gov.courtservice.xhibit.client.xhibitapplication;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;
import uk.gov.courtservice.xhibit.client.util.table.XTable;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;


/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Desiption: Panel to display charges which have missing mandatory values.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class MandatoryChargeFieldsPanel extends XPanel{
    
    private static final long serialVersionUID = 1L;
    
    //Charge Collections
    private ArrayList<ChargeOffence> collectionIndictment = new ArrayList<ChargeOffence>();
    private ArrayList<ChargeOffence> collectionBreach = new ArrayList<ChargeOffence>();
    private ArrayList<ChargeOffence> collectionSummary = new ArrayList<ChargeOffence>();
    private ArrayList<ChargeOffence> collectionCommittal = new ArrayList<ChargeOffence>();
    
    public MandatoryChargeFieldsPanel(ApplicationCaseModel acm, String context) 
    throws CSRecoverableException{       
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(360, 260));
        jbInit(acm, context);        
    }
    
    /**
     * Initialise GUI components
     * @param acm
     * @throws CSRecoverableException
     */
    private void jbInit(ApplicationCaseModel acm, String context) throws CSRecoverableException{
        String missingMandatoryInfo = 
            ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, "missingMandatoryInfo");
        
        JTable myTable = setupTable(acm);
        JScrollPane jsp = new JScrollPane(myTable);
        this.add(jsp,new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(new JLabel(missingMandatoryInfo),
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        
        this.add(new JLabel(context),new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
    }
    
    /**
     * construct and return a JTable to be displayed to user. This will be populated with any charges which are incomplete.
     * @param acm
     * @return
     * @throws CSRecoverableException
     */
    private JTable setupTable(ApplicationCaseModel acm) throws CSRecoverableException{
        ChargeCompositeValue ccv = null;
        String[][] tableArray;
        
        Integer caseID = acm.getCaseId();
        
        try{
             ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseID, true);
        }catch (Exception e){
            throw new CSRecoverableException("gui.MandatoryChargeFieldsPanel.setupTable",
                    "Exception whilst getting the charge composite value object from the mid tier", e);
        }
        
        Collection charges = ccv.getCharges();
        
        Iterator chargeIterator = charges.iterator();
        while(chargeIterator.hasNext()){
            ChargeValue chargeValue = (ChargeValue) chargeIterator.next();
            //Only process charges of type Indictment, Breach, section_41 (Summary) or Committal for sentance
            if(chargeValue.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType()) ||
                    chargeValue.getChargeType().equals(ChargeTypes.BREACH.getChargeType()) ||
                            chargeValue.getChargeType().equals(ChargeTypes.SECTION_41.getChargeType()) ||
                                chargeValue.getChargeType().equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())){
                Collection offences = chargeValue.getOffenceValues();
                Iterator offenceIterator = offences.iterator();
                while(offenceIterator.hasNext()){
                    OffenceValue offenceValue = (OffenceValue) offenceIterator.next();
                    if (!isValidBichardCharge(offenceValue)){
                        addChargeToCollection(new ChargeOffence(chargeValue,offenceValue));                        
                    } 
                }
                
            }
        }
        
        tableArray = constructTableStructure();

        JTable myTable = XTableFactory.getInstance().createDefaultTable(new IncompleteChargesTableModel(tableArray));
        
        //Set all columns to be multi line
        for (int x = 0; x < myTable.getColumnCount(); x++) {
            myTable.getColumnModel().getColumn(x).setCellRenderer(
                    XTableFactory.getInstance().getMultiLineCellRenderer((XTable) myTable));
        }
        myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return myTable;
    }
    
    /**
     * Constuct the 2D String array to populate the table with. Charge/Offence pairs will be retrieved from collections so they are processed in order.
     * @return
     */
    private String[][] constructTableStructure(){
       int numRecords = collectionIndictment.size()+collectionSummary.size()+collectionBreach.size()+collectionCommittal.size();
       String[][] tableStructure = new String[numRecords][IncompleteChargesTableModel.NUM_COLUMNS];
       int lineNum = 0;
       Iterator it;
       
       //Process indictments
       it = collectionIndictment.iterator();
       while(it.hasNext()){
           tableStructure[lineNum] = constructRow((ChargeOffence) it.next());
           lineNum++;
       }
       //process Summary Offences
       it = collectionSummary.iterator();
       while(it.hasNext()){
           tableStructure[lineNum] = constructRow((ChargeOffence) it.next());
           lineNum++;
       }
       //Process Commital for sentances
       it = collectionCommittal.iterator();
       while(it.hasNext()){
           tableStructure[lineNum] = constructRow((ChargeOffence) it.next());
           lineNum++;
       }
       //Process Breaches
       it = collectionBreach.iterator();
       while(it.hasNext()){
           tableStructure[lineNum] = constructRow((ChargeOffence) it.next());
           lineNum++;
       }
       
       return tableStructure;
    }
    
    /**
     * Populate a particular row based on a chargeOffence
     * @param chargeOffence
     * @return
     */
    private String[] constructRow(ChargeOffence chargeOffence){
        String[] rowArray = new String[IncompleteChargesTableModel.NUM_COLUMNS];
        Collection defValues;
        DefendantValue defValue;
        Iterator it;
        
        if(chargeOffence.getChargeValue().getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())){
            rowArray[0] = "Indictment";   
            rowArray[1] = chargeOffence.getChargeValue().getCrestChargeSeqNo().toString()+
                "(Count "+chargeOffence.getOffenceValue().getCrestOffenceSeqNo().toString()+")";
        }else if(chargeOffence.getChargeValue().getChargeType().equals(ChargeTypes.BREACH.getChargeType())){
            rowArray[0] = "Breach";
            rowArray[1] = chargeOffence.getOffenceValue().getCrestOffenceSeqNo().toString();
        }else if(chargeOffence.getChargeValue().getChargeType().equals(ChargeTypes.SECTION_41.getChargeType())){
            rowArray[0] = "Summary Offence";
            rowArray[1] = chargeOffence.getOffenceValue().getCrestOffenceSeqNo().toString();
        }else if(chargeOffence.getChargeValue().getChargeType().equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())){
            rowArray[0] = "Committal For Sentance";
            rowArray[1] = chargeOffence.getOffenceValue().getCrestOffenceSeqNo().toString();
        }        
        
        defValues = chargeOffence.getOffenceValue().getDefendantValues();
        if (defValues.size() >0){
            rowArray[2] = "";
            it = defValues.iterator();
            while(it.hasNext()){
                defValue = (DefendantValue)it.next();
                rowArray[2] += defValue.getSurName()+", "+defValue.getFirstName();
                if(it.hasNext()){
                    rowArray[2]+= ";\n";
                }
            }
        }else{
            rowArray[2] = "";
        }
        
        rowArray[3] = chargeOffence.getOffenceValue().getOffenceDescription();
        
        
        return rowArray;
    }
    
    /**
     * Add the Charge/Offence pair to the appropriate collection ready to be added to the table
     * @param chargeOffence
     */
    private void addChargeToCollection(ChargeOffence chargeOffence){
        String chargeType = chargeOffence.getChargeValue().getChargeType(); 
            if(chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())){
                collectionIndictment.add(chargeOffence);    
            }else if(chargeType.equals(ChargeTypes.BREACH.getChargeType())){
                collectionBreach.add(chargeOffence);
            }else if(chargeType.equals(ChargeTypes.SECTION_41.getChargeType())){
                collectionSummary.add(chargeOffence);
            }else if(chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())){
                collectionCommittal.add(chargeOffence);
            }
    }
    
    /**This method works out whether a given offence is valid in bichard.
     * Currently only the offence StartDateTime and 2 lines of the address are currently mandatory
     * 
     * @param offenceValue
     * @return
     */
    private boolean isValidBichardCharge(OffenceValue offenceValue){     
        if (offenceValue.getOffenceStartDateTime() == null){
            return false;
            
        }
        if (offenceValue.getAddressId() == null){
            return false;
        }
        return true;
    }

    @Override
    public void stepActivate() throws CSRecoverableException {
        // empty
    }

    @Override
    public void stepDeactivate() throws CSRecoverableException {
        //empty
    }

    @Override
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // empty
    }

    @Override
    public void stepInitialise() throws CSRecoverableException {
        // empty
    }

    @Override
    public void stepUpdateViewState() throws CSRecoverableException {
        // empty
        
    }

    @Override
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // empty
        
    }
    
    /**This class represents a Charge/Offence pair.
     * */
    class ChargeOffence{
        private ChargeValue charge;
        private OffenceValue offence;
        
        public ChargeOffence(ChargeValue charge,OffenceValue offence){
            this.charge = charge;
            this.offence = offence;
        }
        
        public ChargeValue getChargeValue(){
            return charge;
        }
        
        public OffenceValue getOffenceValue(){
            return offence;
        }
    }
    
    class IncompleteChargesTableModel extends XHIBITDefaultTableModel{

        private static final long serialVersionUID = 1L;

        private static final int NUM_COLUMNS = 4; 
        
        public static final int COL_CHARGE_TYPE = 0;
        public static final int COL_IDENTIFIER = 1;
        public static final int COL_DEFENDANTS = 2;
        public static final int COL_OFFENCE = 3;
        
        public IncompleteChargesTableModel(String[][] tableData){
            super();
            setData(tableData);
            String[] columns = new String[NUM_COLUMNS];
            columns[COL_CHARGE_TYPE] = ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,"bc_colChargeType");            
            columns[COL_IDENTIFIER] = ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,"bc_colIdentifier");
            columns[COL_DEFENDANTS] = ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,"bc_colDefendants");
            columns[COL_OFFENCE] = ResourceBundleHelper.getResource(XhibitBundles.XhibitClientDefaultResources,"bc_colOffence");
            
            setColumnNames(columns);
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            // Read only so never returns anything
            String[] s = (String[]) getDataAt(rowIndex);
            String st = s[columnIndex];
            return st;
        }
    }
}
