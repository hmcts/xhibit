package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

/**
 * <p>
 * Title:
 * Instance of CrestFormBFXmlHelper that populates the Crest Form Castor object for a
 * 'Form B'
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Instance of CrestFormBFXmlHelper that populates the Crest Form Castor object for a
 * 'Form B'
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Surtar Bachra
 * @version 1.0
 */
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.CrestForm;

public class CrestFormBFFormBXMLHelper extends CrestFormBFXmlHelper {

    // Only Indictment type charges 'I' apply to this type of Crest Form
    private String chargeType = this.INDICTMENT;

    /**
     * Simple constructor instantiating a functional instance of XML helper for
     * a 'Crest Form B'
     * 
     * @param typeCode
     *            the type code for this particular form.
     */
    public CrestFormBFFormBXMLHelper(String typeCode) {
        super(typeCode);
    }

    /**
     * Utility method that populates an Order XML structure using Castor objects
     * and the Xhibit entity beans.
     * 
     * @param crestForm
     *            The Order to be populated.
     * @param doc
     *            The defendant on case EB from which to begin population.
     * @throws CrestFormBFXMLException
     *             When there is a problem in population.
     */
    protected void populateSchema(CrestForm crestForm, XhbDefendantOnCase doc, String courtClerkName,
            Integer scheduledHearingID) throws CrestFormBFXMLException {
        // populate the case reference
        CrestFormBFCaseReferenceHelper.populateCaseReference(crestForm.getCrestFormB().getCase(), doc);

        // populate the court clerk
        if (courtClerkName != null) {
            crestForm.getCrestFormB().getCourtClerk().setClerkName(courtClerkName);
        }

        // populate the defendant details
        CrestFormBFDefendantHelper.populateDefendant(crestForm.getCrestFormB().getDefendant(), doc);

        // populate the charge details for the given charge type
        crestForm.getCrestFormB().setCharges(
                CrestFormBFChargeHelper2.populateCharges(crestForm.getCrestFormB().getCharges(), doc, chargeType));

        // populate the unrelated disposals
        crestForm.getCrestFormB().setUnrelatedDisposals(
                CrestFormBFUnrelatedDisposalHelper2.populateUnrelatedDisposals(crestForm.getCrestFormB()
                        .getUnrelatedDisposals(), doc));

        // populate No of Taken Into Considerations
        Short tics = doc.getNoOfTics();
        if (tics != null) {
            crestForm.getCrestFormB().setTICS(tics.intValue());
        }
    }

    /**
     * Get the crest form to marshal from the root element
     * 
     * @param crestForm
     *            the root element
     * @return the form to marshal
     */
    public Object getCrestForm(CrestForm crestForm) {
        return crestForm.getCrestFormB();
    }

    /**
     * 
     * @return String representing type of CrestForm
     */
    protected String getCrestFormType() {
        return "B";
    }
}
