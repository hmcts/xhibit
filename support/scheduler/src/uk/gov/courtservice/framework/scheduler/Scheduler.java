package uk.gov.courtservice.framework.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.JdbcHelper;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Scheduler class that configures, initialises and controls a set of
 * tasks using Schedulables.
 * </p>
 * <p>
 * Description: This class expects properties of very specific format. The
 * property defined by SCHEDULED_TASK contains a comma separated list of task
 * names for scheduling. The properties for each of the tasks as defined in
 * schedulable and in the defined task strategies are to prefixed by the task
 * name and a full stop.
 * </p>
 * <p>
 * For example:
 * 
 * <pre>
 *   ##############################
 *   #Current tasks for scheduling.
 *   ##############################
 *   scheduledtasks=javatask,sessionbeantask
 *    
 *   ############################
 *   #Configuration for javatask.
 *   ############################
 *   javatask.strategy=uk.gov.courtservice.framework.scheduler.JavaTaskStrategy
 *   javatask.class=uk.gov.courtservice.xhibit.business.tasks.ATask
 *   javatask.fixedrate=false
 *   javatask.delay=0
 *   javatask.period=10000
 *   ###################################
 *   #Configuration for sessionbeantask.
 *   ###################################
 *   sessionbeantask.strategy=uk.gov.courtservice.framework.scheduler.RemoteSessionTaskStrategy
 *   sessionbeantask.remotehome=uk.gov.courtservice.xhibit.business.services.ASessionRemoteHome
 *   sessionbeantask.lookup=ASession
 *   sessionbeantask.fixedrate=false
 *   sessionbeantask.delay=0
 *   sessionbeantask.period=10000
 * </pre>
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 */
public class Scheduler {
    private static Logger log = CSServices.getLogger(Scheduler.class);

    
    /**
     * Optional System property for defining which tasks are scheduled to run.
     */
    public static final String SYSTEM_SCHEDULED_TASKS = "scheduler.scheduledtasks";
    
    /**
     * If system property is set to this value use file.
     */
    public static final String SYSTEM_SCHEDULED_USE_FILE = "default";
        
    /**
     * Mandatory property for defining which tasks are scheduled to run.
     */
    public static final String SCHEDULED_TASKS = "scheduledtasks";

    private Map schedulableMap;

    private String[] scheduledTasks;

    /**
     * Initialise the scheduler, passing it's configuration properties in.
     * 
     * @param props
     *            the properties to use to configure the scheduler and it's
     *            tasks.
     */
    public Scheduler(Properties props) {
        // Sort out what tasks have been scheduled.
        scheduledTasks = getArrayFromDelimitedList(getScheduledTaskDelimitedList(props), ",");

        schedulableMap = Collections.synchronizedMap(new HashMap());

        // Sort out the properties file.
        HashMap splitPropertiesMap = splitPropertiesByPrefix(props, ".");

        // Initialise the Schedulables.
        for (int i = 0; i < scheduledTasks.length; i++) {
            String scheduledTaskName = scheduledTasks[i];

            // Split the properties out for each Schedulable and task.
            Properties splitProperties = (Properties) splitPropertiesMap.get(scheduledTaskName);
            splitProperties = splitProperties == null ? new Properties() : splitProperties;

            schedulableMap.put(scheduledTaskName, new Schedulable(scheduledTaskName, splitProperties));
        }
    }

    private String getScheduledTaskDelimitedList(Properties props) {        
        String scheduledTaskDelimitedList = System.getProperty(SYSTEM_SCHEDULED_TASKS);
        if(scheduledTaskDelimitedList != null && !scheduledTaskDelimitedList.equals(SYSTEM_SCHEDULED_USE_FILE)) {
            return scheduledTaskDelimitedList;
        }
        
        String serverName = System.getProperty("weblogic.Name");
        String taskListName = serverName != null ? SCHEDULED_TASKS + "." + serverName : SCHEDULED_TASKS;

        //get the string from the database matching up to the list
        String taskList = getScheduledString(taskListName);
		//if nothing in the database then check the prop file
        if(taskList==null) {
        	log.debug("task lisk is null so looking in the properties file");
        	taskList = props.getProperty(taskListName, "");
        }  
        
        if(log.isDebugEnabled()) {
            log.debug("Retrieved tasks " + taskList + " for " + serverName + ".");
        }        
        return taskList;
    }
    
    /**
     * Start all scheduled tasks.
     */
    public void start() {
        for (int i = 0; i < scheduledTasks.length; i++) {
            boolean success = ((Schedulable) schedulableMap.get(scheduledTasks[i])).start();
            if (success)
                log.info("Succesfully started the " + scheduledTasks[i] + " task.");
            else
                log.error("Failed to start the " + scheduledTasks[i] + " task.");
        }
    }

    /**
     * Stop the Scheduler tidily.
     */
    public void cleanup() {
        for (int i = 0; i < scheduledTasks.length; i++) {
            ((Schedulable) schedulableMap.get(scheduledTasks[i])).stop();
        }
    }

    /**
     * Utility method to parse a list into a String[].
     * 
     * @param delimitedList
     *            The list to be split.
     * @param delimiter
     *            The delimiter to use.
     * @return a String array containing the split list.
     */
    private String[] getArrayFromDelimitedList(String delimitedList, String delimiter) {
        if (delimitedList != null) {
            // Set up a StringTokenizer that does not returnthe delimiters
            // as tokens.
            StringTokenizer st = new StringTokenizer(delimitedList, delimiter, false);

            // Set up the return array.
            String[] returnArray = new String[st.countTokens()];

            // Populate the return array.
            for (int i = 0; st.hasMoreTokens(); i++)
                returnArray[i] = st.nextToken().trim();

            return returnArray;
        } else
            return new String[0];
    }

    /**
     * Utility method that splits a Properties object by prefix, separation is
     * determined by the passed in delimiter. The prefix is stripped from the
     * new properties.
     * 
     * @return a HashMap of properties objects keyed to their original prefixes
     * @param delimiter
     *            The delimiter used to determine the boundary betwen the prefix
     *            and the rest of the property name.
     */
    private HashMap splitPropertiesByPrefix(Properties props, String delimiter) {
        HashMap split = new HashMap();
        int delimiterLength = delimiter.length();

        for (Enumeration enumeration = props.propertyNames(); enumeration.hasMoreElements();) {
            String propertyName = (String) enumeration.nextElement();

            // Assume no prefix.
            String prefix = "";
            int splitPropertyPos = 0;

            // Find Prefix.
            int prefixPos = propertyName.indexOf(delimiter);

            // If there is a prefix
            if (prefixPos > -1) {
                prefix = propertyName.substring(0, prefixPos);
                splitPropertyPos = prefixPos + delimiterLength;
            }

            String splitPropertyName = propertyName.substring(splitPropertyPos);

            // See if there is a properties file in existence for prefix.
            Properties propertiesForPrefix = (Properties) split.get(prefix);
            // If not create one.
            if (propertiesForPrefix == null) {
                propertiesForPrefix = new Properties();
                split.put(prefix, propertiesForPrefix);
            }

            // Set the split property.
            propertiesForPrefix.setProperty(splitPropertyName, props.getProperty(propertyName));
        }

        return split;
    }
    
    /**
     * Returns the value from the database
     * @param val the value to look for
     * @return String containing the tasks
     */
    private String getScheduledString(final String val) {
    	 Connection conn = null;
         PreparedStatement statement = null;
         ResultSet rs = null;
         final String sqlQuery = "select property_value from xhb_config_prop where property_name=?";
 		
 		try {
 	        conn= CSServices.getServiceLocator().getDataSource().getConnection();
 			statement = conn.prepareStatement(sqlQuery);
 			statement.setString(1, val); 
 			rs = statement.executeQuery();
 			String schedString = null;
 			rs.next();
            schedString =  rs.getString(1); 			
            return schedString;

 		} catch (SQLException e) {
 			log.error("An error occurred while retrieving the text from the database "+e);
 			return null;
 		} finally {
 			JdbcHelper.closeStatement(statement);
 	        JdbcHelper.closeConnection(conn);
 		}
    }
    
}