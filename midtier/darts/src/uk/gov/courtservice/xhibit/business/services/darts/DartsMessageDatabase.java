package uk.gov.courtservice.xhibit.business.services.darts;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractDartsDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.services.CSServices;


public class DartsMessageDatabase  extends AbstractDartsDatabase{

    private static final Logger log = CSServices.getLogger(DartsMessageDatabase.class);
    private static final String GET_DARTS_MESSAGES = "{ ? = call darts_new_msgs_pkg.get_darts_message_refcur() }";

    private static DartsMessageDatabase me;

    private DartsMessageDatabase() {
        log.info("DartsMessageDatabase constructor called");
    }

    public static DartsMessageDatabase getInstance() {
        if (me == null) {
            synchronized(DartsMessageDatabase.class)
            {
                if (me == null) {
                    me = new DartsMessageDatabase();
                }
            }
        }
        return me;
    }

    
    public DartsMessageVO[] getMessages() {

        DartsMessageVO[] messages = getMessagesFromDatabase();
        return messages;
    }

    private DartsMessageVO[] getMessagesFromDatabase() {

        if (log.isDebugEnabled()) {
            log.debug("DartsMessageDatabase v11: polling for Darts messages to send... ");
        }

        DartsMessageVO[] messages = null;
//        List<DartsMessageVO> messageList = new ArrayList<DartsMessageVO>();
       
        final DartsMessageRowProcessor rp = new DartsMessageRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_DARTS_MESSAGES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[]{});
        messages = rp.getMessages();
        
        /* Connection connection = null;
        Statement statement = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("DartsMessageDatabase v10: polling for Darts messages to send... ");
            }
            connection = getDartsConnection();
            statement = connection.createStatement();
            statement.execute("SELECT * FROM DAR_NEW_MESSAGES");
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                int messageId = result.getInt("MESSAGE_ID");
                String xhibitMessageCode = result.getString("XHIBIT_MESSAGE_CODE");
                String exissMessageCode = result.getString("EXISS_MESSAGE_CODE");
                Clob clob = result.getClob("PAYLOAD");
                String clobValue = clob.getSubString(1, (int) clob.length());
                int retryCount = result.getInt("RETRY_COUNT");
                Date nextRetryTime = result.getDate("NEXT_RETRY_TIME");
                DartsMessageVO message = new DartsMessageVO();
                message.setId(messageId);
                message.set_xhibitMessageCode(xhibitMessageCode);
                message.set_exissMessageCode(exissMessageCode);
                message.set_payload(clobValue);
                message.setRetryCount(retryCount);
                message.setNextRetryTime(nextRetryTime);
                messageList.add(message);
            }
            statement.executeUpdate("DELETE FROM DAR_NEW_MESSAGES"); //TODO remove this when the stored procedure does this bit...
            statement.close();
            connection.close();
        }*/
        

       /*f (messageList.size() > 0) {
            messages = new DartsMessageVO[messageList.size()];
            int arrayIndex = 0;
            for (DartsMessageVO messageFromList : messageList) {
                messages[arrayIndex] = messageFromList;
                arrayIndex++;
            }
        }*/
        return messages;
    }

    public void reportMessageSuccess(DartsMessageVO message) {
        String messageId = Integer.toString(message.getId());

        Connection connection = getDartsConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE DAR_MESSAGE_STORE SET STATUS_CODE='SENT' WHERE MESSAGE_ID=" + messageId);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            log.error("Error on reportMessageSuccess()- see stack trace");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                log.error("Error on closing SQL connection/statement");
            }
        }

    }

    public void reportMessageFailed(DartsMessageVO message) {
        String messageId = Integer.toString(message.getId());

        Connection connection = getDartsConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE DAR_MESSAGE_STORE SET STATUS_CODE='FAILED' WHERE MESSAGE_ID=" + messageId);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            log.error("Error on reportMessageFailed()- see stack trace");
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                log.error("Error on closing SQL connection/statement");
            }
        }


    }

    public void reportMessageToBeRetried(DartsMessageVO message) {
        String messageId = Integer.toString(message.getId());

        Connection connection = getDartsConnection();
        Statement statement = null;
        PreparedStatement insert = null;
        int updatedRetryCount = message.getRetryCount() + 1;
        try {
            DartsConfiguration config = DartsConfiguration.getInstance();
            String retryMaxAsString = config.getProperty("darts.retry.max.times");
            int retryMax = (retryMaxAsString==null) ? 20 : Integer.parseInt(retryMaxAsString);
            if (message.getRetryCount() >= retryMax) {
                reportMessageFailed(message);
            } else {
                insert = connection.prepareStatement("UPDATE DAR_NEW_MESSAGES SET PAYLOAD=? WHERE MESSAGE_ID=?");
                String retryIntervalAsString = config.getProperty("darts.retry.interval");            
                int retryIncrementInSeconds = (retryIntervalAsString==null) ? 20 : Integer.parseInt(retryIntervalAsString);
                statement = connection.createStatement();
                statement.executeUpdate("UPDATE DAR_MESSAGE_STORE SET STATUS_CODE='RETRY' WHERE MESSAGE_ID=" + messageId);
                String sql = "INSERT INTO DAR_NEW_MESSAGES VALUES(" + message.getId() + ",'" + message.get_xhibitMessageCode()+ "','" + message.get_exissMessageCode() + "',NULL," + updatedRetryCount + ",sysdate+(" + retryIncrementInSeconds + "/86400),null,null,'DARTS','DARTS',null)";
                log.debug("SQL to be executed: [" + sql + "]");
                statement.executeUpdate(sql);
                statement.close();
                insert.setString(1, message.get_payload());
                insert.setInt(2, message.getId());
                insert.executeUpdate();
                insert.close();
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Error on reportMessageToBeRetried()- see stack trace");
            e.printStackTrace();
        } catch (DartsException de) {
            log.error("Error getting DartsConfiguration in reportMessageToBeRetried()- see stack trace");
            de.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (insert != null)
                    insert.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                log.error("Error on closing SQL connection/statement");
            }
        }


    }

    private Connection getDartsConnection() {
        Connection connection = null;

        try {
            Context initialContext = CSServices.getServiceLocator().getInitialContext();
            javax.sql.DataSource dataSource = (javax.sql.DataSource) initialContext.lookup("DartsOracleTxDataSource");
            connection = dataSource.getConnection();
        }

        catch (SQLException se) {
            log.error("Error on SQL connection/statement");
            se.printStackTrace();
        } catch (NamingException ne) {
            log.error("Error on looking up datasource");
            ne.printStackTrace();
        }
        return connection;

    }

}
