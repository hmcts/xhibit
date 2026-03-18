package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: XHIBIT 2 - Defendant Mapper
 * </p>
 * <p>
 * Description: Holds mapping functionality for defendants.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */
public class DefendantMap extends Object implements java.io.Serializable {
    /** The index in the array that references the original defendant id. */
    protected final static int ORIGINAL = 0;

    /** The index in the array that references the alias defendant id. */
    protected final static int ALIAS = 1;

    /** The logger. */
    private final static Logger log = CSServices.getLogger(DefendantMap.class);

    /**
     * Contains a mapping of defendant ids and defendant value objects. KEY:
     * defendant Id VALUE: defendant value.
     */
    private HashMap idValueMap = null;

    /**
     * The vector of Integer arrays that map original defendant to alias
     * defendant, via their defendant Ids.
     * <P>
     * Integer arrays are of size two: Index 0: Original defendant Id Index 1:
     * Alias defendant Id
     */
    protected Vector originalAliasMap = null;

    /**
     * Contains alias defendantOnCaseId and its associated original
     * defendantOnCaseId. KEY: alias defendantOnCaseId. VALUE: original
     * defendantOnCaseId.
     */
    private HashMap defOnCaseIdsMap = null;

    /**
     * Contains all the aliases defendant ids.
     */
    private HashSet allAliasIds = null;

    /**
     * Default constructor, instantiates an empty map.
     */
    public DefendantMap() {
        init();
    }

    private void init() {
        this.originalAliasMap = new Vector();
        this.idValueMap = new HashMap();
        this.defOnCaseIdsMap = new HashMap();
        this.allAliasIds = new HashSet();
    }

    /**
     * Will determine if a defendant is an alias of an other defendant, from the
     * given id.
     * 
     * @param defendantId
     *            the id of the defendant for the alias check.
     * 
     * @return whether the defendant is an alias of another defendant.
     */
    public boolean isAlias(Integer defendantId) {
        Integer[] originalAlias;
        for (int i = 0; i < this.originalAliasMap.size(); i++) {
            originalAlias = (Integer[]) originalAliasMap.get(i);
            if (originalAlias[ALIAS].equals(defendantId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a pair of original/alias defendant ids, to the map.
     * 
     * @param originalDefValue
     *            the original defendant value
     * @param aliasDefValue
     *            the alias defendant value
     */
    public void addMap(DefendantValue originalDefValue, DefendantValue aliasDefValue) {
        Integer originalDefendantId = originalDefValue.getDefendantID();

        Integer[] defPairIds = new Integer[] { originalDefendantId, aliasDefValue.getDefendantID() };

        if (this.getOriginalId(originalDefendantId) == null) {
            this.originalAliasMap.add(defPairIds);
            this.addIdDefendantValueMap(originalDefendantId, originalDefValue);
        }

        // SG Start - not sure the folowing code is required.

        // Hashmap of caseid -- defOnCaseId pair must be sent.
        // The case id must be the id relating to original defendant.
        if (this.getOriginalValue(originalDefendantId) != null) {
            originalDefValue = this.getOriginalValue(originalDefendantId);
        }
        // SG End - not sure above code is required?

        Integer originalDefOnCaseId = originalDefValue.getDefOnCaseBasicValue().getId();
        Integer aliasDefOnCaseId = aliasDefValue.getDefOnCaseBasicValue().getId();
        log.debug("====> Adding alias id = " + aliasDefOnCaseId.toString() + " original id = "
                + originalDefOnCaseId.toString());
        defOnCaseIdsMap.put(aliasDefOnCaseId, originalDefOnCaseId);
    }

    /**
     * Add a map of original id and defendant value.
     * 
     * @param originalId
     *            the defendant original id.
     * @param defVal
     *            the defendant value for the id to be mapped on.
     */
    private void addIdDefendantValueMap(Integer originalId, DefendantValue defVal) {
        this.idValueMap.put(originalId, defVal);
    }

    /**
     * Will get the original defendant id of a given defendant Id. If the
     * defendant Id is an alias, then its corresponding original defendant id is
     * returned. Otherwise null is returned.
     * 
     * @param aliasId
     *            the id given to retrieve the original id
     * 
     * @return the original defendant Id.
     */
    public Integer getOriginalId(Integer aliasId) {
        Integer[] ids = null;
        for (int i = 0; i < originalAliasMap.size(); i++) {
            ids = (Integer[]) originalAliasMap.get(i);
            if (ids[ALIAS].equals(aliasId)) {
                return ids[ORIGINAL];
            }
        }
        return null;
    }

    /**
     * Will retrieve the original defendant value of the given defendant id. If
     * there is no original defendant value mapped to such an id, then null is
     * returned.
     * 
     * @param defendantId
     *            the id to which mapped defendant value will be returned.
     * 
     * @return the defendant value.
     */
    public DefendantValue getOriginalValue(Integer defendantId) {
        return (DefendantValue) idValueMap.get(defendantId);
    }

    /**
     * Gets the hashmap with keys of alias defendantOnCaseId and values of
     * original defendantOnCaseId.
     * 
     * @return the defendant on case id hashmap.
     */
    public HashMap getDefOnCaseIdsMap() {
        return defOnCaseIdsMap;
    }

    /**
     * Gets the hashset containing all alias defendant ids.
     * 
     * @return the all alias ids hashset.
     */
    public HashSet getAllAliasIds() {
        return allAliasIds;
    }
}