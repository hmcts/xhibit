package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.AddressMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DefOnOffenceMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DefendantMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.LinkCountDefMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.OffenceMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.OffenceMVOSequence;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.OffenceMVOSequence2;

/**
 * <p>
 * Title: OffenceTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (OffenceValue) and
 * convert it to OffenceMVO
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Marie Holmberg
 * @version 1.0
 * @author GJS - updated for generated MVOs
 * @version 1.1
 */

public class OffenceTransformer implements VOTransformer {
    // the logger
    private static Logger log = CSServices.getLogger(OffenceTransformer.class);

    public OffenceTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instanceof the CSValueObject before trying to cast it.
        if (!(valueObject instanceof OffenceValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type OffenceValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        OffenceMVO offenceMVO = new OffenceMVO();
        OffenceValue offenceValue = (OffenceValue) valueObject;

        DefendantMVO[] defendantMVOs = null;
        DefendantTransformer defendantTransformer = null;
        
        AddressMVO addressMVO = null;
        AddressTransformer addressTransformer = null;
        AddressMVO[] addressMVOArray = new AddressMVO[1];
        OffenceMVOSequence offenceMVOSequence = new OffenceMVOSequence();
        OffenceMVOSequence2 offenceMVOSequence2 = null;

        int[] defendantIDs = null;
        Vector vDefs = null;
        int numberOfDefs = 0;

        String startDate = null;
        String endDate = null;
        
        // transform all the DefendantValues in a Collection to DefendantMVOs in
        // array
        if (offenceValue.getDefendantValues() != null) {
           
            // get the class transformer for Defendant
            defendantTransformer = new DefendantTransformer();

            // collection of defendants, cast to Vector
            vDefs = (Vector) offenceValue.getDefendantValues();

            // set the array to number of defendants in the collection
            numberOfDefs = vDefs.size();
            defendantMVOs = new DefendantMVO[numberOfDefs];

            // Loop through the vector and add the defendantMVO to the array
            for (int i = 0; i < numberOfDefs; i++) {
                defendantMVOs[i] = (DefendantMVO) defendantTransformer.transformVO(vDefs.get(i));
            }
            vDefs = null;
            numberOfDefs = 0;
        } else {
            log.debug("Offence Transform: OffenceValue contains no DefendantValues");
        }

        // Get the collection of DefendantIDs and convert to an Integer array
        if (offenceValue.getDefendantIDs() != null) {
            vDefs = (Vector) offenceValue.getDefendantIDs();

            numberOfDefs = vDefs.size();

            defendantIDs = new int[numberOfDefs];

            for (int i = 0; i < numberOfDefs; i++) {
                defendantIDs[i] = ((Integer) vDefs.elementAt(i)).intValue();
            }

            vDefs = null;
            numberOfDefs = 0;
        } else {
            log.debug("Offence Transform: OffenceValue contains no DefendantIDs");
        }

        // Set offenceMVO values

        if (offenceValue.getOffenceID() != null) {
            offenceMVO.setOffenceID(offenceValue.getOffenceID().intValue());
        }
        if (offenceValue.getChargeID() != null) {
            offenceMVO.setChargeID(offenceValue.getChargeID().intValue());
        }
        if (offenceValue.getRefOffenceID() != null) {
            offenceMVO.setRefOffenceID(offenceValue.getRefOffenceID().intValue());
        }
        if (defendantIDs != null) {
            offenceMVOSequence.setDefendantIDs(defendantIDs);
        }
        offenceMVO.setCrestOffenceFreeText(offenceValue.getCrestOffenceFreeText());
        offenceMVO.setCrestHOClass(offenceValue.getCrestHOClass());
        offenceMVO.setCrestHOSubclass(offenceValue.getCrestHOSubclass());
        if (offenceValue.getCrestOffenceID() != null) {
            offenceMVO.setCrestOffenceID(offenceValue.getCrestOffenceID().intValue());
        }
        if (offenceValue.getCrestOffenceSeqNo() != null) {
            offenceMVO.setCrestOffenceSeqNo(offenceValue.getCrestOffenceSeqNo().intValue());
        }
        if (offenceValue.getMultiple() != null) {
            offenceMVO.setMultiple(offenceValue.getMultiple().intValue());
        }
        offenceMVO.setOffenceDescription(offenceValue.getOffenceDescription());
        if (offenceValue.getRefSystemCodeID() != null) {
            offenceMVO.setRefSystemCodeID(offenceValue.getRefSystemCodeID().intValue());
        }
        if (offenceValue.getCourtID() != null) {
            offenceMVO.setCourtID(offenceValue.getCourtID().intValue());
        }
        offenceMVO.setPlea(offenceValue.getPlea());
        if (offenceValue.getCaseID() != null) {
            offenceMVO.setCaseID(offenceValue.getCaseID().intValue());
        }
        offenceMVO.setInCourt(offenceValue.isInCourt());
        if (defendantMVOs != null) {
            offenceMVOSequence.setDefendantMVO(defendantMVOs);
        }
        offenceMVO.setDirty(offenceValue.isDirty());

        if (offenceValue.getVersion() != null) {
            offenceMVO.setVersion(offenceValue.getVersion().intValue());
        }
        if (offenceValue.getId() != null) {
            offenceMVO.setId(offenceValue.getId().intValue());
        }
        
        //Only going to get one Offence address as offenceValue only has one
        //get and transform the AddressValue to AddressMVO
        if (offenceValue.getAddressValue() != null) {
            addressTransformer = new AddressTransformer();
            addressMVO = (AddressMVO) addressTransformer.transformVO(offenceValue.getAddressValue());            
            addressMVOArray[0] = addressMVO; 
            offenceMVOSequence2 = new OffenceMVOSequence2();
            offenceMVOSequence2.setAddressMVO(addressMVOArray);
            offenceMVO.setOffenceMVOSequence2(offenceMVOSequence2);
        }

        //check if the dates are null, if not set the Strings
        if (offenceValue.getOffenceStartDateTime() != null) {
            startDate = offenceValue.getOffenceStartDateTime().getTime().toString();
        }
        
        if(startDate!=null)
        {
            offenceMVO.setStartDate(startDate);
        }

        if (offenceValue.getOffenceEndDateTime() != null) {
            endDate = offenceValue.getOffenceEndDateTime().getTime().toString();
        }
        
        if(endDate!=null)
        {
            offenceMVO.setEndDate(endDate);
        }
        
        if(offenceValue.getForceLocationCode()!=null)
        {
            offenceMVO.setForceLocationCode(offenceValue.getForceLocationCode());
        }
        
        // set LinkCountDefMVO
        offenceMVOSequence.setLinkCountDefMVO(getLinkCountDefMVO(offenceValue));
        
        offenceMVO.setOffenceMVOSequence(offenceMVOSequence);        

        return offenceMVO;
    }

    public Object transformOutput(Object result) {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer
         *       method
         */
        return result;
    }

    /**
     * @description Creates a LinkCountDefMVO based on the defendantIDs array of
     *              the offenceMVO
     * @param offence.
     *            Returns null if no defendants found
     * @return
     */
    private LinkCountDefMVO getLinkCountDefMVO(OffenceValue offenceValue) {
        HashMap doofs = offenceValue.getDefOnOffenceBasicValues();

        // if no defendants found return null
        if (doofs == null || doofs.size() < 1) {
            log
                    .debug("Offence Transform: OffenceValue contains no DefOnOffenceBasicValues. LinkCountDefMVO will be null");
            return null;
        }

        DefOnOffenceMVO[] defOnOffenceMVOs = new DefOnOffenceMVO[doofs.size()];

        // get offenceId
        Integer offenceId = offenceValue.getOffenceID();
        
        Integer defendantId = null;
        Integer dooId = null;
        Integer docId = null;
        Integer seqNo = null;
        String arrestDate = null;
        String chargeDate = null;
        
        Iterator defIt = doofs.keySet().iterator();

        DefOnOffenceMVO defOnOffenceMVO = null;

        for (int i = 0; defIt.hasNext(); i++) {
            defendantId = (Integer) defIt.next();
            
            XhbDefendantOnOffenceBasicValue dof = (XhbDefendantOnOffenceBasicValue) doofs.get(defendantId);

            dooId = dof.getDefendantOnOffenceId();
            docId = dof.getDefendantOnCaseId();
            seqNo = dof.getSeqNo();
            
            defOnOffenceMVO = new DefOnOffenceMVO();

            if (offenceId != null) {
                defOnOffenceMVO.setOffenceId(offenceId.intValue());
            }
            if (defendantId != null) {
                defOnOffenceMVO.setDefendantId(defendantId.intValue());
            }
            if (dooId != null) {
                defOnOffenceMVO.setDefOnOffenceId(dooId.intValue());
            }
            if (docId != null) {
                defOnOffenceMVO.setDefOnCaseId(docId.intValue());
            }
            if (seqNo != null) {
                defOnOffenceMVO.setSeqNo(seqNo.intValue());
            }
            
            defOnOffenceMVO.setCrn(dof.getCrnId());
            defOnOffenceMVO.setIsCommittedOnBail(dof.getIsCommittedOnBail());
            
            if (dof.getArrestDate() != null) {
                arrestDate = dof.getArrestDate().toString();
                defOnOffenceMVO.setArrestDate(arrestDate);
            }

            if (dof.getChargeDate() != null) {
                chargeDate = dof.getChargeDate().toString();
                defOnOffenceMVO.setChargeDate(chargeDate);
            }
            
            if (dof.getInterimD20() != null) {
            	defOnOffenceMVO.setInterimD20(dof.getInterimD20());
            }
            
            if (dof.getObsInd() != null) {
                defOnOffenceMVO.setObsInd(dof.getObsInd());
            }
            
            defOnOffenceMVOs[i] = defOnOffenceMVO;
            
            defendantId = null;
            dooId = null;
            docId = null;
            seqNo = null;
            chargeDate=null;
            arrestDate=null;
        }

        // instantiate LinkCountDefMVO
        LinkCountDefMVO linkCountDefMVO = new LinkCountDefMVO();

        if (offenceValue.getCaseID() != null) {
            linkCountDefMVO.setCaseID(offenceValue.getCaseID().intValue());
        }
        if (offenceValue.getCourtID() != null) {
            linkCountDefMVO.setCourtID(offenceValue.getCourtID().intValue());
        }

        linkCountDefMVO.setInCourt(offenceValue.isInCourt());

        if (defOnOffenceMVOs != null) {
            linkCountDefMVO.setDefOnOffenceMVO(defOnOffenceMVOs);
        }

        if (offenceValue.getVersion() != null) {
            linkCountDefMVO.setVersion(offenceValue.getVersion().intValue());
        }
        if (offenceValue.getId() != null) {
            linkCountDefMVO.setId(offenceValue.getId().intValue());
        }

        return linkCountDefMVO;
    }
}