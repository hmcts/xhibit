package uk.gov.courtservice.xhibit.business.services.publicdisplay.data.ejb;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.DataSourceFactory;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.Data;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.DataSource;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.DisplayDocumentURI;

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
 * Company: EDS
 * </p>
 * 
 * @ejb.bean name="PDDataController" description="Public Display Data Controller
 *           Bean" type="Stateless" view-type="remote"
 *           jndi-name="PDDataControllerHome"
 * @ejb.transaction type="Required"
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class PDDataControllerBean implements SessionBean {
    private static final Logger log = CSServices.getLogger(PDDataControllerBean.class);

    protected SessionContext ctx;

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbActivate() {
        log.debug("ejbActivate()");
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbRemove() {
        log.debug("ejbRemove()");
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbPassivate() {
        log.debug("ejbPassivate()");
    }

    /**
     * Sets the session context.
     * 
     * @param ctx
     *            SessionContext Context for session
     */
    public void setSessionContext(SessionContext ctx) {
        log.debug("setSessionContext(SessionContext ctx)");
        this.ctx = ctx;
    }

    public void ejbCreate() throws CreateException {
        log.debug("ejbCreate()");

    }

    /**
     * Retrieve data for a given document.
     * 
     * @param uri
     *            the URi of the document to obtain data for.
     * @return the Data.
     * @post return != null
     * @pre uri != null
     * 
     * @ejb.interface-method view-type="remote"
     */
    public Data getData(DisplayDocumentURI uri) {
        DataSource dataSource = DataSourceFactory.getDataSource(uri);
        dataSource.retrieve();
        Data data = dataSource.getData();
        return data;
    }

}