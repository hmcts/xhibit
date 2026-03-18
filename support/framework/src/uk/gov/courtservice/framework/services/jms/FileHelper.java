package uk.gov.courtservice.framework.services.jms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import uk.gov.courtservice.framework.services.CSServices;
import org.apache.log4j.Logger;

/**
 * This class is used for help when creating files with debug data in them
 */
public class FileHelper
{
	private static final String FAILURESBOX = System.getProperty("file.separator", "/") + "jms_failurebox";
    private static final String ERRORBOX    = System.getProperty("file.separator", "/") + "jms_errorbox";

    private static final String CHARACTER_ENCODING = "CHARACTER_ENCODING";
    private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
 
    private static final Logger log = CSServices.getLogger(FileHelper.class);

	/**
     * <p>Title: logDataToXmlFile</p>
     * <p>Description: Creates a file containing the data String passed in</p>
     * @param String data
     * @param String transactionName
     * @param String id
     * @param Calendar date
     * @param String directoryName
     */
	public static void logDataToXmlFile(final String data,
	                                       String transactionName,
	                                       String id,
	                                       Calendar date,
	                                       String directoryName)
	{
		String fileName  = null;
		File   directory = null;
		File   file      = null;

		FileOutputStream fileOS = null;
		BufferedOutputStream bufferedOutputStream = null;

		try
		{
	        if(data == null)
	        {
				log.debug("<< Data is empty >>");
				return;
			}

            fileName = getXmlFileName(transactionName, date, id);

	        log.debug("<< Writing to an XML file called >>: " + fileName);

		    directory = createXmlDirectory(transactionName, directoryName, null);

			file = new File(directory, fileName);

	        log.debug("<< Create FileOS >>");

	        fileOS = new FileOutputStream (file);

	        log.debug("<< Write failed JMS message to fileOS >>");
	        bufferedOutputStream = new BufferedOutputStream (fileOS, data.length());
            bufferedOutputStream.write (data.getBytes(getCharacterEncoding()));

	        log.debug("<< Completed Writing failed JMS message to fileOS >>");
		}
		catch(Exception e)
		{
			log.warn("<< logDataToXmlFile Exception >>: " + e);
		}
		finally
		{
			try
			{
				if (bufferedOutputStream!=null)
			    {
					log.debug("<< Close bufferedOutputStream >>: ");
					bufferedOutputStream.close();
			    }
			}
			catch(Exception e)
			{
				log.warn("<< logDataToXmlFile bufferedOutputStream close Exception >>: " + e);
			}

			try
			{
				if (fileOS!=null)
			    {
					log.debug("<< Close file os >>: ");
					fileOS.close();
			    }
			}
			catch(Exception e)
			{
				log.warn("<< logDataToXmlFile FileOutputStream close Exception >>: " + e);
			}
		}
	}


    /**
     * <p>
     * Title: createXmlDirectory
     * </p>
     * <p>
     * Description: Creates dated directory to put files in for testing or
     * errors
     * </p>
     *
     * @param transactionName
     * @param directoryName
     * @param calendar (current time)
     * @return File
     */
    public static File createXmlDirectory(String transactionName, String directoryName, Calendar calendar) {

        File logDir = null;

        String boxType = FAILURESBOX;

        if(transactionName.contains("Error"))
        {
            boxType = ERRORBOX;
            log.debug("<< Directory will be error box >>: " + boxType);
        }

        if(System.getProperty("log4j.log.dir") == null ||
           System.getProperty("log4j.log.dir").trim().equals(""))
        {
            log.debug("<< Writing JMS failures log to directory called >>: " + boxType);
            logDir = new File(boxType);
        }
        else
        {
            log.debug("<< Writing JMS failures log to directory called>>: " + System.getProperty("log4j.log.dir") + boxType);
            logDir = new File((System.getProperty("log4j.log.dir") + boxType));
        }

        if (!logDir.exists()) {
            log.debug("<< Creating JMS failures logging directory of type >>: " + boxType);
            boolean isDirCreated = logDir.mkdir();
            if (!isDirCreated) {
                log.debug("<< JMS failures logging directory not created:" + boxType);
            }
        }

        File directory = null;

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        // Note: MONTH starts with January at 0 we need to add 1
        String dayOfMonth = new Integer(calendar.get(Calendar.DAY_OF_MONTH)).toString();
        String month = new Integer(calendar.get(Calendar.MONTH) + 1).toString();
        String year = new Integer(calendar.get(Calendar.YEAR)).toString();

        if (dayOfMonth.length() == 1) {
            dayOfMonth = "0" + dayOfMonth;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }

        directoryName = directoryName + "_" + System.getProperty("weblogic.Name") + "_" + dayOfMonth + "-" + month
                + "-" + year;

        directory = new File(logDir, directoryName);

        if (!directory.exists()) {
            log.debug("<< Creating directory >>: " + directoryName);
            boolean isDirCreated = directory.mkdir();
            if (!isDirCreated) {
                log.debug("<< Directory not created:" + directoryName);
            }
        }

        return directory;
    }

    /**
     * Returns a filename appended with the time and a counter
     *
     * @param String name - the transaction name used to name the file
     * @param Calendar calendar - the file will always include the timestamp of the transaction
     *         - Optional (the current time will be used if this parameter is absent)
     * @param String id - a piece of data that can be used to uniquely identifer the transaction
     *         - Optional
     * @return String
     */
    public static String getXmlFileName(String name, Calendar calendar, String id) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        String hourOfDay = new Integer(calendar.get(Calendar.HOUR_OF_DAY)).toString();
        String minute = new Integer(calendar.get(Calendar.MINUTE)).toString();
        String second = new Integer(calendar.get(Calendar.SECOND)).toString();
        String milliSecond = new Integer(calendar.get(Calendar.MILLISECOND)).toString();

        if (hourOfDay.length() == 1) {
            hourOfDay = "0" + hourOfDay;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        if (milliSecond.length() == 1) {
            milliSecond = "00" + milliSecond;
        }
        if (milliSecond.length() == 2) {
            milliSecond = "0" + milliSecond;
        }

        String fileName = null;

        if(id!=null)
        {
			fileName = id + "_" + name + "_" + hourOfDay + "-" + minute + "-" + second + "-" + milliSecond + ".xml";
	    }
	    else
	    {
            fileName = name + "_" + hourOfDay + "-" + minute + "-" + second + "-" + milliSecond + ".xml";
		}

        log.debug("<< fileName:" + fileName);
        return fileName;
    }

    /**
     * Converts a list of properties in a hashmap to an XML String
     *
     * @param HashMap
     * @return String
     */
    public static String getXmlPropertiesData(HashMap properties) {

        log.debug("Start getXmlPropertiesData");
        
        if(properties==null || properties.isEmpty())
        {
            log.warn("JMS Properties were all null");
            return null;
        }

        final StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("<PropertiesData>");
        
        String propertyName = null;
        String propertyValue = null;
        
        Set keySet = properties.keySet();
        
        Iterator it = keySet.iterator();

        while (it.hasNext())
        {
            propertyName = null;
            propertyValue = null;
            
            propertyName = (String)it.next();
            log.debug("property name: " + propertyName);
            
            propertyValue = (String)properties.get(propertyName);
            log.debug("property value: " + propertyValue);
            
            if(propertyName==null || propertyValue==null)
            {
                log.warn("Invalid property (name or value is null) contained within JMS Properties");
                continue;
            }
            else
            {
                stringBuffer.append("<PropertyName>");
                stringBuffer.append(propertyName);
                stringBuffer.append("</PropertyName>");
                stringBuffer.append("<PropertyValue>");
                stringBuffer.append(propertyValue);
                stringBuffer.append("</PropertyValue>");
            }
        }
        
        stringBuffer.append("</PropertiesData>");
        
        if(log.isDebugEnabled())
        {
            log.debug("End getXmlPropertiesData:" + stringBuffer.toString());
        }
        
        return stringBuffer.toString();
    }
    
    /**
     * Converts an Exception to a list and then to an XML String which is returned
     *
     * @param Exception
     * @return String
     */
    public static String getXmlErrorData(Exception error) {

		if(error==null)
		{
			return null;
		}

        final StringBuffer stringBuffer = new StringBuffer();

		String errorString = null;

		List errorList = createStackTraceList(error);

        errorString = "<ErrorContent><![CDATA[ProgressError: ";

        for (int i = 0; i < errorList.size(); i++) {
            errorString = errorString + errorList.get(i);
        }

        errorString = errorString + "]]></ErrorContent>";

		stringBuffer.append("<ErrorData>");
		stringBuffer.append("<Error>");
		stringBuffer.append("<ErrorName>");
		stringBuffer.append(error.getClass().getName());
		stringBuffer.append("</ErrorName>");
		stringBuffer.append("<ErrorMessage>");
		stringBuffer.append(error.getMessage());
		stringBuffer.append("</ErrorMessage>");
		stringBuffer.append("<ErrorTimestamp>");
		stringBuffer.append(Calendar.getInstance().getTime().toString());
		stringBuffer.append("</ErrorTimestamp>");
		stringBuffer.append(errorString);
        stringBuffer.append("</Error>");
		stringBuffer.append("</ErrorData>");

        return stringBuffer.toString();
    }


    /**
     * Puts the stack trace of an Exception into an ArrayList.
     *
     * @param Exception
     * @return List
     */
    private static List createStackTraceList(Exception e) {
        List frames = null;
        ListWriter out = null;

        try {
            frames = new ArrayList();
            out = new ListWriter(frames);

            e.printStackTrace(out);
        } catch (Exception ex) {
            log.warn("<< ********** Exception message in createStackTraceList: ********** " + ex.getMessage() + " >>");
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch (Exception exc) {
                log.warn("<< ********** Failed to close ListWriter in createStackTraceList ********** >>");
            }
        }

        return frames;
    }

    /**
     * Inner class to manage an ArrayList in a PrintWriter.
     */
    private static class ListWriter extends PrintWriter {
        private List lines = new ArrayList();

        public ListWriter() {
            super(new NullWriter());
        }

        public ListWriter(List lines) {
            super(new NullWriter());
            this.lines = lines;
        }

        public List getList() {
            return lines;
        }

        public void println(Object o) {
            lines.add(o.toString());
        }

        public void println(char[] s) {
            lines.add(new String(s));
        }

        public void println(String s) {
            lines.add(s);
        }
    }

    /**
     * Inner class to manage a NullWriter.
     */
    private static class NullWriter extends Writer {
        public void close() {
        }

        public void flush() {
        }

        public void write(char[] cbuf, int off, int len) {
        }
    }
    
    /**
     * Returns the encoding required.
     * Defaults to UTF-8 which is used for ORACLE 9i
     *
     * @return String
     */
    private static String getCharacterEncoding()
    {
        String characterEncoding = CSServices.getConfigServices().getProperty(CHARACTER_ENCODING,DEFAULT_CHARACTER_ENCODING);

        log.debug("<< characterEncoding: " + characterEncoding + " >>");

        return characterEncoding;
    }       
}