package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;

/**
 * <p>
 * Title: SolicitorHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating Solicitor and Solicitor Summary
 * XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: SolicitorHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class SolicitorHelper {
    /**
     * Get the solicitor's organisation name
     * 
     * @param solicitor
     *            the solicitor from which we are retrieving the organisation
     *            name
     * @return the organisation name
     * @throws NullPointerException
     *             if the solicitor is null
     */
    public static String getOrganisationName(Solicitor solicitor) {
        return solicitor.getParty().getOrganisation().getOrganisationName().trim(); // All
                                                                                    // Mandatory
    }

    /**
     * Return the solicitors organisation code if present and a valid integer
     * else null
     * 
     * @param solicitor
     *            the solicitor from which we are getting the name.
     * @return the solicitor organisation code or null if not found or invalid
     * @throws NullPointerException
     *             if the solicitor is null
     */
    public static Integer getOrganisationCode(Solicitor solicitor) {
        try {
            return new Integer(solicitor.getParty().getOrganisation().getOrganisationCode());
        } catch (NullPointerException nfe) {
            return null;
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    /**
     * Get the recipients id.
     * 
     * @return the recipient type or null if no valid recipient id.
     */
    public static Integer getRecipientId(Solicitor solicitor) {
        try {
            return new Integer(solicitor.getId());
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param solicitor
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(Solicitor solicitor) {
        return "Solicitor[id=" + solicitor.getId() + ", name=" + getOrganisationName(solicitor) + ", code="
                + getOrganisationCode(solicitor) + ", valid=" + solicitor.isValid() + "]";
    }
}
