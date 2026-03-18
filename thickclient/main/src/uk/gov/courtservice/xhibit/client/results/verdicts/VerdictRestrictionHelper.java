package uk.gov.courtservice.xhibit.client.results.verdicts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;

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
 * @author unascribed
 * @version 1.0
 */

public class VerdictRestrictionHelper {
    private static Collection verdictRefData;

    // Hashmap of verdicts that are allowed for specific plea types.
    // For example, a collection will be keyed on plea CPGJ and return
    // verdicts GJJ, GLJ and GAJ.
    private static HashMap restrictedVerdicts = new HashMap();

    // Hashmap to store verdict codes for validating when user inputs code
    // directly.
    private static HashMap restrictedVerdictCodes = new HashMap();

    private static VerdictRestrictionHelper _instance = null;

    private VerdictRestrictionHelper(Collection verdictRefData) {
        this.verdictRefData = verdictRefData;
    }

    public static VerdictRestrictionHelper getInstance(Collection verdictReferenceData) {
        if (_instance == null) {
            _instance = new VerdictRestrictionHelper(verdictReferenceData);
        } else {
            verdictRefData = verdictReferenceData;
        }
        return _instance;
    }

    public static VerdictRestrictionHelper getInstance() {
        if (_instance == null) {
            throw new IllegalStateException(
                    "Verdict Reference Data has not been set. getInstance(verdictRefData) must be called first.");
        }
        return _instance;
    }

    public void putRestrictedVerdictCodes(String pleaCode, String[] verdictCodeList) {
        restrictedVerdictCodes.put(pleaCode, verdictCodeList);
    }

    public String[] getRestrictedVerdictCodes(String pleaCode) {
        if (hasVerdictRestrictions(pleaCode)) {
            return (String[]) restrictedVerdictCodes.get(pleaCode);
        } else {
            return null;
        }
    }

    /**
     * Registers a verdict list allowed for a specific plea code.
     * 
     * @param pleaCode
     * @param verdictList
     */
    public void putRestrictedVerdictList(String pleaCode, Vector verdictList) {
        restrictedVerdicts.put(pleaCode, verdictList);
    }

    /**
     * Identifies if the plea code allows only a subset of verdicts
     * 
     * @param pleaCode
     * @return
     */
    public boolean hasVerdictRestrictions(String pleaCode) {
        return restrictedVerdicts.containsKey(pleaCode);
    }

    private Vector indVerdictList = null;

    /**
     * Returns a list of allowed verdicts for a plea code. If the plea code is
     * not registered, the full list of verdicts is returned.
     * 
     * @param pleaCode
     * @return
     */
    public Vector getRestrictedVerdictList(String pleaCode) {
        if (hasVerdictRestrictions(pleaCode)) {
            return (Vector) restrictedVerdicts.get(pleaCode);
        } else {
            if (indVerdictList == null) {
                buildIndictmentVerdictListforCombo();
            }
            return indVerdictList;
        }
    }

    private void buildIndictmentVerdictListforCombo() {
        RefSystemCodeBasicValue ref;
        String verdictDesc;
        indVerdictList = new Vector();
        Iterator i = verdictRefData.iterator();
        while (i.hasNext()) {
            ref = (RefSystemCodeBasicValue) i.next();
            verdictDesc = ref.getDecode();
            indVerdictList.add(verdictDesc);
        }
    }
}