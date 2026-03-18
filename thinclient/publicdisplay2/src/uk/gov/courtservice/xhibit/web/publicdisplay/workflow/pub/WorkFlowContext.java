package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub;

import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataContext;
import uk.gov.courtservice.xhibit.web.publicdisplay.configuration.DisplayConfigurationReader;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.5 $
 */
public class WorkFlowContext {
    private DataContext dataContext;

    private DisplayConfigurationReader displayConfigurationReader;

    private WorkFlowContext(final DisplayConfigurationReader displayConfigurationReader) {
        this.displayConfigurationReader = displayConfigurationReader;
    }

    private WorkFlowContext() {
        this.displayConfigurationReader = DisplayConfigurationReader.getInstance();
    }

    /**
     * TODO:
     * 
     * @return TODO:
     */
    public static WorkFlowContext newInstance() {
        return new WorkFlowContext();
    }

    /**
     * TODO:
     * 
     * @param displayConfigurationReader
     *            TODO:
     * 
     * @return TODO:
     */
    public static WorkFlowContext newInstance(final DisplayConfigurationReader displayConfigurationReader) {
        return new WorkFlowContext(displayConfigurationReader);
    }

    /**
     * TODO:
     * 
     * @param dataContext
     *            TODO:
     */
    public void setDataContext(DataContext dataContext) {
        this.dataContext = dataContext;
    }

    /**
     * TODO:
     * 
     * @return TODO:
     */
    public DataContext getDataContext() {
        return dataContext;
    }

    /**
     * TODO:
     * 
     * @return TODO:
     */
    public DisplayConfigurationReader getDisplayConfigurationReader() {
        return displayConfigurationReader;
    }
}
