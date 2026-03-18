package uk.gov.courtservice.xhibit.services.gdgateway.inbound;

import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.InboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.InboundGdgateDatabase;

/**
 * The class <code>GdGatewayControllerBean</code> acts as a facade over the
 * processing of the inbound messages from the CJSEService Submit Web Service.
 *
 * @ejb.bean name="GdGatewayController" description="GD Gateway Controller Bean"
 *           type="Stateless" view-type="local"
 *           jndi-name="GdGatewayControllerHome"
 *           local-jndi-name="GdGatewayControllerLocalHome"
 *
 * @author Will Fardell & Steve Tully & Simon Gilmore
 * @version $Id: GdGatewayControllerBean.java,v 1.1 2006/08/18 14:18:54 bzjrnl
 *          Exp $
 */
public class GdGatewayControllerBean extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(GdGatewayControllerBean.class);

    private static final int ORACLE_DUPLICATE_KEY = 20201;

    private InboundGdgateDatabase database;

    /**
     * Initialises all of the instance variables for this session bean.
     *
     * @see uk.gov.courtservice.framework.business.services.CSSessionBean
     *      #ejbCreate()
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        database = new InboundGdgateDatabase();
    }

    /**
     * Inserts the inbound message into the GdGateway db.
     *
     * @ejb.interface-method view-type="local"
     * @ejb.transaction type="Required"
     */
    public void insertItemInbound(InboundMessageVO inboundMessageVO) {
        try {
            database.insertInboundMessages(inboundMessageVO);
        } catch (DataAccessException dae) {
            log.info("[insertItemInbound] : catch DataAccessException " + dae.getClass());
            Throwable t = dae.getCause();
            if ((t instanceof SQLException) && (((SQLException) t).getErrorCode() == ORACLE_DUPLICATE_KEY)) {
                // This key has been sent before but SCJSE (ExISS) probably
                // did not receive the response and re-sent the message.
                // We need to send the same response. As the unique key is in
                // database, the insert was previously successful.
                log.warn("[insertItemInbound] Already received inbound message with request id = "
                        + inboundMessageVO.getRequestIdentifier() + " and source id = "
                        + inboundMessageVO.getSourceIdentifier());
            } else {
                log.fatal("[insertItemInbound] catch DataAccessException about to rethrow");
                log.fatal("[insertItemInbound] catch DataAccessException col      " + dae.getColumn());
                log.fatal("[insertItemInbound] catch DataAccessException error id " + dae.getErrorID());
                log.fatal("[insertItemInbound] catch DataAccessException message  " + dae.getMessage());
                log.fatal("[insertItemInbound] catch DataAccessException cause    " + dae.getCause());
                if (t instanceof SQLException) {
                    log.fatal("[insertItemInbound] SQLException");
                    SQLException sqle = (SQLException) t;
                    log.fatal("[insertItemInbound] SQLException error code " + sqle.getErrorCode());
                    log.fatal("[insertItemInbound] SQLException sql state  " + sqle.getSQLState());
                    log.fatal("[insertItemInbound] SQLException message    " + sqle.getMessage());
                    log.fatal("[insertItemInbound] SQLException cause      " + sqle.getCause());
                }

                throw dae;
            }
        } catch (Throwable t) {
            log.fatal("[insertItemInbound] catch Throwable " + t.getClass());
        }
    }

}
