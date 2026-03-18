package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import java.util.ListResourceBundle;

import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;

/**
 * <p>
 * Title: VOTransformerBundle
 * </p>
 * <p>
 * Description: This class lists the VOs that can be transformed. If the entry
 * is an instance of class then a new instance will be created otherwise the
 * instantiated VOTransformer will be used
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.1
 * @history Kelvin Davies - CCN1263 - 29/04/2009 - Updated to include new caseUpdateTransform().
 */
public class VOTransformerBundle extends ListResourceBundle {

    public VOTransformerBundle() {
    }

    protected Object[][] getContents() {
        return new Object[][] { { "AddressValue", new AddressTransformer() },
                { "BreachValue", new BreachTransformer() }, { "ChargeValue", new ChargeTransformer() },
                { "ChargeValue[]", new ChargeTransformer() }, { "DefendantValue", new DefendantTransformer() },
                { "DelChargeValue", new DeleteChargeTransformer() },
                { "DelOffenceValue", new DeleteOffenceTransformer() },
                { "JoinderOffenceValue", new OffenceTransformer() },
                { "LinkCountDefValue", new LinkCountDefTransformer() }, { "OffenceValue", new OffenceTransformer() },
                { "SignIndValue", new SignIndTransformer() }, { "AddCaseValue", new AddCaseTransformer() },
                { "CaseUpdateValue", new UpdateCaseTransform() },
                { "ResultsSaveValue", ResultTransformer.class },
                { "OriginalChargeVO[]", new OriginalChargeTransformer() },
                { "DefendantOnCaseValue", new DefendantOnCaseTransformer() }};
    }
}