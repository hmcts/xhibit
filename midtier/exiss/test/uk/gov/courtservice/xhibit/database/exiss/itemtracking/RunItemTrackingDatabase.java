package uk.gov.courtservice.xhibit.database.exiss.itemtracking;

import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.vos.exiss.itemtracking.ItemOutboundTrackingVO;

/**
 * <p>
 * Title: RunMessageBuilderDatabase
 * </p>
 * <p>
 * Description: Application for testing database, not a test as relies on
 * database. Assumes there are records.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jeremy Shields, Will Fardell
 * @version $id:$
 */
public class RunItemTrackingDatabase {

    /** The log4j <code>Logger</code> instance */
    protected static final Logger log = CSServices.getLogger(RunItemTrackingDatabase.class);

    static {
        // Initialise Log4j For Testing
        BasicConfigurator.configure();

        // Initialise Database Properties
        System.setProperty("database.driver", "oracle.jdbc.xa.client.OracleXADataSource");
        System.setProperty("database.url", "jdbc:oracle:thin:@130.177.1.35:1521:CSDBDEV3");
        System.setProperty("database.user", "exiss");
        System.setProperty("database.password", "exiss");
    }

    public static void main(String[] args) {
        // Initialise database, override to create standalone datasource
        ItemTrackingDatabase database = new ItemTrackingDatabase() {
            @Override
            protected DataSource getDataSource() {
                return new StandAloneDataSource();
            }
        };

        ItemOutboundTrackingVO value = new ItemOutboundTrackingVO();
        value.setItemId(new Long(2));
        value.setRefTrackingStatusInternalCode(ItemTrackingInternalCode.UNDELIVERABLE.getInternalCode());
        value.setTrackingDate(new Date());
        try {
            Long id = database.insertItemOutboundTracking(value);
            log.info("Inserted into ItemOutboundTracking with ID <" + id + ">");
        } catch (DataAccessException dae) {
            handleDataAccessException(dae, value);
        } catch (Throwable t) {
            log.fatal("[insertItemInbound] catch Throwable " + t.getClass());
        }

        // Test Get Statuses
//        database.getStatusMap();
    }

    private static void handleDataAccessException(DataAccessException dae, ItemOutboundTrackingVO value) {
        Throwable t = dae.getCause();
        if ((t instanceof SQLException) && (((SQLException) t).getErrorCode() == 20401)) {
            log.debug("Tracking disabled for status <"
                    + value.getRefTrackingStatusInternalCode() + ">");
        } else {
            log.debug("catch DataAccessException about to rethrow");
            log.debug("catch DataAccessException col      " + dae.getColumn());
            log.debug("catch DataAccessException error id " + dae.getErrorID());
            log.debug("catch DataAccessException message  " + dae.getMessage());
            log.debug("catch DataAccessException cause    " + dae.getCause());
            if (t instanceof SQLException) {
                log.debug("SQLException");
                SQLException sqle = (SQLException) t;
                log.debug("SQLException error code " + sqle.getErrorCode());
                log.debug("SQLException sql state  " + sqle.getSQLState());
                log.debug("SQLException message    " + sqle.getMessage());
                log.debug("SQLException cause      " + sqle.getCause());
            }

            throw dae;
        }
    }
}
