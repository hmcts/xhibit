package uk.gov.courtservice.xhibit.business.services.crestformsbf;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.crestformsbf.CrestFormsBFDatabase;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFForm;

/**
 * <p>
 * Title: The Crest Form Controller
 * </p>
 * <p>
 * Description: To Do
 * 
 * @ejb.bean name="CrestFormsBFController" description="Crest Form B - F
 *           Controller Bean" type="Stateless" view-type="remote"
 *           jndi-name="CrestFormsBFControllerHome"
 * 
 * @ejb.transaction type="Required"
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */

public class CrestFormsBFControllerBean extends CSSessionBean implements SessionBean {
    /**
     * The class log4j logger
     */
    private static final Logger log = CSServices.getLogger(CrestFormsBFControllerBean.class);

    /**
     * Instantiates all relevant home interfaces for use during active life of
     * Stateless Session Bean.
     * 
     * @ejb.create-method
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {
    }

    /**
     * This method is a work around until we can query active directory for the
     * full name of the logged on court clark.
     * 
     * @return the name of the last court clerk added to a scheculed hearing
     * @throws CSUnrecoverableException
     *             if it cant find one.
     * 
     * @ejb.interface-method view-type="remote"
     * 
     */
    public String getCourtClerkName(Integer scheduledHearingId) throws CSUnrecoverableException {
        String name = CrestFormsBFDatabase.getCourtClerkName(scheduledHearingId);
        if (name == null) {
            return "";
            // throw new CSUnrecoverableException("Could not determine court
            // clerk name for scheduled hearing " + scheduledHearingId);
        } else {
            return name;
        }
    }

    /**
     * Returns the forms for given case as specified by the business rules
     * (Business Rules Catalogue - section 3.4)
     * 
     * @param caseId
     *            the id of the case to retrieve the forms list for
     * @return an <code>CrestFormsBFForm</code> containing the forms available
     *         for a case
     * @throws CSUnrecoverableException
     *             if an error occures
     * 
     * @ejb.interface-method view-type="remote"
     */
    public CrestFormsBFForm[] getForms(Integer caseId) throws CSUnrecoverableException {
        CrestFormsBFCase caze = CrestFormsBFDatabase.getCase(caseId);
        if (caze == null) {
            throw new CSUnrecoverableException("Could not create case object for case " + caseId);
        }
        return CrestFormsBFFormHelper.getForms(caze);
    }

    /**
     * Get the xml documents for the form in the array
     * 
     * @return a <code>String</code> array populated with the xml for the
     *         forms specified in the parameter.
     * 
     * @ejb.interface-method view-type="remote"
     */
    public String[] getXml(CrestFormsBFForm[] forms, String courtClerkName, Integer scheduledHearingId)
            throws CrestFormBFXMLException {
        return CrestFormsBFFormHelper.getXml(forms, courtClerkName, scheduledHearingId);
    }

}
