package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DefOnOffenceMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.LinkCountDefMVO;

/**
 * <p>
 * Title: LinkCountDefTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (LinkCountDefValue) and
 * convert it to LinkCountDefMVO
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Marie Holmberg
 * @history 2002-04-22 MH Added debugging section.
 * @version 1.0
 * @author GJS - updated for generated MVOs
 * @version 1.1
 */

public class LinkCountDefTransformer implements VOTransformer {
    // the logger
    private static Logger log = CSServices.getLogger(LinkCountDefTransformer.class);

    public LinkCountDefTransformer() {
        // empty
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instanceof the CSValueObject before trying to cast it.
        if (!(valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type LinkCountDefValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        LinkCountDefMVO linkCountDefMVO = new LinkCountDefMVO();
        LinkCountDefValue linkCountDefValue = (LinkCountDefValue) valueObject;

        DefOnOffenceMVO[] defOnOffenceMVOs = null;

        /**
         * Get the collection of defendants and counts and convert to
         * Integer[][].
         * 
         * Vector countDefPairs = new Vector(); Integer[] pairOne = {new
         * Integer(1), new Integer(2)}; Integer[] pairTwo = {new Integer(1), new
         * Integer(3)}; countDefPairs.add(pairOne); countDefPairs.add(pairTwo);
         */
        Collection defendantOnOffenceValues = linkCountDefValue.getDefendantOnOffenceValues();

        if (defendantOnOffenceValues != null) {
            defOnOffenceMVOs = new DefOnOffenceMVO[defendantOnOffenceValues.size()];
            Iterator doofs = defendantOnOffenceValues.iterator();

            DefOnOffenceMVO defOnOffenceMVO = null;

            String dateOfArrest = null;
            String dateOfCharge = null;
            
            for (int i = 0; doofs.hasNext(); i++) {
                dateOfArrest = null;
                dateOfCharge = null;
                
                DefendantOnOffenceValue dof = (DefendantOnOffenceValue) doofs.next();

                defOnOffenceMVO = new DefOnOffenceMVO();
                if (dof.getOffenceId() != null) {
                    defOnOffenceMVO.setOffenceId(dof.getOffenceId().intValue());
                }
                if (dof.getDefendantId() != null) {
                    defOnOffenceMVO.setDefendantId(dof.getDefendantId().intValue());
                }
                defOnOffenceMVO.setCrn(dof.getCrn());
                
                if(dof.getIsCommittedOnBail()!=null)
                {
                    defOnOffenceMVO.setIsCommittedOnBail(dof.getIsCommittedOnBail());
                }
                
                if(dof.getSequenceNo()!=null)
                {
                    defOnOffenceMVO.setSeqNo(dof.getSequenceNo().intValue());
                }
                
                //check if the dates are null, if not set the Strings
                if (dof.getDateOfArrest() != null) {
                    dateOfArrest = dof.getDateOfArrest().getTime().toString();
                }
                
                if(dateOfArrest!=null)
                {
                    defOnOffenceMVO.setArrestDate(dateOfArrest);
                }
                
                if (dof.getDateOfCharge() != null) {
                    dateOfCharge = dof.getDateOfCharge().getTime().toString();
                }
                
                if(dateOfCharge!=null)
                {
                    defOnOffenceMVO.setChargeDate(dateOfCharge);
                }

                if (dof.getVersion() != null) {
                    defOnOffenceMVO.setVersion(dof.getVersion().intValue());
                }
                
                if (dof.getInterimD20() != null) {
                	defOnOffenceMVO.setInterimD20(dof.getInterimD20());
                }
                
                if (dof.getId() != null) {
                    //The DOOID is in the ID of defendantOnOffenceValue
                    log.debug("LinkCountDef Transform: DefOnOffenceId:" + dof.getId());
                    defOnOffenceMVO.setDefOnOffenceId(dof.getId().intValue());
                    defOnOffenceMVO.setId(dof.getId().intValue());
                }
                
                if (dof.getObsInd() != null) {
                    defOnOffenceMVO.setObsInd(dof.getObsInd());
                }
                
                defOnOffenceMVOs[i] = defOnOffenceMVO;
            }
        } else {
            log.debug("LinkCountDef Transform: LinkCountDefValue contains no defendantOnOffenceValues");
        }

        // Set linkCountDefMVO values

        if (linkCountDefValue.getCaseID() != null) {
            linkCountDefMVO.setCaseID(linkCountDefValue.getCaseID().intValue());
        }
        if (linkCountDefValue.getCourtID() != null) {
            linkCountDefMVO.setCourtID(linkCountDefValue.getCourtID().intValue());
        }
        linkCountDefMVO.setInCourt(linkCountDefValue.isInCourt());
        if (defOnOffenceMVOs != null) {
            linkCountDefMVO.setDefOnOffenceMVO(defOnOffenceMVOs);
        }
        if (linkCountDefValue.getVersion() != null) {
            linkCountDefMVO.setVersion(linkCountDefValue.getVersion().intValue());
        }
        if (linkCountDefValue.getId() != null) {
            linkCountDefMVO.setId(linkCountDefValue.getId().intValue());
        }

        return linkCountDefMVO;
    }

    public Object transformOutput(Object result) {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer
         *       method
         */
        return result;
    }
}