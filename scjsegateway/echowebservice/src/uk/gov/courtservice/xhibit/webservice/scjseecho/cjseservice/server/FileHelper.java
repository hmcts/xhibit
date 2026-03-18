package uk.gov.courtservice.xhibit.webservice.scjseecho.cjseservice.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * This class is used for help when creating files with debug data in them
 */
public class FileHelper
{
    private static final String INBOX    = System.getProperty("file.separator", "/") + "echo_inbox";
    private static final String OUTBOX   = System.getProperty("file.separator", "/") + "echo_outbox";
    private static final String ERRORBOX = System.getProperty("file.separator", "/") + "echo_errorbox";

    private static final String CHARACTER_ENCODING = "CHARACTER_ENCODING";
    private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
    
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
				out("<< Data is empty >>");
				return;
			}

            fileName = getXmlFileName(transactionName, date, id);

	        out("<< Writing to an XML file called >>: " + fileName);

		    directory = createXmlDirectory(transactionName, directoryName, null);

			file = new File(directory, fileName);

	        out("<< Create FileOS >>");

	        fileOS = new FileOutputStream (file);

	        out("<< Write Echo Web Service message to fileOS >>");
	        bufferedOutputStream = new BufferedOutputStream (fileOS, data.length());
            bufferedOutputStream.write (data.getBytes(getCharacterEncoding()));

	        out("<< Completed Writing Echo Web Service message to fileOS >>");
		}
		catch(Exception e)
		{
			out("<< logDataToXmlFile Exception >>: " + e);
		}
		finally
		{
			try
			{
				if (bufferedOutputStream!=null)
			    {
					out("<< Close bufferedOutputStream >>: ");
					bufferedOutputStream.close();
			    }
			}
			catch(Exception e)
			{
				out("<< logDataToXmlFile bufferedOutputStream close Exception >>: " + e);
			}

			try
			{
				if (fileOS!=null)
			    {
					out("<< Close file os >>: ");
					fileOS.close();
			    }
			}
			catch(Exception e)
			{
				out("<< logDataToXmlFile FileOutputStream close Exception >>: " + e);
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

        String boxType = OUTBOX;
        
        if(transactionName.contains("Request"))
        {            
            boxType = INBOX;
            out("<< Directory will be inbox >>: " + boxType);
        }
        else if(transactionName.contains("Error"))
        {
            boxType = ERRORBOX;
            out("<< Directory will be error box >>: " + boxType); 
        }
        else
        {
            out("<< Directory will be outbox >>: " + boxType); 
        }
        
        if(System.getProperty("log4j.log.dir") == null ||
           System.getProperty("log4j.log.dir").trim().equals(""))
        {
            out("<< Writing echo web service log to directory called >>: " + boxType);
            logDir = new File(boxType);
        }
        else
        {
            out("<< Writing echo web service log to directory called>>: " + System.getProperty("log4j.log.dir") + boxType);
            logDir = new File((System.getProperty("log4j.log.dir") + boxType));
        }

        if (!logDir.exists()) {
            out("<< Creating echo web service logging directory of type >>: " + boxType);
            boolean isDirCreated = logDir.mkdir();
            if (!isDirCreated) {
                out("<< echo web service logging directory not created:" + boxType);
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
            out("<< Creating directory >>: " + directoryName);
            boolean isDirCreated = directory.mkdir();
            if (!isDirCreated) {
                out("<< Directory not created:" + directoryName);
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
			fileName = name + "_" + id + "_" + hourOfDay + "-" + minute + "-" + second + "-" + milliSecond + ".xml";
	    }
	    else
	    {
            fileName = name + "_" + hourOfDay + "-" + minute + "-" + second + "-" + milliSecond + ".xml";
		}

        out("<< fileName:" + fileName);
        return fileName;
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
            out("<< ********** Exception message in createStackTraceList: ********** " + ex.getMessage() + " >>");
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch (Exception exc) {
                out("<< ********** Failed to close ListWriter in createStackTraceList ********** >>");
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

    protected static void out(String in)
    {
        System.out.println(in+"\n\n");
    }

    /**
     * Returns the encoding required.
     * Defaults to UTF-8 which is used for ORACLE 9i
     *
     * @return String
     */
    private static String getCharacterEncoding()
    {
        String characterEncoding = System.getProperty(CHARACTER_ENCODING,DEFAULT_CHARACTER_ENCODING);

        out("<< characterEncoding: " + characterEncoding + " >>");

        return characterEncoding;
    }
}