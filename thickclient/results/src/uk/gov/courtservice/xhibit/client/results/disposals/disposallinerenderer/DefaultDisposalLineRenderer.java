package uk.gov.courtservice.xhibit.client.results.disposals.disposallinerenderer;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentFactory;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponentFactory;

/**
 * <p>
 * Title: DefaultDisposalLineRenderer
 * </p>
 * <p>
 * Description: The default renderer provides the default data component!
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.5 $
 */
public class DefaultDisposalLineRenderer extends AbstractDisposalLineRenderer {
    /**
     * Create an instance of the data component
     */
    protected DataComponent createDataComponent() {
        return DataComponentFactory.create(getReference());
    }
    
    /**
     * Create an instance of the insert component
     */
    protected InsertComponent createInsertComponent() {
        return InsertComponentFactory.create(getReference());
    }
}
