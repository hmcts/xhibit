package uk.gov.courtservice.xhibit.business.database.results;

// JDK
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.query.LatestRefDisposalLineQuery;
import uk.gov.courtservice.xhibit.business.database.results.query.LatestRefDisposalTypeQuery;
import uk.gov.courtservice.xhibit.business.database.results.query.RefDisposalLineQuery;
import uk.gov.courtservice.xhibit.business.database.results.query.RefDisposalMenuQuery;
import uk.gov.courtservice.xhibit.business.database.results.query.RefDisposalTypeQuery;
import uk.gov.courtservice.xhibit.business.database.results.query.SortedRefDisposalLineQuery;
import uk.gov.courtservice.xhibit.business.database.results.query.SortedRefDisposalTypeQuery;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalMenuReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: ResultsDatabase
 * </p>
 * <p>
 * Description: The facade interface for fast lane readers.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 * @version 1.0
 */
public class ResultsDatabase {
    /**
     * The logger
     */
    private static final Logger log = CSServices.getLogger(ResultsDatabase.class);
    static {
        // Logging is very verbose, so switched off here
        log.setLevel(Level.INFO);
    }

    /**
     * Stop unnecessary production of this static class
     */
    private ResultsDatabase() {
    }

    /**
     * Return the root menu, this requires a lot of processing to link the
     * mapped data.
     * 
     * @param menuGroup
     *            the menu group to retrive
     * @param courtId
     *            the court to retrive
     * @return the root menu
     * @throws ResultsDatabaseException
     *             if the root can not be found, multiple roots are found or any
     *             parent nodes are missing
     */
    public static DisposalMenuReferenceValue getReferenceDisposalMenuRoot(Integer courtId, String menuGroup)
            throws ResultsDatabaseException {

        Map menuMap = new RefDisposalMenuQuery().getMenuMap(courtId, menuGroup);

        DisposalMenuReferenceValue rootValue = null;

        // Iterate over the menuItems looking for the root and linking parents
        // to children and children to parents.
        Iterator menuItems = menuMap.values().iterator();
        while (menuItems.hasNext()) {
            DisposalMenuReferenceValue currentValue = (DisposalMenuReferenceValue) menuItems.next();
            if (currentValue.isRoot()) {
                if (rootValue == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Disposal Menu: Setting root to " + currentValue.toDebug() + ".");
                    }
                    rootValue = currentValue;
                } else {
                    throw new ResultsDatabaseException("Multiple roots " + rootValue.toDebug() + " and "
                            + currentValue.toDebug() + " in root disposal menu data.");
                }
            } else {
                DisposalMenuReferenceValue parentValue = (DisposalMenuReferenceValue) menuMap.get(new Integer(
                        currentValue.getParent()));
                if (parentValue != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Disposal Menu: Linking child " + currentValue.toDebug() + " to parent "
                                + parentValue.toDebug() + ".");
                    }
                    parentValue.addChildValue(currentValue);
                    currentValue.setParentValue(parentValue);
                } else {
                    throw new ResultsDatabaseException("Could not find parent for disposal menu "
                            + currentValue.toDebug() + ".");
                }
            }
        }

        // Return if we have found the root
        if (rootValue != null) {
            return rootValue;
        } else {
            throw new ResultsDatabaseException("Could not find the root disposal menu data.");
        }
    }

    /**
     * Get the latest disposal with the specifed code
     * 
     * @param courtId
     *            the court to retrive
     * @param disposalCode
     *            the code to retrieve
     * @throws ResultsDatabaseException
     *             if the disposal can not be found
     */
    public static DisposalReferenceValue getLatestReferenceDisposal(Integer courtId, String disposalCode) {
        DisposalReferenceValue disposal = new LatestRefDisposalTypeQuery().getType(courtId, disposalCode);
        if (disposal == null) {
            throw new ResultsDatabaseException("Could not find latest disposal " + disposalCode + " in court "
                    + courtId + ".");
        }

        List disposalLineList = new LatestRefDisposalLineQuery().getLineList(courtId, disposalCode);
        for (int i = 0, s = disposalLineList.size(); i < s; i++) {
            disposal.addLine((DisposalLineReferenceValue) disposalLineList.get(i));
        }

        return disposal;
    }

    /**
     * Get the disposal with the specifed type id
     * 
     * @param refDisposalTypeId
     *            the disposal to retrieve
     * @throws ResultsDatabaseException
     *             if the disposal can not be found
     */
    public static DisposalReferenceValue getReferenceDisposal(Integer refDisposalTypeId) {
        DisposalReferenceValue disposal = new RefDisposalTypeQuery().getType(refDisposalTypeId);
        if (disposal == null) {
            throw new ResultsDatabaseException("Could not find disposal with refDisposalTypeId " + refDisposalTypeId
                    + ".");
        }

        List disposalLineList = new RefDisposalLineQuery().getLineList(refDisposalTypeId);
        for (int i = 0, s = disposalLineList.size(); i < s; i++) {
            disposal.addLine((DisposalLineReferenceValue) disposalLineList.get(i));
        }

        return disposal;
    }

    /**
     * Retrieve an array of disposals for the specified menu group and court
     * 
     * @param courtId
     *            the court to retrive
     * @param menuGroup
     *            the menu group (disposal type) to retrive
     * @throws ResultsDatabaseException
     *             if the disposal type can not be found for a given line
     */
    public static DisposalReferenceValue[] getReferenceDisposals(Integer courtId, String menuGroup)
            throws ResultsDatabaseException {
        List disposalList = new SortedRefDisposalTypeQuery().getSortedTypeList(courtId, menuGroup);
        List disposalLineList = new SortedRefDisposalLineQuery().getSortedLineList(courtId, menuGroup);

        DisposalReferenceValue[] disposals = new DisposalReferenceValue[disposalList.size()];

        // Assume both lists are sorted by ref_disposal_type_id
        int lineIndex = 0;
        int lineSize = disposalLineList.size();

        for (int index = 0, size = disposalList.size(); index < size; index++) {
            DisposalReferenceValue disposal = (DisposalReferenceValue) disposalList.get(index);
            if (log.isDebugEnabled()) {
                log.debug("Disposal: Finding disposal lines for " + disposal + ".");
            }
            int refDisposalTypeId = disposal.getRefDisposalTypeId();
            while (lineIndex < lineSize) {
                DisposalLineReferenceValue line = (DisposalLineReferenceValue) disposalLineList.get(lineIndex);
                if (log.isDebugEnabled()) {
                    log.debug("Disposal: Finding disposal for " + line + ".");
                }
                if (refDisposalTypeId == line.getRefDisposalTypeId()) {
                    disposal.addLine(line);
                    lineIndex++;
                } else {
                    break;
                }
            }
            disposals[index] = disposal;
        }

        // Check we have completly processed the line list (All records should
        // have been assigned to a disposal)
        if (lineIndex == lineSize) {
            return disposals;
        } else {
            throw new ResultsDatabaseException("Could not find disposal for line " + disposalLineList.get(lineIndex));
        }
    }
}
