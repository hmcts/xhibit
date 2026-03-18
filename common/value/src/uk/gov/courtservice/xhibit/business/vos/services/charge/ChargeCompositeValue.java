package uk.gov.courtservice.xhibit.business.vos.services.charge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffenceBasicValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: ChargeCompositeValue
 * </p>
 * <p>
 * Description: ChargeCompositeValue is intended to hold the case value object
 * and the charge value objects for a given case.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Laurent Bossard
 * @version 1.0
 */
public class ChargeCompositeValue extends CSAbstractValue {

    private static final long serialVersionUID = 1L;

    private XhbCaseBasicValue caseBasicValue;

    // A collection of CourtLogViewValue objects
    private CourtLogViewValue[] chargeLogEvents;

    // A Collection of ChargeValue objects. It contains all the ChargeValue
    // objects for a case.
    private Collection charges;

    // A Collection of DefendantValue object. Contains all defendants on the
    // case
    private Collection<DefendantValue> allDefendants;

    public ChargeCompositeValue() {
        // emtpy
    }

    /**
     *
     * @param chargeLogEvents
     *            Array of ChargeLogViewValue objects
     */
    public void setChargeLogItems(CourtLogViewValue[] chargeLogEvents) {
        this.chargeLogEvents = chargeLogEvents;
    }

    /**
     *
     * @return Array of CourtLogViewValue objects
     */
    public CourtLogViewValue[] getChargeLogItems() {
        return this.chargeLogEvents;
    }

    /**
     * @param XhbCaseBasicValue
     *            caseBasicValue
     * @param Collection
     *            charges
     * @roseuid 3DBD13D601E5
     */
    public ChargeCompositeValue(XhbCaseBasicValue caseBasicValue, Collection charges) {
        this.caseBasicValue = caseBasicValue;
        this.charges = charges;
    }

    /**
     * This method returns a case basic value object
     *
     * @return XhbCaseBasicValue
     */
    public XhbCaseBasicValue getCaseBasicValue() {
        return caseBasicValue;
    }

    /**
     * This method returns a collection of offence value objects
     *
     * @param chargeID
     * @return java.util.Collection
     * @roseuid 3DBD14740337
     */
    public Collection getOffenceValues(Integer chargeID) {

        /**
         * Loop through all the ChargeValue objects contained in charges to find
         * the one specified by the chargeID Return the collection of
         * OffenceValue objects contained in this ChargeValue
         */

        if (charges.size() > 0) {
            Iterator chargesIterator = null;
            ChargeValue chargeValue = null;
            chargesIterator = charges.iterator();

            while (chargesIterator.hasNext()) {
                chargeValue = (ChargeValue) chargesIterator.next();
                if (chargeValue.getChargeID().intValue() == chargeID.intValue()) {
                    break;
                }
            }
            return chargeValue.getOffenceValues();
        } else
            return null;
    }
    
    
    /**
     * 
     * @param defendant
     * @param validOffences
     * @return
     */
    public Collection getDefOnOffenceIDForDef(Integer defendant, ArrayList<Integer>validOffences) {
    	ArrayList<OffenceValue> offences = new ArrayList<OffenceValue>(getOffenceValuesForDefendant(defendant));
    	ArrayList<Integer> defOnOffenceID = new ArrayList();
    	
    	for(OffenceValue off: offences) {
    		if(!validOffences.contains(off.getOffenceID())) {
	    		defOnOffenceID.add(off.getDefendantOnOffence(defendant).getDefendantOnOffenceId());
    		}
    	}
    	
    	return defOnOffenceID;
    }
    
    /**
     * Gets offences from any offence with the specified defendant
     * @param defendant
     * @return
     */
    public Collection getOffenceValuesForDefendant(Integer defendant) {
    	ArrayList<OffenceValue> offences = new ArrayList();
    	
    	ArrayList<ChargeValue>charges = (ArrayList<ChargeValue>) getChargesForDefendant(defendant);
    	
    	for(ChargeValue charge : charges) {
    		ArrayList<OffenceValue>chrgOffences = new ArrayList<OffenceValue>(charge.getOffenceValues());
    		offences.addAll(chrgOffences);
    	}
    	
    	return offences;
    	
    }
    
    /**
     * Gets a single defendant on offence where the defendant on offence matches the one specified
     * @param id
     */
    public DefendantOnOffenceComplexValue getDefendantOnOffence(Integer defendantOffID, Integer defendant) {
    	ArrayList<OffenceValue> offences = new ArrayList<OffenceValue>(getOffenceValuesForDefendant(defendant));
    	ArrayList<Integer> defOnOffenceID = new ArrayList();
    	
    	for(OffenceValue off: offences) {
    		try {
    			DefendantOnOffenceComplexValue defOnOff = off.getDefendantOnOffence(defendant);
    			if(defOnOff!=null) {
    				if(defOnOff.getDefendantOnOffenceId().intValue() == defendantOffID.intValue()) {
						return defOnOff;
					}
    			}
    		} catch(Exception e) {
    			return null;
    		}

    	}
    	return null;
    }
    
    /**
     * Gets all charges for a charge that contains the defendantID
     * 
     * @param defendantID
     * @return
     */
    public Collection getChargesForDefendant(Integer defendantID) {
    	 
    	ArrayList<ChargeValue> retCharges = new ArrayList();

        if (charges.size() > 0) {
            Iterator chargesIterator = null;
            ChargeValue chargeValue = null;
            chargesIterator = charges.iterator();

            while (chargesIterator.hasNext()) {
                chargeValue = (ChargeValue) chargesIterator.next();
                if(chargeValue.getOffenceValues()!=null) {
	   
	                Iterator iter = chargeValue.getOffenceValues().iterator();
	        		while (iter.hasNext()) {
	        			OffenceValue offence  = (OffenceValue) iter.next();
	                	
		                if(offence!=null) {
			                if(offence.getDefendantIDs().contains(defendantID)) {
			                	retCharges.add(chargeValue);
			                	break; // No need to continue this loop now we've found, and also avoids adding duplicates
			                }
		                }
	                }
                }
            }
            
        } else {
            return null;
        }
        
        return retCharges;
    
    }

    /**
     * This method returns a collection of charge value objects
     *
     * @return java.util.Collection
     * @roseuid 3DBD149B02C5
     */
    public Collection getCharges() {
        return charges;
    }

    /**
     * This method returns a collection of defendant value objects for a given
     * offence id
     *
     * @param offenceID
     * @return java.util.Collection
     * @roseuid 3DBD14B70198
     */
    public Collection getDefendants(Integer offenceID) {

        /**
         * Loop through all the OffenceValue objects contained in the
         * ChargeValue objects (which are contained in charges) Find the
         * OffenceValue object specified by the offenceID passed as parameter
         * Return the collection of DefendantValue objects contained in this
         * OffenceValue
         */

        if (charges.size() > 0) {
            Iterator chargesIterator = null;
            ChargeValue chargeValue = null;
            OffenceValue offValue = null;

            chargesIterator = charges.iterator();

            outer: while (chargesIterator.hasNext()) {
                chargeValue = (ChargeValue) chargesIterator.next();

                Collection offences = chargeValue.getOffenceValues();
                if (offences.size() > 0) {
                    Iterator offenceIter = offences.iterator();

                    while (offenceIter.hasNext()) {
                        offValue = (OffenceValue) offenceIter.next();

                        if (offValue.getOffenceID().intValue() == offenceID.intValue()) {
                            break outer;
                        }
                    }
                }
            }
            return offValue.getDefendantValues();
        } else
            return null;
    }
    
    
    /**
     * 
     * @param defendant
     * @return
     */
    public Collection getCurrentOffencesForDefendant(Integer defendantID) {
    	ArrayList<OffenceValue> offences = new ArrayList();
    	
    	ArrayList<ChargeValue>charges = (ArrayList<ChargeValue>) getChargesForDefendant(defendantID);
    	if (charges != null) {
	    	for(ChargeValue charge : charges) {
	    		
	    		ArrayList<OffenceValue>chrgOffences = new ArrayList<OffenceValue>(charge.getOffenceValues());
	    		if(offences.isEmpty()) {
	    			offences.addAll(chrgOffences);
	    		} else {
	    			if(!offences.containsAll(chrgOffences)) {
	    				offences.addAll(chrgOffences);
					}
	    		}
	    	}
    	}
    	
    	return offences;
    }

    /**
     * This method returns a collection of defendant value objects corresponding
     * to all the defendant for the case.
     *
     * @return java.util.Collection
     */
    public Collection<DefendantValue> getAllDefendants() {
        return allDefendants;
    }

    public void setAllDefendants(Collection<DefendantValue> allDefendants) {
        this.allDefendants = allDefendants;
    }
}
