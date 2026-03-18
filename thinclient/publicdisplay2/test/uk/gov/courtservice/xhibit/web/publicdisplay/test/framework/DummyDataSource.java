package uk.gov.courtservice.xhibit.web.publicdisplay.test.framework;

import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataSource;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;


/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 *
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Neil Ellis
 * @version $Revision: 1.2 $
 */
public class DummyDataSource extends DataSource
{
    /**
     * Creates a new DummyDataSource object.
     *
     * @param uri TODO:
     */
    public DummyDataSource(DisplayDocumentURI uri)
    {
        super(uri);
    }

    /**
     * TODO:
     */
    public void retrieve()
    {
        //getData().put("test", new Object());
    }
}
