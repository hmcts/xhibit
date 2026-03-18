package uk.gov.courtservice.xhibit.business.services.hearingschedule.schedule;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingType;
import uk.gov.courtservice.xhibit.business.entities.refhearingtype.RefHearingTypeMaintainer;

/**
 * <p>
 * Title: HearingTypeHelper
 * </p>
 * <p>
 * Description: Determines the appropriate hearing type to use for Add Hearing
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

public class HearingTypeHelper {
    private static final Logger log = CSServices.getLogger(HearingTypeHelper.class);

    private final Vector partHeardHearingTypes;

    private final Vector hearingTypeGroups;

    private final HearingMaintainer hearingMaintainer;

    private final RefHearingTypeMaintainer refHearingTypeMaintainer;

    public HearingTypeHelper() {
        hearingMaintainer = new HearingMaintainer();
        refHearingTypeMaintainer = new RefHearingTypeMaintainer();

        partHeardHearingTypes = new Vector();
        hearingTypeGroups = new Vector();
        Properties props = CSServices.getConfigServices().getProperties("addHearing");

        // Get the part heard hearing types list from the properties file
        String partHeardHearingTypeList = props.getProperty("partHeardHearingTypes");

        // Get each part heard hearing type
        StringTokenizer partHeardLOV = new StringTokenizer(partHeardHearingTypeList, ",");
        while (partHeardLOV.hasMoreTokens()) {
            String partHeardToken = (String) partHeardLOV.nextToken();

            // Save it in a Vector
            partHeardHearingTypes.add(partHeardToken);

            // Get the coresponding parent group list from the properties
            // file
            String parentHearingTypeList = props.getProperty("hearingTypeGroup." + partHeardToken);

            // Get each parent hearing type
            Vector temp = new Vector();
            StringTokenizer groupLOV = new StringTokenizer(parentHearingTypeList, ",");
            while (groupLOV.hasMoreTokens()) {
                String groupToken = (String) groupLOV.nextToken();

                // Save it in a temporary Vector
                temp.add(groupToken);
            }

            // Save the temporary Vector in a Vector of hearing type groups
            hearingTypeGroups.add(temp);
        }
    }

    /**
     * Determines whether or not one of the existing hearings registered for the
     * case can be used rather than create a new one.
     * 
     * @param caseId
     * @param refHearingTypeId
     * @return the reuslt of matching the selected hearing type against existing
     *         hearings for the case be created
     */
    public HearingTypeMatchResult getHearingRule(Integer caseId, Integer selectedRefHearingTypeId) {
        log.debug("***getHearingRule STARTED using caseId/refHearingTypeId:" + caseId + "/" + selectedRefHearingTypeId);
        Collection openHearings = new Vector();
        Iterator iter;

        HearingTypeMatchResult result = new HearingTypeMatchResult();
        result.setMatchRule(HearingTypeMatchResult.NO_MATCH);
        try {
            // Get details of the selected hearing type
            RefHearingType selectedRefHearingType = refHearingTypeMaintainer.findByPrimaryKey(selectedRefHearingTypeId);
            String selectedRefHearingTypeCode = selectedRefHearingType.getHearingTypeCode();

            // Obtain all the hearings for the case
            Collection hearingsForCase = hearingMaintainer.findByCaseId(caseId);

            // Save the "open" hearings
            iter = hearingsForCase.iterator();
            while (iter.hasNext()) {
                Hearing temp = (Hearing) iter.next();
                if (isHearingOpen(temp)) {
                    openHearings.add(temp);
                }
            }

            // Sort in descending start date order, i.e. most recent hearing
            // first
            Sorter.sort((List) openHearings, new String[] { "hearingStartDate" }, Sorter.DESCENDING);

            // Get the hearing type group to which the selected hearing type
            // belongs
            Vector fullGroup = getFullGroup(selectedRefHearingTypeCode);

            // If the selected hearing type belongs to one of the 'special'
            // groups,
            // then try to match it against one of the existing hearings
            // usings the
            // match rules
            if (fullGroup != null) {
                // RULE_2: the selected hearing type is part of a 'special'
                // group
                // but there are no open hearings for the case. This is set as
                // the
                // default
                result.setMatchRule(HearingTypeMatchResult.MATCH_RULE_2);
                iter = openHearings.iterator();
                while (iter.hasNext()) {
                    Hearing hearing = (Hearing) iter.next();
                    log.debug("HearingID/RefHearingTypeID:" + hearing.getHearingId() + "/"
                            + hearing.getRefHearingTypeId());

                    RefHearingType refHearingType = refHearingTypeMaintainer.findByPrimaryKey(hearing
                            .getRefHearingTypeId());

                    if (fullGroup.contains(refHearingType.getHearingTypeCode())
                            && !partHeardHearingTypes.contains(refHearingType.getHearingTypeCode())) {
                        // RULE_1: the current hearing type is in the same
                        // 'special'
                        // group as the selected hearing but it is not one of
                        // the
                        // part heard hearing types, i.e. APH, CSP, PPH or TPH
                        result.setMatchRule(HearingTypeMatchResult.MATCH_RULE_1);
                        result.setHearing(hearing);
                        break;
                    } else if (partHeardHearingTypes.contains(refHearingType.getHearingTypeCode())
                            && !partHeardHearingTypes.contains(selectedRefHearingTypeCode)) {
                        // RULE_3: the current hearing type is one of the part
                        // heard
                        // hearing types but the selected hearing type is not
                        result.setMatchRule(HearingTypeMatchResult.MATCH_RULE_3);
                        break;
                    } else if (partHeardHearingTypes.contains(refHearingType.getHearingTypeCode())
                            && selectedRefHearingTypeCode.equalsIgnoreCase(refHearingType.getHearingTypeCode())) {
                        // RULE_4: the current hearing type is one of the part
                        // heard
                        // hearing types and is the same as the selected hearing
                        // type
                        result.setMatchRule(HearingTypeMatchResult.MATCH_RULE_4);
                        result.setHearing(hearing);
                        break;
                    } else {
                        continue;
                    }
                }
            }
        } catch (ObjectNotFoundException onfe) {
            // No value found, ignore this exception
        } finally {
            // Return the saved Hearing( this could be NULL if no match was
            // found )
            log.debug("Match rule " + result.getMatchRule());
            if (result.getHearing() != null) {
                log.debug("hearingId:" + result.getHearing().getHearingId());
            }
            log.debug("***getHearingRule ENDED!");
            return result;
        }
    }

    /**
     * Determines whether or not if the hearing is 'open'
     * 
     * @param hearing
     * @return true if it considered open - there is no end date set
     */
    private boolean isHearingOpen(Hearing hearing) {
        return (hearing.getHearingEndDate() == null);
    }

    /**
     * Determine whether or not the selected hearing type is in one of the
     * 'special' groups.
     * 
     * @param selectedHearingType
     * @return Vector containing the 'special' group in which the hearing type
     *         belongs
     */
    private Vector getFullGroup(String selectedHearingType) {
        log.debug("getFullGroup.selectedHearingType.start:" + selectedHearingType);
        Vector returnValue = null;
        Iterator iter = hearingTypeGroups.iterator();
        while (iter.hasNext()) {
            Vector temp = (Vector) iter.next();
            if (temp.contains(selectedHearingType)) {
                returnValue = temp;
                ;
            }
        }

        if (returnValue == null) {
            log.debug("selectedHearingType not in a group!");
        } else {
            log.debug("selectedHearingType is in a group");
            if (log.isDebugEnabled()) {
                iter = returnValue.iterator();
                while (iter.hasNext()) {
                    log.debug("item:" + (String) iter.next());
                }
            }
        }
        log.debug("getFullGroup.selectedHearingType.end:");

        return returnValue;
    }
}
