package datamigration1745;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * Class whose methods perform useful calculations regarding performance
 * statistics This includes a getSizeOf method that will determine the size of
 * the Serializable object passed to it
 */

public class DataMigrationStatistics {
    private static final int DEFAULT_SIZE = 0;
    
    private static final Calendar DEFAULT_TIME = Calendar.getInstance();
    
    private static String IS_DM_STATS_LOGGED = "datamigration.statistics";
    
    public static String DEFAULT_IS_DM_STATS_LOGGED = "false";

    private static ArrayList<DataMigrationObject> dataMigrationObjects = new ArrayList<DataMigrationObject>();
 
    private static final String DEFAULT_STATS_NAME = getCurrentDate() + "_data_migration_statistics.xml";
   
    public static synchronized void addObject(DataMigrationObject dmo) {
        
        if (!isDataMigrationStats())
        {
            return;
        }
        
        if(dmo!=null)
        {
            if(dmo.getEndTime()==null)
            {
                dmo.setEndTime(Calendar.getInstance());
            }
            if(dmo.getObject()!=null && dmo.getSize()==null)
            {
                dmo.setSize(getSizeOf(dmo.getObject()));
            }
            if(dmo.getStartTime()!=null && dmo.getElapsedTime()==null)
            {
                dmo.setElapsedTime(getElapsedTime(dmo.getStartTime(),dmo.getEndTime()));
            }
        }
   
        dataMigrationObjects.add(dmo);
    }
    

    /**
     * Prints performance stats to log
     */
    public static boolean generateStatisticsReport() 
    {
        if (!isDataMigrationStats())
        {
            return false;
        }
        String statsString = "";
        boolean success = false;
        String fileName  = null;
        File   file      = null;
        FileOutputStream fileOS = null;
        BufferedWriter bw = null;
        Long totalTime = new Long(0);
        
        try
        {
            if (dataMigrationObjects!=null && dataMigrationObjects.size()>0) {
               
                for(DataMigrationObject dmo:dataMigrationObjects)
                {
                    totalTime = totalTime + dmo.getElapsedTime();
                    
                    statsString = statsString + "<StatsData objectId=\"" + dmo.getObjectId() + "\" objectName=\""
                        + dmo.getObjectName() + "\" size=\"" + dmo.getSize() 
                        + "\" elapsedTime=\"" + dmo.getElapsedTime()
                        + "\" totalElapsedTime=\"" + totalTime
                        + "\" />";
                }
                
                if (statsString != null) {
                    fileName = DEFAULT_STATS_NAME;
                    file = new File(DataMigrationProperties.getProperties().getReportDir(), fileName);
                    
                    System.out.println("<< Write Data Migration Stats to XML file: >>" + fileName);
                    bw = new BufferedWriter(new FileWriter(file));

                    bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><DataMigrationStats xmlns=\"http://www.courtservice.gov.uk/schemas/courtservice/xhibit/datamigrationstats\">");
                    bw.write(statsString);
                    bw.write("</DataMigrationStats>");
                }
            }
            success=true;
        }
        catch(Exception e)
        {
            System.out.println("<< ERROR: Exception during creation of the Data Migration Stats Report:" + e);
        }
        finally
        {
            try
            {
                if (bw!=null)
                {
                    System.out.println("<< Close BufferedWriter >>: ");
                    bw.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("<< ERROR: Exception during closing of Data Migration Stats Report Buffered Writer:" + e);
            }

            try
            {
                if (fileOS!=null)
                {
                    System.out.println("<< Close file os >>: ");
                    fileOS.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("<< ERROR: Exception during closing of Data Migration Stats Report file output stream:" + e);
            }
        }    
        return success;
    }
    
    /**
     * Determines the size of the Serializable object passed to it
     *
     * Exception handling: All IOExceptions handled internally, method will
     * return 0 The object passed in is checked to ensure it is not null and
     * isSerializable - if these conditions are not met 0 is returned
     *
     * @param object
     *            Mandatory. This is the object we are going to determine the
     *            size of which must be Serializable or Externalizable
     * @return The size in bytes of the object
     */
    private static int getSizeOf(final Serializable object) {
        if (!checkSerializable(object)) {
            System.out.println("Check serializable failed");
            return DEFAULT_SIZE;
        }

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        int objectSize = 0;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(object);

            objectSize = baos.size();
        } catch (final IOException io) {
            System.out.println("getSizeOf IOException manipulating oos and baos, error is: " + io.toString());
            return DEFAULT_SIZE;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
            } catch (final IOException e) {
                System.out.println("getSizeOf IOException closing baos, error is: " + e.toString());
                return DEFAULT_SIZE;
            } finally {
                try {
                    if (oos != null) {
                        oos.close();
                        oos = null;
                    }
                } catch (final IOException e) {
                    System.out.println("getSizeOf IOException closing oos, error is: " + e.toString());
                    return DEFAULT_SIZE;
                }
            }
        }

        System.out.println("***** Sizeof object is: " + objectSize);
        return objectSize;
    } // sizeof

    /**
     * Determines the difference in milliseconds between two times. The
     * startTime must be before the endTime
     *
     * @param startTime:
     *            Optional: this is the start time
     * @param endTime:
     *            Mandatory: this is the end time
     * @return The diffence in milliseconds between the start and end time
     */
    private static long getElapsedTime(Calendar startTime, Calendar endTime) {
        long elapsedTime = 0;

        if (startTime == null) {
            System.out.println("getElapsedTime: StartTime was null so use: " + DEFAULT_TIME.getTime().toString());
            startTime = DEFAULT_TIME;
        }          

        if (endTime != null)
        {
            System.out.println("getElapsedTime: StartTime was : " + startTime.getTime().toString());
            System.out.println("getElapsedTime: EndTime was : " + endTime.getTime().toString());
            
            if (endTime.getTime().getTime() >= startTime.getTime().getTime()) {
                System.out.println("getElapsedTime: endTime before startTime");
                elapsedTime = endTime.getTime().getTime() - startTime.getTime().getTime();
            } else {
                System.out.println("getElapsedTime: endTime before startTime so return 0 elapsed time");
                return 0;
            }
        }
        else {
            System.out.println("EndTime as null so return 0 elapsed time");
            return 0;
        }
        
        System.out.println("***** Elapsed Time: " + elapsedTime);

        return elapsedTime;
    }
 
    private static boolean isDataMigrationStats() {
        String isDataMigrationStats = System.getProperty(IS_DM_STATS_LOGGED,
                DEFAULT_IS_DM_STATS_LOGGED);

        System.out.println("<< isDataMigrationStats: " + isDataMigrationStats + " >>");

        return isDataMigrationStats != null && isDataMigrationStats.equalsIgnoreCase("TRUE");
    }
   
    /**
     * Checks that an object is not null and is serializable
     *
     * @param object:
     *            the object we are going check to see if it is serializable
     * @return boolean
     */
    private static boolean checkSerializable(final Object object) {
        if (object == null) {
            System.out.println("checkSerializable object was null");
            return false;
        }

        if (!(object instanceof Serializable)) {
            System.out.println("checkSerializable object was not Serializable");
            return false;
        }

        return true;
    }
    
    private static String getCurrentDate()
    {
        String date = "";
        
        DateFormat sdf = SimpleDateFormat.getInstance();
        ((SimpleDateFormat)sdf).applyPattern("yyyyMMdd");
        date = ((SimpleDateFormat)sdf).format(Calendar.getInstance().getTime());
        return date;
    }  
}