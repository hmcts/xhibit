package uk.gov.courtservice.xhibit.client.results.disposals.disposallineprocessor;

import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: DataDisposalLineProcessor
 * </p>
 * <p>
 * Description: DataDisposalLineProcessor set the data to the specified value
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public class DataDisposalLineProcessor extends AbstractDisposalLineProcessor {
    /**
     * The new value
     */
    private final String data;

    /**
     * Construct a new processor to set the data as specified
     */
    public DataDisposalLineProcessor(String data) {
        this.data = data;
    }

    /**
     * Process implementation, line must not be null
     */
    protected void processImpl(DisposalLineReferenceValue line) {
        line.setData(data);
    }
}
