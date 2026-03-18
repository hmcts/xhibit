package uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay;

import java.util.HashSet;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;

/**
 * <p>
 * Title: CourtSitePDComplexValue
 * </p>
 * 
 * <p>
 * Description: Complex value object that builds a tree of information for a
 * court site and the locations within it (<code>DisplayLocationComplexValue</code>)
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: CourtSitePDComplexValue.java,v 1.1 2004/01/15 10:34:17 rz3jq5
 *          Exp $
 * 
 * @see DisplayLocationComplexValue
 */
public class CourtSitePDComplexValue extends CSAbstractValue {
	
	static final long serialVersionUID = -8705423954144444243L;
	
    private static final Logger log = CSServices.getLogger(CourtSitePDComplexValue.class);

    /**
     * Hash set that stores all the location complex values
     */
    private HashSet displayLocationComplexValues = new HashSet();

    private XhbCourtSiteBasicValue courtSiteBasicValue;

    /**
     * Set a court site for the VO
     * 
     * @param courtSiteBasicValue
     *            The court site
     */
    public void setCourtSiteBasicValue(XhbCourtSiteBasicValue courtSiteBasicValue) {
        this.courtSiteBasicValue = courtSiteBasicValue;
    }

    /**
     * Get the court site that this VO is for
     * 
     * @return XhbCourtSiteBasicValue
     */
    public XhbCourtSiteBasicValue getCourtSiteBasicValue() {
        return courtSiteBasicValue;
    }

    /**
     * Get all the locations for the court site
     * 
     * @return An array of DisplayLocationComplexValue
     */
    public DisplayLocationComplexValue[] getDisplayLocationComplexValue() {
        return ((DisplayLocationComplexValue[]) displayLocationComplexValues
                .toArray(new DisplayLocationComplexValue[displayLocationComplexValues.size()]));
    }

    /**
     * Add a display location to the hash set. If one already exists (based on
     * the equals method in DisplayLocationComplexValue) then that one will be
     * replaced
     * 
     * @param displayLocationComplexValue
     *            The Location to add
     */
    public void addDisplayLocationComplexValue(DisplayLocationComplexValue displayLocationComplexValue) {
        displayLocationComplexValues.add(displayLocationComplexValue);
        log.debug("Adding display location complex "
                + displayLocationComplexValue.getDisplayLocationBasicValue().getPrimaryKey());
    }
}
