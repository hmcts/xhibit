package uk.gov.courtservice.xhibit.business.services.charge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_charge.XhbJoinderCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_charge.XhbJoinderChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_defendant_on_case.XhbJoinderDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_defendant_on_case.XhbJoinderDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_xml.XhbJoinderXml;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_xml.XhbJoinderXmlBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_joinder_xml.XhbJoinderXmlBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderChargeInfoValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderExportValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AddCaseMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ChargeMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DefendantMVO;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformerFactory;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Abdul Rahim Hussain
 * @version 1.0
 * @version $Id: JoinderIndictmentHelper.java,v 1.51 2004/04/28 16:38:45 czvsws
 *          Exp $
 *
 * <Change History/> *
 *
 * <P>
 * 05/03/03 - ARH - First release.
 * </P>
 * <P>
 * 13/08/03 - ST - Use MultiCaseCourtLogCRUDValue to log the Joinder event.
 * Logging to all cases is now handled by the court log code and the event is
 * sent only once to the CJSE.
 * </P>
 * @author GJS - updated for generated MVOs
 * @version $Revision: 1.63 $
 */
public class JoinderIndictmentHelper {

    private static final Logger logger = CSServices.getLogger(JoinderIndictmentHelper.class);

    private static final String CASE_NOT_FOUND = "joinderindictmenthelper.casenotfound";

    private static final String READY = "R";

    private static final String EXPORTING = "E";

    private static final String COMPLETE = "C";

    private static final String FAILED = "F";

    
    public DefendantValue getDefendantAlias(XhbDefendantOnCase doc, Integer joinderId) throws ChargeControllerException {

        String methodName = "getDefendantAlias - ";
        logger.debug(methodName + "called with  defendantOnCaseId = " + doc.getDefendantOnCaseId());
        DefendantHelper defHelper = new DefendantHelper();
        // JoinderDefOnCase jdoc =
        // jdocM.findByPrimaryKey(doc.getDefendantOnCaseId());
        Collection jdocs = XhbJoinderDefendantOnCaseBeanHelper2.findByDefOnCaseId2AndJId(doc.getDefendantOnCaseId(), joinderId);
        try {
            switch (jdocs.size()) {
            case 0:
                logger.debug(methodName + "Exited OK");
                return defHelper.getDefendantDetails(doc.getDefendantId(), doc.getCaseId());
            case 1:
                XhbJoinderDefendantOnCase jdoc = (XhbJoinderDefendantOnCase) jdocs.iterator().next();
                logger.debug(methodName + "Exited OK");
                XhbDefendantOnCase defOnCase = jdoc.getXhbDefendantOnCaseByDefendantOnCaseId1();
                return defHelper.getDefendantDetails(defOnCase.getDefendantId(), defOnCase.getCaseId());
            default:
                logger
                        .error("findByDefOnCaseId2AndJId returned more than one row, there should be only one DefendantOnCase row per defendantOnCaseId2,joinderId");
                throw new EJBException(
                        "findByDefOnCaseId2AndJId returned more than one row, there should be only one DefendantOnCase row per defendantOnCaseId2,joinderId");
            }
        } catch (DefendantControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            Object[] params = e.getUserMessageAsMessage().getParameters();
            if (params.length > 0)
                throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), params, e.getMessage(), e);
            else
                throw new ChargeControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
        }
    }

    // returns hashmap of chargeIds[] that have the same joinderId as the
    // passed
    // chargeId, keyed on joinder Id
    public HashMap<Integer, Integer[]> getJoinderCharges(Integer chargeId) {

        String methodName = "getJoinderCharges - ";
        logger.debug(methodName + "called with  chargeId = " + chargeId);
        HashMap<Integer, Integer[]> joinedCharges = new HashMap<Integer, Integer[]>();
        Integer joinderId = getJoinderId(chargeId);
        // expected to return null if no joinder charges
        if (joinderId == null) {
            logger.debug("No joinder found for chargeId " + chargeId);
            return null;
        }
        Collection jcCol = XhbJoinderChargeBeanHelper2.findByJoinderId(joinderId);
        // instantiate chargeIds array
        Integer[] chargeIds = new Integer[jcCol.size()];
        Iterator jcIt = jcCol.iterator();
        // iterate collection and populate chargeIds array
        for (int i = 0; jcIt.hasNext(); i++) {
            XhbJoinderCharge joinderCharge = (XhbJoinderCharge) jcIt.next();
            chargeIds[i] = joinderCharge.getChargeId();
        }
        joinedCharges.put(joinderId, chargeIds);
        logger.debug(methodName + "Exited OK");
        return joinedCharges;
    }

    public void exportJoinderIndictment(Integer caseId, GetChargesHelper gcHelper) throws ChargeControllerException,
            ExportChargeInProgressException {

        String methodName = "exportJoinderIndictment - ";
        logger.debug(methodName + "called with  caseId = " + caseId);
        XMLServicesImpl xmlService = XMLServicesImpl.getInstance();
        XhbCase caze = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
        Iterator charges = caze.getXhbCharges().iterator();
        // store a list of joinderIds to ensure that the same joinderId is not
        // processed more than once
        HashSet joinderIds = new HashSet();
        // loop charges for this case
        while (charges.hasNext()) {
            // get the chargeId
            XhbCharge charge = (XhbCharge) charges.next();
            Integer chargeId = charge.getChargeId();

            // is this charge a joinder?
            Collection joinderChargeCol = XhbJoinderChargeBeanHelper2.findByChargeId(chargeId);

            // if nothing found, this is not a joinder indictment, so skip
            if (joinderChargeCol.size() == 0) {
                logger.debug("Not a joinder charge:" + chargeId);
                continue;
            }
            logger.debug("Joinder charge:" + chargeId);
            logger.debug("No: of joinder charges:" + joinderChargeCol.size());
            Iterator joinderCharges = joinderChargeCol.iterator();
            // if joinder charge get joinderId
            XhbJoinderCharge joinderCharge = (XhbJoinderCharge) joinderCharges.next();
            Integer joinderId = joinderCharge.getJoinderId();
            // has this joinder already been processed if so skip
            if (joinderIds.contains(joinderId))
                continue;
            joinderIds.add(joinderId);
            logger.debug("Joinder charge:" + chargeId);
            // if joinder get charge
            ChargeCompositeValue chargeCV = gcHelper.getJoinderCharges(caseId, joinderId);
            logger.debug("chargeCV:" + chargeCV);
            // clear unwanted fields to reduce size of xml generated
            chargeCV.setChargeLogItems(null);
            // only include the fields of the Charge Composite Value for
            // Mercator
            JoinderExportValue joinderEV = new JoinderExportValue();
            createExportValue(chargeCV, joinderEV);
            logger.debug("joinderEV:" + joinderEV);
            // convert it to xml
            String xmlClob = xmlService.getStringXML(xmlService.createDocFromValue(joinderEV, true));
            logger.debug("xmlClob:" + xmlClob);

            // save it to joinderxml
            XhbJoinderXmlBasicValue joinderXmlBV = new XhbJoinderXmlBasicValue();
            joinderXmlBV.setJoinderId(joinderId);
            joinderXmlBV.setStatus(READY);
            joinderXmlBV.setXmlClob(xmlClob);
            logger.debug("joinderXmlBV:" + joinderXmlBV);

            createUpdateJoinderXml(joinderXmlBV);
        }
        logger.debug(methodName + "Exited OK");
    }

    private void createExportValue(ChargeCompositeValue chargeCV, JoinderExportValue joinderEV)
            throws ChargeControllerException {

        try {
            VOTransformerFactory transformer = VOTransformerFactory.getInstance();
            VOTransformer transformCase = transformer.createVOTransformer("AddCaseValue");
            VOTransformer transformDefendant = transformer.createVOTransformer("DefendantValue");
            VOTransformer transformCharge = transformer.createVOTransformer("ChargeValue");
            XhbCaseBasicValue caseBasicV = chargeCV.getCaseBasicValue();
            AddCaseValue addCaseV = new AddCaseValue();
            addCaseV.setId(caseBasicV.getCaseId());
            addCaseV.setCourtID(caseBasicV.getCourtId());
            addCaseV.setCaseType(caseBasicV.getCaseType());
            addCaseV.setCaseNumber(caseBasicV.getCaseNumber());
            joinderEV.setCaseMVO((AddCaseMVO) transformCase.transformVO(addCaseV));
            Collection defendantVCol = chargeCV.getAllDefendants();
            Iterator iterDefendant = defendantVCol.iterator();
            DefendantMVO[] defendantMVO = new DefendantMVO[defendantVCol.size()];
            for (int i = 0; iterDefendant.hasNext(); i++) {
                defendantMVO[i] = (DefendantMVO) transformDefendant.transformVO((DefendantValue) iterDefendant.next());
            }
            joinderEV.setDefendantMVO(defendantMVO);
            Collection chargeVCol = chargeCV.getCharges();
            Iterator iterCharge = chargeVCol.iterator();
            ChargeMVO[] chargeMVO = new ChargeMVO[chargeVCol.size()];
            // Cascading JoinderChargeInfoValue of Charges to have one array
            Collection joinderCICollection = Collections.synchronizedCollection(new Vector());
            for (int i = 0; iterCharge.hasNext(); i++) {
                ChargeValue chargeV = (ChargeValue) iterCharge.next();
                chargeMVO[i] = (ChargeMVO) transformCharge.transformVO(chargeV);
                JoinderChargeInfoValue[] joinderValue = chargeV.getJoinderChargeInfoValues();
                for (int j = 0; j < joinderValue.length; j++) {
                    joinderCICollection.add(joinderValue[j]);
                }
            }
            joinderEV.setChargeMVO(chargeMVO);
            Iterator joinderCIIterator = joinderCICollection.iterator();
            JoinderChargeInfoValue[] joinderCIValue = new JoinderChargeInfoValue[joinderCICollection.size()];
            for (int k = 0; joinderCIIterator.hasNext(); k++) {
                joinderCIValue[k] = (JoinderChargeInfoValue) joinderCIIterator.next();
            }
            joinderEV.setJoinderCIValue(joinderCIValue);
        } catch (TransformationException e) {
            handleWrapAndRethrowException(e);
        }
    }

    private void createUpdateJoinderXml(XhbJoinderXmlBasicValue jx)
            throws ExportChargeInProgressException {

        String methodName = "createUpdateJoinderXml - ";
        logger.debug(methodName + "called.");
        Iterator joinderXmls = XhbJoinderXmlBeanHelper2.findByJoinderId(jx.getJoinderId()).iterator();
        if (joinderXmls.hasNext()) {
            // get JoinderXmlBasicValue
            XhbJoinderXml joinderXml = (XhbJoinderXml) joinderXmls.next();

            XhbJoinderXmlBasicValue jxbv = joinderXml.getData();

            // don't update if currently in progress
            if (EXPORTING.equals(jxbv.getStatus()))
                throw new ExportChargeInProgressException();
            // set attributes
            jx.setJoinderXmlId(jxbv.getJoinderXmlId());
            jx.setVersion(jxbv.getVersion());
            // update
            XhbJoinderXmlBeanHelper2.update(jx);
            logger.debug("joinderXml with id =" + jxbv.getJoinderXmlId() + " updated");
        } else {
            // create
            XhbJoinderXmlBeanHelper2.create(jx);
            logger.debug("joinderXml with created");
        }
        logger.debug(methodName + "Exited OK");
    }

    /**
     * Gets the joinder id for a given charge
     *
     * @param chargeId
     * @return The joinder id or null if none found
     */
    public Integer getJoinderId(Integer chargeId) {

        Collection joinderCharges = XhbJoinderChargeBeanHelper2.findByChargeId(chargeId);

        // if nothing found, this is not a joinder indictment, so return null
        if (joinderCharges.isEmpty()) {
            return null;
        }
        // if more than one in collection then this is an error
        if (joinderCharges.size() > 1) {
            logger
                    .error("findByChargeId returned more than one row, there should be only one joinderCharge row per chargeId");
            throw new EJBException(
                    "findByChargeId returned more than one row, there should be only one joinderCharge row per chargeId");
        }
        // get joinder charge
        XhbJoinderCharge jc = (XhbJoinderCharge) joinderCharges.iterator().next();
        // get joinder charges with the same joinder Id as the passed chargeId
        Integer joinderId = jc.getJoinderId();
        return joinderId;
    }
    
    /**
     * Find the Joinder ID for a specific Offence
     *
     * @param offenceId
     *            the offence id of the entity you wish to know the joinder id
     * @return Integer the joinder id for this offence
     */
    public static Integer getJoinderIdForOffence(Integer offenceId) {
        logger.debug("getJoinderIdForOffence(" + offenceId + ") called");
        try {
            Collection col = XhbJoinderChargeBeanHelper2.findByOffenceId(offenceId);
            Iterator iter = col.iterator();

            logger.debug("Collection size = " + col.size());
            if (col.size() > 1) {
                throw new FinderException("More than 1 value returned");
            }
            if (iter.hasNext()) {
                return ((XhbJoinderCharge) iter.next()).getJoinderId();
            } else {
                return null;
            }
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, JoinderIndictmentHelper.class, e.toString());
            throw new EJBException(e);
        }
    }

    /**
     * Utility method to deal with handling Exceptions from other controllers,
     * and wrapping and rethrowing them as ChargeControllerException
     *
     * @param ex
     *            The Exception to handle and rethrow, must be a
     *            <code>CSRecoverableException</code> or child of
     * @throws ChargeControllerException
     *             the new Exception to throw
     */
    private void handleWrapAndRethrowException(CSRecoverableException ex) throws ChargeControllerException {

        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
        if (ex.getUserMessageAsMessage().getParameters().length > 0) {
            throw new ChargeControllerException(ex.getUserMessageAsMessage().getKey(), ex.getUserMessageAsMessage()
                    .getParameters(), ex.getMessage(), ex);
        } else {
            throw new ChargeControllerException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }
    }
}