package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

/**
 * <p>
 * Title: Utility class for populating Offence Information in a Crest Form Schema.
 * </p>
 * <p>
 * Description:
 * Offence information population
 * </p>
 * <p>
 * This class populates castor bound xml objects from entity beans.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Surtar Bachra
 * @version 1.0
 */

import java.sql.Timestamp;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.ResultsDatabase;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffence;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffenceHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.types.AppealType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.types.VerdictTypeType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.types.YesNoType;

public class CrestFormBFOffenceHelper2 {

    // set up required home interfaces
    private static final RefOffenceHome refOffenceHome;

    protected static Logger log = CSServices.getLogger(CrestFormBFOffenceHelper2.class);

    private static final String MAGISTRATES_COURT = "M";

    // CR58 Uncoded Offences
    private static final String UNCODED_OFFENCE_CODE = "ZZ99999";

    private static final String HYPHEN_DELIM = " - ";

    private static final char CHAR_ASTERISK = '*';

    private static final char CHAR_SPACE = ' ';

    static {
        refOffenceHome = (RefOffenceHome) CSServices.getServiceLocator().getLocalHome(RefOffenceHome.class);
    }

    public static void populateOffence(
            uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Offence castorOffence,
            OffenceValue offenceEntity, Integer docID, ResultsCompositeValue rcv) throws CrestFormBFXMLException {
        log.debug("************ OFFENCE HELPER *************");

        Integer crestOffenceSeqNo = offenceEntity.getCrestOffenceSeqNo();
        if (crestOffenceSeqNo != null) {
            castorOffence.setOffenceNo(crestOffenceSeqNo.intValue());
        }

        // get the refOffence record for the offence
        RefOffence refOffence = getRefOffence(offenceEntity.getRefOffenceID());

        if (refOffence != null) {
            // If Uncoded Offence display "Uncoded Offence - ", followed by
            // the
            // HO Desc and RS Desc separated by a space
            if (UNCODED_OFFENCE_CODE.equalsIgnoreCase(refOffence.getOffenceCode())) {
                castorOffence.setOffenceCode(refOffence.getOffenceDesc());
                castorOffence
                        .setOffenceDesc(offenceEntity.getCrestOffenceFreeText().replace(CHAR_ASTERISK, CHAR_SPACE));

            } else {
                castorOffence.setOffenceCode(refOffence.getOffenceCode());
                castorOffence.setOffenceDesc(refOffence.getOffenceDesc());
            }
        }

        // get the DefendantOnOffence record for the given doc and offence
        XhbDefendantOnOffence doo = getDefendantOnOffence(docID, offenceEntity.getOffenceID());

        Integer defOnOffenceID = doo.getDefendantOnOffenceId();

        String appealType = doo.getAppealAgainstType();

        if (appealType != null) {
            // set the appeal type
            castorOffence.setAppealType(getAppealType(appealType));
        }

        VerdictValue verdict = rcv.getVerdict(defOnOffenceID);
        PleaValue plea = rcv.getPlea(defOnOffenceID);

        log.debug("DEF ON OFFENCE ID ***************************** " + defOnOffenceID);

        // populate the offence verdict details
        if (verdict != null) {
            populateVerdicts(castorOffence, verdict);
        }

        if (plea != null) {
            // populate the offence plea details
            populatePleas(castorOffence, plea);
        }

        // set up the VCO details
        if (defOnOffenceID != null) {
            XhbDefendantOnOffence defOnOff = getDefendantOnOffence(defOnOffenceID);
            if (defOnOff != null) {
                log.debug("doo id :::::::::::::::::::::::::::; " + doo.getDefendantOnOffenceId());
                String vcoFlag = doo.getVcoFlag();
                java.util.Date vcoDate = doo.getVcoDate();
                log.debug("vcoFlag :::::::::::::::::::::::::::; " + vcoFlag);
                log.debug("vcoDate :::::::::::::::::::::::::::; " + vcoDate);

                if (vcoFlag != null) {
                    castorOffence.setVCOFlag(getVCO(vcoFlag));
                }
                if (vcoDate != null) {
                    castorOffence.setVCODate(new Date(vcoDate));
                }
            }
        }

        if (defOnOffenceID != null) {
            // populate the offence disposal details
            int noOfDisposals = rcv.getDisposalCount(defOnOffenceID);
            for (int i = 0; i < noOfDisposals; i++) {
                // pick up each diposal
                DisposalValue dv = rcv.getDisposal(defOnOffenceID, i);
                populateDisposals(castorOffence, dv);
            }
        }
    }

    /**
     * Util method to return the constant representing the type of appeal
     *
     * @param appealType
     *            String value from the database
     * @return AppealType for the provided string
     */
    private static AppealType getAppealType(String appealType) {
        AppealType rtnType = null;

        AppealType SENTENCE = AppealType.SENTENCE;
        AppealType CONVICTION = AppealType.CONVICTION;
        AppealType BOTH = AppealType.CONVICTIONSENTENCE;

        if (appealType.equals("S")) {
            rtnType = SENTENCE;
        }

        if (appealType.equals("C")) {
            rtnType = CONVICTION;
        }

        if (appealType.equals("B")) {
            rtnType = BOTH;
        }

        return rtnType;
    }

    /**
     * Util method to return the refOffence record for the given refOffenceID
     * used to retrieve the code and desc
     *
     * @param refOffenceID
     *            Primary key for the table
     * @return RefOffence record
     */
    private static RefOffence getRefOffence(Integer refOffenceID) throws CrestFormBFXMLException {
        RefOffence ro = null;
        try {
            ro = refOffenceHome.findByPrimaryKey(refOffenceID);
        } catch (FinderException e) {
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX",
                    "Could not find reference offence record ref offence id:" + refOffenceID, e);

        }
        return ro;
    }

    /**
     * Get the defendantOnOffence record for the doc and offence ID
     *
     * @param docID
     *            The Defendant On the Case
     * @param offenceID
     *            The Offence ID
     * @return Matching DefendantOnOffence record
     */

    private static XhbDefendantOnOffence getDefendantOnOffence(Integer docID, Integer offenceID)
            throws CrestFormBFXMLException {
        XhbDefendantOnOffence doo = null;

        try {
            doo = XhbDefendantOnOffenceBeanHelper2.findByDefOnCaseAndOffence(docID, offenceID);
        } catch (XhbDefendantOnOffenceBeanNotFoundException e) {
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX",
                    "Could not find defendant on offence for defendant on case id:" + docID + " and offence id: "
                            + offenceID, e);

        }
        return doo;
    }

    /**
     * Util method to return defendant on offence record using defendant on
     * offence id
     *
     */
    private static XhbDefendantOnOffence getDefendantOnOffence(Integer defendantOnOffenceId) {
        XhbDefendantOnOffence doo = null;

        try {
            doo = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(defendantOnOffenceId);
        } catch (XhbDefendantOnOffenceBeanNotFoundException e) {
            // do nothing.. no doo record
        }
        return doo;
    }

    /**
     * Util class used for the population of the Verdicts for a given
     * defendantOnOffenceId
     *
     * @param offence
     *            The Castor bound offence object
     * @param verdict
     *            Verdict Entity
     */
    private static void populateVerdicts(uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Offence offence,
            VerdictValue verdict) throws CrestFormBFXMLException {
        uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Verdict xmlVerdict = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Verdict();
        CrestFormBFVerdictPleaHelper2.populateCastorVerdict(xmlVerdict, verdict);
        offence.setVerdict(xmlVerdict);

    }

    /**
     * Util class used for the population of the Pleas for a given
     * defendantOnOffenceId
     *
     * @param offence
     *            The Castor bound offence object
     */
    private static void populatePleas(uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Offence offence,
            PleaValue plea) throws CrestFormBFXMLException {
        uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Plea castorPlea = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Plea();
        CrestFormBFVerdictPleaHelper2.populateCastorPlea(castorPlea, plea);
        if (castorPlea != null) {
            log.debug("populatePleas :: PLEA SET");
            offence.setPlea(castorPlea);
        }
    }

    /**
     * Util class used for the population of the Disposals for a given disposal
     *
     * @param offence
     *            The Castor bound offence object
     * @param dv
     *            Disposal Value
     */
    private static void populateDisposals(uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Offence offence,
            DisposalValue dv) {
        uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Disposal od = new uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Disposal();

        int dispTypeId = dv.getRefDisposalTypeId();
        DisposalReferenceValue drv = ResultsDatabase.getReferenceDisposal(new Integer(dispTypeId));
        String disposalCode = drv.getDisposalCode();
        String disposalDesc = drv.getTitle();
        String formBFText = drv.getFormBFText(dv);
        od.setDisposalCode(disposalCode);
        od.setDisposalDesc(disposalDesc);
        od.setFormBFText(formBFText);
        od.setIsMagistrateCourt(YesNoType.NO); // set up default value
        // test if magistrates court disposal
        if (dv.getCourtType().equals(MAGISTRATES_COURT)) {
            od.setIsMagistrateCourt(YesNoType.YES);
        }
        // add the disposal to the offence
        offence.addDisposal(od);
    }

    /**
     * Util method to return a valid Verdict Type
     *
     * @param vcoText
     * @return Verdict Type for the given description
     */
    private static VerdictTypeType getVCO(String vcoText) {
        VerdictTypeType vt = null;

        if (vcoText == null) {
            // if the passed in verdict is null set the verdict type to
            // blank
            vt = VerdictTypeType.VALUE_3; // blank
        } else {
            if (vcoText.equals("V")) {
                vt = VerdictTypeType.VALUE_0; // verdict
            } else if (vcoText.equals("C")) {
                vt = VerdictTypeType.VALUE_1; // conviction
            } else if (vcoText.equals("O")) {
                vt = VerdictTypeType.VALUE_2; // other
            } else {
                // if not one of the above descriptions set to blank
                vt = VerdictTypeType.VALUE_3; // blank
            }
        }
        return vt; // return the verdict type
    }
}