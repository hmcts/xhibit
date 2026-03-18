package uk.gov.courtservice.xhibit.business.services.witness.reference;

import uk.gov.courtservice.xhibit.business.services.witness.reference.interfaces.WitnessReferenceData;

/**
 * <p>
 * Title: Witness Reference Data Factory
 * </p>
 * <p>
 * Description: This is used to get instances of WitnessReferenceData from which
 * various items of refernce data can be obtained..
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.7 $
 * @see WitnessReferenceData
 * 
 */
public class WitnessReferenceDataFactory {
    private WitnessReferenceDataFactory() {
    }

    public static WitnessReferenceData getWitnessReferenceData() {
        return new WitnessReferenceDataImpl();
    }
}
