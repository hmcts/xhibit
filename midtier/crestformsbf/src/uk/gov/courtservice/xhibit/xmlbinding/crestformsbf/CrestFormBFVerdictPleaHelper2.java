package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.refappresult.RefAppResult;
import uk.gov.courtservice.xhibit.business.entities.refappresult.RefAppResultHome;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffence;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffenceHome;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.AlternativeOffence;
import uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.types.YesNoType;

/**
 * <p/> Title: Utility class for populating Verdict Information in a Crest Form
 * Schema.
 * </p>
 * <p/> Description: Verdict information population
 * </p>
 * <p/> This class populates castor bound xml objects from entity beans.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Surtar Bachra
 * @version 1.0
 */
public class CrestFormBFVerdictPleaHelper2 {
    // set up required home interfaces
    private static final RefAppResultHome refAppResultHome;

    private static final RefOffenceHome refOffenceHome;

    // CR58 Uncoded Offences
    private static final String UNCODED_OFFENCE_CODE = "ZZ99999";

    private static final String STR_ASTERISK = "*";

    private static final char CHAR_ASTERISK = '*';

    private static final char CHAR_SPACE = ' ';

    protected static Logger log = CSServices.getLogger(CrestFormBFVerdictPleaHelper2.class);

    static {
        refOffenceHome = (RefOffenceHome) CSServices.getServiceLocator().getLocalHome(RefOffenceHome.class);
        refAppResultHome = (RefAppResultHome) CSServices.getServiceLocator().getLocalHome(RefAppResultHome.class);
    }

    public static void populateCastorVerdict(uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Verdict ver,
            VerdictValue v) throws CrestFormBFXMLException {

        // if the Verdict.RefVerdictId is null then use the RefAppResultId to
        // search xhb_app_result
        Integer verdictCode = v.getRefVerdictId();
        Integer appVerdictCode = v.getRefAppResultId();

        // Test to ensure that the Verdict Description can be determined
        if (verdictCode == null && appVerdictCode == null) {
            log.error("No Ref Verdict ID or Ref App Result ID");
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX",
                    "Could not determine the type of verdict using either Verdict Id or App Result Id");
        }

        if (verdictCode != null) {
            // set up verdict details using Verdict Code
            String vCode = v.getRefVerdictCode();
            String vDesc = v.getRefVerdictDesc();

            if (vCode != null) {
                ver.setVerdictCode(vCode);
            }
            if (vDesc != null) {
                ver.setVerdictDesc(vDesc);
            }
        } else {
            // use the RefAppResultId to search RefAppResult
            log.debug("Ref App Result Code is :: " + appVerdictCode);
            RefAppResult rar = getRefAppealResult(appVerdictCode);
            if (rar != null) {
                // set the verdict code and description
                ver.setVerdictCode(rar.getAppResultCode());
                // set up description text Desc1 + Desc2 (if present)
                StringBuffer description = new StringBuffer();
                description.append(rar.getAppResultDescr1());
                String description2 = rar.getAppResultDescr2();
                if (description2 != null) {
                    description.append(" ");
                    description.append(description2);
                }
                ver.setVerdictDesc(description.toString());
            }
        }

        String verdictOtherText = v.getOtherVerdictText();
        if (verdictOtherText != null) {
            ver.setVerdictOtherText(verdictOtherText);
        }

        // S.Bachra: Amended to handle Business Rule RESULT37 - If the Verdict
        // is 'Guilty' and
        // 'Unanimous' then Jurors Dissenting set to 0 and Assenting left blank
        // - these values
        // are stored in the database like this but need to also be displayed on
        // the CrestForms B-F
        // the same way as represented in the application.

        // set up the default Jurors Assenting and Jurors Dissenting values as
        // single space
        ver.setJurorsAssenting(" ");
        ver.setJurorsDissenting(" ");

        // set Jurors Assenting
        Integer jurorsAssenting = v.getJurorsAssenting();
        if (jurorsAssenting != null) {
            // set Jurors Assenting Value
            ver.setJurorsAssenting(jurorsAssenting.toString());
        }

        // set Jurors Dissenting
        Integer jurorsDissenting = v.getJurorsDissenting();
        if (jurorsDissenting != null) {
            // set Jurors Dissenting Value
            ver.setJurorsDissenting(jurorsDissenting.toString());
        }

        // get alternative offence info - details held for non Criminal Appeals
        String altOffenceCode = v.getAltRefOffenceCode();
        String altOffenceDesc = v.getAltRefOffenceDesc();
        log.debug("AlternativeOffence Code>>  VERDICT >> " + altOffenceCode);
        log.debug("AlternativeOffence Desc>>  VERDICT >> " + altOffenceDesc);
        if (altOffenceCode != null)

        {
            // set up a new alternatve offence element
            AlternativeOffence altOffence = new AlternativeOffence();

            // If Uncoded Offence display "Uncoded Offence - ", followed by
            // the
            // HO Desc and RS Desc separated by a space
            if (UNCODED_OFFENCE_CODE.equalsIgnoreCase(altOffenceCode)) {
                altOffence.setAltOffenceCode(getRefOffence(v.getAltRefOffenceId()).getOffenceDesc());
                altOffence.setAltOffenceDesc(altOffenceDesc.replace(CHAR_ASTERISK, CHAR_SPACE));

            } else {
                altOffence.setAltOffenceCode(altOffenceCode);
                altOffence.setAltOffenceDesc(altOffenceDesc);
            }

            // add the alternative offence
            ver.setAlternativeOffence(altOffence);
        }

        // get alternative offence info - details held for Criminal Appeals
        String altOffencetextCriminal = v.getAppLesserOffence();
        log.debug("AlternativeOffence Criminal >>  VERDICT >> " + altOffencetextCriminal);
        if (altOffencetextCriminal != null) {
            // set up a new alternatve offence element
            AlternativeOffence altOffence = new AlternativeOffence();
            // If there is only 1 asterisk and it it not at the beginning or
            // end
            // assume it is uncoded offence and replace with space
            if (altOffencetextCriminal.indexOf(STR_ASTERISK) >= 0
                    && altOffencetextCriminal.lastIndexOf(STR_ASTERISK) < altOffencetextCriminal.length() - 1
                    && altOffencetextCriminal.indexOf(STR_ASTERISK) == altOffencetextCriminal.lastIndexOf(STR_ASTERISK)) {
                if (v.getAltRefOffenceId() != null) {
                    altOffence.setAltOffenceCode(getRefOffence(v.getAltRefOffenceId()).getOffenceDesc());
                } else {
                    altOffence.setAltOffenceCode(" ");
                }
                altOffence.setAltOffenceDesc(altOffencetextCriminal.replace(CHAR_ASTERISK, CHAR_SPACE));
            } else {
                altOffence.setAltOffenceCode(" ");
                altOffence.setAltOffenceDesc(altOffencetextCriminal);
            }
            // add the alternative offence
            ver.setAlternativeOffence(altOffence);
        }
    }

    public static void populateCastorPlea(uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Plea castorPlea,
            PleaValue plea) throws CrestFormBFXMLException {
        // set up default plea code and description
        castorPlea.setPleaCode(" ");
        castorPlea.setPleaDesc(" ");

        // set up plea information
        String pleaCode = plea.getRefPleaCode();
        String pleaDesc = plea.getRefPleaDesc();
        if (pleaCode != null) {
            castorPlea.setPleaCode(pleaCode);
        }
        if (pleaDesc != null) {
            castorPlea.setPleaDesc(pleaDesc);
        }

        // set the plea other text if present
        castorPlea.setPleaFreeText(" "); // set up default
        String pleaFreeText = plea.getOtherPleaText();
        log.debug("pl getOtherPleaText :: " + pleaFreeText);
        if (pleaFreeText != null) {
            castorPlea.setPleaFreeText(pleaFreeText);
        }

        java.util.Date arraignmentDate = plea.getArraignmentDate();
        log.debug("pl getArraignmentDate :: " + arraignmentDate);
        // check to ensure that date is not null
        if (arraignmentDate != null) {
            castorPlea.setArraignmentDate(new Date(arraignmentDate));
        }

        // set the Breach Admitted option
        // used by the Breach Charge structure to determine whether the Breach
        // has been admitted
        // if the return value is 'Y' then set to 'Yes' otherwise set to 'No'
        log.debug("pl getBreachAdmitted :: " + plea.getBreachAdmitted());
        castorPlea.setBreachAdmitted(YesNoType.NO);
        if (plea.getBreachAdmitted() != null && plea.getBreachAdmitted().equals(Boolean.TRUE)) {
            log.debug("BreachAdmitted :: Set to YES");
            castorPlea.setBreachAdmitted(YesNoType.YES);
        }

        // get any alternative offence information
        Integer altOffenceId = plea.getAltRefOffenceId();
        log.debug("AlternativeOffenceId >>  PLEA >> " + altOffenceId);
        if (altOffenceId != null) {
            // set up a new alternatve offence element
            AlternativeOffence altOffence = new AlternativeOffence();
            String altOffenceCode = plea.getAltRefOffenceCode();
            String altOffenceDesc = plea.getAltRefOffenceDesc();
            log.debug("altOffenceCode >>  PLEA >> " + altOffenceCode);
            log.debug("altOffenceDesc >>  PLEA >> " + altOffenceDesc);
            if (altOffenceCode != null) {
                // If Uncoded Offence display "Uncoded Offence - ", followed by
                // the
                // HO Desc and RS Desc separated by a space
                if (UNCODED_OFFENCE_CODE.equalsIgnoreCase(altOffenceCode)) {
                    altOffence.setAltOffenceCode(getRefOffence(plea.getAltRefOffenceId()).getOffenceDesc());
                    if (altOffenceDesc != null) {
                        altOffence.setAltOffenceDesc(altOffenceDesc.replace(CHAR_ASTERISK, CHAR_SPACE));
                    }
                } else {
                    altOffence.setAltOffenceCode(altOffenceCode);
                    if (altOffenceDesc != null) {
                        altOffence.setAltOffenceDesc(altOffenceDesc);
                    }
                }
            }
            castorPlea.setAlternativeOffence(altOffence);
        }
    }

    /**
     * Util method to retrieve a given RefSystemCode record for the given
     * primary key
     * 
     * @param appVerdictCode
     *            Primary key
     * @return RefSystemCode record for the given primary key
     */
    private static RefAppResult getRefAppealResult(Integer appVerdictCode) throws CrestFormBFXMLException {
        RefAppResult rar = null;
        try {
            rar = refAppResultHome.findByPrimaryKey(appVerdictCode);
        } catch (FinderException e) {
            throw new CrestFormBFXMLException("CRESTFORMBF_XXX",
                    "Could not find reference appeal result code record for id:" + appVerdictCode, e);
        }
        return rar;
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

}
