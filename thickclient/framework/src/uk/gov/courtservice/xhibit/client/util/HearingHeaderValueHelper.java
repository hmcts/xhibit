package uk.gov.courtservice.xhibit.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.LegalRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT2 HearingHeaderValueHelper
 * </p>
 * <p>
 * Description: Provides 'normalized' access to defendants and their advocates
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class HearingHeaderValueHelper {
    private static final String resources = XhibitBundles.CounselFacilities;

    private Collection theLegalReps;

    private Collection theStaff;

    public HearingHeaderValueHelper(HearingHeaderValue hhv) {
        this.theLegalReps = hhv.getLegalRepValues();
        this.theStaff = hhv.getStaffValues();
    }

    /**
     * Get the defendants as PersonValues sorted by surname, first name and
     * middle name.
     * 
     * @return Collection of PersonValues that are defendants.
     */
    private Collection getDefendants() {
        // Get unique list of defendants using defendant id.
        final Hashtable hashTable = new Hashtable();
        for (Iterator iter = getTheLegalReps().iterator(); iter.hasNext();) {
            LegalRepValue item = (LegalRepValue) iter.next();
            if (item.getDefendant() != null) {
                if (item.getDefendant().getId() != null) {
                    hashTable.put(item.getDefendant().getId(), item.getDefendant());
                } else if (item.getDefendant().getFullName() != null) {
                    hashTable.put(item.getDefendant().getFullName(), item.getDefendant());
                }
            }
        }

        // Sort the defendants by surname, first name and middle name.
        final List defendants = new ArrayList(hashTable.values());
        Sorter.sort(defendants, new String[] { "surname", "firstName", "middleName" }, Sorter.ASCENDING);

        return defendants;
    }

    /**
     * Gets the defendant names.
     * 
     * @return Collection of Strings which are defendant full names.
     */
    public Collection getDefendantNames() {
        return getTheseNames(getDefendants().iterator());
    }

    private Collection getLegalRepresentativesByType(String type) {
        Hashtable hashTable = new Hashtable();
        Iterator iter = getTheLegalReps().iterator();
        while (iter.hasNext()) {
            LegalRepValue item = (LegalRepValue) iter.next();
            if (item.getLegalRep() != null) {
                if (type.equalsIgnoreCase(item.getLegalRep().getPersonType())) {
                    hashTable.put(item.getLegalRep().getId(), item.getLegalRep());
                }
            } else {
                // Special case for handling "In Person" and "Non-Attendance" sign-ins
                if (item.getSHLegRep() != null) {
                    if (item.getSHLegRep().getSolFirmOrRefLegalRep().equalsIgnoreCase("I")
                            && item.getSHLegRep().getLegalRole().equalsIgnoreCase(PersonValue.DEFENCE)
                            && type.equalsIgnoreCase(PersonValue.DEFENCE)) {
                        PersonValue pv = new PersonValue();
                        pv.setFullName(ResourceBundleHelper.getResource(resources, "dcdI"));
                        hashTable.put(new Integer(-1), pv);
                    } else if (item.getSHLegRep().getSolFirmOrRefLegalRep().equalsIgnoreCase("N")
                            && item.getSHLegRep().getLegalRole().equalsIgnoreCase(PersonValue.DEFENCE)
                            && type.equalsIgnoreCase(PersonValue.DEFENCE)) {
                        PersonValue pv = new PersonValue();
                        pv.setFullName(ResourceBundleHelper.getResource(resources, "dcdN"));
                        hashTable.put(new Integer(-1), pv);
                    }
                }
            }
        }
        return hashTable.values();
    }

    public Collection getLegalRepresentativesNamesByType(String type) {
        return getTheseNames(getLegalRepresentativesByType(type).iterator());
    }

    private Collection getStaff() {
        return getThesePersons(getTheStaff().iterator());
    }

    public Collection getStaffByType(String type) {
        return getThesePersonsByType(getStaff().iterator(), type);
    }

    public Collection getStaffNamesByType(String type) {
        return getTheseNames(getStaffByType(type).iterator());
    }

    // public Collection getProsecutorNames( ) {
    // Collection collection = new Vector( );
    // Iterator iter = getTheProsecutorValues( ).iterator();
    // while( iter.hasNext( ) ) {
    // PersonValue item = (PersonValue)iter.next( );
    //
    // collection.add( item.getFullName( ) );
    // }
    //
    // return collection;
    // }

    /**
     * Get the full names for the given iterator of PersonValues
     * 
     * @param iter
     *            Iterator for a Collection of PersonValues
     * @return Collection of Strings which are full names.
     */
    private Collection getTheseNames(Iterator iter) {
        Collection collection = new Vector();
        while (iter.hasNext()) {
            PersonValue item = (PersonValue) iter.next();
            if (item.getFullName() != null) {
                collection.add(item.getFullName());
            }
        }
        return collection;
    }

    private Collection getThesePersons(Iterator iter) {
        Collection collection = new Vector();
        while (iter.hasNext()) {
            collection.add((PersonValue) iter.next());
        }
        return collection;
    }

    private Collection getThesePersonsByType(Iterator iter, String type) {
        Collection collection = new Vector();
        while (iter.hasNext()) {
            PersonValue item = (PersonValue) iter.next();

            if (item != null && item.getPersonType() != null) {
                if (item.getPersonType().equals(type)) {
                    collection.add(item);
                    // XHIBITConstant.debug("HearingHeaderValueHelper -
                    // item.getPersonType() == "+item.getPersonType() + "
                    // and == " + type );
                } else {
                    // XHIBITConstant.debug("HearingHeaderValueHelper -
                    // item.getPersonType() == "+item.getPersonType() + "
                    // and != " + type );
                }
            } else {
                XHIBITConstant.debug("HearingHeaderValueHelper - item.getPersonType() == null");
            }
        }
        return collection;
    }

    private Collection getTheLegalReps() {
        return theLegalReps;
    }

    private Collection getTheStaff() {
        return theStaff;
    }
}
