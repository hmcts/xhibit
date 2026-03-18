package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ProsecutingOrganisation;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ProsecutionSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.ProsecutingAuthorityType;

/**
 * <p>
 * Title: ProsecutionHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating Prosecution and Prosecution
 * Summary XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ProsecutionHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class ProsecutionHelper {
    /**
     * CPS Prosecuting Authority
     */
    private static final ProsecutingAuthorityType CPS_PROSECUTING_AUTHORITY = ProsecutingAuthorityType
            .valueOf("Crown Prosecution Service");

    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(ProsecutionHelper.class);

    /**
     * Create a summary for the detailed prosecution
     * 
     * @param prosecution
     *            the prosecution to create the summary for
     * @return the summary
     */
    public static ProsecutionSummary createSummary(Prosecution prosecution, Solicitor solicitor) {
        ProsecutionSummary prosecutionSummary = _createSummary(prosecution);

        prosecutionSummary.setSolicitorRef(getSolicitorRef(prosecution, solicitor));

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(prosecutionSummary) + ".");
        }

        return prosecutionSummary;
    }

    /**
     * Create a summary for the detailed prosecution
     * 
     * @param prosecution
     *            the prosecution to create the summary for
     * @return the summary
     */
    public static ProsecutionSummary createSummary(Prosecution prosecution) {
        ProsecutionSummary prosecutionSummary = _createSummary(prosecution);

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(prosecutionSummary) + ".");
        }

        return prosecutionSummary;
    }

    private static ProsecutionSummary _createSummary(Prosecution prosecution) {
        ProsecutionSummary prosecutionSummary = new ProsecutionSummary();
        prosecutionSummary.setId(prosecution.getId());

        prosecutionSummary.setProsecutingReference(prosecution.getProsecutingReference());
        prosecutionSummary.setOrganisationName(getOrganisationName(prosecution));

        return prosecutionSummary;
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
    public static String getSolicitorRef(Prosecution prosecution, Solicitor solicitor) {
        for (int i = 0, c = prosecution.getSolicitorCount(); i < c; i++) {
            if (solicitor.getId().equals(prosecution.getSolicitor(i).getId())) {
                return prosecution.getSolicitor(i).getSolicitorRef();
            }
        }
        return null;
    }

    /**
     * Check if the prosecution is the CPS
     * 
     * @return true if the prosecution is the CPS else false
     */
    public static boolean isCps(Prosecution prosecution) {
        return CPS_PROSECUTING_AUTHORITY.equals(prosecution.getProsecutingAuthority());
    }

    /**
     * Get the prosecution organisation name
     * 
     * @param prosecution
     *            the prosecution from which we are retrieving the organisation
     *            name
     * @return the organisation name
     * @throws NullPointerException
     *             if the prosecution is null
     */
    public static String getOrganisationName(Prosecution prosecution) {
        ProsecutingOrganisation prosecutingOrganisation = prosecution.getProsecutingOrganisation();
        if (prosecutingOrganisation != null) {
            String trimedOrganisationName = prosecutingOrganisation.getOrganisationName().trim(); // Mandatory
            if (trimedOrganisationName.length() > 0) {
                return trimedOrganisationName;
            }
        }
        return "";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param prosecution
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(Prosecution prosecution) {
        return "Prosecution[id=" + prosecution.getId() + ", name=" + getOrganisationName(prosecution) + ", valid="
                + prosecution.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param prosecution
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(ProsecutionSummary prosecutionSummary) {
        return "ProsecutionSummary[id=" + prosecutionSummary.getId() + ", name="
                + prosecutionSummary.getOrganisationName() + ", valid=" + prosecutionSummary.isValid() + "]";
    }

    /**
     * Get the recipients id.
     * 
     * @return the recipient type or null if no valid recipient id.
     */
    public static Integer getRecipientId(Prosecution prosecution) {
        try {
            return new Integer(prosecution.getId());
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

}
