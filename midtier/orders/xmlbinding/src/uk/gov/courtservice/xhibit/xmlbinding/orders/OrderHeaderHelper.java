package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.apache.regexp.RESyntaxException;
import org.exolab.castor.types.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.address.AddressHome;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtHome;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffence;
import uk.gov.courtservice.xhibit.business.entities.refoffence.RefOffenceHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResult;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_app_result.XhbRefAppResultBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_system_code.XhbRefSystemCodeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.address.CitizenNameStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.address.types.SexType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.address.types.VerifiedByType;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Charge;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Charges;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.CourtHouse;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.DateOfBirth;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Name;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderHeader;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.ChargeTypeType;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * OrderHeader
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Populates the OrderHeader for all orders.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 53687 07-06-2003 AW Daley Check added to populateDefenfant method to test for
 * a URN set to space.If space then URN is not set on the defendant.
 */

public class OrderHeaderHelper {
    // Assuming we can share instance of CourtHome across all instances.
    private static final CourtHome courtHome;

    private static final RefOffenceHome refOffenceHome;

    private static final AddressHome addressHome;

    private static final Logger log = CSServices.getLogger(OrderHeaderHelper.class);

    private static final String OFFENCE_PARTICULARS_DESC = "tba";

    private static final String DEFAULT_CHARGE_TYPE = "I";

    private static final String UNCODED_OFFENCE_CODE = "ZZ99999";

    private static final String STR_ASTERISK = "*";

    private static final String STR_SPACE = " ";

    // Statically initialise the required home interface.
    // Should be done for all home interfaces used.
    static {
        courtHome = (CourtHome) CSServices.getServiceLocator().getLocalHome(CourtHome.class);
        refOffenceHome = (RefOffenceHome) CSServices.getServiceLocator().getLocalHome(RefOffenceHome.class);
        addressHome = (AddressHome) CSServices.getServiceLocator().getLocalHome(AddressHome.class);
    }

    /**
     * Utility method to populate an OrderHeader from the rest of the Xhibit
     * Entities.
     * 
     * @param orderHeader
     *            The OrderHeader to be populated (pass by reference).
     * @param docEntity
     *            The defendant on case EB for which to populate the order.
     * @param typeCode
     *            The type code of the order.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */
    public static void populateHeader(OrderHeader orderHeader, DefendantOnCase docEntity, String typeCode)
            throws OrderXMLException {
        // Set order type.
        orderHeader.setOrderType(typeCode);

        // Setup the court house.
        CourtHouse ch = orderHeader.getCourtHouse();
        // Navigated entity beans to get FK to court..
        Integer courtId = docEntity.getCaze().getCourtId();
        Court court = null;
        try {
            court = courtHome.findByPrimaryKey(courtId);
        } catch (FinderException ex) {
            throw new OrderXMLException("order.validation.court.missing",
                    "Could not find court for defendant on case id:" + docEntity.getDefendantOnCaseId(), ex);
        }

        CourtHouseHelper.populateCourtHouse(ch, court);

        // Populate case number.
        String caseType = docEntity.getCaze().getCaseType();
        Integer caseNumber = docEntity.getCaze().getCaseNumber();
        // Make sure that the number is correctly padded.
        String trueCaseNumber = caseType + FormatHelper.EIGHT_DIGIT.format(caseNumber);
        orderHeader.setCaseNumber(trueCaseNumber);

        // Setup the defendant.
        uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Defendant def = orderHeader.getDefendant();
        populateDefendant(def, docEntity, courtId, typeCode);

        // Set date to today's date. If not set to a date, then will
        // get nasty exception.
        orderHeader.setOrderDate(new Date(new java.util.Date()));

    }
    
    
    public static void populateHeader(OrderHeader orderHeader, Integer courtId) throws OrderXMLException {
        
        // Setup the court house.
        CourtHouse ch = orderHeader.getCourtHouse();
        Court court = null;
        try {
            court = courtHome.findByPrimaryKey(courtId);
        } catch (FinderException ex) {
            ex.printStackTrace();
        }
        
        CourtHouseHelper.populateCourtHouse(ch, court);
    }

    /**
     * Utlitity method to populate a Defendant from the Xhibit entities.
     * 
     * @param def
     *            The Defendant Castor object to be populated (pass by
     *            reference).
     * @param docEntity
     *            The Defendant On Case EB from which to populate the Defendant.
     * @param typeCode
     *            The Order type.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */
    private static void populateDefendant(uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Defendant def,
            DefendantOnCase docEntity, Integer courtId, String typeCode) throws OrderXMLException {

        // Get the defendant.
        uk.gov.courtservice.xhibit.business.entities.defendant.Defendant defEntity = docEntity.getDefendant();

        // Crest Defendant ID.
        def.setCRESTdefendantID(defEntity.getCrestDefendantId().intValue());

        // Defendant Name.
        CitizenNameStructure cns = def.getPersonalDetails().getName();
        if (cns == null) {
            def.getPersonalDetails().setName(new Name());
            cns = def.getPersonalDetails().getName();
        }
        String firstName = defEntity.getFirstName();
        if (firstName != null) {
            cns.addCitizenNameForename(firstName);
        }

        String middleName = defEntity.getMiddleName();
        if (middleName != null) {
            cns.addCitizenNameForename(middleName);
        }

        String surname = defEntity.getSurname();
        if (surname != null) {
            cns.setCitizenNameSurname(surname);
        }

        // populate gender of Defendant
        Integer gender = docEntity.getDefendant().getGender();

        if (gender == null) {
            // if there is no gender within the database set to unknown
            def.getPersonalDetails().setSex(SexType.UNKNOWN);
        } else {
            switch (gender.intValue()) {
            case 1:
                // male
                def.getPersonalDetails().setSex(SexType.MALE);
                break;
            case 2:
                // female
                def.getPersonalDetails().setSex(SexType.FEMALE);
                break;
            default:
                // otherwise set to unknown
                def.getPersonalDetails().setSex(SexType.UNKNOWN);
                break;
            }
        }

        log.debug("Defendant Gender Value :" + def.getPersonalDetails().getSex());

        // populate DOB of Defendant
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        ParsePosition pos = new ParsePosition(0);

        // The defendant dob may be null - must allow for this
        Timestamp dobvalue = docEntity.getDefendant().getDateOfBirth();
        // String dobvalue = dob.toString();
        java.util.Date xhibitDate = null;
        if (dobvalue != null) {
            xhibitDate = formatter.parse(dobvalue.toString(), pos);
        }

        if (xhibitDate != null) {
            // expects castor type date
            org.exolab.castor.types.Date cdate = new org.exolab.castor.types.Date(xhibitDate);

            def.getPersonalDetails().setDateOfBirth(new DateOfBirth());
            // set the verified option for the DOB
            def.getPersonalDetails().getDateOfBirth().setVerifiedBy(VerifiedByType.NOT_VERIFIED);
            def.getPersonalDetails().getDateOfBirth().setBirthDate(new Date(xhibitDate));
            log.debug("dob verified by :" + def.getPersonalDetails().getDateOfBirth().getVerifiedBy());
            log.debug("dob date with formatting :" + def.getPersonalDetails().getDateOfBirth().getBirthDate());

        }

        // Populate Address of Defendant
        Integer addressId = docEntity.getDefendant().getAddressId(); // get
        // the
        // address
        // ID
        Address defAddress = null;
        try {
            defAddress = addressHome.findByPrimaryKey(addressId); // find
            // the
            // address
        } catch (FinderException ex) {
            throw new OrderXMLException("order.validation.address.missing",
                    "Could not find address for defendant on case id:" + docEntity.getDefendantOnCaseId(), ex);
        }

        // use helper to populate the xml
        if (defAddress != null) {
            def.getPersonalDetails().setAddress(new uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Address());
            uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Address address = def.getPersonalDetails()
                    .getAddress();
            try {
                AddressHelper.populateAddress(address, defAddress);
            } catch (RESyntaxException e) {
                e.printStackTrace(); // To change body of catch statement use
                // Options | File Templates.
                log.error(e);
                throw new OrderXMLException("order.validation.postcode.invalid",
                        "Error within regular expression for postcode", e);

            }
        }

        // S.Bachra 22/5/03 Tracker 53251 Population of the PTIURN
        String urn = docEntity.getPtiurn();
        log.debug("**************PTI URN VALUE FROM DB : " + urn);

        if (urn != null && !urn.equals("") && !urn.equals(" ")) {
            def.setURN(urn);
        }

        // Populate the charges relevant to the order.
        def.setCharges(populateRelevantCharges(def.getCharges(), docEntity, courtId, typeCode));
    }

    /**
     * Utility method to populate a Charges castor bound class from the
     * defendant on case entity and the order type. If a null value was passed
     * in for charges
     * 
     * @param charges
     *            The Charges instance to populate, if a null value is passed,
     *            this method will evaluate whether there are any charges before
     *            instantiating a new Charges instace to be returned.
     * @param docEntity
     *            The defendant on case from which to start populating the
     *            Charges instance
     * @param typeCode
     *            The order type.
     * @return the populated Charges instance.
     * @throws OrderXMLException
     *             if there is a problem in population.
     */
    private static Charges populateRelevantCharges(Charges charges, DefendantOnCase docEntity, Integer courtId,
            String typeCode) throws OrderXMLException {
        log.debug("POPULATE CHARGES FOR DEFENDANT");

        HashMap chargeTypesMap = setChargeTypeMap();

        HashMap chargeTypeTypeMap = setChargeTypeTypeMap();

        // Collection of XhbDefendantOnOffence entities
        Collection relevantDOOS = XhbDefendantOnOffenceBeanHelper2.findByCourtIdOrderCodeDefendantOnCaseId(courtId,
                typeCode, docEntity.getDefendantOnCaseId());

        log.debug("RELEVANT DOOS: " + relevantDOOS.size());

        // Step through the charges.
        Iterator i = relevantDOOS.iterator();
        while (i.hasNext()) {
            XhbDefendantOnOffence doo = (XhbDefendantOnOffence) i.next();

            Charge charge = new Charge();
            charge.setOffenceParticulars(OFFENCE_PARTICULARS_DESC);

            XhbOffence offenceEntity = doo.getXhbOffence();
            log.debug(offenceEntity.getCrestOffenceFreetext());
            XhbCharge chargeEntity = offenceEntity.getXhbCharge();
            XhbRefOffence refOffenceEntity = offenceEntity.getXhbRefOffence();

            
            
            if (refOffenceEntity.getOffenceCode() != null &&
                refOffenceEntity.getOffenceCode().equalsIgnoreCase(UNCODED_OFFENCE_CODE)){
                charge.setOffenceStatement(offenceEntity.getCrestOffenceFreetext());             
            } else{
                charge.setOffenceStatement(refOffenceEntity.getOffenceDesc());
            }
            
            charge.setCJSoffenceCode(refOffenceEntity.getOffenceCode());

            charge.setCaseNumber(getCaseNumberFromCharge(chargeEntity));

            // The Indictment Count Number. Nullable in the database and in
            // the schema.
            if (offenceEntity.getCrestOffenceSeqNo() != null) {
                charge.setIndictmentCountNumber(offenceEntity.getCrestOffenceSeqNo().intValue());
            }

            // The Indictment Number. Nullable in the database and in the
            // schema.
            if (chargeEntity.getCrestChargeSeqNo() != null) {
                charge.setIndictmentNumber(chargeEntity.getCrestChargeSeqNo().intValue());
            }

            // Either Crest Count ID or Crest Charge ID
            // New fields to be added to XML
            if ("I".equals(chargeEntity.getChargeType())) {
                Integer crestOffenceId = offenceEntity.getCrestOffenceId();
                if (null != crestOffenceId) {
                    charge.setCRESTcountID(crestOffenceId.intValue());
                }
            } else {
                Integer crestOffenceId = offenceEntity.getCrestOffenceId();
                if (null != crestOffenceId) {
                    charge.setCRESTchargeID(crestOffenceId.intValue());
                }
            }

            // Get the chargeType from the map loaded from config. If can't
            // find
            // type, default to I
            String chargeType = (String) chargeTypesMap.get(chargeEntity.getChargeType());
            if (chargeType == null) {
                chargeType = DEFAULT_CHARGE_TYPE;
            }
            // Set the ChargeType attribute on the Charge structure
            charge.setChargeType((ChargeTypeType) chargeTypeTypeMap.get(chargeType));

            // If Plea or Verdicts is guilty to lesser/alternate offence
            // use that offence for the OffenceStatement
            populateAlternateLesserPleaDesc(charge, doo.getDefendantOnOffenceId());
            populateAlternateLesserVerdictDesc(charge, doo.getDefendantOnOffenceId());

            log.debug("ADDING CHARGE TO MAP ");

            // If this is the first charge create the charge structure
            if (charges == null) {
                charges = new Charges();
            }
            charges.addCharge(charge);
        }

        log.debug("CHARGES VALUE: " + charges);
        return charges;
    }

    /**
     * Load the ChargeTypeTypes into a map so that we can set the ChargeType
     * attribute
     * 
     * @return Map of ChargeTypeTypes
     */
    private static HashMap setChargeTypeTypeMap() {
        HashMap chargeTypeTypeMap = new HashMap();
        Enumeration enumeration = ChargeTypeType.enumerate();
        while (enumeration.hasMoreElements()) {
            ChargeTypeType chargeTypeType = (ChargeTypeType) enumeration.nextElement();
            chargeTypeTypeMap.put(chargeTypeType.toString(), chargeTypeType);
        }
        return chargeTypeTypeMap;
    }

    /**
     * Load the ChargeTypes as retrieved from config (orders.properties)
     * 
     * @return Map of ChargeTypes
     */
    private static HashMap setChargeTypeMap() {
        HashMap chargeTypesMap = new HashMap();
        String[] chargeTypes = getChargeTypes();
        for (int i = 0; i < chargeTypes.length; i++) {
            chargeTypesMap.put(chargeTypes[i], chargeTypes[++i]);
        }
        return chargeTypesMap;
    }

    /**
     * Return the formatted Case Number from the Charge Entity
     * 
     * @param chargeEntity
     * @return the formatted case number
     */
    private static String getCaseNumberFromCharge(XhbCharge chargeEntity) {
        // Get case number.
        XhbCase caseEntity = chargeEntity.getXhbCase();
        return caseEntity.getCaseType() + FormatHelper.EIGHT_DIGIT.format(caseEntity.getCaseNumber());
    }

    /**
     * return the valid charge types for orders
     * 
     * @return
     * 
     */
    private static String[] getChargeTypes() {
        final String propertyName = "order.charge.values";
        return getPropertyAsArray(propertyName);
    }

    /**
     * Get a set of properties from config and return as a string array
     * 
     * @param propertyName
     *            the property to retrieve
     * @return string array of properties
     */
    public static String[] getPropertyAsArray(final String propertyName) {
        final ArrayList list = new ArrayList();
        final String property = CSServices.getConfigServices().getProperties("orders").getProperty(propertyName);
        for (StringTokenizer stringTokenizer = new StringTokenizer(property, ",", false); stringTokenizer
                .hasMoreTokens();) {
            final String s = stringTokenizer.nextToken();
            list.add(s.trim());
        }
        final String[] strings = (String[]) list.toArray(new String[list.size()]);
        return strings;
    }

    /**
     * Get the alternate lesser offence description, if the verdict code is GA,
     * GAJ, GL or GLJ or the ref app resuolt code is ACALO
     * 
     * @param charge
     *            The charge
     * @param defendantOnOffenceId
     *            the defendant on offence id to use
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    private static void populateAlternateLesserVerdictDesc(Charge charge, Integer defendantOnOffenceId)
            throws OrderXMLException {
        log.debug("populateAltLessVerdictDesc - start");
        log.debug("populateAltLessVerdictDesc: defendantOnOffenceId " + defendantOnOffenceId);
        Collection verdictCollection = XhbVerdictBeanHelper2.findByDefendantOnOffenceId(defendantOnOffenceId);
        log.debug("populateAltLessVerdictDesc: verdictCollection size = " + verdictCollection.size());

        Iterator verdicts = verdictCollection.iterator();
        while (verdicts.hasNext()) {
            XhbVerdict verdict = (XhbVerdict) verdicts.next();
            if (null != verdict.getRefVerdictId()) {
                XhbRefSystemCode systemCode = XhbRefSystemCodeBeanHelper2.findByPrimaryKey(verdict.getRefVerdictId());
                log.debug("populateAltLessVerdictDesc: systemCode " + systemCode.getCode());
                if ("GA".equalsIgnoreCase(systemCode.getCode()) || "GAOJ".equalsIgnoreCase(systemCode.getCode()) || "GAJ".equalsIgnoreCase(systemCode.getCode())
                        || "GL".equalsIgnoreCase(systemCode.getCode()) || "GLJ".equalsIgnoreCase(systemCode.getCode()) 
                        || "GLOJ".equalsIgnoreCase(systemCode.getCode())){
                    setVerdictOffenceDesc(charge, verdict);
                }
            }

            if (null != verdict.getRefAppResultId()) {
                XhbRefAppResult refAppResult = XhbRefAppResultBeanHelper2.findByPrimaryKey(verdict.getRefAppResultId());
                log.debug("populateAltLessVerdictDesc: AppResultCode " + refAppResult.getAppResultCode());
                // Appeal Result
                if ("ACALO".equalsIgnoreCase(refAppResult.getAppResultCode())) {
                    setVerdictOffenceDesc(charge, verdict);
                }
            }
        }
    }

    /**
     * Get the alternate lesser offence description, if the plea code is GAO or GLO
     * 
     * @param charge
     *            The charge
     * @param defendantOnOffenceId
     *            the defendant on offence id to use
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    private static void populateAlternateLesserPleaDesc(Charge charge, Integer defendantOnOffenceId) throws OrderXMLException {
        log.debug("populateAltLessPleaDesc - start");
        log.debug("populateAltLessPleaDesc: defendantOnOffenceId " + defendantOnOffenceId);
        Collection pleaCollection = XhbPleaBeanHelper2.findByDefendantOnOffenceId(defendantOnOffenceId);

        log.debug("populateAltLessPleaDesc: pleaCollection size = " + pleaCollection.size());
        Iterator pleas = pleaCollection.iterator();
        while (pleas.hasNext()) {
            XhbPlea plea = (XhbPlea) pleas.next();
            if (null != plea.getRefPleaId()) {
                XhbRefSystemCode systemCode = XhbRefSystemCodeBeanHelper2.findByPrimaryKey(plea.getRefPleaId());
                log.debug("populateAltLessPleaDesc: systemCode " + systemCode.getCode());
                if ("GAO".equalsIgnoreCase(systemCode.getCode()) || "GLO".equalsIgnoreCase(systemCode.getCode())) {
                    setPleaOffenceDesc(charge, plea);
                }
            }
        }
    }

    /**
     * Sets the Verdict Offence Desc on the charge
     * 
     * @param charge
     *            the charge to use
     * @param verdict
     *            the verdict to use
     * @throws OrderXMLException
     *             if there is a problem
     */
    private static void setVerdictOffenceDesc(Charge charge, XhbVerdict verdict) throws OrderXMLException {
        log.debug("setVerdictOffenceDesc - start");
        log.debug("setVerdictOffenceDesc - charge " + charge.getOffenceStatement());
        log.debug("setVerdictOffenceDesc - verdict " + verdict.getVerdictId());
        String description = verdict.getAltUncodedOffenceDesc();
        StringBuffer offenceDesc = new StringBuffer();
        if (null == description && null == verdict.getAppLesserOff()) {
            RefOffence refOffence = getRefOffence(verdict.getAltRefOffenceId());
            offenceDesc.append(refOffence.getOffenceDesc());
            if (null != refOffence.getOffenceDesc2()) {
                offenceDesc.append(STR_SPACE);
                offenceDesc.append(refOffence.getOffenceDesc2());
            }
        } else {
            if (null == description && null != verdict.getAppLesserOff()) {
                description = verdict.getAppLesserOff();
            }
            offenceDesc.append(description.substring(description.indexOf(STR_ASTERISK) + 1));
        }
        charge.setVerdict(offenceDesc.toString());
        log.debug("setVerdictOffenceDesc - verdict desc: " + offenceDesc);
        charge.setOffenceStatement(offenceDesc.toString());
    }

    /**
     * Sets the Plea Offence Desc on the charge
     * 
     * @param charge
     *            the charge to use
     * @param plea
     *            the plea to use
     * @throws OrderXMLException
     *             if there is a problem
     */
    private static void setPleaOffenceDesc(Charge charge, XhbPlea plea) throws OrderXMLException {
        log.debug("setPleaOffenceDesc - start");
        log.debug("setPleaOffenceDesc - charge " + charge.getOffenceStatement());
        log.debug("setPleaOffenceDesc - plea " + plea.getPleaId());
        String description = plea.getAltUncodedOffenceDesc();
        StringBuffer offenceDesc = new StringBuffer();
        if (null == description) {
            RefOffence refOffence = getRefOffence(plea.getAltRefOffenceId());
            offenceDesc.append(refOffence.getOffenceDesc());
            if (null != refOffence.getOffenceDesc2()) {
                offenceDesc.append(STR_SPACE);
                offenceDesc.append(refOffence.getOffenceDesc2());
            }
        } else {
            offenceDesc.append(description.substring(description.indexOf(STR_ASTERISK) + 1));
        }
        charge.setPlea(offenceDesc.toString());
        log.debug("setPleaOffenceDesc - plea desc: " + offenceDesc);
        charge.setOffenceStatement(offenceDesc.toString());
    }

    /**
     * Checks if offence is uncoded
     * 
     * @param refOffenceId
     *            the ref offence id to use
     * @return true if offence is uncoded matches the UNCODED_OFFENCE_CODE
     * @throws OrderXMLException
     *             if the ref offence can't be found
     */
    private static boolean isUncodedOffence(Integer refOffenceId) throws OrderXMLException {
        return (UNCODED_OFFENCE_CODE.equalsIgnoreCase(getRefOffence(refOffenceId).getOffenceCode())) ? true : false;
    }

    /**
     * Gets the RefOffence objectfor the given refOffenceId
     * 
     * @param refOffenceId
     *            the ref offence id to use
     * @return the RefOffence for the id
     * @throws OrderXMLException
     *             if the ref offence can't be found
     */
    private static RefOffence getRefOffence(Integer refOffenceId) throws OrderXMLException {
        try {
            return refOffenceHome.findByPrimaryKey(refOffenceId);
        } catch (FinderException ex) {
            throw new OrderXMLException("order.validation.refoffence.missing",
                    "Could not find ref offence for refOffenceId:" + refOffenceId, ex);
        }
    }

}
