package uk.gov.courtservice.xhibit.client.util.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;

public class SeqNoHelper {

    private static final Logger log = CSServices.getLogger(SeqNoHelper.class);

    private static int nextSeqNo;

    private static int MAX_SEQ_NO = 999;
    
    public static void processSeqNoChange(JPanel panel, JTextField seqNoText, List seqNosList,
            DefendantOnOffenceComplexValue defOnOffComplexValue) {
        log.debug("processSeqNoChange START");

        if (seqNosList != null && seqNosList.size() > 0) {
            // here.
            log.debug("seqNosList > 0");
            nextSeqNo = Integer.parseInt(seqNosList.get(seqNosList.size() - 1).toString()) + 1;
            if (defOnOffComplexValue == null
                    || (defOnOffComplexValue != null && defOnOffComplexValue.getSeqNo() == null)) {
                if (nextSeqNo > MAX_SEQ_NO) {
                    JOptionPane.showMessageDialog(panel, getString("messageSeqNoMaxUsed"),
                            getString("messageValidateSeqNo"), JOptionPane.WARNING_MESSAGE);
                    seqNoText.requestFocus();
                } else {
                    seqNoText.setText(nextSeqNo + "");
                }
            }
        }else{
            log.debug("seqNosList null or 0 so default the seq no to 1");
            seqNoText.setText("1");
        }
        

        //    parent.stepUpdateViewState();
        log.debug("processSeqNoChange END");
    }

    /**
     * Validates sequence no. against existing 
     * 
     * @throws UserCancelException
     */
    public static void validateSeqNo(JPanel panel, JTextField seqNoText, List seqNosList,
            DefendantOnOffenceComplexValue defOnOffComplexValue) throws UserCancelException {
        log.debug("validateSeqNo START");
        if (seqNoText.getText() != null && seqNoText.getText().length() > 0) {
            Integer seqNo = new Integer(seqNoText.getText());

            //Check if Seq No is ZERO
            if (seqNo.equals(new Integer(0))) {
                JOptionPane.showMessageDialog(panel, getString("messageSeqNoZero"), getString("messageValidateSeqNo"),
                        JOptionPane.ERROR_MESSAGE);
                seqNoText.requestFocus();
                throw new UserCancelException();
            }

            //Ignore check if sequence no. equals existing sequence no.
            if (!(defOnOffComplexValue != null && (defOnOffComplexValue.getSeqNo() != null && defOnOffComplexValue
                    .getSeqNo().equals(seqNo)))) {

                log.debug("Adding or amending details to an existing defendant.");

                if (seqNosList != null && seqNosList.size() > 0) {

                    // Check if Seq No Already exists
                    if (Collections.binarySearch(seqNosList, seqNo) >= 0) {
                        if (defOnOffComplexValue == null || defOnOffComplexValue.getSeqNo() == null) {
                            if(nextSeqNo > MAX_SEQ_NO){
                                JOptionPane.showMessageDialog(panel, getString("messageSeqNoExistsMaxUsed"), getString("messageValidateSeqNo"),
                                        JOptionPane.WARNING_MESSAGE);
                                seqNoText.setText("");
                                seqNoText.requestFocus();
                                throw new UserCancelException();
                            }else{
                                Object[] options = { "OK", "Cancel" };
                                int reply = JOptionPane.showOptionDialog(panel, getString("messageSeqNoExists1") + " "
                                        + nextSeqNo + ".\n" + getString("messageSeqNoExists2"),
                                        getString("messageValidateSeqNo"), JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    
                                seqNoText.requestFocus();
    
                                log.debug("reply: " + reply);
    
                                if (reply == JOptionPane.OK_OPTION) {
                                    log.debug("reply is OK");
                                    seqNoText.setText(nextSeqNo + "");
                                } else {
                                    log.debug("reply is Cancelled");
                                    throw new UserCancelException();
                                }
                            }
                        } else {
                            //Check if maximum reached for nextSeqno.
                            if (nextSeqNo > MAX_SEQ_NO){ //Max Seq No reached
                                JOptionPane.showMessageDialog(panel, getString("messageSeqNoExistsMaxUsed"), getString("messageValidateSeqNo"),
                                        JOptionPane.WARNING_MESSAGE);
                                
                                seqNoText.setText("");
                                seqNoText.requestFocus();
                                throw new UserCancelException();
                            }else{ //Max Seq No not reached
                                Object[] options = { "OK", "Cancel" };
                                int reply = JOptionPane.showOptionDialog(panel, getString("messageSeqNoExists1") + " "
                                        + nextSeqNo + ".\n" + getString("messageSeqNoExists3"),
                                        getString("messageValidateSeqNo"), JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    
                                seqNoText.requestFocus();
    
                                if (reply == JOptionPane.OK_OPTION) {
                                    seqNoText.setText(nextSeqNo + "");
                                } else {
                                    seqNoText.setText(defOnOffComplexValue.getSeqNo() + "");
                                    seqNoText.requestFocus();
                                    throw new UserCancelException();
                                }
                            }
                        }
                    }
                }
            } else {
                log.debug("Sequence No. equals existing sequence no.");
            }
        }
        log.debug("validateSeqNo END");
    }

    
    /**
     * Creates a hashmap with an emppty list for each defendant in ccv
     * 
     * @param ccv
     * @return
     */
    public static HashMap<Integer,List> createSequenceNosMap(Collection defendants){
        log.debug("createSequenceNosMap START");
        // Create HashMap
       HashMap<Integer,List> defOnCaseSeqNosMap = new HashMap<Integer,List>();
        Iterator defIter = defendants.iterator();
        while (defIter.hasNext()){
            //For each defendant, create a new list and add it to the hashmap using id as hash
            DefendantValue defendantValue = (DefendantValue)defIter.next();
            log.debug("DOC ID: " + defendantValue.getDefOnCaseBasicValue().getId());
            defOnCaseSeqNosMap.put(defendantValue.getDefOnCaseBasicValue().getId(), new ArrayList());
        }
        log.debug("createSequenceNosMap END");
        
        return defOnCaseSeqNosMap;
    }
    
    /**
     * Processes a charge value. Constructs the list of sequence numbers for defendants on offence for all offences in charge. 
     * Places the list in the relevant position in HashMap.
     * 
     * @param chargeValue
     * @param defOnCaseSeqNosMap
     */
    public static void processSequenceNos(ChargeValue chargeValue,HashMap<Integer,List>defOnCaseSeqNosMap){

    
        log.debug("processSequenceNos START");
        if(chargeValue==null)
        {
            log.debug("processSequenceNos: Charge is null so return");
            return;
        }
        log.debug("ChargeValue ID: "+ chargeValue.getChargeID());
        log.debug("ChargeValue Type: "+ chargeValue.getChargeType());
        
        if(chargeValue.getOffenceValues()==null)
        {
            log.debug("processSequenceNos: Charge has no offences so return");
            return;
        }
        
        Iterator offenceIter = chargeValue.getOffenceValues().iterator();
        
        //defOnCaseSeqNosMap can't be null as it is created as an empty hashMap
        log.debug("defOnCaseSeqNosMap.size: "+ defOnCaseSeqNosMap.size());
        
        while (offenceIter.hasNext()){
            HashMap defOnOffMap = ((OffenceValue)offenceIter.next()).getDefOnOffenceBasicValues();
            Iterator defOnOffIter = defOnOffMap.values().iterator(); 
            while (defOnOffIter.hasNext()){
                DefendantOnOffenceComplexValue dofValue = ((DefendantOnOffenceComplexValue)defOnOffIter.next());
                Integer docId = dofValue.getDefendantOnCaseId();
                log.debug("docId: "+ docId);
                if (dofValue.getSeqNo() != null){
                    List sequenceNos = (List)defOnCaseSeqNosMap.get(docId);
                    log.debug("sequenceNo: "+ dofValue.getSeqNo());
                    //bug fix - the list returned can be null especially regarding Joinders 
                    if(sequenceNos==null)
                    {
                        log.warn("sequenceNos array list is null for docId: " + docId);
                        sequenceNos=new ArrayList();
                    }
                    else
                    {
                        log.debug("sequenceNos not null and is of size: "+ sequenceNos.size() + " for docId: " + docId);
                    }
                    sequenceNos.add(dofValue.getSeqNo());
                    //Add new entry / Replace the existing entry with the updated list
                    defOnCaseSeqNosMap.put(docId, sequenceNos);
                }
            }
        }
        //Set Map to model so is publicly available
        //model.setDefendantsOnCaseSeqNosMap(defOnCaseSeqNosMap);
        log.debug("processSequenceNos END");
    }
    

    /**
     * Takes a hashmap and iterates through all lists, returning a boolean value which relates to whether the list is sequential.
     * i.e. there are no 'gaps' and no duplicates. Empty Lists are ok.
     * @param defOnCaseSeqNosMap
     * @return
     */
    public static boolean areListsSequential(HashMap<Integer,List> defOnCaseSeqNosMap){ 
        
        if(defOnCaseSeqNosMap!=null && defOnCaseSeqNosMap.size() > 0){
            Collection c = defOnCaseSeqNosMap.values();
            Integer intChecker = new Integer(1);
            Iterator it = c.iterator();
            
            while (it.hasNext()){
                List seqNosList = (List)it.next();
                
                if(seqNosList.size() > 0){
                    Collections.sort(seqNosList);
                    intChecker = 1;
                    Iterator listIt = seqNosList.iterator();
                    while (listIt.hasNext()){
                        if (!intChecker.equals(listIt.next())){
                            return false;
                        }
                        intChecker++;
                    }
                }
            
            }
        }        
        return true;
    }
    
    /**
     * Takes a collection of charges and a collection of defendants and constructs the sequence number hashmap
     * @param Collection - Charges
     * @param Collection - Defendants
     */
    public static HashMap<Integer,List> constructSequenceNumberMap(Collection charges, Collection defendants){
        HashMap<Integer,List> defOnCaseSeqNosMap = SeqNoHelper.createSequenceNosMap(defendants);
        
        if( charges == null )
            return  null;
                
        Iterator it = charges.iterator();        
    
        //Create list of indictments
        List indictmentsList = new ArrayList<ChargeValue>();
        while(it.hasNext()){
            ChargeValue charge = (ChargeValue)it.next();
            if(charge.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())){
                indictmentsList.add(charge);
            }
        }
        it = charges.iterator();
        
        
        while(it.hasNext()){
            ChargeValue chargeValue = (ChargeValue) it.next();
            if(chargeToBeAdded(chargeValue,indictmentsList)){
                //only process the sequence number if the charge is not of type 'Original Charge' or 
                //there are no indictments for this deft.
                SeqNoHelper.processSequenceNos(chargeValue,defOnCaseSeqNosMap);
            }
        }
        
        return defOnCaseSeqNosMap;

    }
    
    /**
     * Given a chargeValue, and a list of indictment charges. if the given chargevalue is an original charge,
     * this method works out whether or not the original charge should be addeed to the relevant list.
     * Basically this method should always return true unless there is an indictment which contains the
     * same deft. as the given charge. If this is the case then original charges should be ignored as per MOJ requirements
     * 
     * @param chargeValue
     * @param indictmentsList
     * @return
     */
    private static boolean chargeToBeAdded(ChargeValue chargeValue,List <ChargeValue> indictmentsList){
        if(isOriginalCharge(chargeValue)){
            Collection offences = chargeValue.getOffenceValues();
            if(offences==null)
                log.error("No offences found for charge");
            else{
                if(offences.size() == 1){
                    OffenceValue offence = (OffenceValue)offences.iterator().next();
                    if(offence!=null){
                        return !hasIndictments((DefendantValue)offence.getDefendantValues().iterator().next(),indictmentsList);
                    }else
                        log.error("Offence value for original charge is null");
                    
                }else
                    log.error("Should only be one offence for original charge");
                
            }
        }
        return true;
    }
    
    /**Searches all indictments on case to see if the given defendant has an indictment
     * 
     * @param def - DefendantValue
     * @return
     */
    private static boolean hasIndictments(DefendantValue def,List <ChargeValue> indictmentsList){
        if (def == null)
            return false;
            
        Integer defOnCaseID = def.getDefOnCaseBasicValue().getDefendantID();
        
        Iterator indIt = indictmentsList.iterator();
        while(indIt.hasNext()){
            //For every indictment on the case
            ChargeValue indictmentCharge = (ChargeValue) indIt.next();
            Collection offences = indictmentCharge.getOffenceValues();
            Iterator offenceIt = offences.iterator();
            while(offenceIt.hasNext()){
                //For every count within the indictment
                OffenceValue offence = (OffenceValue)offenceIt.next();
                Collection defendants = offence.getDefendantValues();
                Iterator defIt = defendants.iterator();
                while(defIt.hasNext()){
                    //For every defendant on the count
                    DefendantValue indDef = (DefendantValue)defIt.next();
                    if(indDef.getDefOnCaseBasicValue().getDefendantID().equals(defOnCaseID)){
                        //The defendant matches the defendant on the count so return true
                        return true;
                    }
                }
            }
        }
        
        //No indictments found for this defendant so return false
        return false;
    
    }
    
    private static boolean isOriginalCharge(ChargeValue chargeValue){
        return chargeValue.getChargeType().equals(ChargeTypes.ORIGINAL_CHARGE.getChargeType());
    }
    
    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private static String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
    }
    
    public static void printSeqNosListsInMap(HashMap<Integer, List> defOnCaseSeqNosMap){
        log.debug("printSeqNosListsInMap START");
        Iterator mapKeyIter = defOnCaseSeqNosMap.keySet().iterator();
        while (mapKeyIter.hasNext()){
            Object key =  mapKeyIter.next();
            log.debug("Key: "+key);
            List seqNoList = (List)defOnCaseSeqNosMap.get(key);
            Collections.sort(seqNoList);
            if (seqNoList.size() > 0){
            Iterator listIter = seqNoList.iterator();
            while (listIter.hasNext()){
                log.debug("**** ITEM IN LIST ****");
                log.debug("List item: " + listIter.next());
            }
            }else{
                log.debug("NO ITEMS IN LIST");
            }
        }
        log.debug("printSeqNosListsInMap END");
    }
}
