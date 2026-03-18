package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Counsel;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DefendantSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DefendantSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendants;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Name;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;

/**
 * <p>
 * Title: DefendantHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating Defendant and Defendant Summary
 * XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DefendantHelper.java,v 1.4 2007/09/21 17:00:26 qz4rwx Exp $
 */
public class DefendantHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(DefendantHelper.class);

    /**
     * Create a summary for the detailed defendant being represented by the
     * solicitor
     * 
     * @param defendant
     *            the defendant to create the summary for
     * @return the summary
     */
    public static DefendantSummary createSummary(Defendant defendant, Solicitor solicitor) {
        DefendantSummary defendantSummary = _createSummary(defendant);
        defendantSummary.setSolicitorRef(getSolicitorRef(defendant, solicitor));

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(defendantSummary) + ".");
        }

        return defendantSummary;
    }

    /**
     * Create a summary for the detailed defendant
     * 
     * @param defendant
     *            the defendant to create the summary for
     * @return the summary
     */
    public static DefendantSummary createSummary(Defendant defendant) {
        DefendantSummary defendantSummary = _createSummary(defendant);

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(defendantSummary) + ".");
        }

        return defendantSummary;
    }

    private static DefendantSummary _createSummary(Defendant defendant) {
        DefendantSummary defendantSummary = new DefendantSummary();
        defendantSummary.setId(defendant.getId());

        Name name = defendant.getPersonalDetails().getName();
        for (int i = 0, c = name.getCitizenNameForenameCount(); i < c; i++) {
            defendantSummary.addCitizenNameForename(name.getCitizenNameForename(i));
        }
        defendantSummary.setCitizenNameSurname(name.getCitizenNameSurname());
        defendantSummary.setPrisonerID(defendant.getPrisonerID());
        defendantSummary.setPrisonLocation(defendant.getPrisonLocation());
        defendantSummary.setCustodyStatus(defendant.getCustodyStatus());
        //Req1745
        defendantSummary.setPTIURN(defendant.getURN());

        return defendantSummary;
    }

    /**
     * Create the defendant summaries for the defendant (single entry)
     * 
     * @param defendant
     *            the defendant being sumarised
     * @return the new summaries
     */
    public static DefendantSummaries createSummaries(Defendant defendant) {
        DefendantSummaries defendantSummaries = new DefendantSummaries();
        defendantSummaries.addDefendantSummary(DefendantHelper.createSummary(defendant));
        return defendantSummaries;
    }

    /**
     * Create the defendant summaries for all the defendants represented by the
     * solicitor
     * 
     * @param defendants
     *            the defendants who are being sumarised
     * @param solicitor
     *            the solicitor representing the defendants.
     * @param represented
     *            if true only return those represented by this solicitor
     * @return the new summaries
     */
    public static DefendantSummaries createSummaries(Defendants defendants, Solicitor solicitor, boolean represented) {
        DefendantSummaries defendantSummaries = new DefendantSummaries();
        for (int i = 0, c = defendants.getDefendantCount(); i < c; i++) {
            Defendant defendant = defendants.getDefendant(i);
            if (!represented || DefendantHelper.isRepresented(defendant, solicitor)) {
                defendantSummaries.addDefendantSummary(DefendantHelper.createSummary(defendant, solicitor));
            }
        }
        return defendantSummaries;
    }

    /**
     * Create the defendant summaries for all the defendants
     * 
     * @param defendants
     *            the defendants who are being sumarised
     * @return the new summaries
     */
    public static DefendantSummaries createSummaries(Defendants defendants) {
        DefendantSummaries defendantSummaries = new DefendantSummaries();
        for (int i = 0, c = defendants.getDefendantCount(); i < c; i++) {
            defendantSummaries.addDefendantSummary(DefendantHelper.createSummary(defendants.getDefendant(i)));
        }
        return defendantSummaries;
    }

    /**
     * Return the solicitor reference for the solicitor or null if not
     * represented by this solicitor
     * 
     * @param defendant
     *            the defendant who may be being represented
     * @param solicitor
     *            the solicitor who may be representing the defendant
     * @param return
     *            the solicitor reference for the solicitor or null if not
     *            represented by this solicitor
     */
    public static String getSolicitorRef(Defendant defendant, Solicitor solicitor) {
        for (int i = 0, c = defendant.getCounselCount(); i < c; i++) {
            Counsel counsel = defendant.getCounsel(i);
            for (int j = 0, d = counsel.getSolicitorCount(); j < d; j++) {
                if (solicitor.getId().equals(counsel.getSolicitor(j).getId())) {
                    return counsel.getSolicitor(j).getSolicitorRef();
                }
            }
        }
        return null;
    }

    /**
     * Return true if the defendant is represented by the solicitor
     * 
     * @param defendant
     *            the defendant who may be being represented
     * @param solicitor
     *            the solicitor who may be representing the defendant
     * @param return
     *            true if defendant is being represented by the solicitor,
     *            otherwise false
     */
    public static boolean isRepresented(Defendant defendant, Solicitor solicitor) {
        for (int i = 0, c = defendant.getCounselCount(); i < c; i++) {
            Counsel counsel = defendant.getCounsel(i);
            for (int j = 0, d = counsel.getSolicitorCount(); j < d; j++) {
                if (solicitor.getId().equals(counsel.getSolicitor(j).getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the defendant's full name
     * 
     * @param defendant
     *            the defendant from which we are retrieving the full name
     * @return the full name
     * @throws NullPointerException
     *             if the defendant is null
     */
    public static String getFullName(Defendant defendant) {
        Name name = defendant.getPersonalDetails().getName();

        // If we have a requested name use that
        /*
         * ReguestedName should not be here it is only used for judges! String
         * requestedName = name.getCitizenNameRequestedName(); if (requestedName !=
         * null) { String trimedRequestedName = requestedName.trim(); if
         * (trimedRequestedName.length() != 0) { return trimedRequestedName; } }
         */

        // If we have any forenames prepend them to the surname
        int forenameCount = name.getCitizenNameForenameCount();
        if (0 < forenameCount) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(name.getCitizenNameForename(0).trim());
            buffer.append(" ");
            for (int i = 1; i < forenameCount; i++) {
                buffer.append(name.getCitizenNameForename(i).trim());
                buffer.append(" ");
            }
            buffer.append(name.getCitizenNameSurname().trim()); // Mandatory
            return buffer.toString();
        }

        // Otherwise return just the mandatory surname
        return name.getCitizenNameSurname().trim(); // Mandatory
    }

    /**
     * Get the defendant's full name
     * 
     * @param defendant
     *            the defendant from which we are retrieving the full name
     * @return the full name
     * @throws NullPointerException
     *             if the defendant is null
     */
    public static String getFullName(DefendantSummary defendantSummary) {
        int forenameCount = defendantSummary.getCitizenNameForenameCount();
        if (0 < forenameCount) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(defendantSummary.getCitizenNameForename(0).trim());
            buffer.append(" ");
            for (int i = 1; i < forenameCount; i++) {
                buffer.append(defendantSummary.getCitizenNameForename(i).trim());
                buffer.append(" ");
            }
            buffer.append(defendantSummary.getCitizenNameSurname().trim()); // Mandatory
            return buffer.toString();
        }

        // Otherwise return just the mandatory surname
        return defendantSummary.getCitizenNameSurname().trim(); // Mandatory
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param defendant
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(Defendant defendant) {
        return "Defendant[id=" + defendant.getId() + ", name=" + getFullName(defendant) + ", id="
                + defendant.getCRESTdefendantID() + ", valid=" + defendant.isValid() + ",URN=" + defendant.getURN() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param defendant
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(DefendantSummary defendantSummary) {
        return "DefendantSummary[id=" + defendantSummary.getId() + ", name=" + getFullName(defendantSummary)
                + ", valid=" + defendantSummary.isValid() + ",PTIURN=" + defendantSummary.getPTIURN() + "]";
    }

}
